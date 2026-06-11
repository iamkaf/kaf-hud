package com.iamkaf.kafhud.client;

import com.iamkaf.kafhud.Constants;
import com.iamkaf.kafhud.KafHUDClient;
import com.iamkaf.konfig.neoforge.api.v1.KonfigNeoForgeClientScreens;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(value = Constants.MOD_ID, dist = Dist.CLIENT)
public class KafHUDClientNeoForge {
    public KafHUDClientNeoForge(ModContainer container) {
        KonfigNeoForgeClientScreens.register(container, Constants.MOD_ID);
        KafHUDClient.init();
    }
}
