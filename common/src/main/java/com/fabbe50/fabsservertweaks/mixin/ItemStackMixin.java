package com.fabbe50.fabsservertweaks.mixin;

import com.fabbe50.fabsservertweaks.events.ItemStackEvent;
import com.fabbe50.fabsservertweaks.registries.ModRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    @Inject(method = "Lnet/minecraft/world/item/ItemStack;<init>(Lnet/minecraft/core/Holder;ILnet/minecraft/core/component/PatchedDataComponentMap;)V", at = @At("TAIL"))
    private void injectConstructor(Holder<Item> item, int count, PatchedDataComponentMap components, CallbackInfo ci) {
        ItemStackEvent.CREATED.invoker().create((ItemStack) (Object)this);
        components.set(DataComponents.MAX_STACK_SIZE, ((ItemStack) (Object) this).getMaxStackSize());
    }
}
