package com.fabbe50.fabsservertweaks.fabric.plugins.lootr;

import net.fabricmc.fabric.api.networking.v1.context.PacketContext;
import net.minecraft.world.level.block.state.BlockState;

public interface PolymerStateSupplier {
    BlockState get(BlockState state, PacketContext context);
}
