package com.fabbe50.fabsservertweaks.fabric.plugins.vanilla;

import com.fabbe50.fabsservertweaks.LogUtil;
import com.fabbe50.fabsservertweaks.fabric.plugins.base.PolymerPlugin;
import com.fabbe50.fabsservertweaks.fabric.plugins.polymer.PolymerRegistry;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;

public class VanillaPlugin extends PolymerPlugin {
    public void init() {
        LogUtil.log("Initializing Vanilla Plugin");

        PolymerResourcePackUtils.RESOURCE_PACK_CREATION_EVENT.register(resourcePackBuilder -> {
            PolymerRegistry.addAsset(resourcePackBuilder, "resourcepacks/vanilla_plugin/assets", "minecraft", "items/campfire.json");
            PolymerRegistry.addAsset(resourcePackBuilder, "resourcepacks/vanilla_plugin/assets", "minecraft", "items/soul_campfire.json");
            PolymerRegistry.addAsset(resourcePackBuilder, "resourcepacks/vanilla_plugin/assets", "minecraft", "models/item/unlit_campfire.json");
            PolymerRegistry.addAsset(resourcePackBuilder, "resourcepacks/vanilla_plugin/assets", "minecraft", "models/item/unlit_soul_campfire.json");
            PolymerRegistry.addAsset(resourcePackBuilder, "resourcepacks/vanilla_plugin/assets", "minecraft", "textures/item/unlit_campfire.png");
            PolymerRegistry.addAsset(resourcePackBuilder, "resourcepacks/vanilla_plugin/assets", "minecraft", "textures/item/unlit_soul_campfire.png");
        });
    }
}
