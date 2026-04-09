package com.iamkaf.kafhud.client;

import com.iamkaf.kafhud.KafHUDClient;
import net.fabricmc.api.ClientModInitializer;

public class KafHUDClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        KafHUDClient.init();
    }
}
