package com.iamkaf.kafhud;

import com.iamkaf.amber.api.core.v2.AmberInitializer;
import net.fabricmc.api.ModInitializer;

/**
 * Fabric entry point.
 */
public class KafHUDFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        AmberInitializer.initialize(Constants.MOD_ID);
        KafHUDMod.init();
    }
}
