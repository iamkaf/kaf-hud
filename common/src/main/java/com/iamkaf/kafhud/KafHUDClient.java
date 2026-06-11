package com.iamkaf.kafhud;

import com.iamkaf.amber.api.event.v1.events.common.client.ClientTickEvents;
import com.iamkaf.amber.api.event.v1.events.common.client.HudEvents;
import com.iamkaf.amber.api.functions.v1.ClientFunctions;
import com.iamkaf.amber.api.platform.v1.Platform;
import com.iamkaf.kafhud.registry.Keybinds;
import com.iamkaf.kafhud.util.StringUtil;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.biome.Biome;

import java.util.ArrayList;
import java.util.List;

/*
 * TODO next version:
 * - Time display.
 * - Light level, default off.
 * - Chunk coordinates, default off.
 * - Facing details, default off.
 * - Nether/Overworld coordinate conversion, default off.
 * - Movement speed, default off.
 * - FPS, default off.
 * - Contextual biome extras, default off.
 * - HUD anchors.
 * - Scale setting.
 */
public class KafHUDClient {
    private static final int TEXT_HEIGHT = 9;
    private static final int LINE_SPACING = 10;
    private static final int BACKGROUND_PADDING = 3;

    public static boolean enabled = true;

    static {
        Keybinds.init();
    }

    public static void init() {
        Constants.LOG.info("Initializing KafHUDClient on {}...", Platform.getPlatformName());
        ClientTickEvents.END_CLIENT_TICK.register(KafHUDClient::onClientTick);
        HudEvents.RENDER_HUD.register(KafHUDClient::onRenderHUD);
    }

    public static void onRenderHUD(GuiGraphicsExtractor guiGraphics, DeltaTracker deltaTracker) {
        Minecraft mc = Minecraft.getInstance();

        if (mc.getDebugOverlay().showDebugScreen()) {
            return;
        }

        if (mc.options.hideGui) {
            return;
        }

        if (!enabled || !KafHUDConfig.enabled()) {
            return;
        }

        Font font = mc.font;
        LocalPlayer player = mc.player;

        int xPadding = 3;
        int yPadding = 3;
        int xGlobalOffset = 1 + xPadding;

        if (player == null) {
            return;
        }

        List<HudRow> rows = makeHudRows(font, player);
        if (rows.isEmpty()) {
            return;
        }

        renderBackground(guiGraphics, xGlobalOffset, 1 + yPadding, rows);
        renderRows(guiGraphics, font, xGlobalOffset, 1 + yPadding, rows);
    }

    private static List<HudRow> makeHudRows(Font font, Player player) {
        List<HudRow> rows = new ArrayList<>();

        if (KafHUDConfig.showCoordinates()) {
            Component coordinatesComponent = makeCoordinatesComponent(player);
            HudRow coordinatesRow = new HudRow();
            coordinatesRow.add(coordinatesComponent, KafHUDConfig.coordinatesColor(), font.width(coordinatesComponent));
            if (KafHUDConfig.showDirection()) {
                coordinatesRow.addSpacer(4);
                Component directionComponent = makeDirectionComponent(player);
                coordinatesRow.add(
                        directionComponent,
                        KafHUDConfig.directionColor(player.getDirection()),
                        font.width(directionComponent)
                );
            }
            rows.add(coordinatesRow);
        } else if (KafHUDConfig.showDirection()) {
            Component directionComponent = makeDirectionComponent(player);
            rows.add(HudRow.single(
                    directionComponent,
                    KafHUDConfig.directionColor(player.getDirection()),
                    font.width(directionComponent)
            ));
        }

        if (KafHUDConfig.showBiome()) {
            Component biomeComponent = makeBiomeComponent(player);
            rows.add(HudRow.single(biomeComponent, KafHUDConfig.biomeColor(), font.width(biomeComponent)));
        }

        if (KafHUDConfig.showDay()) {
            Component dayComponent = makeDayComponent(player);
            rows.add(HudRow.single(dayComponent, KafHUDConfig.dayColor(), font.width(dayComponent)));
        }

        return rows;
    }

    private static void renderBackground(GuiGraphicsExtractor guiGraphics, int x, int y, List<HudRow> rows) {
        KafHUDConfig.BackgroundMode mode = KafHUDConfig.backgroundMode();
        if (mode == KafHUDConfig.BackgroundMode.OFF) {
            return;
        }

        int color = KafHUDConfig.backgroundColor();
        if (mode == KafHUDConfig.BackgroundMode.SINGLE_PANEL) {
            int width = rows.stream().mapToInt(HudRow::width).max().orElse(0);
            int height = TEXT_HEIGHT + LINE_SPACING * (rows.size() - 1);
            guiGraphics.fill(
                    x - BACKGROUND_PADDING,
                    y - BACKGROUND_PADDING,
                    x + width + BACKGROUND_PADDING,
                    y + height + BACKGROUND_PADDING,
                    color
            );
            return;
        }

        int previousRight = x - BACKGROUND_PADDING;
        int previousBottom = y - BACKGROUND_PADDING;
        for (int index = 0; index < rows.size(); index++) {
            int rowY = y + LINE_SPACING * index;
            int left = x - BACKGROUND_PADDING;
            int top = rowY - BACKGROUND_PADDING;
            int right = x + rows.get(index).width() + BACKGROUND_PADDING;
            int bottom = rowY + TEXT_HEIGHT + BACKGROUND_PADDING;
            int visibleTop = Math.max(top, previousBottom);

            if (top < previousBottom && previousRight < right) {
                guiGraphics.fill(previousRight, top, right, previousBottom, color);
            }

            guiGraphics.fill(
                    left,
                    visibleTop,
                    right,
                    bottom,
                    color
            );
            previousRight = right;
            previousBottom = bottom;
        }
    }

    private static void renderRows(GuiGraphicsExtractor guiGraphics, Font font, int x, int y, List<HudRow> rows) {
        for (int rowIndex = 0; rowIndex < rows.size(); rowIndex++) {
            int rowX = x;
            int rowY = y + LINE_SPACING * rowIndex;
            for (HudSegment segment : rows.get(rowIndex).segments()) {
                if (segment.component() != null) {
                    ClientFunctions.renderText(guiGraphics, font, segment.component(), rowX, rowY, segment.color());
                }
                rowX += segment.width();
            }
        }
    }

    private static Component makeCoordinatesComponent(Player player) {
        double x = player.getX();
        double y = player.getY();
        double z = player.getZ();
        return Component.literal(String.format("XYZ: %.1f / %.1f / %.1f", x, y, z));
    }

    private static Component makeDirectionComponent(Player player) {
        Direction direction = player.getDirection();
        if (KafHUDConfig.directionDisplayMode() == KafHUDConfig.DirectionDisplayMode.ARROWS) {
            return Component.literal(directionArrow(direction));
        }

        String directionName =
                Component.translatable(String.format("text.kafhud.direction.%s", direction.getName())).getString();
        return Component.literal(String.format("(%s)", directionName));
    }

    private static String directionArrow(Direction direction) {
        return switch (direction) {
            case NORTH -> "▲";
            case SOUTH -> "▼";
            case WEST -> "◀";
            case EAST -> "▶";
            default -> "";
        };
    }

    private static Component makeBiomeComponent(Player player) {
        Holder<Biome> biome = player.level().getBiome(player.blockPosition());
        String biomeNamespace = biome.getRegisteredName().split(":")[0];
        String biomePath = biome.getRegisteredName().split(":")[1];
        Component biomeComponent = Component.translatable(String.format("biome.%s.%s", biomeNamespace, biomePath));

        // untranslated biome names
        if (biomeComponent.getString().startsWith("biome.")) {
            // Handle names with / - use only the last segment (e.g. "caves/frostfire_caves" -> "frostfire_caves")
            String lastSlashIndex = biomePath.contains("/")
                ? biomePath.substring(biomePath.lastIndexOf("/") + 1)
                : biomePath;
            biomeComponent = Component.literal(StringUtil.toReadableSentence(lastSlashIndex));
        }
        return biomeComponent;
    }

    private static Component makeDayComponent(Player player) {
        long day = player.level().getOverworldClockTime() / 24000;
        return Component.translatable("text.kafhud.day", day);
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

        player.sendOverlayMessage(
                Component.translatable("text.kafhud.coordinates_copied", coordinates)
                        .withStyle(Style.EMPTY.withColor(0xfcb8de))
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

    private record HudSegment(Component component, int color, int width) {
    }

    private static final class HudRow {
        private final List<HudSegment> segments = new ArrayList<>();
        private int width;

        private static HudRow single(Component component, int color, int width) {
            HudRow row = new HudRow();
            row.add(component, color, width);
            return row;
        }

        private void add(Component component, int color, int width) {
            this.segments.add(new HudSegment(component, color, width));
            this.width += width;
        }

        private void addSpacer(int width) {
            this.segments.add(new HudSegment(null, 0, width));
            this.width += width;
        }

        private List<HudSegment> segments() {
            return this.segments;
        }

        private int width() {
            return this.width;
        }
    }
}
