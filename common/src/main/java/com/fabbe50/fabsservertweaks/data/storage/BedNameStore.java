package com.fabbe50.fabsservertweaks.data.storage;

import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.network.chat.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BedNameStore {
    private static final Map<GlobalPos, Component> BED_NAMES = new ConcurrentHashMap<>();

    private static GlobalPos key(Level level, BlockPos pos) {
        ResourceKey<Level> dim = level.dimension();
        return GlobalPos.of(dim, pos.immutable());
    }

    public static void put(Level level, BlockPos basePos, Component name) {
        BED_NAMES.put(key(level, basePos), name);
    }

    public static Component get(Level level, BlockPos basePos) {
        return BED_NAMES.get(key(level, basePos));
    }

    public static Component getAndRemove(Level level, BlockPos basePos) {
        return BED_NAMES.remove(key(level, basePos));
    }
}
