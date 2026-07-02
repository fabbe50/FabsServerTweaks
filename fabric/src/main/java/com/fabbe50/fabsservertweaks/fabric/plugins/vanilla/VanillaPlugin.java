package com.fabbe50.fabsservertweaks.fabric.plugins.vanilla;

import com.fabbe50.fabsservertweaks.LogUtil;
import com.fabbe50.fabsservertweaks.fabric.plugins.base.Plugin;
import com.fabbe50.fabsservertweaks.fabric.plugins.polymer.PolymerPlugin;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;

public class VanillaPlugin extends Plugin {

    public void init() {
        LogUtil.log("Initializing Vanilla Plugin");

        PolymerResourcePackUtils.RESOURCE_PACK_CREATION_EVENT.register(resourcePackBuilder -> {
            PolymerPlugin.addAsset(resourcePackBuilder, "resourcepacks/vanilla_plugin/assets", "minecraft", "items/campfire.json");
            PolymerPlugin.addAsset(resourcePackBuilder, "resourcepacks/vanilla_plugin/assets", "minecraft", "items/soul_campfire.json");
            PolymerPlugin.addAsset(resourcePackBuilder, "resourcepacks/vanilla_plugin/assets", "minecraft", "models/item/unlit_campfire.json");
            PolymerPlugin.addAsset(resourcePackBuilder, "resourcepacks/vanilla_plugin/assets", "minecraft", "models/item/unlit_soul_campfire.json");
            PolymerPlugin.addAsset(resourcePackBuilder, "resourcepacks/vanilla_plugin/assets", "minecraft", "textures/item/unlit_campfire.png");
            PolymerPlugin.addAsset(resourcePackBuilder, "resourcepacks/vanilla_plugin/assets", "minecraft", "textures/item/unlit_soul_campfire.png");
        });
    }
}
