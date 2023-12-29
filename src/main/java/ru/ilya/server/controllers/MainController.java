package ru.ilya.server.controllers;

import com.sun.net.httpserver.HttpExchange;
import ru.ilya.models.ResponseModel;

import java.io.IOException;

public class MainController extends Controller {
    public MainController() {

    }

    public void index(HttpExchange exchange) throws IOException {
        ResponseModel<String> response = new ResponseModel<>(null, "Hello there");
        this.response(exchange, response);
    }
}
