package com.bivashy.javafx.authentication.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public interface JDBCConnectionPool {

    static JDBCConnectionPool of(String url, String user, String password) {
        return () -> DriverManager.getConnection(url, user, password);
    }

    Connection createConnection() throws SQLException;

}
