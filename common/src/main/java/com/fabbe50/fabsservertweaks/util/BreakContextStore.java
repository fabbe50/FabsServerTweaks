package com.fabbe50.fabsservertweaks.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class BreakContextStore {
    private static final Map<Key, Direction> BREAK_FACES = new ConcurrentHashMap<>();

    private BreakContextStore() {
    }

    public static void recordFace(Player player, BlockPos pos, Direction face) {
        BREAK_FACES.put(new Key(player.getUUID(), pos.immutable()), face);
    }

    public static @Nullable Direction getFace(Player player, BlockPos pos) {
        return BREAK_FACES.get(new Key(player.getUUID(), pos));
    }

    public static @Nullable Direction consumeFace(Player player, BlockPos pos) {
        return BREAK_FACES.remove(new Key(player.getUUID(), pos));
    }

    public static void clearPlayer(Player player) {
        UUID playerId = player.getUUID();
        BREAK_FACES.keySet().removeIf(key -> key.playerId.equals(playerId));
    }

    private record Key(UUID playerId, BlockPos pos) {
    }
}
