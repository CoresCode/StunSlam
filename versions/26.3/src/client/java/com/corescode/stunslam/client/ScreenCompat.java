package com.corescode.stunslam.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;

/**
 * Minecraft 26.3 way of switching the open screen.
 */
final class ScreenCompat {

    private ScreenCompat() {
    }

    static void setScreen(Minecraft minecraft, Screen screen) {

        minecraft.gui.setScreen(screen);
    }
}
