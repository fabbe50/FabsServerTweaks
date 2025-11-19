package com.fabbe50.fabsservertweaks.mixin;

import com.fabbe50.fabsservertweaks.util.XPUtil;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.entity.projectile.ThrownExperienceBottle;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ThrownExperienceBottle.class)
public abstract class ThrownExperienceBottleMixin extends ThrowableItemProjectile {
    public ThrownExperienceBottleMixin(EntityType<? extends ThrowableItemProjectile> entityType, LivingEntity livingEntity, Level level, ItemStack itemStack) {
        super(entityType, livingEntity, level, itemStack);
    }

    @Inject(method = "onHit", at = @At("HEAD"), cancellable = true)
    private void injectOnHit(HitResult hitResult, CallbackInfo ci) {
        ItemStack stack = this.getItem();
        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        if (tag.contains("xp")) {
            super.onHit(hitResult);
            Level level = this.level();
            if (level instanceof ServerLevel serverLevel) {
                serverLevel.levelEvent(2002, this.blockPosition(), -13083194);
                int xpAmount = tag.getInt("xp").get();
                if (hitResult instanceof BlockHitResult blockHitResult) {
                    Vec3 movementDelta = blockHitResult.getDirection().getUnitVec3();
                    ExperienceOrb.awardWithDirection(serverLevel, hitResult.getLocation(), movementDelta, xpAmount);
                } else {
                    ExperienceOrb.awardWithDirection(serverLevel, hitResult.getLocation(), this.getDeltaMovement().scale(-1), xpAmount);
                }
                ci.cancel();
            }
        }
    }
}
