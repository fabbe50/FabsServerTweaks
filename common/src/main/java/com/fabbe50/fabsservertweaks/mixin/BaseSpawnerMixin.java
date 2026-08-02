package com.fabbe50.fabsservertweaks.mixin;

import com.fabbe50.fabsservertweaks.registries.ModGameRules;
import com.fabbe50.fabsservertweaks.util.ParticleUtil;
import com.fabbe50.fabsservertweaks.util.SpawnerUtil;
import com.fabbe50.fabsservertweaks.util.WorldUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedList;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.SpawnData;
import org.apache.commons.compress.harmony.unpack200.bytecode.forms.IMethodRefForm;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BaseSpawner.class)
public abstract class BaseSpawnerMixin {
    @Shadow
    public int requiredPlayerRange;

    @Shadow
    private WeightedList<SpawnData> spawnPotentials;

    @Shadow
    protected abstract SpawnData getOrCreateNextSpawnData(@Nullable Level level, RandomSource random, BlockPos pos);

    @Unique
    public int tick;

    @Inject(method = "isNearPlayer", at = @At("HEAD"), cancellable = true)
    private void injectIsNearPlayer(Level level, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (this.requiredPlayerRange == -1) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "serverTick", at = @At("HEAD"), cancellable = true)
    private void injectServerTick(ServerLevel level, BlockPos pos, CallbackInfo ci) {
        if (ModGameRules.getGameRuleBoolean(level, ModGameRules.RULE_SPAWNERS_DONT_WORK_IN_LIGHT)) {
            if (!SpawnerUtil.isSpawningAnimals(level, getOrCreateNextSpawnData(level, level.getRandom(), pos))) {
                for (BlockPos neighborPos : WorldUtil.getAdjacentPositions(pos)) {
                    if (level.getBrightness(LightLayer.BLOCK, neighborPos) > 7) {
                        tick++;
                        if (tick == 20) {
                            ParticleUtil.spawnParticlesOnBlockFaces(level, pos, ParticleTypes.SMOKE, UniformInt.of(5, 10));
                            tick = 0;
                        }
                        ci.cancel();
                        return;
                    }
                }
            }
        }
    }
}
