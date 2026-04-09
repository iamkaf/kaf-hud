package com.iamkaf.kafhud;

import com.iamkaf.amber.api.core.v2.AmberInitializer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(value = Constants.MOD_ID, dist = Dist.CLIENT)
public class KafHUDNeoForge {
    public KafHUDNeoForge(IEventBus eventBus) {
        AmberInitializer.initialize(Constants.MOD_ID);
        KafHUDMod.init();
    }
}
