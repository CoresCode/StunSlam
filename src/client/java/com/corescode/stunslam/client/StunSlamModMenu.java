package com.corescode.stunslam.client;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;

public class StunSlamModMenu implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> new StunSlamConfigScreen(parent);
    }

    private static class StunSlamConfigScreen extends Screen {

        private final Screen parent;

        private DurationSlider durationSlider;
        private SizeSlider sizeSlider;
        private PositionXSlider positionXSlider;
        private PositionYSlider positionYSlider;

        protected StunSlamConfigScreen(Screen parent) {
            super(Component.literal("Stun Slam Settings"));
            this.parent = parent;
        }

        @Override
        protected void init() {

            StunSlamConfig config =
                    StunSlamConfig.getInstance();

            int centerX = this.width / 2;

            // =========================
            // Animation Duration
            // =========================

            durationSlider =
                    new DurationSlider(
                            centerX - 100,
                            this.height / 2 - 85,
                            200,
                            20,
                            config.getAnimationDurationMs()
                    );

            this.addRenderableWidget(durationSlider);

            // =========================
            // Animation Size
            // =========================

            sizeSlider =
                    new SizeSlider(
                            centerX - 100,
                            this.height / 2 - 35,
                            200,
                            20,
                            config.getAnimationWidth()
                    );

            this.addRenderableWidget(sizeSlider);

            // =========================
            // Position X
            // =========================

            positionXSlider =
                    new PositionXSlider(
                            centerX - 100,
                            this.height / 2 + 15,
                            200,
                            20,
                            config.getPositionX()
                    );

            this.addRenderableWidget(positionXSlider);

            // =========================
            // Position Y
            // =========================

            positionYSlider =
                    new PositionYSlider(
                            centerX - 100,
                            this.height / 2 + 65,
                            200,
                            20,
                            config.getPositionY()
                    );

            this.addRenderableWidget(positionYSlider);

            // =========================
            // Done Button
            // =========================

            this.addRenderableWidget(
                    Button.builder(
                            Component.literal("Done"),
                            button -> {

                                config.setAnimationDurationMs(
                                        durationSlider.getDuration()
                                );

                                config.setAnimationWidth(
                                        sizeSlider.getWidthValue()
                                );

                                config.setPositionX(
                                        positionXSlider.getPosition()
                                );

                                config.setPositionY(
                                        positionYSlider.getPosition()
                                );

                                StunSlamConfig.save();

                                if (this.minecraft != null) {
                                    ScreenCompat.setScreen(this.minecraft, parent);
                                }
                            }
                    ).bounds(
                            centerX - 100,
                            this.height / 2 + 105,
                            200,
                            20
                    ).build()
            );
        }

        @Override
        public void onClose() {

            if (this.minecraft != null) {
                ScreenCompat.setScreen(this.minecraft, parent);
            }
        }

        // ============================================================
        // Duration Slider
        // ============================================================

        private static class DurationSlider
                extends AbstractSliderButton {

            private static final int MIN = 40;
            private static final int MAX = 300;

            private DurationSlider(
                    int x,
                    int y,
                    int width,
                    int height,
                    int currentValue
            ) {

                super(
                        x,
                        y,
                        width,
                        height,
                        Component.empty(),
                        valueToSlider(currentValue)
                );

                updateMessage();
            }

            private static double valueToSlider(int currentValue) {

                return (
                        currentValue - MIN
                ) / (double) (MAX - MIN);
            }

            private int sliderToValue() {

                int currentValue =
                        MIN
                                + (int) Math.round(
                                this.value
                                        * (MAX - MIN)
                        );

                currentValue =
                        Math.round(
                                currentValue / 10.0f
                        ) * 10;

                return Math.max(
                        MIN,
                        Math.min(MAX, currentValue)
                );
            }

            public int getDuration() {
                return sliderToValue();
            }

            @Override
            protected void updateMessage() {

                this.setMessage(
                        Component.literal(
                                "Animation Duration: "
                                        + getDuration()
                                        + " ms"
                        )
                );
            }

            @Override
            protected void applyValue() {
                updateMessage();
            }
        }

        // ============================================================
        // Size Slider
        // ============================================================

        private static class SizeSlider
                extends AbstractSliderButton {

            private static final int MIN = 64;
            private static final int MAX = 256;

            private SizeSlider(
                    int x,
                    int y,
                    int width,
                    int height,
                    int currentValue
            ) {

                super(
                        x,
                        y,
                        width,
                        height,
                        Component.empty(),
                        valueToSlider(currentValue)
                );

                updateMessage();
            }

            private static double valueToSlider(int currentValue) {

                return (
                        currentValue - MIN
                ) / (double) (MAX - MIN);
            }

            private int sliderToValue() {

                int currentValue =
                        MIN
                                + (int) Math.round(
                                this.value
                                        * (MAX - MIN)
                        );

                currentValue =
                        Math.round(
                                currentValue / 8.0f
                        ) * 8;

                return Math.max(
                        MIN,
                        Math.min(MAX, currentValue)
                );
            }

            public int getWidthValue() {
                return sliderToValue();
            }

            @Override
            protected void updateMessage() {

                this.setMessage(
                        Component.literal(
                                "Animation Size: "
                                        + getWidthValue()
                                        + " px"
                        )
                );
            }

            @Override
            protected void applyValue() {
                updateMessage();
            }
        }

        // ============================================================
        // Position X Slider
        // ============================================================

        private static class PositionXSlider
                extends AbstractSliderButton {

            private static final int MIN = 0;
            private static final int MAX = 100;

            private PositionXSlider(
                    int x,
                    int y,
                    int width,
                    int height,
                    int currentValue
            ) {

                super(
                        x,
                        y,
                        width,
                        height,
                        Component.empty(),
                        valueToSlider(currentValue)
                );

                updateMessage();
            }

            private static double valueToSlider(int currentValue) {

                return currentValue / 100.0;
            }

            private int sliderToValue() {

                return Math.max(
                        MIN,
                        Math.min(
                                MAX,
                                (int) Math.round(
                                        this.value * 100.0
                                )
                        )
                );
            }

            public int getPosition() {
                return sliderToValue();
            }

            @Override
            protected void updateMessage() {

                this.setMessage(
                        Component.literal(
                                "Position X: "
                                        + getPosition()
                                        + "%"
                        )
                );
            }

            @Override
            protected void applyValue() {
                updateMessage();
            }
        }

        // ============================================================
        // Position Y Slider
        // ============================================================

        private static class PositionYSlider
                extends AbstractSliderButton {

            private static final int MIN = 0;
            private static final int MAX = 100;

            private PositionYSlider(
                    int x,
                    int y,
                    int width,
                    int height,
                    int currentValue
            ) {

                super(
                        x,
                        y,
                        width,
                        height,
                        Component.empty(),
                        valueToSlider(currentValue)
                );

                updateMessage();
            }

            private static double valueToSlider(int currentValue) {

                return currentValue / 100.0;
            }

            private int sliderToValue() {

                return Math.max(
                        MIN,
                        Math.min(
                                MAX,
                                (int) Math.round(
                                        this.value * 100.0
                                )
                        )
                );
            }

            public int getPosition() {
                return sliderToValue();
            }

            @Override
            protected void updateMessage() {

                this.setMessage(
                        Component.literal(
                                "Position Y: "
                                        + getPosition()
                                        + "%"
                        )
                );
            }

            @Override
            protected void applyValue() {
                updateMessage();
            }
        }
    }
}