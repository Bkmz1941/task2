package ru.ilya.models;

public class ResponseModel<T> {
    protected final T data;
    protected final String message;

    public ResponseModel(T data, String message) {
        this.data = data;
        this.message = message;
    }
}
