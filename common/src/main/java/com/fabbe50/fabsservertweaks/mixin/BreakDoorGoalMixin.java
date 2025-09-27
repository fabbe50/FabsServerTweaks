package com.fabbe50.fabsservertweaks.mixin;

import com.fabbe50.fabsservertweaks.registries.ModGameRules;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.BreakDoorGoal;
import net.minecraft.world.entity.ai.goal.DoorInteractGoal;
import net.minecraft.world.entity.monster.Zombie;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BreakDoorGoal.class)
public class BreakDoorGoalMixin extends DoorInteractGoal {
    public BreakDoorGoalMixin(Mob mob) {
        super(mob);
    }

    @Inject(method = "canUse", at = @At("HEAD"), cancellable = true)
    private void injectCanUse(CallbackInfoReturnable<Boolean> cir) {
        if (this.mob instanceof Zombie zombie) {
            if (!getServerLevel(zombie).getGameRules().getBoolean(ModGameRules.RULE_MOB_GRIEF_ZOMBIE)) {
                cir.setReturnValue(false);
            }
        }
    }
}
