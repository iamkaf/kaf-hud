package com.iamkaf.kafhud.registry.fabric;

import com.iamkaf.kafhud.registry.Keybinds;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class KeybindsImpl {
    public static void registerKeybind() {
        KeyBindingHelper.registerKeyBinding(Keybinds.TOGGLE_HUD);
    }
}
