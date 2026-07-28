package com.fabbe50.fabsservertweaks.mixin;

import com.fabbe50.fabsservertweaks.registries.ModGameRules;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.CauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CauldronBlock.class)
public class CauldronBlockMixin {
    @Inject(method = "handlePrecipitation", at = @At("HEAD"), cancellable = true)
    private void injectHandlePrecipitation(BlockState state, Level level, BlockPos pos, Biome.Precipitation precipitation, CallbackInfo ci) {
        if (level.isClientSide()) {
            ci.cancel();
        } else {
            ServerLevel serverLevel = (ServerLevel) level;
            if (precipitation.equals(Biome.Precipitation.RAIN) && !ModGameRules.getGameRuleBoolean(serverLevel, ModGameRules.RULE_RAIN_FILLS_CAULDRON)) {
                ci.cancel();
            } else if (precipitation.equals(Biome.Precipitation.SNOW) && !ModGameRules.getGameRuleBoolean(serverLevel, ModGameRules.RULE_SNOW_FILLS_CAULDRON)) {
                ci.cancel();
            }
        }
    }

    @Inject(method = "receiveStalactiteDrip", at = @At("HEAD"), cancellable = true)
    private void injectReceiveStalactiteDrip(BlockState state, Level level, BlockPos pos, Fluid fluid, CallbackInfo ci) {
        if (level.isClientSide()) {
            ci.cancel();
        } else {
            ServerLevel serverLevel = (ServerLevel) level;
            if (fluid.equals(Fluids.WATER) && !ModGameRules.getGameRuleBoolean(serverLevel, ModGameRules.RULE_WATER_DRIPSTONE_FILL_CAULDRON)) {
                ci.cancel();
            } else if (fluid.equals(Fluids.LAVA) && !ModGameRules.getGameRuleBoolean(serverLevel, ModGameRules.RULE_LAVA_DRIPSTONE_FILL_CAULDRON)) {
                ci.cancel();
            }
        }
    }
}
