package com.fabbe50.fabsservertweaks.mixin;

import com.fabbe50.fabsservertweaks.Fabsservertweaks;
import com.fabbe50.fabsservertweaks.registries.ModGameRules;
import com.fabbe50.fabsservertweaks.registries.gamerules.TrampleValue;
import com.fabbe50.fabsservertweaks.util.EnchantmentUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FarmBlock.class)
public class FarmBlockMixin extends Block {
    public FarmBlockMixin(Properties properties) {
        super(properties);
    }

    @Inject(method = "fallOn", at = @At("HEAD"), cancellable = true)
    private void injectFallOn(Level level, BlockState blockState, BlockPos blockPos, Entity entity, double d, CallbackInfo ci) {
        if (level instanceof ServerLevel serverLevel && entity instanceof LivingEntity livingEntity) {
            TrampleValue.TrampleMode trampleMode = Fabsservertweaks.CONFIG.cropTrampleMode;
            switch (trampleMode) {
                case NO_TRAMPLE -> {
                    ci.cancel();
                    super.fallOn(level, blockState, blockPos, entity, d);
                }
                case FEATHER_FALLING -> {
                    if (EnchantmentUtil.hasFeatherFalling(livingEntity)) {
                        ci.cancel();
                        super.fallOn(level, blockState, blockPos, entity, d);
                    }
                }
            }
        }
    }
}
