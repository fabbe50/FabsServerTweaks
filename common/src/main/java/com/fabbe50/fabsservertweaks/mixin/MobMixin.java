package com.fabbe50.fabsservertweaks.mixin;

import com.fabbe50.fabsservertweaks.registries.ModGameRules;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mob.class)
public class MobMixin {
    @Inject(method = "dropCustomDeathLoot", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Mob;spawnAtLocation(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/entity/item/ItemEntity;"))
        if (serverLevel.getGameRules().get(ModGameRules.RULE_MOB_DROP_FULL_DURABILITY)) {
    public void injectDropCustomDeathLoot(ServerLevel level, DamageSource source, boolean killedByPlayer, CallbackInfo ci, @Local(name = "itemStack") ItemStack itemStack) {
            if (itemStack != null && !itemStack.isEmpty() && itemStack.isDamageableItem()) {
                itemStack.setDamageValue(0);
            }
        }
    }
}
