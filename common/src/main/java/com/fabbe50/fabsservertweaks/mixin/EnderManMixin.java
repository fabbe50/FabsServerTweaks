package com.fabbe50.fabsservertweaks.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnderMan.class)
public class EnderManMixin extends Monster {
    protected EnderManMixin(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }

    @Inject(method = "teleport()Z", at = @At("HEAD"), cancellable = true)
    private void injectTeleport(CallbackInfoReturnable<Boolean> cir) {
        Level level = level();
        if (level instanceof ServerLevel serverLevel) {
            BlockPos pos = getOnPos();
            AABB aabb = new AABB(pos);
            aabb = aabb.inflate(10, 5, 10);
            for (BlockPos blockPos : BlockPos.betweenClosed(aabb)) {
                BlockState state = serverLevel.getBlockState(blockPos);
                if (state.is(Blocks.RESPAWN_ANCHOR) && serverLevel.getBlockState(blockPos.above()).is(Blocks.DRAGON_HEAD)) {
                    cir.setReturnValue(true);
                    return;
                }
            }
        }
    }
}
