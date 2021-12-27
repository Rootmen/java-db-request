package com.rootmen.Database.DatabaseQuery;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionsManager {
    private String url = null;
    private String user = null;
    private String pass = null;
    boolean isOneConnection = false;
    Connection activeConnection = null;

    public ConnectionsManager() {
    }

    public ConnectionsManager(String url, String user, String pass, boolean oneConnection) throws SQLException {
        this.url = url;
        this.user = user;
        this.pass = pass;
        this.isOneConnection = oneConnection;
    }


    public Connection getConnection() throws SQLException {
        if (this.isOneConnection) {
            if (this.activeConnection == null) {
                this.activeConnection = DriverManager.getConnection(url, user, pass);
            }
            return this.activeConnection;
        }
        return DriverManager.getConnection(url, user, pass);
    }
}
