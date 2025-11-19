package com.fabbe50.fabsservertweaks.util;

import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class ServerUtil {
    public static void sendSound(ServerLevel serverLevel, @Nullable Entity player, Entity targetEntity, Holder<SoundEvent> soundEvent, SoundSource soundSource, float volume, float pitch) {
        sendSound(serverLevel, player, targetEntity, soundEvent.value(), soundSource, volume, pitch);
    }

    public static void sendSound(ServerLevel serverLevel, @Nullable Entity player, Entity targetEntity, SoundEvent soundEvent, SoundSource soundSource, float volume, float pitch) {
        serverLevel.playSound(player, targetEntity, soundEvent, soundSource, volume, pitch);
    }

    public static <T extends ParticleOptions> void sendParticle(ServerLevel serverLevel, T particleType, Entity entity, int count, double distance, double maxSpeed) {
        sendParticle(serverLevel, particleType, entity, count, new Vec3(distance, distance, distance), maxSpeed);
    }

    public static <T extends ParticleOptions> void sendParticle(ServerLevel serverLevel, T particleType, Entity entity, Vec3 offset, int count, double distance, double maxSpeed) {
        sendParticle(serverLevel, particleType, entity, offset, count, new Vec3(distance, distance, distance), maxSpeed);
    }

    public static <T extends ParticleOptions> void sendParticle(ServerLevel serverLevel, T particleType, Entity entity, int count, Vec3 distances, double maxSpeed) {
        sendParticle(serverLevel, particleType, entity, Vec3.ZERO, count, distances, maxSpeed);
    }

    public static <T extends ParticleOptions> void sendParticle(ServerLevel serverLevel, T particleType, Entity entity, Vec3 offset, int count, Vec3 distances, double maxSpeed) {
        Vec3 pos = entity.getOnPos().getCenter().add(offset);
        serverLevel.sendParticles(particleType, pos.x(), pos.y(), pos.z(), count, distances.x(), distances.y(), distances.z(), maxSpeed);
    }
}
