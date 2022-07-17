package com.rootmen.Database.DatabaseQuery.Query;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionsManager {
    String url;
    String username;
    String password;
    String connectionClass;
    //private HikariDataSource dataSource;

    public ConnectionsManager(String url, String username, String password, String connectionClass) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.connectionClass = connectionClass;
        /*HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        dataSource = new HikariDataSource(config);*/
    }

    public Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName(connectionClass);
        return DriverManager.getConnection(this.url, this.username, this.password);
        //return dataSource.getConnection();
    }
}