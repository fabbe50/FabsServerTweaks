package com.fabbe50.fabsservertweaks.mixin;

import com.fabbe50.fabsservertweaks.registries.ModGameRules;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.entity.npc.villager.Villager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Zombie.class)
public class ZombieMixin {
    @Inject(method = "killedEntity", at = @At("HEAD"), cancellable = true)
    private void injectKilledEntity(ServerLevel serverLevel, LivingEntity livingEntity, DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        if (ModGameRules.getGameRuleBoolean(serverLevel, ModGameRules.RULE_ALWAYS_CONVERT_VILLAGERS)) {
            Zombie instance = ((Zombie)(Object) this);
            if (livingEntity instanceof Villager villager) {
                if (instance.convertVillagerToZombieVillager(serverLevel, villager)) {
                    cir.setReturnValue(false);
                }
            }
        }
    }
}
