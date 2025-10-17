package com.fabbe50.fabsservertweaks.mixin;

import com.fabbe50.fabsservertweaks.registries.ModGameRules;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ThrownTrident.class)
public abstract class ThrownTridentMixin extends AbstractArrow {
    @Shadow @Final private static EntityDataAccessor<Byte> ID_LOYALTY;

    @Shadow protected abstract boolean isAcceptibleReturnOwner();

    @Shadow public abstract void playerTouch(Player player);

    protected ThrownTridentMixin(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void onBelowWorld() {
        if (this.level() instanceof ServerLevel level) {
            if (level.getGameRules().getBoolean(ModGameRules.RULE_LOYALTY_TRIDENT_RETURNS_FROM_VOID)) {
                if (this.entityData.get(ID_LOYALTY) > 0 && this.isAcceptibleReturnOwner()) {
                    if (this.getOwner() instanceof Player player) {
                        this.setNoPhysics(true);
                        this.playerTouch(player);
                        return;
                    }
                }
            }
        }
        super.onBelowWorld();
    }
}
