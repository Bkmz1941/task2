package ru.ilya.server.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import ru.ilya.models.ResponseModel;

import java.io.*;

abstract public class Controller {
    protected Gson gson;

    public Controller() {
        this.gson = new Gson();
    }

    protected void response(HttpExchange exchange, ResponseModel<?> response) throws IOException {
        Gson gson = new GsonBuilder().serializeNulls().create();
        String json = gson.toJson(response);

        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.getResponseHeaders().set("Accept", "application/json");
        exchange.sendResponseHeaders(200, json.getBytes().length);

        OutputStream output = exchange.getResponseBody();
        output.write(json.getBytes());
        output.flush();
    }
}
