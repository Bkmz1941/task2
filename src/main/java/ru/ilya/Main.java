package ru.ilya;

import ru.ilya.core.DIContainer;
import ru.ilya.server.Server;

public class Main {
    public static void main(String[] args) {
        DIContainer container = DIContainer.get();
        Server server = Server.get();
        server.run();
    }
}