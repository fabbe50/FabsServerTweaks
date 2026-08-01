package com.fabbe50.fabsservertweaks.mixin;

import com.fabbe50.fabsservertweaks.registries.ModRegistry;
import com.fabbe50.fabsservertweaks.util.ItemStackUtil;
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
        int maxStackSize = ItemStackUtil.getCustomMaxStackSize(instance);
        if (maxStackSize != -1) {
            cir.setReturnValue(maxStackSize);
        }
    }
}
