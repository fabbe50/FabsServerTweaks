package com.fabbe50.fabsservertweaks.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public final class PistonChestSourceStateStore {
    private static final Map<StateKey, StateEntry> STATES = new ConcurrentHashMap<>();

    private PistonChestSourceStateStore() {
    }

    public static void put(Level level, BlockPos pos, BlockState state) {
        STATES.put(new StateKey(level.dimension(), pos.immutable()), new StateEntry(state, level.getGameTime()));
    }

    @Nullable
    public static BlockState get(Level level, BlockPos pos) {
        StateKey key = new StateKey(level.dimension(), pos);
        StateEntry entry = STATES.get(key);
        if (entry == null) {
            return null;
        }

        if (entry.gameTime != level.getGameTime()) {
            STATES.remove(key);
            return null;
        }

        return entry.state;
    }

    private record StateKey(ResourceKey<Level> dimension, BlockPos pos) {
    }

    private record StateEntry(BlockState state, long gameTime) {
    }
}
