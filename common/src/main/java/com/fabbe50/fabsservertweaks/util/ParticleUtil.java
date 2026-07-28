package com.fabbe50.fabsservertweaks.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.phys.Vec3;

import java.util.function.Supplier;

public class ParticleUtil {
    public static void spawnParticlesOnBlockFaces(final ServerLevel level, final BlockPos pos, final ParticleOptions particle, final IntProvider particlesPerFaceRange) {
        RandomSource random = level.getRandom();

        for(Direction direction : Direction.values()) {
            spawnParticlesOnBlockFace(level, pos, particle, particlesPerFaceRange, direction, () -> getRandomSpeedRanges(random), 0.55);
        }

    }

    private static Vec3 getRandomSpeedRanges(final RandomSource random) {
        return new Vec3(Mth.nextDouble(random, -0.5F, 0.5F), Mth.nextDouble(random, -0.5F, 0.5F), Mth.nextDouble(random, -0.5F, 0.5F));
    }

    public static void spawnParticlesOnBlockFace(final ServerLevel level, final BlockPos pos, final ParticleOptions particle, final IntProvider particlesPerFaceRange, final Direction face, final Supplier<Vec3> speedSupplier, final double stepFactor) {
        int particleCount = particlesPerFaceRange.sample(level.getRandom());
        spawnParticleOnFace(level, pos, face, particleCount, particle, speedSupplier.get(), stepFactor);
    }

    public static void spawnParticleOnFace(final ServerLevel level, final BlockPos pos, final Direction face, final int particleCount, final ParticleOptions particle, final Vec3 speed, final double stepFactor) {
        Vec3 centerOfBlock = Vec3.atCenterOf(pos);
        int stepX = face.getStepX();
        int stepY = face.getStepY();
        int stepZ = face.getStepZ();
        RandomSource random = level.getRandom();
        double x = centerOfBlock.x + (stepX == 0 ? Mth.nextDouble(random, -0.5F, 0.5F) : (double)stepX * stepFactor);
        double y = centerOfBlock.y + (stepY == 0 ? Mth.nextDouble(random, -0.5F, 0.5F) : (double)stepY * stepFactor);
        double z = centerOfBlock.z + (stepZ == 0 ? Mth.nextDouble(random, -0.5F, 0.5F) : (double)stepZ * stepFactor);
        double xBaseSpeed = stepX == 0 ? speed.x() : (double)0.0F;
        double yBaseSpeed = stepY == 0 ? speed.y() : (double)0.0F;
        double zBaseSpeed = stepZ == 0 ? speed.z() : (double)0.0F;
        double averageLifetime = 1.1d;
        double xDist = xBaseSpeed * averageLifetime;
        double yDist = yBaseSpeed * averageLifetime;
        double zDist = zBaseSpeed * averageLifetime;
        double averageSpeed = (xBaseSpeed + yBaseSpeed + zBaseSpeed) / 3;
        level.sendParticles(particle, x, y, z, particleCount, xDist, yDist, zDist, averageSpeed);
    }

    public static void spawnParticleExplodeSphere(final ServerLevel level, final Vec3 pos, final ParticleOptions particle, final int particleCount, final double radius, final double speed) {
        int particleCountHalf = particleCount / 2;
        spawnParticleExplodeUpperSphere(level, pos, particle, particleCountHalf, radius, speed);
        spawnParticleExplodeLowerSphere(level, pos, particle, particleCountHalf, radius, speed);
    }

    public static void spawnParticleExplodeUpperSphere(final ServerLevel level, final Vec3 pos, final ParticleOptions particle, final int particleCount, final double radius, final double speed) {
        double xDist = level.getRandom().nextDouble() * radius;
        double yDist = level.getRandom().nextDouble() * radius;
        double zDist = level.getRandom().nextDouble() * radius;
        level.sendParticles(particle, pos.x(), pos.y(), pos.z(), particleCount, xDist, yDist, zDist, speed);
    }

    public static void spawnParticleExplodeLowerSphere(final ServerLevel level, final Vec3 pos, final ParticleOptions particle, final int particleCount, final double radius, final double speed) {
        double xDist = -(level.getRandom().nextDouble() * radius);
        double yDist = -(level.getRandom().nextDouble() * radius);
        double zDist = -(level.getRandom().nextDouble() * radius);
        level.sendParticles(particle, pos.x(), pos.y(), pos.z(), particleCount, xDist, yDist, zDist, speed);
    }
}
