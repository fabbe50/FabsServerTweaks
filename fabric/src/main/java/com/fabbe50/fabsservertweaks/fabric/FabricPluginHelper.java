package com.fabbe50.fabsservertweaks.fabric;

import net.fabricmc.loader.api.FabricLoader;

public class FabricPluginHelper {
    public static final String POLYMER_CORE_ID = "polymer-core";
    public static final String POLYMER_BLOCKS_ID = "polymer-blocks";
    public static final String POLYMER_RESOURCE_PACK_ID = "polymer-resource-pack";
    public static final String POLYMER_AUTOHOST_ID = "polymer-autohost";
    public static final String POLYMER_VIRTUAL_ENTITY_ID = "polymer-virtual-entity";
    public static boolean arePolymerComponentsLoaded() {
        return isModLoaded(POLYMER_CORE_ID) && isModLoaded(POLYMER_BLOCKS_ID) && isModLoaded(POLYMER_RESOURCE_PACK_ID) && isModLoaded(POLYMER_AUTOHOST_ID) && isModLoaded(POLYMER_VIRTUAL_ENTITY_ID);
    }

    public static boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }
}
