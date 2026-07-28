package com.fabbe50.fabsservertweaks.events;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import dev.architectury.event.EventResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface ExtendedBlockEvent {
    Event<BlockUpdate> BLOCK_UPDATE = EventFactory.createEventResult();

    interface BlockUpdate {
        EventResult blockUpdate(Level level, BlockPos pos, BlockState state);
    }
}
