package com.fabbe50.fabsservertweaks.mixin;

import com.fabbe50.fabsservertweaks.util.TorchflowerLightHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.redstone.Orientation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockBehaviour.class)
public abstract class BlockBehaviourMixin {
    @Inject(method = "onPlace", at = @At("TAIL"))
    private void fabs$placeTorchflowerLights(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston, CallbackInfo ci) {
        if (level instanceof ServerLevel serverLevel && state.is(Blocks.TORCHFLOWER) && !oldState.is(Blocks.TORCHFLOWER)) {
            TorchflowerLightHelper.refreshAdjacentLights(serverLevel, pos);
        }
    }

    @Inject(method = "neighborChanged", at = @At("TAIL"))
    private void fabs$refreshTorchflowerLights(BlockState state, Level level, BlockPos pos, Block block, Orientation orientation, boolean movedByPiston, CallbackInfo ci) {
        if (level instanceof ServerLevel serverLevel) {
            if (state.is(Blocks.TORCHFLOWER)) {
                TorchflowerLightHelper.refreshAdjacentLights(serverLevel, pos);
            } else if (state.is(Blocks.LIGHT)) {
                TorchflowerLightHelper.removeIfOrphaned(serverLevel, pos);
            }
        }
    }
}
