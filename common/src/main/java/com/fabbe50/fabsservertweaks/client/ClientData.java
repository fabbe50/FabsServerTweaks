package com.fabbe50.fabsservertweaks.client;

import com.fabbe50.fabsservertweaks.Fabsservertweaks;
import com.fabbe50.fabsservertweaks.client.debug.DebugSlimeChunk;
import dev.architectury.event.events.client.ClientGuiEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.debug.DebugScreenEntries;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.WorldgenRandom;

public class ClientData {
    private static long currentSeed = 0L;

    public static void setCurrentSeed(long seed) {
        currentSeed = seed;
    }

    public static long getCurrentSeed() {
        return currentSeed;
    }

    public static final Identifier SLIME_CHUNK = DebugScreenEntries.register(Fabsservertweaks.location("slime_chunk"), new DebugSlimeChunk());
}
