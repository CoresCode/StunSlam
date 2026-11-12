package com.corescode.stunslam.client;

import net.minecraft.resources.Identifier;

/**
 * Minimal drawing surface used by {@link StunSlamHud}.
 * Each Minecraft version supplies its own implementation
 * (see StunSlamHudBridge in versions/&lt;mc&gt;), so the HUD logic
 * itself stays identical across versions.
 */
public interface HudCanvas {

    int guiWidth();

    int guiHeight();

    /**
     * Same meaning as vanilla's GUI_TEXTURED blit:
     * draws a width x height area starting at texel (0, 0)
     * of a texture that is textureWidth x textureHeight texels.
     */
    void drawTexture(
            Identifier texture,
            int x,
            int y,
            int width,
            int height,
            int textureWidth,
            int textureHeight
    );
}
