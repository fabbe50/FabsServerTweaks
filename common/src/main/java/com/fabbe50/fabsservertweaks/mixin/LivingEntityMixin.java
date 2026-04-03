package com.fabbe50.fabsservertweaks.mixin;

import com.fabbe50.fabsservertweaks.events.BedEvents;
import com.fabbe50.fabsservertweaks.registries.ModGameRules;
import dev.architectury.event.EventResult;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Shadow
    public abstract void setSleepingPos(BlockPos blockPos);

    @Inject(method = "dropAllDeathLoot", at = @At("HEAD"), cancellable = true)
    private void injectDropAllDeathLoot(ServerLevel serverLevel, DamageSource damageSource, CallbackInfo ci) {
        if (serverLevel.getGameRules().get(ModGameRules.RULE_MOB_DROPS_REQUIRE_PLAYER_KILL)) {
            if (!damageSource.is(DamageTypes.PLAYER_ATTACK) && !damageSource.is(DamageTypes.PLAYER_EXPLOSION)) {
                ci.cancel();
            }
        }
    }

    @Inject(method = "startSleeping", at = @At("HEAD"), cancellable = true)
    private void injectStartSleeping(BlockPos blockPos, CallbackInfo ci) {
        EventResult result = BedEvents.START_SLEEPING.invoker().sleeping(((LivingEntity)(Object)this), blockPos);
        if (result.interruptsFurtherEvaluation()) {
            ci.cancel();
        }
    }

    @Inject(method = "stopSleeping", at = @At("HEAD"), cancellable = true)
    private void injectStopSleeping(CallbackInfo ci) {
        EventResult result = BedEvents.STOP_SLEEPING.invoker().wakeup(((LivingEntity)(Object)this));
        if (result.interruptsFurtherEvaluation()) {
            ci.cancel();
        }
    }
}
