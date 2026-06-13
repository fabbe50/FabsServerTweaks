package com.fabbe50.fabsservertweaks.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public final class PistonBlockEntityTransferStore {
    private static final Map<TransferKey, Snapshot> SNAPSHOTS = new ConcurrentHashMap<>();

    private PistonBlockEntityTransferStore() {
    }

    public static void put(Level level, BlockPos pos, BlockState movedState, CompoundTag tag, DataComponentMap components) {
        SNAPSHOTS.put(new TransferKey(level.dimension(), pos.immutable()), new Snapshot(movedState, tag.copy(), components));
    }

    @Nullable
    public static Snapshot get(Level level, BlockPos pos) {
        return SNAPSHOTS.get(new TransferKey(level.dimension(), pos));
    }

    @Nullable
    public static Snapshot take(Level level, BlockPos pos) {
        return remove(level, pos);
    }

    @Nullable
    public static Snapshot remove(Level level, BlockPos pos) {
        return SNAPSHOTS.remove(new TransferKey(level.dimension(), pos));
    }

    public record Snapshot(BlockState movedState, CompoundTag tag, DataComponentMap components) {
    }

    private record TransferKey(ResourceKey<Level> dimension, BlockPos pos) {
    }
}
