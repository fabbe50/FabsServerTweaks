package com.fabbe50.fabsservertweaks.neoforge.event;

import com.fabbe50.fabsservertweaks.util.BuiltinDatapack;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.PackSource;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.AddPackFindersEvent;

public final class PackEvents {
    private PackEvents() {
    }

    @SubscribeEvent
    public static void addPackFinders(AddPackFindersEvent event) {
        if (event.getPackType() != PackType.SERVER_DATA) {
            return;
        }

        for (BuiltinDatapack builtinDatapack : BuiltinDatapack.values()) {
            event.addPackFinders(
                    builtinDatapack.id(),
                    PackType.SERVER_DATA,
                    builtinDatapack.displayName(),
                    (builtinDatapack.enabledByDefault() ? PackSource.SERVER : PackSource.FEATURE),
                    false,
                    builtinDatapack.position()
            );
        }
    }
}
