package com.rootmen.Database.DatabaseQuery;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connections {
    static final String DB_URL = "jdbc:postgresql://distance-course.ru:5432/main";
    static final String USER = "authorization_app";
    static final String PASS = "ga4kHTswrjcqwWDi51QA";

    static Connection getConnection(String id) throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }
}
