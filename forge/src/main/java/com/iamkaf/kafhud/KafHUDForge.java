package com.iamkaf.kafhud;

import com.iamkaf.amber.api.core.v2.AmberInitializer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Constants.MOD_ID)
public class KafHUDForge {

    public KafHUDForge(FMLJavaModLoadingContext ctx) {
        AmberInitializer.initialize(Constants.MOD_ID);
        KafHUDMod.init();
        KafHUDClient.init();
    }
}