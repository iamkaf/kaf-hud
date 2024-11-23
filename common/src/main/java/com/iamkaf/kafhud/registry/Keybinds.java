package com.iamkaf.kafhud.registry;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class Keybinds {
    public static final KeyMapping TOGGLE_HUD =
            new KeyMapping("key.kafhud.toggle_hud", GLFW.GLFW_KEY_N, "key.categories.kafhud");

    public static void init() {

    }

    static {
        registerKeybind();
    }

    @ExpectPlatform
    private static void registerKeybind() {
        throw new AssertionError("platform method");
    }
}
