package com.iamkaf.kafhud.client;

import com.iamkaf.kafhud.Constants;
import com.iamkaf.kafhud.KafHUDClient;
import net.minecraftforge.fml.common.Mod;

@Mod(value = Constants.MOD_ID)
public class KafHUDClientForge {
    public KafHUDClientForge() {
        KafHUDClient.init();
    }
}
