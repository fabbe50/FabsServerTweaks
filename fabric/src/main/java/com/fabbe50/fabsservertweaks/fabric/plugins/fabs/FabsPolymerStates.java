package com.fabbe50.fabsservertweaks.fabric.plugins.fabs;

import com.fabbe50.fabsservertweaks.fabric.plugins.polymer.PolymerStates;
import eu.pb4.polymer.blocks.api.BlockModelType;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.block.state.BlockState;

public class FabsPolymerStates extends PolymerStates {
    public static BlockState CHUNK_LOADER_BLOCK;

    public static void init() {
        CHUNK_LOADER_BLOCK = textured(BlockModelType.getLightningRod(Axis.Y, false), "fabs_polymer_plugin", "block/chunk_loader", 0, 0);
    }
}
