package com.corescode.stunslam.client;

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

public class StunSlamHud {

    private static final Identifier HUD_ID =
            Identifier.of("stunslam", "stun_slam");

    // 2 frames × 60 ms = 120 ms
    private static final long FRAME_DURATION = 60;
    private static final int FRAME_COUNT = 2;

    private static final int TEXTURE_WIDTH = 128;
    private static final int TEXTURE_HEIGHT = 24;

    // Final display size
    private static final int DRAW_WIDTH = 128;
    private static final int DRAW_HEIGHT = 24;

    private static long animationStart = -1;

    private static final Identifier[] FRAMES = {
            Identifier.of("stunslam", "textures/hud/stunslam/frame_1.png"),
            Identifier.of("stunslam", "textures/hud/stunslam/frame_2.png")
    };

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

        long elapsed =
                Util.getMeasuringTimeMs() - animationStart;

        if (elapsed >= FRAME_DURATION * FRAME_COUNT) {
            animationStart = -1;
            return;
        }

        int frameIndex =
                (int) (elapsed / FRAME_DURATION);

        if (frameIndex >= FRAME_COUNT) {
            frameIndex = FRAME_COUNT - 1;
        }

        Identifier texture = FRAMES[frameIndex];

        int screenWidth =
                context.getScaledWindowWidth();

        int screenHeight =
                context.getScaledWindowHeight();

        int x =
                (screenWidth - DRAW_WIDTH) / 2;

        int y =
                screenHeight / 2 + 14;

        context.drawTexture(
                RenderPipelines.GUI_TEXTURED,
                texture,
                x,
                y,
                0.0f,
                0.0f,
                DRAW_WIDTH,
                DRAW_HEIGHT,
                TEXTURE_WIDTH,
                TEXTURE_HEIGHT
        );
    }
}