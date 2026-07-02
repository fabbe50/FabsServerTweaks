package com.fabbe50.fabsservertweaks.fabric.world.blocks;

import com.fabbe50.fabsservertweaks.fabric.plugins.lootr.PolymerStateSupplier;
import eu.pb4.polymer.blocks.api.PolymerTexturedBlock;
import net.fabricmc.fabric.api.networking.v1.context.PacketContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public class SuppliedPolymerTexturedBlock implements PolymerTexturedBlock {
    private final PolymerStateSupplier stateSupplier;
    private Block polymerBlock;

    public SuppliedPolymerTexturedBlock(PolymerStateSupplier stateSupplier) {
        this.stateSupplier = stateSupplier;
        this.polymerBlock = null;
    }

    public SuppliedPolymerTexturedBlock(PolymerStateSupplier stateSupplier, Block polymerBlock) {
        this.stateSupplier = stateSupplier;
        this.polymerBlock = polymerBlock;
    }

    @Override
    public Block getPolymerReplacement(Block block, PacketContext context) {
        return polymerBlock != null ? polymerBlock : PolymerTexturedBlock.super.getPolymerReplacement(block, context);
    }

    @Override
    public BlockState getPolymerBlockState(BlockState state, @Nullable PacketContext context) {
        return stateSupplier.get(state, context);
    }
}
