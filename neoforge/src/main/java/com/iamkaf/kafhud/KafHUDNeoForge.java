package com.iamkaf.kafhud;

import com.iamkaf.amber.api.core.v2.AmberInitializer;
import com.iamkaf.amber.api.core.v2.AmberModInfo;
import com.iamkaf.amber.api.platform.v1.Platform;
import net.minecraft.world.InteractionResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import static net.minecraft.world.InteractionResult.*;

@Mod(value = Constants.MOD_ID, dist = Dist.CLIENT)
public class KafHUDNeoForge {
    public KafHUDNeoForge(IEventBus eventBus) {
        var info = Platform.getModInfo(Constants.MOD_ID);
        AmberInitializer.initialize(info.id(), info.name(), info.version(), AmberModInfo.AmberModSide.CLIENT, eventBus);
        KafHUDMod.init();
        KafHUDClient.init();
    }
}