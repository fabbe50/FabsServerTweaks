package com.fabbe50.fabsservertweaks.events;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import dev.architectury.event.EventResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface CollisionEvent {
    Event<OnCollision> COLLISION_EVENT = EventFactory.createEventResult();

    interface OnCollision {
        EventResult onCollision(Level level, BlockPos pos, BlockState state, Entity entity);
    }
}
