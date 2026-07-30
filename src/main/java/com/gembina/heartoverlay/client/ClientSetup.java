package com.gembina.heartoverlay.client;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.ClientRegistry;
import org.lwjgl.glfw.GLFW;

public class ClientSetup {
    public static final KeyMapping TOGGLE_KEY = new KeyMapping("key.gembinasrca.toggle", GLFW.GLFW_KEY_R, "key.categories.misc");

    public static void init() {
        ClientRegistry.registerKeyBinding(TOGGLE_KEY);
    }
}
