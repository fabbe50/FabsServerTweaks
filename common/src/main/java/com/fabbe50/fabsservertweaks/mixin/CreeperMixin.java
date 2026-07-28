package com.fabbe50.fabsservertweaks.mixin;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.monster.Creeper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Creeper.class)
public class CreeperMixin {
    @Inject(method = "explodeCreeper", at = @At("HEAD"))
    private void injectExplodeCreeper(CallbackInfo ci) {
        Creeper instance = ((Creeper)(Object) this);
        for (MobEffectInstance effectInstance : instance.getActiveEffects()) {
            if (effectInstance.isInfiniteDuration()) {
                instance.removeEffect(effectInstance.getEffect());
                instance.addEffect(new MobEffectInstance(effectInstance.getEffect(), 20 * 60 * 8, effectInstance.getAmplifier()));
            }
        }
    }
}
