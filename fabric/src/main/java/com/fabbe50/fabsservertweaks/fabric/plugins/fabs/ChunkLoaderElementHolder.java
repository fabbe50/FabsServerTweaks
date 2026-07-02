package com.fabbe50.fabsservertweaks.fabric.plugins.fabs;

import com.fabbe50.fabsservertweaks.fabric.plugins.polymer.PolymerElementHolder;
import com.fabbe50.fabsservertweaks.util.WorldUtil;
import eu.pb4.polymer.virtualentity.api.attachment.HolderAttachment;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;

public class ChunkLoaderElementHolder extends PolymerElementHolder {
    private static final int PARTICLE_TICK = 40;
    private int tick = 0;

    @Override
    protected void onTick() {
        tick++;
        if (tick < PARTICLE_TICK) {
            return;
        }
        tick = 0;

        HolderAttachment attachment = getAttachment();
        if (attachment == null) {
            return;
        }

        ServerLevel level = attachment.getWorld();
        Vec3 vec3 = attachment.getPos();
        BlockPos pos = BlockPos.containing(vec3);

        Vec3 particlePos = pos.getBottomCenter().add(0, 0.5, 0);
        Vec3 particleMotion = new Vec3((level.getRandom().nextDouble() - 0.5) / 2, (level.getRandom().nextDouble() - 0.5) / 2, (level.getRandom().nextDouble() - 0.5) / 2);
        double speed = level.getRandom().nextInt(10) / 100d;

        getWatchingPlayers().forEach(listener -> {
            if (WorldUtil.isChunkLoaded(level, pos)) {
                level.sendParticles(
                        listener.getPlayer(),
                        ParticleTypes.HAPPY_VILLAGER,
                        false, false,
                        particlePos.x(), particlePos.y(), particlePos.z(),
                        8,
                        particleMotion.x(), particleMotion.y(), particleMotion.z(),
                        speed
                );
            }
        });
    }
}
