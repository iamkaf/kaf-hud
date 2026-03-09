package com.iamkaf.kafhud.client;

import com.iamkaf.kafhud.Constants;
import com.iamkaf.kafhud.KafHUDClient;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;

@Mod(value = Constants.MOD_ID, dist = Dist.CLIENT)
public class KafHUDClientNeoForge {
    public KafHUDClientNeoForge() {
        KafHUDClient.init();
    }
}
