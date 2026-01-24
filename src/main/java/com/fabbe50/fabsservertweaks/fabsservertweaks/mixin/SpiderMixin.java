package com.fabbe50.fabsservertweaks.fabsservertweaks.mixin;

import com.fabbe50.fabsservertweaks.registries.ModRegistry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.stream.Stream;

@Mixin(Spider.class)
public abstract class SpiderMixin extends Monster {
    @Shadow public abstract void setClimbing(boolean bl);

    protected SpiderMixin(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(at = @At("RETURN"), method = "tick")
    private void injectTick(CallbackInfo ci) {
        if (!this.level().isClientSide()) {
            Stream<BlockState> climbingOnBlocks = level().getBlockStates(getBoundingBox().inflate(0.2, 0, 0.2));
            boolean isOnClimbableBlock = climbingOnBlocks.anyMatch(this::checkIsBlockClimbable);
            this.setClimbing(this.horizontalCollision && isOnClimbableBlock);
        }
    }

    @Unique
    private boolean checkIsBlockClimbable(BlockState state) {
        if (state.isAir()) {
            return false;
        } else return !state.is(ModRegistry.SPIDER_NOT_CLIMBABLE);
    }
}
