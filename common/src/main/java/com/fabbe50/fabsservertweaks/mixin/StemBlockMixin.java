package com.fabbe50.fabsservertweaks.mixin;

import com.fabbe50.fabsservertweaks.util.WorldUtil;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.StemBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StemBlock.class)
public class StemBlockMixin {
    @Shadow
    @Final
    public static IntegerProperty AGE;
    @Shadow
    @Final
    public static int MAX_AGE;
    @Unique
    private int fabsServerTweaks$randomTickOverride;

    @Inject(method = "randomTick", at = @At("HEAD"))
    private void injectRandomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo ci) {
        int age = state.getValue(AGE);
        if (WorldUtil.shouldPlantGrowExtra(level, pos, random, age, MAX_AGE)) {
            this.fabsServerTweaks$randomTickOverride = 1;
        }
    }

    @ModifyExpressionValue(method = "randomTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/RandomSource;nextInt(I)I"))
    private int fabsServerTweaks$stemBlockRandomTick(int i) {
        if (i == 0) {
            return 0;
        } else if (fabsServerTweaks$randomTickOverride == 1) {
            fabsServerTweaks$randomTickOverride = 0;
            return 0;
        }
        return i;
    }
}
