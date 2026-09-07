package com.example.mushroomstewmover;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public final class Config {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("mushroomstewmover.json");

    public static boolean enabled = true;
    public static int delayMs = 100;

    private Config() {}

    public static void load() {
        if (!Files.exists(FILE)) {
            save();
            return;
        }
        try (Reader reader = Files.newBufferedReader(FILE)) {
            Data data = GSON.fromJson(reader, Data.class);
            if (data != null) {
                enabled = data.enabled;
                delayMs = clamp(data.delayMs, 50, 2000);
            }
        } catch (IOException | RuntimeException e) {
            enabled = true;
            delayMs = 100;
            save();
        }
    }

    public static void save() {
        try {
            Files.createDirectories(FILE.getParent());
            Data data = new Data();
            data.enabled = enabled;
            data.delayMs = clamp(delayMs, 50, 2000);
            try (Writer writer = Files.newBufferedWriter(FILE)) {
                GSON.toJson(data, writer);
            }
        } catch (IOException ignored) {
        }
    }

    public static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    private static final class Data {
        boolean enabled = true;
        int delayMs = 100;
    }
}
