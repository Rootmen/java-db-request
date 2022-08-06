package com.rootmen.Database.DatabaseQuery.Query;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionsManager {
    String url;
    String username;
    String password;
    String connectionClass;
    //private HikariDataSource dataSource;
    private DataSource dataSource;

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

    public ConnectionsManager(String jndi, String username, String password) throws NamingException {
        this.username = username;
        this.password = password;
        Context initContext = new InitialContext();
        Context envContext = (Context) initContext.lookup("java:/comp/env");
        this.dataSource = (DataSource) envContext.lookup("jdbc/" + jndi);
    }

    public ConnectionsManager(String jndi) throws NamingException {
        this.dataSource = (DataSource) new InitialContext().lookup("java:/comp/env/jdbc/" + jndi);
    }

    public void setClassName(String name) {
        this.connectionClass = name;
    }

    public Connection getConnection() throws SQLException, ClassNotFoundException {
        if (this.connectionClass != null) {

            Class.forName(this.connectionClass);
        }
        if (this.dataSource == null) {
            return DriverManager.getConnection(this.url, this.username, this.password);
        } else {
            if (this.username != null && this.password != null) {
                return this.dataSource.getConnection(this.username, this.password);
            }
            return this.dataSource.getConnection();
        }
    }
}