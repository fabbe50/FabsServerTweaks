package com.fabbe50.fabsservertweaks.fabric.world.blocks;

import com.fabbe50.fabsservertweaks.fabric.plugins.lootr.PolymerStateSupplier;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class PolymerHorizontalDirectionalTexturedBlock extends PolymerSimpleTexturedBlock {
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;

    public PolymerHorizontalDirectionalTexturedBlock(Properties properties, Block polymerBlock, PolymerStateSupplier stateSupplier) {
        super(properties, polymerBlock, stateSupplier);
    }

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
}
