package com.fabbe50.fabsservertweaks.mixin;

import com.fabbe50.fabsservertweaks.core.dispenser.BlockDispenseItemBehaviour;
import com.fabbe50.fabsservertweaks.util.RegistryHelper;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.DispenserBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map.Entry;

@Mixin(DispenseItemBehavior.class)
public interface DispenserItemBehaviorMixin {
    @Inject(method = "bootStrap", at = @At("TAIL"))
    private static void injectBootStrap(CallbackInfo ci) {
        for (Entry<ResourceKey<Item>, Item> itemEntry : RegistryHelper.getItemEntries()) {
            if (itemEntry.getValue() instanceof BlockItem blockItem) {
                DispenserBlock.registerBehavior(blockItem, new BlockDispenseItemBehaviour());
            }
        }
    }
}
