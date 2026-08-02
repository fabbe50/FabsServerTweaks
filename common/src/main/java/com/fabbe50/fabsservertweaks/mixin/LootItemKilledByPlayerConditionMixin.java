package com.fabbe50.fabsservertweaks.mixin;

import com.fabbe50.fabsservertweaks.world.damagesource.AmethystDamageSource;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LootItemKilledByPlayerCondition.class)
public class LootItemKilledByPlayerConditionMixin {
    @Inject(method = "test(Lnet/minecraft/world/level/storage/loot/LootContext;)Z", at = @At("HEAD"), cancellable = true)
    private void injectTest(LootContext context, CallbackInfoReturnable<Boolean> cir) {
        if (context.getOptionalParameter(LootContextParams.DAMAGE_SOURCE) instanceof AmethystDamageSource amethystDamageSource && amethystDamageSource.shouldDropPlayerLoot()) {
            cir.setReturnValue(true);
        }
    }
}
