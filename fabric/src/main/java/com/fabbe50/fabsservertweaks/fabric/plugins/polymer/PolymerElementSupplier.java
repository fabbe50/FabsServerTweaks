package com.fabbe50.fabsservertweaks.fabric.plugins.polymer;

import eu.pb4.polymer.virtualentity.api.ElementHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;

public interface PolymerElementSupplier {
    ElementHolder createElementHolder(ElementHolder holder, ServerLevel level, BlockPos pos, BlockState initialState);
}
