package com.fabbe50.fabsservertweaks.events;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import dev.architectury.event.EventResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;

public interface BedEvents {
    Event<StartSleeping> START_SLEEPING = EventFactory.createEventResult();
    Event<StopSleeping> STOP_SLEEPING = EventFactory.createEventResult();

    interface StartSleeping {
        EventResult sleeping(LivingEntity livingEntity, BlockPos pos);
    }

    interface StopSleeping {
        EventResult wakeup(LivingEntity livingEntity);
    }
}
