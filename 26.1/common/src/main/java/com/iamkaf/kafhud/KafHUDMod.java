package com.iamkaf.kafhud;

import com.iamkaf.amber.api.core.v2.AmberInitializer;
import com.iamkaf.amber.api.core.v2.AmberModInfo;
import com.iamkaf.amber.api.platform.v1.Platform;
import com.iamkaf.kafhud.platform.Services;

/**
 * Common entry point for the KafHUD mod.
 * Replace the contents with your own implementation.
 */
public class KafHUDMod {

    /**
     * Called during mod initialization for all loaders.
     */
    public static void init() {
        Constants.LOG.info("Initializing {} on {}...", Constants.MOD_NAME, Services.PLATFORM.getPlatformName());
    }
}
