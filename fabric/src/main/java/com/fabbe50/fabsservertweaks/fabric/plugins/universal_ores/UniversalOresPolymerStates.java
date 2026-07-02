package com.fabbe50.fabsservertweaks.fabric.plugins.universal_ores;

import com.fabbe50.fabsservertweaks.fabric.FabricPluginHelper;
import com.fabbe50.fabsservertweaks.fabric.plugins.polymer.PolymerStates;
import eu.pb4.polymer.blocks.api.BlockModelType;
import fr.hugman.universal_ores.block.DropExperienceRotatedPillarBlock;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.LinkedHashMap;
import java.util.Map;

public class UniversalOresPolymerStates extends PolymerStates {
    private static final LinkedHashMap<Block, LinkedHashMap<Axis, BlockState>> BLOCK_STATE_MAP = new LinkedHashMap<>();

    public static void init() {
        for (Block block : UniversalOresPlugin.UNIVERSAL_ORES_BLOCKS_WITH_FALLBACK.keySet()) {
            if (block instanceof DropExperienceRotatedPillarBlock pillarBlock) {
                LinkedHashMap<Axis, BlockState> axisMap = new LinkedHashMap<>();
                for (Axis axis : Axis.values()) {
                    switch (axis) {
                        case X -> axisMap.put(
                                Axis.X,
                                textured(BlockModelType.FULL_BLOCK, FabricPluginHelper.UNIVERSAL_ORES_ID, "block/" + BuiltInRegistries.BLOCK.getKey(pillarBlock).getPath(), 90, 90)
                        );
                        case Y -> axisMap.put(
                                Axis.Y,
                                textured(BlockModelType.FULL_BLOCK, FabricPluginHelper.UNIVERSAL_ORES_ID, "block/" + BuiltInRegistries.BLOCK.getKey(pillarBlock).getPath(), 0, 0)
                        );
                        case Z -> axisMap.put(
                                Axis.Z,
                                textured(BlockModelType.FULL_BLOCK, FabricPluginHelper.UNIVERSAL_ORES_ID, "block/" + BuiltInRegistries.BLOCK.getKey(pillarBlock).getPath(), 90, 0)
                        );
                    }
                }
                BLOCK_STATE_MAP.put(block, axisMap);
            } else {
                BLOCK_STATE_MAP.put(
                        block,
                        new LinkedHashMap<>(Map.of(Axis.Y, textured(BlockModelType.FULL_BLOCK, FabricPluginHelper.UNIVERSAL_ORES_ID, "block/" + BuiltInRegistries.BLOCK.getKey(block).getPath(), 0, 0)))
                );
            }
        }
    }

    public static BlockState getBlockState(Block block, Axis axis) {
        return BLOCK_STATE_MAP.get(block).get(axis);
    }

    public static BlockState getBlockState(Block block) {
        return BLOCK_STATE_MAP.get(block).get(Axis.Y);
    }
}
