package com.fabbe50.fabsservertweaks.mixin;

import com.fabbe50.fabsservertweaks.Fabsservertweaks;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.level.dimension.end.EnderDragonFight;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnderDragonFight.class)
public class EnderDragonFightMixin {
    @Shadow
    private boolean hasPreviouslyKilledDragon;

    @Inject(method = "setDragonKilled", at = @At("HEAD"))
    private void redirectHasPreviouslyKilledDragon(EnderDragon dragon, CallbackInfo ci) {
        if (Fabsservertweaks.CONFIG.shouldEnderDragonAlwaysLootLikeFirst) {
            this.hasPreviouslyKilledDragon = false;
        }
    }
}
