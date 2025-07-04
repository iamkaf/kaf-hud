package com.iamkaf.kafhud;

import com.iamkaf.amber.api.common.client.CommonClientUtils;
import com.iamkaf.amber.api.event.v1.events.common.client.ClientTickEvents;
import com.iamkaf.amber.api.event.v1.events.common.client.HudEvents;
import com.iamkaf.amber.api.platform.v1.Platform;
import com.iamkaf.kafhud.registry.Keybinds;
import com.iamkaf.kafhud.util.StringUtil;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.biome.Biome;

public class KafHUDClient {
    public static boolean enabled = true;

    static {
        Keybinds.init();
    }

    public static void init() {
        Constants.LOG.info("Initializing KafHUDClient on {}...", Platform.getPlatformName());
        ClientTickEvents.END_CLIENT_TICK.register(KafHUDClient::onClientTick);
        HudEvents.RENDER_HUD.register(KafHUDClient::onRenderHUD);
    }

    public static void onRenderHUD(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        Minecraft mc = Minecraft.getInstance();

        if (mc.getDebugOverlay().showDebugScreen()) {
            return;
        }

        if (mc.options.hideGui) {
            return;
        }

        if (!enabled) {
            return;
        }

        Font font = mc.font;
        LocalPlayer player = mc.player;

        int xPadding = 3;
        int yPadding = 3;
        int lineSpacing = 10;

        int xGlobalOffset = 1 + xPadding;
        int yOffset = 1 + yPadding;

        if (player == null) {
            return;
        }

        Component coordinatesComponent = makeCoordinatesComponent(player);
        Component directionComponent = makeDirectionComponent(player);
        Component biomeComponent = makeBiomeComponent(player);

        int coordinatesWidth = font.width(coordinatesComponent.getString()) + 4;

        CommonClientUtils.text(guiGraphics, font, coordinatesComponent, xGlobalOffset, yOffset, CommonClientUtils.WHITE);
        CommonClientUtils.text(
                guiGraphics,
                font,
                directionComponent,
                xGlobalOffset + coordinatesWidth,
                yOffset,
                getDirectionColor(player.getDirection())
        );
        CommonClientUtils.text(
                guiGraphics,
                font,
                biomeComponent,
                xGlobalOffset,
                yOffset + lineSpacing,
                CommonClientUtils.WHITE
        );
    }

    private static Component makeCoordinatesComponent(Player player) {
        double x = player.getX();
        double y = player.getY();
        double z = player.getZ();
        return Component.literal(String.format("XYZ: %.1f / %.1f / %.1f", x, y, z));
    }

    private static Component makeDirectionComponent(Player player) {
        Direction direction = player.getDirection();
        String directionName =
                Component.translatable(String.format("text.kafhud.direction.%s", direction.getName())).getString();
        return Component.literal(String.format("(%s)", directionName));
    }

    private static Component makeBiomeComponent(Player player) {
        Holder<Biome> biome = player.level().getBiome(player.blockPosition());
        String biomeNamespace = biome.getRegisteredName().split(":")[0];
        String biomePath = biome.getRegisteredName().split(":")[1];
        Component biomeComponent = Component.translatable(String.format("biome.%s.%s", biomeNamespace, biomePath));

        // untranslated biome names
        // TODO: handle names with / or other special characters (e.g. "terralith:caves/frostfire_caves")
        if (biomeComponent.getString().startsWith("biome.")) {
            biomeComponent = Component.literal(StringUtil.toReadableSentence(biomePath));
        }
        return biomeComponent;
    }

    private static int getDirectionColor(Direction direction) {
        return switch (direction) {
            case NORTH -> 0xff4287f5;
            case SOUTH -> 0xfff5ce42;
            case WEST -> 0xff98ffad;
            case EAST -> 0xffeb73a9;
            default -> 0xffffffff;
        };
    }

    public static void toggle() {
        KafHUDClient.enabled = !KafHUDClient.enabled;
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.5f));
    }

    public static void copyCoordinates() {
        LocalPlayer player = Minecraft.getInstance().player;

        if (player == null) {
            return;
        }

        String coordinates = String.format("%.2f %.2f %.2f", player.getX(), player.getY(), player.getZ());
        Minecraft.getInstance().keyboardHandler.setClipboard(coordinates);

        player.displayClientMessage(
                Component.translatable("text.kafhud.coordinates_copied", coordinates)
                        .withStyle(Style.EMPTY.withColor(0xfcb8de)), false
        );

        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.NOTE_BLOCK_BELL, 1.5f));
    }

    public static void onClientTick() {
        LocalPlayer player = Minecraft.getInstance().player;

        if (player == null) {
            return;
        }

        while (Keybinds.TOGGLE_HUD.consumeClick()) {
            if (!player.isShiftKeyDown()) {
                toggle();
                return;
            }
            copyCoordinates();
        }
    }
}
