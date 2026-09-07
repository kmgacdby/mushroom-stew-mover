package com.example.mushroomstewmover;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class ConfigScreen extends Screen {
    private final Screen parent;
    private static final int MIN_DELAY = 50, MAX_DELAY = 2000, STEP = 50;

    public ConfigScreen(Screen parent) {
        super(Text.translatable("screen.mushroomstewmover.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int cx = width / 2;
        addDrawableChild(ButtonWidget.builder(Text.translatable("screen.mushroomstewmover.enabled")
                .append(Config.enabled ? Text.translatable("screen.mushroomstewmover.on")
                        : Text.translatable("screen.mushroomstewmover.off")),
                b -> {
                    Config.enabled = !Config.enabled;
                    Config.save();
                    MushroomStewMoverClient.resetTimer();
                    clearAndInit();
                }).dimensions(cx - 100, height / 2 - 55, 200, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.translatable("screen.mushroomstewmover.minus"),
                b -> {
                    Config.delayMs = Config.clamp(Config.delayMs - STEP, MIN_DELAY, MAX_DELAY);
                    Config.save();
                    MushroomStewMoverClient.resetTimer();
                }).dimensions(cx - 100, height / 2 - 20, 40, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.translatable("screen.mushroomstewmover.plus"),
                b -> {
                    Config.delayMs = Config.clamp(Config.delayMs + STEP, MIN_DELAY, MAX_DELAY);
                    Config.save();
                    MushroomStewMoverClient.resetTimer();
                }).dimensions(cx + 60, height / 2 - 20, 40, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.translatable("screen.mushroomstewmover.close"), b -> close())
                .dimensions(cx - 100, height / 2 + 55, 200, 20).build());
    }

    @Override
    public void close() {
        if (client != null) client.setScreen(parent);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);

        int cx = width / 2;
        context.drawCenteredTextWithShadow(textRenderer, title, cx, height / 2 - 95, 0xFFFFFF);
        context.drawCenteredTextWithShadow(textRenderer,
                Text.translatable("screen.mushroomstewmover.delay", Config.delayMs),
                cx, height / 2 + 10, 0xFFFFFF);
        context.drawCenteredTextWithShadow(textRenderer,
                Text.translatable("screen.mushroomstewmover.key_hint"),
                cx, height / 2 + 35, 0xAAAAAA);
    }
}
