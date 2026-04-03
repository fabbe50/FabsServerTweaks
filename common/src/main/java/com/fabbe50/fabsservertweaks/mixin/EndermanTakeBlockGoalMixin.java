package com.fabbe50.fabsservertweaks.mixin;

import com.fabbe50.fabsservertweaks.registries.ModGameRules;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.EnderMan;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnderMan.EndermanTakeBlockGoal.class)
public abstract class EndermanTakeBlockGoalMixin extends Goal {
    @Shadow @Final private EnderMan enderman;

    @Inject(method = "canUse", at = @At("HEAD"), cancellable = true)
    private void injectCanUse(CallbackInfoReturnable<Boolean> cir) {
        if (!getServerLevel(this.enderman).getGameRules().get(ModGameRules.RULE_MOB_GRIEF_ENDERMAN)) {
            cir.setReturnValue(false);
        }
    }
}
