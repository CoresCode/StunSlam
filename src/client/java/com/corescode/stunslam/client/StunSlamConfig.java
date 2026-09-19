package com.corescode.stunslam.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public class StunSlamConfig {

    // =========================
    // Defaults
    // =========================

    public static final int DEFAULT_DURATION_MS = 128;
    public static final int DEFAULT_WIDTH = 130;

    // Current HUD position:
    // centered horizontally and just below the crosshair.
    public static final int DEFAULT_POSITION_X = 50;
    public static final int DEFAULT_POSITION_Y = 51;

    private static final int MIN_DURATION_MS = 40;
    private static final int MAX_DURATION_MS = 300;

    private static final int MIN_WIDTH = 64;
    private static final int MAX_WIDTH = 256;

    private static final int MIN_POSITION = 0;
    private static final int MAX_POSITION = 100;

    private static final double TEXTURE_ASPECT_RATIO = 24.0 / 128.0;

    private static final Gson GSON =
            new GsonBuilder()
                    .setPrettyPrinting()
                    .create();

    private static final Path CONFIG_FILE =
            FabricLoader.getInstance()
                    .getConfigDir()
                    .resolve("stunslam.json");

    // =========================
    // Config values
    // =========================

    private int animationDurationMs = DEFAULT_DURATION_MS;
    private int animationWidth = DEFAULT_WIDTH;

    private int positionX = DEFAULT_POSITION_X;
    private int positionY = DEFAULT_POSITION_Y;

    // =========================
    // Singleton
    // =========================

    private static StunSlamConfig INSTANCE;

    public static StunSlamConfig getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new StunSlamConfig();
        }

        return INSTANCE;
    }

    // =========================
    // Load / Save
    // =========================

    public static void load() {

        if (!Files.exists(CONFIG_FILE)) {
            INSTANCE = new StunSlamConfig();
            save();
            return;
        }

        try (Reader reader = Files.newBufferedReader(CONFIG_FILE)) {

            INSTANCE = GSON.fromJson(
                    reader,
                    StunSlamConfig.class
            );

            if (INSTANCE == null) {
                INSTANCE = new StunSlamConfig();
            }

            INSTANCE.clampValues();

        } catch (Exception e) {

            System.err.println(
                    "[Stun Slam] Failed to load config: "
                            + e.getMessage()
            );

            INSTANCE = new StunSlamConfig();
        }
    }

    public static void save() {

        try {

            Files.createDirectories(
                    CONFIG_FILE.getParent()
            );

            try (Writer writer =
                         Files.newBufferedWriter(CONFIG_FILE)) {

                GSON.toJson(
                        getInstance(),
                        writer
                );
            }

        } catch (IOException e) {

            System.err.println(
                    "[Stun Slam] Failed to save config: "
                            + e.getMessage()
            );
        }
    }

    // =========================
    // Getters
    // =========================

    public int getAnimationDurationMs() {
        return animationDurationMs;
    }

    public int getAnimationWidth() {
        return animationWidth;
    }

    public int getAnimationHeight() {

        return (int) Math.round(
                animationWidth * TEXTURE_ASPECT_RATIO
        );
    }

    public int getPositionX() {
        return positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    // =========================
    // Setters
    // =========================

    public void setAnimationDurationMs(int value) {

        animationDurationMs =
                clamp(
                        value,
                        MIN_DURATION_MS,
                        MAX_DURATION_MS
                );
    }

    public void setAnimationWidth(int value) {

        animationWidth =
                clamp(
                        value,
                        MIN_WIDTH,
                        MAX_WIDTH
                );
    }

    public void setPositionX(int value) {

        positionX =
                clamp(
                        value,
                        MIN_POSITION,
                        MAX_POSITION
                );
    }

    public void setPositionY(int value) {

        positionY =
                clamp(
                        value,
                        MIN_POSITION,
                        MAX_POSITION
                );
    }

    // =========================
    // Validation
    // =========================

    private void clampValues() {

        animationDurationMs =
                clamp(
                        animationDurationMs,
                        MIN_DURATION_MS,
                        MAX_DURATION_MS
                );

        animationWidth =
                clamp(
                        animationWidth,
                        MIN_WIDTH,
                        MAX_WIDTH
                );

        positionX =
                clamp(
                        positionX,
                        MIN_POSITION,
                        MAX_POSITION
                );

        positionY =
                clamp(
                        positionY,
                        MIN_POSITION,
                        MAX_POSITION
                );
    }

    private static int clamp(
            int value,
            int min,
            int max
    ) {

        return Math.max(
                min,
                Math.min(max, value)
        );
    }
}