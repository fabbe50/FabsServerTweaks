package com.fabbe50.fabsservertweaks.fabric.mixin;

import com.fabbe50.fabsservertweaks.fabric.plugins.polymer.PolymerRegistry;
import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "noobanidus.mods.lootr.fabric.init.ModEntities")
public class LootrModEntitiesMixin {
    @Inject(method = "registerEntities", at = @At("TAIL"))
    private static void registerEntities(CallbackInfo ci) {
        PolymerRegistry.registerEntity(noobanidus.mods.lootr.fabric.init.ModEntities.MINECART_WITH_CHEST, EntityType.CHEST_MINECART);
        PolymerRegistry.registerEntity(noobanidus.mods.lootr.fabric.init.ModEntities.ITEM_FRAME, EntityType.ITEM_FRAME);
    }
}
