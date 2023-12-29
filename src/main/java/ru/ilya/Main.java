package ru.ilya;

import ru.ilya.core.DIContainer;
import ru.ilya.database.Database;
import ru.ilya.server.Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        DIContainer container = DIContainer.get();
        Server server = Server.get();
        server.run();
    }
}