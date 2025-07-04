package com.iamkaf.kafhud.registry;

import com.iamkaf.amber.api.keymapping.KeybindHelper;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class Keybinds {
    public static final KeyMapping TOGGLE_HUD =
            KeybindHelper.register(new KeyMapping("key.kafhud.toggle_hud", GLFW.GLFW_KEY_N, "key.categories.kafhud"));

    public static void init() {
    }
}
