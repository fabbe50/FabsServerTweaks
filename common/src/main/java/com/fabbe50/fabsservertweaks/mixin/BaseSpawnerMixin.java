package com.fabbe50.fabsservertweaks.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BaseSpawner.class)
public class BaseSpawnerMixin {
    @Shadow
    public int requiredPlayerRange;

    @Inject(method = "isNearPlayer", at = @At("HEAD"), cancellable = true)
    private void injectIsNearPlayer(Level level, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (this.requiredPlayerRange == -1) {
            cir.setReturnValue(true);
        }
    }
}
