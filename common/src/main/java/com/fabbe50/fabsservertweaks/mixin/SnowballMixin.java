package com.fabbe50.fabsservertweaks.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.entity.projectile.throwableitemprojectile.Snowball;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Snowball.class)
public abstract class SnowballMixin extends ThrowableProjectile {
    protected SnowballMixin(EntityType<? extends ThrowableProjectile> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "onHitEntity", at = @At("HEAD"), cancellable = true)
    private void injectOnHitEntity(EntityHitResult entityHitResult, CallbackInfo ci) {
        super.onHitEntity(entityHitResult);
        Entity entity = entityHitResult.getEntity();
        int i = 0;
        if (entity instanceof LivingEntity livingEntity && livingEntity.isSensitiveToWater()) {
            i = 3;
        }
        Level level = entity.level();
        if (level instanceof ServerLevel serverLevel) {
            entity.hurtServer(serverLevel, this.damageSources().thrown(this, this.getOwner()), i);
            ci.cancel();
        } else {
            entity.hurtClient(this.damageSources().thrown(this, this.getOwner()));
            ci.cancel();
        }
    }
}
