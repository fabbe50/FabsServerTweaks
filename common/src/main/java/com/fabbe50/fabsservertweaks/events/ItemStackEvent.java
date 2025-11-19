package com.fabbe50.fabsservertweaks.events;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import net.minecraft.world.item.ItemStack;

public interface ItemStackEvent {
    Event<OnCreated> CREATED = EventFactory.createEventResult();

    interface OnCreated {
        void create(ItemStack stack);
    }
}
