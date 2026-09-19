package com.corescode.stunslam.client;

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

public class StunSlamHud {

    private static final Identifier HUD_ID =
            Identifier.of("stunslam", "stun_slam");

    private static final int FRAME_COUNT = 2;

    // Original texture dimensions
    private static final int TEXTURE_WIDTH = 128;
    private static final int TEXTURE_HEIGHT = 24;

    private static long animationStart = -1;

    private static final Identifier[] FRAMES = {
            Identifier.of(
                    "stunslam",
                    "textures/hud/stunslam/frame_1.png"
            ),
            Identifier.of(
                    "stunslam",
                    "textures/hud/stunslam/frame_2.png"
            )
    };

    public static void initialize() {

        HudElementRegistry.addLast(
                HUD_ID,
                (context, tickCounter) -> render(context)
        );
    }

    public static void trigger() {

        animationStart =
                Util.getMeasuringTimeMs();
    }

    private static void render(DrawContext context) {

        if (animationStart < 0) {
            return;
        }

        // Get duration from config
        int frameDuration =
                StunSlamConfig.getInstance()
                        .getAnimationDurationMs();

        long elapsed =
                Util.getMeasuringTimeMs()
                        - animationStart;

        // Two frames total
        if (elapsed >= (long) frameDuration * FRAME_COUNT) {
            animationStart = -1;
            return;
        }

        int frameIndex =
                (int) (elapsed / frameDuration);

        if (frameIndex >= FRAME_COUNT) {
            frameIndex = FRAME_COUNT - 1;
        }

        Identifier texture =
                FRAMES[frameIndex];

        // Get configured size
        int drawWidth =
                StunSlamConfig.getInstance()
                        .getAnimationWidth();

        int drawHeight =
                StunSlamConfig.getInstance()
                        .getAnimationHeight();

        int screenWidth =
                context.getScaledWindowWidth();

        int screenHeight =
                context.getScaledWindowHeight();

        // Get configured position
        int positionX =
                StunSlamConfig.getInstance()
                        .getPositionX();

        int positionY =
                StunSlamConfig.getInstance()
                        .getPositionY();

        /*
         * Convert percentage position into actual screen coordinates.
         *
         * The animation is constrained so the entire texture remains
         * inside the screen.
         */
        int availableWidth =
                screenWidth - drawWidth;

        int availableHeight =
                screenHeight - drawHeight;

        int x =
                Math.round(
                        availableWidth
                                * (positionX / 100.0f)
                );

        int y =
                Math.round(
                        availableHeight
                                * (positionY / 100.0f)
                );

        context.drawTexture(
                RenderPipelines.GUI_TEXTURED,
                texture,
                x,
                y,
                0.0f,
                0.0f,
                drawWidth,
                drawHeight,
                TEXTURE_WIDTH,
                TEXTURE_HEIGHT
        );
    }
}