package com.fabbe50.fabsservertweaks.fabric.plugins.fabs;

import com.fabbe50.fabsservertweaks.fabric.plugins.polymer.PolymerStates;
import eu.pb4.polymer.blocks.api.BlockModelType;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.block.state.BlockState;

public class FabsPolymerStates extends PolymerStates {
    public static BlockState CHUNK_LOADER_BLOCK;
    public static BlockState BROKEN_ANVIL_BLOCK_NORTH;
    public static BlockState BROKEN_ANVIL_BLOCK_EAST;
    public static BlockState BROKEN_ANVIL_BLOCK_SOUTH;
    public static BlockState BROKEN_ANVIL_BLOCK_WEST;

    public static void init() {
        CHUNK_LOADER_BLOCK = textured(BlockModelType.getLightningRod(Axis.Y, false), "fabs_polymer_plugin", "block/chunk_loader", 0, 0);
        BROKEN_ANVIL_BLOCK_NORTH = textured(BlockModelType.LEAVES, "fabs_polymer_plugin", "block/broken_anvil", 0, 180);
        BROKEN_ANVIL_BLOCK_EAST = textured(BlockModelType.LEAVES, "fabs_polymer_plugin", "block/broken_anvil", 0, 270);
        BROKEN_ANVIL_BLOCK_SOUTH = textured(BlockModelType.LEAVES, "fabs_polymer_plugin", "block/broken_anvil", 0, 0);
        BROKEN_ANVIL_BLOCK_WEST = textured(BlockModelType.LEAVES, "fabs_polymer_plugin", "block/broken_anvil", 0, 90);
    }

    public static BlockState getAnvilBlockState(Direction facing) {
        return switch (facing) {
            case EAST -> BROKEN_ANVIL_BLOCK_EAST;
            case SOUTH -> BROKEN_ANVIL_BLOCK_SOUTH;
            case WEST -> BROKEN_ANVIL_BLOCK_WEST;
            default -> BROKEN_ANVIL_BLOCK_NORTH;
        };
    }
}
