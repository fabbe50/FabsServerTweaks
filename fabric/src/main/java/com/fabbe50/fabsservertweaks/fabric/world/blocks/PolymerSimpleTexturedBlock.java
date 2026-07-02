package com.fabbe50.fabsservertweaks.fabric.world.blocks;

import com.fabbe50.fabsservertweaks.fabric.plugins.lootr.PolymerStateSupplier;
import eu.pb4.polymer.blocks.api.PolymerTexturedBlock;
import eu.pb4.polymer.core.api.block.SimplePolymerBlock;
import net.fabricmc.fabric.api.networking.v1.context.PacketContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public class PolymerSimpleTexturedBlock extends SimplePolymerBlock implements PolymerTexturedBlock {
    private final PolymerStateSupplier stateSupplier;

    public PolymerSimpleTexturedBlock(Properties properties, Block polymerBlock, PolymerStateSupplier stateSupplier) {
        super(properties, polymerBlock);
        this.stateSupplier = stateSupplier;
    }

    @Override
    public BlockState getPolymerBlockState(BlockState state, @Nullable PacketContext context) {
        return stateSupplier.get(state, context);
    }
}
