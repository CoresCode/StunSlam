package com.corescode.stunslam.client;

import java.util.function.Consumer;

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;

/**
 * Minecraft 26.3 side of the HUD: registers the HUD element and
 * adapts the vanilla graphics object to {@link HudCanvas}.
 */
final class StunSlamHudBridge {

    private StunSlamHudBridge() {
    }

    static void register(Identifier id, Consumer<HudCanvas> renderer) {

        HudElementRegistry.addLast(
                id,
                (graphics, deltaTracker) ->
                        renderer.accept(new Canvas(graphics))
        );
    }

    private record Canvas(GuiGraphicsExtractor graphics) implements HudCanvas {

        @Override
        public int guiWidth() {
            return graphics.guiWidth();
        }

        @Override
        public int guiHeight() {
            return graphics.guiHeight();
        }

        @Override
        public void drawTexture(
                Identifier texture,
                int x,
                int y,
                int width,
                int height,
                int textureWidth,
                int textureHeight
        ) {

            graphics.blit(
                    RenderPipelines.GUI_TEXTURED,
                    texture,
                    x,
                    y,
                    0.0f,
                    0.0f,
                    width,
                    height,
                    textureWidth,
                    textureHeight
            );
        }
    }
}
