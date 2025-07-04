package com.iamkaf.kafhud;

import com.iamkaf.amber.api.core.v2.AmberInitializer;
import com.iamkaf.amber.api.core.v2.AmberModInfo;
import com.iamkaf.amber.api.platform.v1.Platform;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Constants.MOD_ID)
public class KafHUDForge {

    public KafHUDForge(FMLJavaModLoadingContext ctx) {
        var info = Platform.getModInfo(Constants.MOD_ID);
        AmberInitializer.initialize(info.id(), info.name(), info.version(), AmberModInfo.AmberModSide.CLIENT, ctx.getModBusGroup());
        KafHUDMod.init();
        KafHUDClient.init();
    }
}