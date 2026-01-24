package com.fabbe50.fabsservertweaks.fabsservertweaks.client;

import dev.architectury.event.events.client.ClientGuiEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.core.BlockPos;
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

    public static void createDebugInfo() {
        ClientGuiEvent.DEBUG_TEXT_LEFT.register(list -> {
            long seed = ClientData.getCurrentSeed();
            String seedString = "Unknown";
            if (seed != 0L) {
                seedString = String.valueOf(seed);
            }
            list.add("Seed: " + seedString);

            Minecraft client = Minecraft.getInstance();
            final Entity cameraEntity = client.getCameraEntity();
            final IntegratedServer integratedServer = client.getSingleplayerServer();
            final Level serverWorld;
            if (client.level != null) {
                assert cameraEntity != null;
                final BlockPos blockPos = cameraEntity.blockPosition();
                final ChunkPos chunkPos = new ChunkPos(blockPos);

                boolean isIntegrated = integratedServer != null;

                serverWorld = isIntegrated ? integratedServer.getLevel(client.level.dimension()) : null;
                if (serverWorld instanceof ServerLevel serverLevel) {
                    ClientData.setCurrentSeed(serverLevel.getSeed());
                }

                if (ClientData.getCurrentSeed() != 0L) {
                    final RandomSource slimeChunk = WorldgenRandom.seedSlimeChunk(chunkPos.x, chunkPos.z, ClientData.getCurrentSeed(), 0x3ad8025fL);
                    list.add("Slime Chunk: " + ((slimeChunk.nextInt(10) == 0) ? "True" : "False"));
                } else {
                    list.add("Slime Chunk: Unknown");
                }
            }
        });
    }
}
