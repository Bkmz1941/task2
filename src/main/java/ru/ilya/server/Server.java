package ru.ilya.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import ru.ilya.core.DIContainer;
import ru.ilya.models.ResponseModel;
import ru.ilya.server.controllers.MainController;
import ru.ilya.server.controllers.StudentsController;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Server {
    private static Server instance;
    private final DIContainer container;

    public static Server get() {
        if (Server.instance == null) {
            Server.instance = new Server();
        }
        return Server.instance;
    }

    public Server() {
        this.container = DIContainer.get();
        this.container.register(MainController.class, new MainController());
        this.container.register(StudentsController.class, new StudentsController());
    }

    public void run() {
        try {
            int serverPort = 8000;
            HttpServer server = HttpServer.create(new InetSocketAddress(serverPort), 0);
            server.createContext("/", (exchange -> {
                this.handleRoutes(exchange);
                exchange.close();
            }));
            server.setExecutor(null);
            server.start();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private void handleRoutes(HttpExchange exchange) throws IOException {
        try {
            exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization");
            exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS,HEAD");

            if (this.methodIsOPTIONS(exchange)) {
                exchange.sendResponseHeaders(200, -1);
                return;
            }

            if (this.getURI(exchange).equals("/") && this.methodIsGet(exchange)) {
                container.resolve(MainController.class).index(exchange);
                return;
            }
            if (this.getURI(exchange).contains("/api/students")) {
                if (this.methodIsGet(exchange) && this.getURI(exchange).equals("/api/students")) {
                    container.resolve(StudentsController.class).index(exchange);
                } else if (this.methodIsGet(exchange) && this.methodHasModelId(exchange) != -1) {
                    container.resolve(StudentsController.class).get(exchange, this.methodHasModelId(exchange));
                } else if (this.methodIsPOST(exchange) && this.getURI(exchange).equals("/api/students")) {
                    container.resolve(StudentsController.class).create(exchange);
                } else if (this.methodIsDELETE(exchange) && this.methodHasModelId(exchange) != -1) {
                    container.resolve(StudentsController.class).delete(exchange, this.methodHasModelId(exchange));
                } else {
                    throw new Exception("Route doesn't exist");
                }
                return;
            }
            throw new Exception("Route doesn't exist");
        } catch (Exception e) {
            this.handleErrors(exchange, e.getMessage());

        }
    }

    private void handleErrors(HttpExchange exchange, String message) throws IOException {
        try {
            Gson gson = new GsonBuilder().serializeNulls().create();
            String json = gson.toJson(new ResponseModel<>(null, message));
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.getResponseHeaders().set("Accept", "application/json");
            exchange.sendResponseHeaders(400, json.getBytes().length);

            OutputStream output = exchange.getResponseBody();
            output.write(json.getBytes());
            output.flush();
        } catch (Exception e) {
            exchange.sendResponseHeaders(500, -1);
        }
    }

    private int methodHasModelId(HttpExchange exchange) {
        Pattern p = Pattern.compile("/(\\d+)");
        Matcher m = p.matcher(exchange.getRequestURI().toString());
        if (m.find()) {
            return Integer.parseInt(m.group(1));
        }
        return -1;
    }

    private boolean methodIsGet(HttpExchange exchange) {
        return "GET".equals(exchange.getRequestMethod());
    }

    private boolean methodIsPOST(HttpExchange exchange) {
        return "POST".equals(exchange.getRequestMethod());
    }

    private boolean methodIsDELETE(HttpExchange exchange) {
        return "DELETE".equals(exchange.getRequestMethod());
    }

    private boolean methodIsOPTIONS(HttpExchange exchange) {
        return "OPTIONS".equals(exchange.getRequestMethod());
    }

    private String getURI(HttpExchange exchange) {
        return exchange.getRequestURI().toString();
    }
}
