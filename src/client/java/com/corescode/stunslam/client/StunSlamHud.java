package com.corescode.stunslam.client;

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

public class StunSlamHud {

    private static final Identifier HUD_ID =
            Identifier.of("stunslam", "stun_slam");

    // 500 ms → 1000 ms
    private static final long ANIMATION_DURATION = 1000;

    private static long animationStart = -1;

    public static void initialize() {
        HudElementRegistry.addLast(
                HUD_ID,
                (context, tickCounter) -> render(context)
        );
    }

    public static void trigger() {
        animationStart = Util.getMeasuringTimeMs();
    }

    private static void render(DrawContext context) {

        if (animationStart < 0) {
            return;
        }

        long elapsed = Util.getMeasuringTimeMs() - animationStart;

        if (elapsed >= ANIMATION_DURATION) {
            animationStart = -1;
            return;
        }

        MinecraftClient client = MinecraftClient.getInstance();

        if (client.player == null) {
            return;
        }

        float progress = elapsed / (float) ANIMATION_DURATION;

        int width = context.getScaledWindowWidth();
        int height = context.getScaledWindowHeight();

        int centerX = width / 2;
        int centerY = height / 2 + 35;

        // ---------------------------------------------------------
        // ANIMATION PHASES
        // ---------------------------------------------------------

        float expansion;
        float alpha;

        if (progress < 0.25f) {

            // Very fast initial impact
            float p = progress / 0.25f;

            expansion = p;
            alpha = 1.0f;

        } else {

            // Slowly expand and fade
            float p = (progress - 0.25f) / 0.75f;

            expansion = 1.0f + p * 0.8f;
            alpha = 1.0f - p;
        }

        int crackAlpha = (int) (255 * alpha);

        int crackColor =
                (crackAlpha << 24) | 0xFFFFFF;

        // ---------------------------------------------------------
        // CENTRAL IMPACT
        // ---------------------------------------------------------

        int coreSize = (int) (3 + expansion * 3);

        context.fill(
                centerX - coreSize,
                centerY - coreSize,
                centerX + coreSize,
                centerY + coreSize,
                crackColor
        );

        // ---------------------------------------------------------
        // CRACKS
        // ---------------------------------------------------------

        int length = (int) (12 + expansion * 28);

        drawCrack(
                context,
                centerX,
                centerY,
                centerX + length,
                centerY - length / 3,
                crackColor
        );

        drawCrack(
                context,
                centerX,
                centerY,
                centerX - length,
                centerY - length / 2,
                crackColor
        );

        drawCrack(
                context,
                centerX,
                centerY,
                centerX + length / 2,
                centerY + length,
                crackColor
        );

        drawCrack(
                context,
                centerX,
                centerY,
                centerX - length / 2,
                centerY + length,
                crackColor
        );

        drawCrack(
                context,
                centerX,
                centerY,
                centerX + length,
                centerY + length / 2,
                crackColor
        );

        drawCrack(
                context,
                centerX,
                centerY,
                centerX - length,
                centerY + length / 3,
                crackColor
        );
    }

    // -------------------------------------------------------------
    // DRAW A JAGGED CRACK
    // -------------------------------------------------------------

    private static void drawCrack(
            DrawContext context,
            int startX,
            int startY,
            int endX,
            int endY,
            int color
    ) {

        int midX = (startX + endX) / 2;
        int midY = (startY + endY) / 2;

        // Small offset creates the jagged Minecraft-like shape
        int offsetX = endY > startY ? 3 : -3;
        int offsetY = endX > startX ? -3 : 3;

        drawLine(
                context,
                startX,
                startY,
                midX + offsetX,
                midY + offsetY,
                color
        );

        drawLine(
                context,
                midX + offsetX,
                midY + offsetY,
                endX,
                endY,
                color
        );
    }

    // -------------------------------------------------------------
    // SIMPLE 1-PIXEL LINE
    // -------------------------------------------------------------

    private static void drawLine(
            DrawContext context,
            int x1,
            int y1,
            int x2,
            int y2,
            int color
    ) {

        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);

        int steps = Math.max(dx, dy);

        if (steps == 0) {
            return;
        }

        for (int i = 0; i <= steps; i++) {

            float t = i / (float) steps;

            int x = (int) (x1 + (x2 - x1) * t);
            int y = (int) (y1 + (y2 - y1) * t);

            context.fill(
                    x,
                    y,
                    x + 2,
                    y + 2,
                    color
            );
        }
    }
}