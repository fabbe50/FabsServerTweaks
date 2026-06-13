package com.fabbe50.fabsservertweaks.client.debug;

import com.fabbe50.fabsservertweaks.Fabsservertweaks;
import com.fabbe50.fabsservertweaks.client.ClientData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.debug.DebugScreenDisplayer;
import net.minecraft.client.gui.components.debug.DebugScreenEntry;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class DebugSlimeChunk implements DebugScreenEntry {
    public static final Identifier GROUP = Fabsservertweaks.location("slime_chunk");

    @Override
    public void display(@NonNull DebugScreenDisplayer debugScreenDisplayer, @Nullable Level level, @Nullable LevelChunk levelChunk, @Nullable LevelChunk levelChunk2) {
        long seed = ClientData.getCurrentSeed();
        String seedString = "Unknown";
        if (seed != 0L) {
            seedString = String.valueOf(seed);
        }
        String seedDisplay = "Seed: " + seedString;
        String slimeChunkDisplay = "Slime Chunk: Unknown";

        final Entity cameraEntity = Minecraft.getInstance().getCameraEntity();
        final IntegratedServer integratedServer = Minecraft.getInstance().getSingleplayerServer();
        final Level serverWorld;
        if (Minecraft.getInstance().level != null) {
            assert cameraEntity != null;
            final BlockPos blockPos = cameraEntity.blockPosition();
            final ChunkPos chunkPos = ChunkPos.containing(blockPos);

            boolean isIntegrated = integratedServer != null;

            serverWorld = isIntegrated ? integratedServer.getLevel(Minecraft.getInstance().level.dimension()) : null;
            if (serverWorld instanceof ServerLevel serverLevel) {
                ClientData.setCurrentSeed(serverLevel.getSeed());
            }

            if (ClientData.getCurrentSeed() != 0L) {
                final RandomSource slimeChunk = WorldgenRandom.seedSlimeChunk(chunkPos.x(), chunkPos.z(), ClientData.getCurrentSeed(), 0x3ad8025fL);
                slimeChunkDisplay = "Slime Chunk: " + ((slimeChunk.nextInt(10) == 0) ? "True" : "False");
            }
        }
        debugScreenDisplayer.addToGroup(GROUP, List.of(seedDisplay, slimeChunkDisplay));
    }
}
