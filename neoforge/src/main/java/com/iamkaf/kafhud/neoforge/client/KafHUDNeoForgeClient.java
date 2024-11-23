package com.iamkaf.kafhud.neoforge.client;

import com.iamkaf.kafhud.KafHUD;
import com.iamkaf.kafhud.KafHUDClient;
import com.iamkaf.kafhud.registry.Keybinds;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

@Mod(value = KafHUD.MOD_ID, dist = Dist.CLIENT)
public class KafHUDNeoForgeClient {
    public KafHUDNeoForgeClient() {
        KafHUDClient.init();
    }

    @EventBusSubscriber(value = Dist.CLIENT)
    static class ClientEvents {
        @SubscribeEvent(priority = EventPriority.HIGH)
        public static void eventRenderGameOverlayEvent(RenderGuiEvent.Post event) {
            KafHUDClient.onRenderHUD(event.getGuiGraphics(), event.getPartialTick());
        }

        @SubscribeEvent
        public static void eventClientTickEventPost(ClientTickEvent.Post event) {
            KafHUDClient.onClientTick();
        }
    }

    @EventBusSubscriber(value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
    static class ModClientEvents {
        @SubscribeEvent
        public static void eventRegisterKeyMappingsEvent(RegisterKeyMappingsEvent event) {
            event.register(Keybinds.TOGGLE_HUD);
        }
    }
}
