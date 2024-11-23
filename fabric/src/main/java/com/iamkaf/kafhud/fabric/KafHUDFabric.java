package com.iamkaf.kafhud.fabric;

import net.fabricmc.api.ModInitializer;

import com.iamkaf.kafhud.KafHUD;

public final class KafHUDFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        KafHUD.init();
    }
}
