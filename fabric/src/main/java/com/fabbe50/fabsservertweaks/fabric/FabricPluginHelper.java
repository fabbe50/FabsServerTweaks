package com.fabbe50.fabsservertweaks.fabric;

import net.fabricmc.loader.api.FabricLoader;

public class FabricPluginHelper {
    public static final String LOOTR_ID = "lootr";
    public static final String UNIVERSAL_ORES_ID = "universal_ores";
    public static final String POLYMER_CORE_ID = "polymer-core";
    public static final String POLYMER_BLOCKS_ID = "polymer-blocks";
    public static final String POLYMER_RESOURCE_PACK_ID = "polymer-resource-pack";
    public static final String POLYMER_AUTOHOST_ID = "polymer-autohost";
    public static final String POLYMER_VIRTUAL_ENTITY_ID = "polymer-virtual-entity";
    public static final String LOOTR_POLYMER_ID = "lootr_polymer";
    public static final String RESOURCE_LIBRARY_ID = "resourcelibrary";
    public static final String RESOURCE_NETHER_ORES_ID = "resource_nether_ores";

    public static boolean areLootrPluginComponentsLoaded() {
        return isModLoaded(LOOTR_ID) && arePolymerComponentsLoaded() && !isModLoaded(LOOTR_POLYMER_ID);
    }

    public static boolean areUniversalOresComponentsLoaded() {
        return isModLoaded(UNIVERSAL_ORES_ID) && arePolymerComponentsLoaded();
    }

    public static boolean areResourceNetherOresComponentsLoaded() {
        return isModLoaded(RESOURCE_NETHER_ORES_ID) && isModLoaded(RESOURCE_LIBRARY_ID) && arePolymerComponentsLoaded();
    }

    public static boolean arePolymerComponentsLoaded() {
        return isModLoaded(POLYMER_CORE_ID) && isModLoaded(POLYMER_BLOCKS_ID) && isModLoaded(POLYMER_RESOURCE_PACK_ID) && isModLoaded(POLYMER_AUTOHOST_ID) && isModLoaded(POLYMER_VIRTUAL_ENTITY_ID);
    }

    public static boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }
}
