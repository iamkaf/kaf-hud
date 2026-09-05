package com.iamkaf.kafhud.registry;

import com.iamkaf.amber.api.registry.v1.KeybindHelper;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;

public class Keybinds {
    private static final KeyMapping.Category KAFHUD_CATEGORY =
            KeyMapping.Category.register(Identifier.fromNamespaceAndPath("kafhud", "kafhud"));

    public static final KeyMapping TOGGLE_HUD =
            KeybindHelper.register(new KeyMapping("key.kafhud.toggle_hud", InputConstants.KEY_N, KAFHUD_CATEGORY));

    public static void init() {
    }
}
