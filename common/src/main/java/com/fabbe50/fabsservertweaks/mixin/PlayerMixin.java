package com.fabbe50.fabsservertweaks.mixin;

import com.fabbe50.fabsservertweaks.util.EnchantmentUtil;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class PlayerMixin {
    @Inject(method = "getDestroySpeed(Lnet/minecraft/world/level/block/state/BlockState;)F", at = @At("RETURN"), cancellable = true)
    private void ft_removeSwimPenaltyWithAquaAffinity(BlockState state, CallbackInfoReturnable<Float> cir) {
        Player instance = (Player)(Object) this;
        if (instance.isInWater() && EnchantmentUtil.hasAquaAffinity(instance) && !instance.onGround()) {
            cir.setReturnValue(cir.getReturnValueF() * 5.0F);
        }
    }
}
