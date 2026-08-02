package com.fabbe50.fabsservertweaks.events;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import dev.architectury.event.EventResult;
import net.minecraft.world.entity.LivingEntity;

public interface ExtendedEntityEvent {
    Event<OnEntityTick> PRE_ENTITY_TICK = EventFactory.createEventResult();
    Event<OnEntityTick> POST_ENTITY_TICK = EventFactory.createEventResult();

    interface OnEntityTick {
        EventResult onEntityTick(LivingEntity entity);
    }
}
