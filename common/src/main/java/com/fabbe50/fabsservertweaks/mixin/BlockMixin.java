package com.fabbe50.fabsservertweaks.mixin;

import com.fabbe50.fabsservertweaks.util.TorchflowerLightHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public abstract class BlockMixin {
    @Inject(method = "playerWillDestroy", at = @At("HEAD"))
    private void fabs$cleanupTorchflowerLightsBeforeBreak(Level level, BlockPos pos, BlockState state, Player player, CallbackInfoReturnable<BlockState> cir) {
        if (level instanceof ServerLevel serverLevel && state.is(Blocks.TORCHFLOWER)) {
            TorchflowerLightHelper.cleanupAdjacentLights(serverLevel, pos);
        }
    }

    @Inject(method = "updateOrDestroy(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/core/BlockPos;II)V", at = @At("TAIL"))
    private static void fabs$updateTorchflowerLights(BlockState oldState, BlockState newState, LevelAccessor level, BlockPos pos, int flags, int recursionLeft, CallbackInfo ci) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }
        boolean wasTorchflower = oldState.is(Blocks.TORCHFLOWER);

        if (wasTorchflower && !newState.is(Blocks.TORCHFLOWER)) {
            TorchflowerLightHelper.cleanupAdjacentLights(serverLevel, pos);
        }
    }
}
