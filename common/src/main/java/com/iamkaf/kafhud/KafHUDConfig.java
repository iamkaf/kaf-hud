package com.iamkaf.kafhud;

import com.iamkaf.konfig.api.v1.ConfigBuilder;
import com.iamkaf.konfig.api.v1.ConfigHandle;
import com.iamkaf.konfig.api.v1.ConfigScope;
import com.iamkaf.konfig.api.v1.ConfigValue;
import com.iamkaf.konfig.api.v1.Konfig;
import com.iamkaf.konfig.api.v1.SyncMode;
import net.minecraft.core.Direction;

public final class KafHUDConfig {
    public static final ConfigHandle HANDLE;
    private static final ConfigValue<Boolean> ENABLED;
    private static final ConfigValue<BackgroundMode> BACKGROUND_MODE;
    private static final ConfigValue<Integer> BACKGROUND_COLOR;
    private static final ConfigValue<Boolean> SHOW_COORDINATES;
    private static final ConfigValue<Boolean> SHOW_DIRECTION;
    private static final ConfigValue<DirectionDisplayMode> DIRECTION_DISPLAY_MODE;
    private static final ConfigValue<Integer> COORDINATES_COLOR;
    private static final ConfigValue<Integer> DIRECTION_NORTH_COLOR;
    private static final ConfigValue<Integer> DIRECTION_SOUTH_COLOR;
    private static final ConfigValue<Integer> DIRECTION_EAST_COLOR;
    private static final ConfigValue<Integer> DIRECTION_WEST_COLOR;
    private static final ConfigValue<Boolean> SHOW_BIOME;
    private static final ConfigValue<Integer> BIOME_COLOR;
    private static final ConfigValue<Boolean> SHOW_DAY;
    private static final ConfigValue<Integer> DAY_COLOR;

    static {
        ConfigBuilder builder = Konfig.builder(Constants.MOD_ID, "client")
                .scope(ConfigScope.CLIENT)
                .syncMode(SyncMode.NONE)
                .schemaVersion(3)
                .migrate(1, context -> context.remove("hud"))
                .migrate(2, context -> {
                })
                .comment("Client display settings for KafHUD.");

        builder.push("general");
        builder.header("General");
        builder.categoryComment("General KafHUD overlay settings.");
        builder.categoryInfo(info -> info
                .header("KafHUD")
                .inlineText("These options only affect the local HUD overlay.")
                .inlineText("The toggle key still acts as a temporary in-session visibility switch."));
        ENABLED = builder.bool("enabled", true)
                .comment("Render the KafHUD overlay.")
                .tooltip("Master toggle for the overlay.")
                .build();
        BACKGROUND_MODE = builder.enumValue("background_mode", BackgroundMode.OFF)
                .comment("Controls the HUD background style.")
                .tooltip("Choose how KafHUD draws its background.")
                .build();
        BACKGROUND_COLOR = builder.colorArgb("background_color", 0x80000000)
                .comment("HUD background color.")
                .tooltip("ARGB color for HUD backgrounds.")
                .build();
        builder.pop();

        builder.push("coordinates");
        builder.header("Coordinates");
        builder.categoryComment("Controls coordinates and direction display.");
        SHOW_COORDINATES = builder.bool("show_coordinates", true)
                .comment("Show the player's current XYZ coordinates.")
                .tooltip("Show XYZ coordinates on the HUD.")
                .build();
        SHOW_DIRECTION = builder.bool("show_direction", true)
                .comment("Show the player's facing direction next to coordinates.")
                .tooltip("Show the cardinal direction on the HUD.")
                .build();
        DIRECTION_DISPLAY_MODE = builder.enumValue("direction_display_mode", DirectionDisplayMode.TEXT)
                .comment("Controls how direction is displayed.")
                .tooltip("Show direction as text or arrow symbols.")
                .build();
        COORDINATES_COLOR = builder.colorArgb("color", 0xffffffff)
                .comment("Coordinates text color.")
                .tooltip("ARGB color for the coordinates readout.")
                .build();
        DIRECTION_NORTH_COLOR = builder.colorArgb("direction_north_color", 0xff4287f5)
                .comment("North direction text color.")
                .tooltip("ARGB color for north direction text.")
                .build();
        DIRECTION_SOUTH_COLOR = builder.colorArgb("direction_south_color", 0xfff5ce42)
                .comment("South direction text color.")
                .tooltip("ARGB color for south direction text.")
                .build();
        DIRECTION_EAST_COLOR = builder.colorArgb("direction_east_color", 0xffeb73a9)
                .comment("East direction text color.")
                .tooltip("ARGB color for east direction text.")
                .build();
        DIRECTION_WEST_COLOR = builder.colorArgb("direction_west_color", 0xff98ffad)
                .comment("West direction text color.")
                .tooltip("ARGB color for west direction text.")
                .build();
        builder.pop();

        builder.push("biome");
        builder.header("Biome");
        builder.categoryComment("Controls biome display.");
        SHOW_BIOME = builder.bool("show_biome", true)
                .comment("Show the biome at the player's current position.")
                .tooltip("Show the current biome on the HUD.")
                .build();
        BIOME_COLOR = builder.colorArgb("color", 0xffffffff)
                .comment("Biome text color.")
                .tooltip("ARGB color for the biome readout.")
                .build();
        builder.pop();

        builder.push("day_counter");
        builder.header("Day Counter");
        builder.categoryComment("Controls day counter display.");
        SHOW_DAY = builder.bool("show_day", true)
                .comment("Show the current world day.")
                .tooltip("Show the world day counter on the HUD.")
                .build();
        DAY_COLOR = builder.colorArgb("color", 0xfff5ce42)
                .comment("Day counter text color.")
                .tooltip("ARGB color for the day counter readout.")
                .build();
        builder.pop();

        HANDLE = builder.build();
    }

    private KafHUDConfig() {
    }

    public static void init() {
    }

    public static boolean enabled() {
        return ENABLED.get();
    }

    public static BackgroundMode backgroundMode() {
        return BACKGROUND_MODE.get();
    }

    public static int backgroundColor() {
        return BACKGROUND_COLOR.get();
    }

    public static boolean showCoordinates() {
        return SHOW_COORDINATES.get();
    }

    public static boolean showDirection() {
        return SHOW_DIRECTION.get();
    }

    public static DirectionDisplayMode directionDisplayMode() {
        return DIRECTION_DISPLAY_MODE.get();
    }

    public static int coordinatesColor() {
        return COORDINATES_COLOR.get();
    }

    public static int directionColor(Direction direction) {
        return switch (direction) {
            case NORTH -> DIRECTION_NORTH_COLOR.get();
            case SOUTH -> DIRECTION_SOUTH_COLOR.get();
            case EAST -> DIRECTION_EAST_COLOR.get();
            case WEST -> DIRECTION_WEST_COLOR.get();
            default -> 0xffffffff;
        };
    }

    public static boolean showBiome() {
        return SHOW_BIOME.get();
    }

    public static int biomeColor() {
        return BIOME_COLOR.get();
    }

    public static boolean showDay() {
        return SHOW_DAY.get();
    }

    public static int dayColor() {
        return DAY_COLOR.get();
    }

    public enum DirectionDisplayMode {
        TEXT,
        ARROWS
    }

    public enum BackgroundMode {
        OFF,
        SINGLE_PANEL,
        PER_LINE
    }
}
