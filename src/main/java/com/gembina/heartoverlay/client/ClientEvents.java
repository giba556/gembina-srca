package com.gembina.heartoverlay.client;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import com.gembina.heartoverlay.HeartOverlayMod;

@Mod.EventBusSubscriber(modid = HeartOverlayMod.MODID, value = net.minecraftforge.api.distmarker.Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (ClientSetup.TOGGLE_KEY.consumeClick()) {
            OverlayConfig.enabled = !OverlayConfig.enabled;
        }
    }
}
