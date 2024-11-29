package com.iamkaf.kafhud;

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
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class KafHUDClient {
    public static final int WHITE = 0xffffff;
    public static final int RED = 0xdb3559;
    public static final int OUTLINE_COLOR = 0x0b2347;
    public static final int TRANSPARENT = -1;

    public static boolean enabled = true;

    public static void init() {
        Keybinds.init();
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

        Font textRenderer = mc.font;
        LocalPlayer player = mc.player;

        int xPadding = 2;
        int yPadding = 2;
        int lineSpacing = 10;

        int xGlobalOffset = 1 + xPadding;
        int yOffset = 1 + yPadding;

        if (player == null) {
            return;
        }

        Level level = player.level();

        double x = player.getX();
        double y = player.getY();
        double z = player.getZ();

        Direction direction = player.getDirection();
        String directionName =
                Component.translatable(String.format("text.kafhud.direction.%s", direction.getName()))
                        .getString();
        Holder<Biome> biome = level.getBiome(player.blockPosition());
        String biomeNamespace = biome.getRegisteredName().split(":")[0];
        String biomePath = biome.getRegisteredName().split(":")[1];

        Component coordinatesComponent = Component.literal(String.format("XYZ: %.1f / %.1f / %.1f", x, y, z));
        Component directionComponent = Component.literal(String.format("(%s)", directionName));
        Component biomeComponent =
                Component.translatable(String.format("biome.%s.%s", biomeNamespace, biomePath));

        // untranslated biome names
        if (biomeComponent.getString().startsWith("biome.")) {
            biomeComponent = Component.literal(StringUtil.toReadableSentence(biomePath));
        }

        int coordinatesWidth = textRenderer.width(coordinatesComponent.getString()) + 4;


        // this code is mega nested for performance
        if (player.swinging) {
            ItemStack heldItem = player.getMainHandItem();
            Tool toolComponent = heldItem.get(DataComponents.TOOL);
            if (toolComponent != null) {
                BlockHitResult hitResult = raytrace(level, player);
                BlockState aimedBlock = level.getBlockState(hitResult.getBlockPos());
                if (aimedBlock.requiresCorrectToolForDrops() && !aimedBlock.isAir() && !toolComponent.isCorrectForDrops(
                        aimedBlock) && !player.hasInfiniteMaterials()) {
                    int screenWidth = guiGraphics.guiWidth();
                    int centerX = screenWidth / 2;
                    int centerY = 1 + yPadding;
                    Component indicatorComponent = Component.translatable("text.kafhud.wrong_tool_indicator");
                    int widgetWidth = textRenderer.width(indicatorComponent);
                    text(guiGraphics,
                            textRenderer,
                            indicatorComponent,
                            centerX - (widgetWidth / 2),
                            centerY,
                            RED,
                            TRANSPARENT
                    );
                }
            }
        }

        text(guiGraphics,
                textRenderer,
                coordinatesComponent,
                xGlobalOffset,
                yOffset,
                WHITE,
                OUTLINE_COLOR
        );
        text(guiGraphics,
                textRenderer,
                directionComponent,
                xGlobalOffset + coordinatesWidth,
                yOffset,
                getDirectionColor(direction),
                OUTLINE_COLOR
        );
        text(guiGraphics,
                textRenderer,
                biomeComponent,
                xGlobalOffset,
                yOffset + lineSpacing,
                WHITE,
                OUTLINE_COLOR
        );
    }

    private static int getDirectionColor(Direction direction) {
        return switch (direction) {
            case NORTH -> 0x4287f5;
            case SOUTH -> 0xf5ce42;
            case WEST -> 0x98ffad;
            case EAST -> 0xeb73a9;
            default -> 0xffffff;
        };
    }

    private static void text(GuiGraphics context, Font font, Component message, int x, int y, int color,
            int outlineColor) {
        if (outlineColor == TRANSPARENT) {
            context.drawString(font, message, x, y, color, false);
            return;
        }

        font.drawInBatch8xOutline(message.getVisualOrderText(),
                x,
                y,
                color,
                outlineColor,
                context.pose().last().pose(),
                context.bufferSource(),
                15728880
        );
        context.flush();
    }

    public static void toggle() {
        KafHUDClient.enabled = !KafHUDClient.enabled;
        Minecraft.getInstance()
                .getSoundManager()
                .play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.5f));
    }

    public static void copyCoordinates() {
        LocalPlayer player = Minecraft.getInstance().player;

        if (player == null) {
            return;
        }

        String coordinates = String.format("%.2f %.2f %.2f", player.getX(), player.getY(), player.getZ());
        Minecraft.getInstance().keyboardHandler.setClipboard(coordinates);

        player.sendSystemMessage(Component.translatable("text.kafhud.coordinates_copied", coordinates)
                .withStyle(Style.EMPTY.withColor(0xfcb8de)));

        Minecraft.getInstance()
                .getSoundManager()
                .play(SimpleSoundInstance.forUI(SoundEvents.NOTE_BLOCK_BELL, 1.5f));
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

    public static @NotNull BlockHitResult raytrace(Level level, Player player) {
        Vec3 eyePosition = player.getEyePosition();
        Vec3 rotation = player.getViewVector(1);
        double reach = player.getAttributeValue(Attributes.BLOCK_INTERACTION_RANGE);
        Vec3 combined = eyePosition.add(rotation.x * reach, rotation.y * reach, rotation.z * reach);

        return level.clip(new ClipContext(eyePosition,
                combined,
                ClipContext.Block.OUTLINE,
                ClipContext.Fluid.NONE,
                player
        ));
    }
}
