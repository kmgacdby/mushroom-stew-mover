package com.example.mushroomstewmover;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.Items;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import org.lwjgl.glfw.GLFW;

public class MushroomStewMoverClient implements ClientModInitializer {
    private static long nextMoveAt;
    public static KeyBinding toggleKey;
    public static KeyBinding settingsKey;

    @Override
    public void onInitializeClient() {
        Config.load();
        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.mushroomstewmover.toggle", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_G,
                "key.category.mushroomstewmover"));
        settingsKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.mushroomstewmover.settings", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_H,
                "key.category.mushroomstewmover"));
        ClientTickEvents.END_CLIENT_TICK.register(MushroomStewMoverClient::tick);
    }

    private static void tick(MinecraftClient client) {
        while (toggleKey.wasPressed()) {
            Config.enabled = !Config.enabled;
            Config.save();
            resetTimer();
        }
        while (settingsKey.wasPressed()) {
            client.setScreen(new ConfigScreen(client.currentScreen));
        }

        // Trigger on the actual inventory screen, never on the E key.
        if (!Config.enabled || client.player == null || client.interactionManager == null
                || !(client.currentScreen instanceof InventoryScreen inventoryScreen)) {
            resetTimer();
            return;
        }

        long now = System.currentTimeMillis();
        if (now < nextMoveAt) return;

        PlayerScreenHandler handler = inventoryScreen.getScreenHandler();
        int source = findStew(handler);
        int target = findEmptyHotbar(handler);
        if (source < 0 || target < 0) return;

        // Player inventory slots: 9-35 main inventory, 36-44 hotbar, 45 offhand.
        // We never inspect slot 45. SWAP button is hotbar index 0-8.
        int hotbarIndex = target - 36;
        client.interactionManager.clickSlot(
                handler.syncId, source, hotbarIndex, SlotActionType.SWAP, client.player);
        nextMoveAt = now + Config.delayMs;
    }

    private static int findStew(PlayerScreenHandler handler) {
        for (int slotId = 9; slotId <= 35; slotId++) {
            if (handler.getSlot(slotId).getStack().isOf(Items.MUSHROOM_STEW)) return slotId;
        }
        return -1;
    }

    private static int findEmptyHotbar(PlayerScreenHandler handler) {
        for (int slotId = 36; slotId <= 44; slotId++) {
            if (handler.getSlot(slotId).getStack().isEmpty()) return slotId;
        }
        return -1;
    }

    public static void resetTimer() {
        nextMoveAt = 0L;
    }
}
