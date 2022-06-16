package com.rootmen.Database.DatabaseQuery;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionsManager {
    HikariDataSource dataSource = null;
    boolean isCloseConnection = false;
    HikariConfig config = null;


    public ConnectionsManager(String url, String user, String pass) throws SQLException {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(user);
        config.setPassword(pass);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.setMaximumPoolSize(10);
        config.setAutoCommit(false);
        dataSource = new HikariDataSource(config);
    }


    public ConnectionsManager(HikariConfig config) throws SQLException {
        dataSource = new HikariDataSource(config);
    }


    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }


    public boolean closeConnectionsPool() {
        dataSource.close();
        return true;
    }
}