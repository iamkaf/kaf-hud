package com.iamkaf.kafhud;

import com.iamkaf.amber.api.core.v2.AmberInitializer;
import com.iamkaf.amber.api.core.v2.AmberModInfo;
import com.iamkaf.amber.api.platform.v1.Platform;
import com.iamkaf.kafhud.registry.Keybinds;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;

/**
 * Fabric entry point.
 */
public class KafHUDFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        var info = Platform.getModInfo(Constants.MOD_ID);
        AmberInitializer.initialize(info.id(), info.name(), info.version(), AmberModInfo.AmberModSide.CLIENT, null);
        KafHUDMod.init();
    }
}
