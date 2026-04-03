package com.fabbe50.fabsservertweaks.util;

import com.fabbe50.fabsservertweaks.LogUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.lang.reflect.Type;
import java.util.Objects;
import java.util.function.Supplier;

public final class JsonUtil {
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .create();

    private JsonUtil() {
    }

    public static Path resolvePath(Path gameDirectory, String subfolder, String fileName) {
        Objects.requireNonNull(gameDirectory, "gameDirectory");
        validateSegment(subfolder, "subfolder");
        validateSegment(fileName, "fileName");
        return gameDirectory.resolve(subfolder).resolve(fileName);
    }

    public static <T> void save(Path gameDirectory, String subfolder, String fileName, T data) {
        Path path = resolvePath(gameDirectory, subfolder, fileName);
        save(path, data);
    }

    public static <T> void save(Path path, T data) {
        Objects.requireNonNull(path, "path");
        Objects.requireNonNull(data, "data");

        try {
            Path parent = path.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }

            try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
                GSON.toJson(data, writer);
            }
        } catch (IOException | JsonIOException exception) {
            throw new IllegalStateException("Failed to save json data to " + path, exception);
        }
    }

    public static <T> @Nullable T load(Path gameDirectory, String subfolder, String fileName, Class<T> type) {
        return load(resolvePath(gameDirectory, subfolder, fileName), type);
    }

    public static <T> @Nullable T load(Path path, Class<T> type) {
        return load(path, (Type) type);
    }

    public static <T> @Nullable T load(Path gameDirectory, String subfolder, String fileName, TypeToken<T> typeToken) {
        return load(resolvePath(gameDirectory, subfolder, fileName), typeToken);
    }

    public static <T> @Nullable T load(Path path, TypeToken<T> typeToken) {
        Objects.requireNonNull(typeToken, "typeToken");
        return load(path, typeToken.getType());
    }

    public static <T> @Nullable T load(Path gameDirectory, String subfolder, String fileName, Type type) {
        return load(resolvePath(gameDirectory, subfolder, fileName), type);
    }

    public static <T> @Nullable T load(Path path, Type type) {
        Objects.requireNonNull(path, "path");
        Objects.requireNonNull(type, "type");

        if (!Files.exists(path)) {
            return null;
        }

        try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            return GSON.fromJson(reader, type);
        } catch (IOException | JsonParseException exception) {
            throw new IllegalStateException("Failed to load json data from " + path, exception);
        }
    }

    public static <T> T loadOrDefault(Path gameDirectory, String subfolder, String fileName, Class<T> type, Supplier<T> defaultFactory) {
        Path path = resolvePath(gameDirectory, subfolder, fileName);
        return loadOrDefault(path, type, defaultFactory);
    }

    public static <T> T loadOrDefault(Path path, Class<T> type, Supplier<T> defaultFactory) {
        return loadOrDefault(path, (Type) type, defaultFactory);
    }

    public static <T> T loadOrDefault(Path gameDirectory, String subfolder, String fileName, TypeToken<T> typeToken, Supplier<T> defaultFactory) {
        Path path = resolvePath(gameDirectory, subfolder, fileName);
        return loadOrDefault(path, typeToken, defaultFactory);
    }

    public static <T> T loadOrDefault(Path path, TypeToken<T> typeToken, Supplier<T> defaultFactory) {
        Objects.requireNonNull(typeToken, "typeToken");
        return loadOrDefault(path, typeToken.getType(), defaultFactory);
    }

    public static <T> T loadOrDefault(Path gameDirectory, String subfolder, String fileName, Type type, Supplier<T> defaultFactory) {
        Path path = resolvePath(gameDirectory, subfolder, fileName);
        return loadOrDefault(path, type, defaultFactory);
    }

    public static <T> T loadOrDefault(Path path, Type type, Supplier<T> defaultFactory) {
        Objects.requireNonNull(path, "path");
        Objects.requireNonNull(type, "type");
        Objects.requireNonNull(defaultFactory, "defaultFactory");

        T loaded = load(path, type);
        if (loaded != null) {
            return loaded;
        }

        T defaultValue = Objects.requireNonNull(defaultFactory.get(), "defaultFactory returned null");
        save(path, defaultValue);
        return defaultValue;
    }

    public static <T> T loadOrCreate(Path gameDirectory, String subfolder, String fileName, Class<T> type, Supplier<T> defaultFactory) {
        return loadOrDefault(gameDirectory, subfolder, fileName, type, defaultFactory);
    }

    public static <T> T loadOrCreate(Path gameDirectory, String subfolder, String fileName, TypeToken<T> typeToken, Supplier<T> defaultFactory) {
        return loadOrDefault(gameDirectory, subfolder, fileName, typeToken, defaultFactory);
    }

    public static <T> T loadOrCreate(Path gameDirectory, String subfolder, String fileName, Type type, Supplier<T> defaultFactory) {
        return loadOrDefault(gameDirectory, subfolder, fileName, type, defaultFactory);
    }

    public static boolean exists(Path gameDirectory, String subfolder, String fileName) {
        return Files.exists(resolvePath(gameDirectory, subfolder, fileName));
    }

    public static boolean delete(Path gameDirectory, String subfolder, String fileName) {
        return delete(resolvePath(gameDirectory, subfolder, fileName));
    }

    public static boolean delete(Path path) {
        Objects.requireNonNull(path, "path");

        try {
            return Files.deleteIfExists(path);
        } catch (IOException exception) {
            LogUtil.warn("Failed to delete json file at " + path + ": " + exception.getMessage());
            return false;
        }
    }

    private static void validateSegment(String value, String name) {
        Objects.requireNonNull(value, name);
        if (value.isBlank()) {
            throw new IllegalArgumentException(name + " must not be blank");
        }
    }
}
