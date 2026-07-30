package com.gembina.heartoverlay.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.gembina.heartoverlay.HeartOverlayMod;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.client.gui.GuiComponent;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.resources.ResourceLocation;

@Mod.EventBusSubscriber(modid = HeartOverlayMod.MODID, value = Dist.CLIENT)
public class RenderEvents {

    // GUI icons texture (vanilla)
    private static final ResourceLocation ICONS = GuiComponent.GUI_ICONS_LOCATION; // "textures/gui/icons.png"

    @SubscribeEvent
    public static void onRenderLivingPost(RenderLivingEvent.Post<LivingEntity, ?> event) {
        if (!OverlayConfig.enabled) return;

        LivingEntity entity = event.getEntity();
        if (entity == null) return;
        if (entity.isInvisible()) return;

        boolean isPlayer = entity instanceof net.minecraft.world.entity.player.Player;
        if (isPlayer && !OverlayConfig.showPlayers) return;
        if (!isPlayer && !OverlayConfig.showMobs) return;

        Minecraft mc = Minecraft.getInstance();

        float health = entity.getHealth();
        float absorption = entity.getAbsorptionAmount();

        int redHearts = Mth.ceil(health / 2.0f);
        int goldHearts = Mth.ceil(absorption / 2.0f);

        if (redHearts <= 0 && goldHearts <= 0) return;

        PoseStack ms = event.getPoseStack();

        ms.pushPose();
        double yOffset = entity.getBbHeight() + 0.5D; // iznad glave
        ms.translate(0.0D, yOffset, 0.0D);

        // Okreni prema kameri
        ms.mulPose(mc.getEntityRenderDispatcher().cameraOrientation());

        // Skaliranje
        float scale = OverlayConfig.scale; // podesivo
        ms.scale(-scale, -scale, scale);

        // Pripremi teksturu ikonica
        RenderSystem.setShaderTexture(0, ICONS);

        // width of one heart icon in pixels (vanilla hearts are 9x9?) we'll blit 9x9
        int iconWidth = 9;
        int iconHeight = 9;

        // Prikaz crvenih srca
        int toShowRed = Math.min(redHearts, OverlayConfig.maxHeartsToShow);
        int toShowGold = Math.min(goldHearts, OverlayConfig.maxHeartsToShow);

        // Spacing i centriranje
        int totalWidth = (toShowRed > 0 ? toShowRed : toShowGold) * (iconWidth);
        float startX = -totalWidth / 2.0f;

        // crvena srca: u icons.png lokacija srca u GUI ikonicama
        // U vanilnom icons.png srca obično na u=16,v=0 (solid) i u=52??? (partial). Koordinate se razlikuju,
        // ali najčešće srca se nalaze na (16,0) za empty/half/full hearts. Ovde koristimo u=16,v=0 kao približno.

        int heartU = 16; // približno
        int heartV = 0;

        ms.pushPose();
        ms.translate(startX, -10f, 0.0f); // pomeri gore i levo koliko treba

        for (int i = 0; i < toShowRed; i++) {
            int x = i * iconWidth;
            // Blit heart icon (full)
            GuiComponent.blit(ms, (int)x, 0, heartU, heartV, iconWidth, iconHeight, 256, 256);
        }
        ms.popPose();

        // Zlatna srca: koristimo u=52,v=0 kao približno
        if (toShowGold > 0) {
            int goldU = 52;
            int goldV = 0;
            ms.pushPose();
            ms.translate(startX, 0f, 0.0f);
            for (int i = 0; i < toShowGold; i++) {
                int x = i * iconWidth;
                GuiComponent.blit(ms, (int)x, 0, goldU, goldV, iconWidth, iconHeight, 256, 256);
            }
            ms.popPose();
        }

        // Opcionalno: prikaži broj HP ispod srca
        if (OverlayConfig.showNumbers) {
            ms.pushPose();
            ms.translate(-20f, 10f, 0.0f);
            String s = String.format("HP: %.1f", health);
            mc.font.drawShadow(ms, Component.literal(s), 0f, 0f, 0xFFFFFF);
            ms.popPose();
        }

        ms.popPose();
    }
}
