package com.rootmen.Server;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.sockjs.SockJSBridgeOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;
import io.vertx.micrometer.*;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.SqlClient;

import java.util.EnumSet;

public class Server {
    public static void main(String[] args) {
        VertxOptions options = new VertxOptions();

        options.setMetricsOptions(
                new MicrometerMetricsOptions()
                        .setPrometheusOptions(new VertxPrometheusOptions().setEnabled(true))
                        .setLabels(EnumSet.of(Label.HTTP_PATH, Label.HTTP_METHOD, Label.HTTP_CODE))
                        .setJvmMetricsEnabled(true)
                        .setEnabled(true));
        options.setWorkerPoolSize(200);
        options.setEventLoopPoolSize(300);
        options.setInternalBlockingPoolSize(200);
        Vertx vertx = Vertx.vertx(options);
        HttpServerOptions serverOptions = new HttpServerOptions();
        serverOptions.setPort(8080);
        serverOptions.setMaxInitialLineLength(1024 * 1024 * 1024).setMaxHeaderSize(1024 * 1024 * 1024);
        HttpServer server = vertx.createHttpServer(serverOptions);

        PgConnectOptions connectOptions = new PgConnectOptions()
                .setPort(5432)
                .setHost("distance-course.ru")
                .setDatabase("main")
                .setUser("authorization_app")
                .setPassword("ga4kHTswrjcqwWDi51QA")
                .setCachePreparedStatements(true)
                .setReconnectAttempts(2)
                .setReconnectInterval(1000)
                .setPreparedStatementCacheMaxSize(1000);

        PoolOptions poolOptions = new PoolOptions()
                .setEventLoopSize(8)
                .setMaxSize(90);
        SqlClient client = PgPool.client(vertx, connectOptions, poolOptions);
        Router restApi = Router.router(vertx);
        restApi.get("/test").handler(request -> {
            HttpServerResponse response = request.response();
            response.setStatusCode(200);
            response.end("hello wyrd");
        });
        restApi.get("/").handler(request -> {
            HttpServerResponse response = request.response();
            response.setStatusCode(200);
            response.setChunked(true);
            response.putHeader("content-type", "application/json");
            response.putHeader("text/json", "charset=UTF-8");
            client.preparedQuery("SELECT 1 AS DATA,  2 AS DATA2 UNION ALL  SELECT 2 AS DATA,  2 AS DATA2 UNION ALL  SELECT 3 AS DATA,  2 AS DATA2")
                    .execute().onSuccess(users -> {
                users.forEach(row -> {
                    response.write(row.toJson().toString());
                });
                response.end();
            }).onFailure(throwable -> {
                request.fail(500);
            });
            ;
        });
        restApi.route("/metrics").handler(PrometheusScrapingHandler.create());

        SockJSBridgeOptions opts = new SockJSBridgeOptions()
                .addOutboundPermitted(new PermittedOptions()
                        .setAddress("metrics")); // В целях безопасности можно указать, какие очереди доступны для трансляции, а какие могут принимать сообщения из вебсокета. Фильтры возможны на основе регулярных выражений.
        restApi.route("/eventbus/*").subRouter(SockJSHandler.create(vertx).bridge(opts));
        server.requestHandler(restApi).listen();
    }
}


