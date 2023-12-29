package ru.ilya.core;

import ru.ilya.database.Database;

import java.util.HashMap;
import java.util.Map;

public class DIContainer {
    private static DIContainer instance;
    private final Map<Class<?>, Object> instances = new HashMap<>();

    public static DIContainer get() {
        if (DIContainer.instance == null) {
            DIContainer.instance = new DIContainer();
        }
        return DIContainer.instance;
    }

    public <T> void register(Class<T> type, T instance) {
        instances.put(type, instance);
    }

    public <T> T resolve(Class<T> type) {
        T instance = (T) instances.get(type);

        if (instance == null) {
            throw new IllegalArgumentException("No class: " + type.getName());
        }

        return instance;
    }
}
