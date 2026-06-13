package com.fabbe50.fabsservertweaks.mixin;

import com.fabbe50.fabsservertweaks.util.PistonBlockEntityTransferStore;
import net.minecraft.core.BlockPos;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.storage.TagValueInput;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LevelChunk.class)
public abstract class LevelChunkMixin {
    @Shadow @Final
    Level level;

    @Inject(method = "setBlockState", at = @At("TAIL"))
    private void restoreMovedBlockEntityData(BlockPos blockPos, BlockState blockState, int i, CallbackInfoReturnable<BlockState> cir) {
        if (this.level.isClientSide() || cir.getReturnValue() == null || !blockState.hasBlockEntity()) {
            return;
        }

        PistonBlockEntityTransferStore.Snapshot snapshot = PistonBlockEntityTransferStore.get(this.level, blockPos);
        if (snapshot == null) {
            return;
        }

        BlockEntity blockEntity = this.level.getBlockEntity(blockPos);
        if (blockEntity == null) {
            return;
        }

        blockEntity.loadCustomOnly(TagValueInput.create(ProblemReporter.DISCARDING, this.level.registryAccess(), snapshot.tag()));
        blockEntity.setComponents(snapshot.components());
        blockEntity.setChanged();
        PistonBlockEntityTransferStore.remove(this.level, blockPos);
    }
}
