package com.fabbe50.fabsservertweaks.fabric.plugins;

import com.fabbe50.fabsservertweaks.Fabsservertweaks;
import com.fabbe50.fabsservertweaks.LogUtil;
import com.fabbe50.fabsservertweaks.fabric.FabricPluginHelper;
import com.fabbe50.fabsservertweaks.fabric.plugins.base.Plugin;
import com.fabbe50.fabsservertweaks.fabric.plugins.fabs.FabsPolymerPlugin;
import com.fabbe50.fabsservertweaks.fabric.plugins.lootr.LootrPlugin;
import com.fabbe50.fabsservertweaks.fabric.plugins.resource_nether_ores.ResourceNetherOresPlugin;
import com.fabbe50.fabsservertweaks.fabric.plugins.universal_ores.UniversalOresPlugin;
import com.fabbe50.fabsservertweaks.fabric.plugins.vanilla.VanillaPlugin;

import java.util.LinkedList;

public class Plugins {
    private static final LinkedList<Plugin> plugins = new LinkedList<>();

    public static void init() {
        if (FabricPluginHelper.arePolymerComponentsLoaded()) {
            plugins.add(new FabsPolymerPlugin());
            plugins.add(new VanillaPlugin());
        }
        if (FabricPluginHelper.areLootrPluginComponentsLoaded() && Fabsservertweaks.CONFIG.enableLootrPolymerPlugin) {
            plugins.add(new LootrPlugin());
        } else {
            LogUtil.warn("Lootr plugin disabled or components are not loaded, disabling Lootr support.");
        }
        if (FabricPluginHelper.areUniversalOresComponentsLoaded() && Fabsservertweaks.CONFIG.enableUniversalOresPlugin) {
            plugins.add(new UniversalOresPlugin());
        } else {
            LogUtil.warn("Universal Ores plugin disabled or components are not loaded, disabling Universal Ores support.");
        }
        if (FabricPluginHelper.areResourceNetherOresComponentsLoaded() && Fabsservertweaks.CONFIG.enableResourceNetherOresPlugin) {
            plugins.add(new ResourceNetherOresPlugin());
        } else {
            LogUtil.warn("Resource Nether Ores plugin disabled or components are not loaded, disabling Resource Nether Ores support.");
        }

        plugins.forEach(Plugin::initialize);
    }
}
