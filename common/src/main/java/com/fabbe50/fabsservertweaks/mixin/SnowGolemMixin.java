package com.fabbe50.fabsservertweaks.mixin;

import com.fabbe50.fabsservertweaks.registries.ModGameRules;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.golem.AbstractGolem;
import net.minecraft.world.entity.animal.golem.SnowGolem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SnowGolem.class)
public abstract class SnowGolemMixin extends AbstractGolem {
    protected SnowGolemMixin(EntityType<? extends AbstractGolem> entityType, Level level) {
        super(entityType, level);
    }

    @Redirect(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/golem/SnowGolem;hurtServer(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)Z"))
    private boolean redirectHurtServer(SnowGolem instance, ServerLevel serverLevel, DamageSource damageSource, float v) {
        if (ModGameRules.getGameRuleBoolean(serverLevel, ModGameRules.RULE_SNOW_GOLEMS_SURVIVE_ON_ICE)) {
            BlockState standingOnBlock = instance.level().getBlockState(instance.blockPosition().relative(Direction.DOWN));
            if (standingOnBlock.is(BlockTags.ICE)) {
                return false;
            }
        }
        return super.hurtServer(serverLevel, damageSource, v);
    }

    @Inject(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;setBlockAndUpdate(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)Z"), cancellable = true)
    private void injectAiStep(CallbackInfo ci) {
        if (this.level() instanceof ServerLevel serverLevel && !ModGameRules.getGameRuleBoolean(serverLevel, ModGameRules.RULE_SNOW_GOLEMS_GENERATE_SNOW)) {
            ci.cancel();
        }
    }
}
