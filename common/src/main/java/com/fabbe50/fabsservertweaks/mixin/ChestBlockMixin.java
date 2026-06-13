package com.fabbe50.fabsservertweaks.mixin;

import com.fabbe50.fabsservertweaks.util.PistonChestStateStore;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChestBlock.class)
public abstract class ChestBlockMixin {
    @Inject(method = "updateShape", at = @At("HEAD"), cancellable = true)
    private void keepMovedChestState(
        BlockState state,
        LevelReader levelReader,
        ScheduledTickAccess scheduledTickAccess,
        BlockPos pos,
        Direction direction,
        BlockPos neighborPos,
        BlockState neighborState,
        RandomSource randomSource,
        CallbackInfoReturnable<BlockState> cir
    ) {
        if (!(levelReader instanceof Level level)) {
            return;
        }

        BlockState lockedState = PistonChestStateStore.get(level, pos);
        if (lockedState != null) {
            cir.setReturnValue(lockedState);
        }
    }
}
