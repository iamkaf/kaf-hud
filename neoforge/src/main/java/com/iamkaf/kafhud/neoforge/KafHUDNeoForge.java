package com.iamkaf.kafhud.neoforge;

import net.neoforged.fml.common.Mod;

import com.iamkaf.kafhud.KafHUD;

@Mod(KafHUD.MOD_ID)
public final class KafHUDNeoForge {
    public KafHUDNeoForge() {
        // Run our common setup.
        KafHUD.init();
    }
}
