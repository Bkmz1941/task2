package ru.ilya.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static Database instance;
    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void connect() {
        try {
            String connectionUrl = "jdbc:mysql://localhost:3306/test2?serverTimezone=UTC";
            this.connection = DriverManager.getConnection(connectionUrl, "root", "1234");
        } catch (Exception e) {
            throw new RuntimeException("Database doesn't work");
        }
    }

    public static Database get() {
        if (Database.instance == null) {
            Database.instance = new Database();
        }
        return Database.instance;
    }

    public void closeConnection() {
        try {
            if (this.connection != null) {
                this.connection.close();
            }
        } catch (Exception e) {
            throw new RuntimeException("Database couldn't be closed");
        }
    }
}
