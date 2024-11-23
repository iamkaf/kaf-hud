package com.iamkaf.kafhud;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

public class KafHUD {
    public static final String MOD_ID = "kafhud";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static void init() {
        LOGGER.info("Where Am I?");
    }
}
