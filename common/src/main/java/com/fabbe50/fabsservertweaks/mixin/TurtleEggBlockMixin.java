package com.fabbe50.fabsservertweaks.mixin;

import com.fabbe50.fabsservertweaks.registries.ModGameRules;
import com.fabbe50.fabsservertweaks.registries.gamerules.TrampleValue;
import com.fabbe50.fabsservertweaks.util.EnchantmentUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.TurtleEggBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TurtleEggBlock.class)
public class TurtleEggBlockMixin extends Block {
    public TurtleEggBlockMixin(Properties properties) {
        super(properties);
    }

    @Inject(method = "stepOn", at = @At("HEAD"), cancellable = true)
    private void injectStepOn(Level level, BlockPos blockPos, BlockState blockState, Entity entity, CallbackInfo ci) {
        if (level instanceof ServerLevel serverLevel && entity instanceof LivingEntity livingEntity) {
            TrampleValue.TrampleMode trampleMode = serverLevel.getGameRules().getRule(ModGameRules.RULE_TURTLE_EGG_TRAMPLE_MODE).getValue();
            switch (trampleMode) {
                case NO_TRAMPLE -> {
                    ci.cancel();
                    super.stepOn(level, blockPos, blockState, entity);
                }
                case FEATHER_FALLING -> {
                    if (EnchantmentUtil.hasFeatherFalling(livingEntity)) {
                        ci.cancel();
                        super.stepOn(level, blockPos, blockState, entity);
                    }
                }
            }
        }
    }

    @Inject(method = "fallOn", at = @At("HEAD"), cancellable = true)
    private void injectFallOn(Level level, BlockState blockState, BlockPos blockPos, Entity entity, double d, CallbackInfo ci) {
        if (level instanceof ServerLevel serverLevel && entity instanceof LivingEntity livingEntity) {
            TrampleValue.TrampleMode trampleMode = serverLevel.getGameRules().getRule(ModGameRules.RULE_TURTLE_EGG_TRAMPLE_MODE).getValue();
            switch (trampleMode) {
                case NO_TRAMPLE -> {
                    ci.cancel();
                    super.stepOn(level, blockPos, blockState, entity);
                }
                case FEATHER_FALLING -> {
                    if (EnchantmentUtil.hasFeatherFalling(livingEntity)) {
                        ci.cancel();
                        super.stepOn(level, blockPos, blockState, entity);
                    }
                }
            }
        }
    }
}
