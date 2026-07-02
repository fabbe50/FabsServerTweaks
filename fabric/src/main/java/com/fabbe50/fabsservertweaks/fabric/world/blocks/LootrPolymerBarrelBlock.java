package com.fabbe50.fabsservertweaks.fabric.world.blocks;

import com.fabbe50.fabsservertweaks.fabric.plugins.lootr.PolymerStateSupplier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class LootrPolymerBarrelBlock extends PolymerDirectionalTexturedBlock {
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;

    public LootrPolymerBarrelBlock(Properties properties, Block polymerBlock, PolymerStateSupplier stateSupplier) {
        super(properties, polymerBlock, stateSupplier);
    }

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(OPEN);
    }
}
