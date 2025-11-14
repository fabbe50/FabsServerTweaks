package com.fabbe50.fabsservertweaks.mixin;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockItem.class)
public class BlockItemMixin {
    @Inject(method = "shouldPrintOpWarning", at = @At("HEAD"), cancellable = true)
    private void injectShouldPrintOpWarning(ItemStack itemStack, Player player, CallbackInfoReturnable<Boolean> cir) {
        if (itemStack.is(Items.SPAWNER) || itemStack.is(Items.TRIAL_SPAWNER) || itemStack.is(Items.VAULT)) {
            cir.setReturnValue(false);
        }
    }
}
