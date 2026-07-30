package com.gembina.heartoverlay;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import com.gembina.heartoverlay.client.ClientSetup;

@Mod(HeartOverlayMod.MODID)
public class HeartOverlayMod {
    public static final String MODID = "gembinasrca";

    public HeartOverlayMod() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::clientSetup);
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        ClientSetup.init();
    }
}
