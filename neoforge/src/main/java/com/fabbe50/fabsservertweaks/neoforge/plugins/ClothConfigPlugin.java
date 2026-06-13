package com.fabbe50.fabsservertweaks.neoforge.plugins;

import com.fabbe50.fabsservertweaks.ModConfig;
import me.shedaniel.autoconfig.AutoConfigClient;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

public class ClothConfigPlugin {
    public static void registerConfigScreen(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, (modContainer, parent) -> AutoConfigClient.getConfigScreen(ModConfig.class, parent).get());
    }
}
