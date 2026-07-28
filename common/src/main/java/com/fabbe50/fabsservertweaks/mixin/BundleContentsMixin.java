package com.fabbe50.fabsservertweaks.mixin;

import com.fabbe50.fabsservertweaks.Fabsservertweaks;
import com.fabbe50.fabsservertweaks.registries.ModRegistry;
import com.mojang.serialization.DataResult;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.component.BundleContents;
import org.apache.commons.lang3.math.Fraction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(BundleContents.class)
public abstract class BundleContentsMixin {
    @Inject(method = "getWeight", at = @At("HEAD"), cancellable = true)
    private static void injectGetWeight(ItemInstance item, CallbackInfoReturnable<DataResult<Fraction>> cir) {
        if (Fabsservertweaks.CONFIG.toolsInBundle) {
            if (item.is(ModRegistry.FITS_IN_BUNDLE_16)) {
                cir.setReturnValue(DataResult.success(Fraction.getFraction(1, 16)));
            }
            if (item.is(ModRegistry.FITS_IN_BUNDLE_64)) {
                cir.setReturnValue(DataResult.success(Fraction.getFraction(1, 64)));
            }
        }
    }

    @Inject(method = "canItemBeInBundle", at = @At("HEAD"), cancellable = true)
    private static void injectCanItemBeInBundle(ItemStack itemToAdd, CallbackInfoReturnable<Boolean> cir) {
        if (Fabsservertweaks.CONFIG.toolsInBundle) {
            if (!itemToAdd.isEmpty() && (itemToAdd.is(ModRegistry.FITS_IN_BUNDLE_16) || itemToAdd.is(ModRegistry.FITS_IN_BUNDLE_64))) {
                cir.setReturnValue(true);
            }
        }
    }
}
