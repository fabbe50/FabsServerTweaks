package com.fabbe50.fabsservertweaks.mixin;

import com.fabbe50.fabsservertweaks.Fabsservertweaks;
import com.fabbe50.fabsservertweaks.registries.ModRegistry;
import com.fabbe50.fabsservertweaks.util.PistonBlockEntityTransferStore;
import com.fabbe50.fabsservertweaks.util.interfaces.PistonBlockEntityMover;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.piston.MovingPistonBlock;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PistonBaseBlock.class)
public abstract class PistonBaseBlockMixin extends DirectionalBlock {
    protected PistonBaseBlockMixin(Properties properties) {
        super(properties);
    }

    @Redirect(method = "isPushable", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;hasBlockEntity()Z"))
    private static boolean blockEntityPushableCheck(BlockState blockState) {
        return !(Fabsservertweaks.CONFIG.canPistonsPushBlockEntities && blockState.hasBlockEntity());
    }

    @Inject(method = "isPushable", at = @At("HEAD"), cancellable = true)
    private static void injectIsPushable(BlockState blockState, Level level, BlockPos blockPos, Direction direction, boolean bl, Direction direction2, CallbackInfoReturnable<Boolean> cir) {
        if (blockState.is(ModRegistry.PISTON_PUSH_BLACKLIST)) {
            cir.setReturnValue(false);
            return;
        }
        if (blockState.is(ModRegistry.PISTON_PUSH_WHITELIST)) {
            if (blockState.hasBlockEntity()) {
                cir.setReturnValue(Fabsservertweaks.CONFIG.canPistonsPushBlockEntities);
                return;
            }
            cir.setReturnValue(true);
        }
    }

    @Redirect(
        method = "moveBlocks",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/piston/MovingPistonBlock;newMovingBlockEntity(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;ZZ)Lnet/minecraft/world/level/block/entity/BlockEntity;"
        )
    )
    private BlockEntity copyMovedBlockEntityData(BlockPos movedPos, BlockState movingPistonState, BlockState movedState, Direction moveDirection, boolean isExtending, boolean isSourcePiston, Level level, BlockPos pistonPos, Direction facing, boolean extending) {
        BlockEntity movingBlockEntity = MovingPistonBlock.newMovingBlockEntity(movedPos, movingPistonState, movedState, moveDirection, isExtending, isSourcePiston);

        if (!Fabsservertweaks.CONFIG.canPistonsPushBlockEntities || isSourcePiston || !movedState.hasBlockEntity()) {
            return movingBlockEntity;
        }

        BlockPos sourcePos = movedPos.relative(extending ? facing.getOpposite() : facing);
        BlockEntity sourceBlockEntity = level.getBlockEntity(sourcePos);
        if (sourceBlockEntity != null && movingBlockEntity instanceof PistonBlockEntityMover pistonBlockEntityMover) {
            var customData = sourceBlockEntity.saveCustomOnly(level.registryAccess());
            var components = sourceBlockEntity.components();
            pistonBlockEntityMover.fabsservertweaks$setMovedBlockEntityData(customData, components);
            PistonBlockEntityTransferStore.put(level, movedPos, customData, components);
            level.removeBlockEntity(sourcePos);
        }

        return movingBlockEntity;
    }
}
