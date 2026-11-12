package com.corescode.stunslam.client;

import net.minecraft.resources.Identifier;

public class StunSlamHud {

    private static final Identifier HUD_ID =
            Identifier.fromNamespaceAndPath("stunslam", "stun_slam");

    private static final int FRAME_COUNT = 2;

    // Original texture dimensions
    private static final int TEXTURE_WIDTH = 128;
    private static final int TEXTURE_HEIGHT = 24;

    private static long animationStart = -1;

    private static final Identifier[] FRAMES = {
            Identifier.fromNamespaceAndPath(
                    "stunslam",
                    "textures/hud/stunslam/frame_1.png"
            ),
            Identifier.fromNamespaceAndPath(
                    "stunslam",
                    "textures/hud/stunslam/frame_2.png"
            )
    };

    public static void initialize() {

        StunSlamHudBridge.register(
                HUD_ID,
                StunSlamHud::render
        );
    }

    public static void trigger() {

        animationStart =
                currentTimeMs();
    }

    private static void render(HudCanvas context) {

        if (animationStart < 0) {
            return;
        }

        // Get duration from config
        int frameDuration =
                StunSlamConfig.getInstance()
                        .getAnimationDurationMs();

        long elapsed =
                currentTimeMs()
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
                context.guiWidth();

        int screenHeight =
                context.guiHeight();

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
                texture,
                x,
                y,
                drawWidth,
                drawHeight,
                TEXTURE_WIDTH,
                TEXTURE_HEIGHT
        );
    }

    private static long currentTimeMs() {

        return System.nanoTime() / 1_000_000L;
    }
}
