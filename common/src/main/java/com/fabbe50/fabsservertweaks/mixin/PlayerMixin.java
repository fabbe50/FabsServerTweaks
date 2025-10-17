package com.fabbe50.fabsservertweaks.mixin;

import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class PlayerMixin {
    @Inject(method = "getDestroySpeed", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;onGround()Z"), cancellable = true)
    private void injectGetDestroySpeed(BlockState blockState, CallbackInfoReturnable<Float> cir) {
        Player instance = ((Player)(Object) this);
        if (!instance.onGround() && instance.isEyeInFluid(FluidTags.WATER)) {
            cir.cancel();
        }
    }
}
