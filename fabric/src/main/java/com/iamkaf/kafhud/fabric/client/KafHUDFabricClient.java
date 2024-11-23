package com.iamkaf.kafhud.fabric.client;

import com.iamkaf.kafhud.KafHUDClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.Minecraft;

public final class KafHUDFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        KafHUDClient.init();
        HudRenderCallback.EVENT.register(KafHUDClient::onRenderHUD);
        ClientTickEvents.END_CLIENT_TICK.register(this::onClientTick);
    }

    private void onClientTick(Minecraft minecraft) {
        KafHUDClient.onClientTick();
    }
}
