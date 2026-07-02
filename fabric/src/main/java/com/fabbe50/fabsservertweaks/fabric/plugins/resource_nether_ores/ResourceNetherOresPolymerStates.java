package com.fabbe50.fabsservertweaks.fabric.plugins.resource_nether_ores;

import com.fabbe50.fabsservertweaks.fabric.FabricPluginHelper;
import com.fabbe50.fabsservertweaks.fabric.plugins.polymer.PolymerStates;
import eu.pb4.polymer.blocks.api.BlockModelType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.LinkedHashMap;

public class ResourceNetherOresPolymerStates extends PolymerStates {
    private static final LinkedHashMap<Block, BlockState> BLOCK_STATE_MAP = new LinkedHashMap<>();

    public static void init() {
        for (Block block : ResourceNetherOresPlugin.RESOURCE_NETHER_ORES_BLOCKS_WITH_FALLBACK.keySet()) {
            BLOCK_STATE_MAP.put(block, textured(BlockModelType.FULL_BLOCK, FabricPluginHelper.RESOURCE_NETHER_ORES_ID, "block/" + BuiltInRegistries.BLOCK.getKey(block).getPath(), 0, 0));
        }
    }

    public static BlockState getBlockState(Block block) {
        return BLOCK_STATE_MAP.get(block);
    }
}
