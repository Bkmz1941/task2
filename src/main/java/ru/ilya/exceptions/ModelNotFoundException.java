package ru.ilya.exceptions;

public class ModelNotFoundException extends Exception {
    public ModelNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}