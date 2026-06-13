package com.fabbe50.fabsservertweaks.mixin;

import com.fabbe50.fabsservertweaks.registries.ModRegistry;
import net.minecraft.world.item.ItemInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemInstance.class)
public interface ItemInstanceMixin {
    @Inject(method = "getMaxStackSize", at = @At("HEAD"), cancellable = true)
    private void injectGetMaxStackSize(CallbackInfoReturnable<Integer> cir) {
        ItemInstance instance = ((ItemInstance) this);

        if (instance.is(ModRegistry.STACK_16) || instance.is(ModRegistry.STACK_64)) {
            int targetMax = 1;
            if (instance.is(ModRegistry.STACK_16)) {
                targetMax = 16;
            } else if (instance.is(ModRegistry.STACK_64)) {
                targetMax = 64;
            }

            cir.setReturnValue(targetMax);
        }
    }
}
