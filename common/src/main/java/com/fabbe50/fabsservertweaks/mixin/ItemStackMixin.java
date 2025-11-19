package com.fabbe50.fabsservertweaks.mixin;

import com.fabbe50.fabsservertweaks.events.ItemStackEvent;
import com.fabbe50.fabsservertweaks.registries.ModRegistry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    @Inject(method = "<init>(Lnet/minecraft/world/level/ItemLike;ILnet/minecraft/core/component/PatchedDataComponentMap;)V", at = @At("TAIL"))
    private void injectConstructor(ItemLike itemLike, int i, PatchedDataComponentMap patchedDataComponentMap, CallbackInfo ci) {
        ItemStackEvent.CREATED.invoker().create((ItemStack) (Object)this);
    }

    @Inject(method = "getMaxStackSize", at = @At("HEAD"), cancellable = true)
    private void injectGetMaxStackSize(CallbackInfoReturnable<Integer> cir) {
        ItemStack instance = ((ItemStack)(Object) this);

        if (instance.is(ModRegistry.STACK_16) || instance.is(ModRegistry.STACK_64)) {
            int targetMax = 1;
            if (instance.is(ModRegistry.STACK_16)) {
                targetMax = 16;
            } else if (instance.is(ModRegistry.STACK_64)) {
                targetMax = 64;
            }

            if (instance.isDamageableItem()) {
                return;
            }

            if (instance.getOrDefault(DataComponents.MAX_STACK_SIZE, 1) != targetMax) {
                instance.set(DataComponents.MAX_STACK_SIZE, targetMax);
            }

            cir.setReturnValue(targetMax);
        }
    }
}
