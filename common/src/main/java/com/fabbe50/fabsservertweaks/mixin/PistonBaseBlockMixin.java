package com.fabbe50.fabsservertweaks.mixin;

import com.fabbe50.fabsservertweaks.Fabsservertweaks;
import com.fabbe50.fabsservertweaks.registries.ModRegistry;
import com.fabbe50.fabsservertweaks.util.PistonChestStateStore;
import com.fabbe50.fabsservertweaks.util.PistonChestSourceStateStore;
import com.fabbe50.fabsservertweaks.util.PistonBlockEntityTransferStore;
import com.fabbe50.fabsservertweaks.util.interfaces.PistonBlockEntityMover;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SignalGetter;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.piston.PistonMovingBlockEntity;
import net.minecraft.world.level.block.piston.MovingPistonBlock;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.piston.PistonStructureResolver;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.material.PushReaction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mixin(PistonBaseBlock.class)
public abstract class PistonBaseBlockMixin extends DirectionalBlock {
    protected PistonBaseBlockMixin(Properties properties) {
        super(properties);
    }

    @Redirect(method = "isPushable", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;hasBlockEntity()Z"))
    private static boolean blockEntityPushableCheck(BlockState blockState) {
        return blockState.hasBlockEntity() && !Fabsservertweaks.CONFIG.canPistonsPushBlockEntities;
    }

    @Redirect(method = "isPushable", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getPistonPushReaction()Lnet/minecraft/world/level/material/PushReaction;"))
    private static PushReaction pistonMoveOverride(BlockState blockState) {
        PushReaction pushReaction = blockState.getPistonPushReaction();
        if (blockState.is(ModRegistry.PISTON_MOVE_OVERRIDE) && pushReaction == PushReaction.DESTROY) {
            return PushReaction.NORMAL;
        }

        return pushReaction;
    }

    @Redirect(method = "triggerEvent", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getPistonPushReaction()Lnet/minecraft/world/level/material/PushReaction;"))
    private PushReaction pistonMoveOverrideOnRetract(BlockState blockState) {
        PushReaction pushReaction = blockState.getPistonPushReaction();
        if (blockState.is(ModRegistry.PISTON_MOVE_OVERRIDE) && pushReaction == PushReaction.DESTROY) {
            return PushReaction.NORMAL;
        }

        return pushReaction;
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
            target = "Lnet/minecraft/world/level/block/piston/PistonStructureResolver;resolve()Z"
        )
    )
    private boolean validateChestMoves(PistonStructureResolver resolver, Level level, BlockPos pistonPos, Direction facing, boolean extending) {
        if (!resolver.resolve()) {
            return false;
        }

        return this.fabsservertweaks$validateChestMoves(level, resolver.getToPush(), resolver.getPushDirection());
    }

    @Redirect(
        method = "moveBlocks",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/piston/MovingPistonBlock;newMovingBlockEntity(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;ZZ)Lnet/minecraft/world/level/block/entity/BlockEntity;"
        )
    )
    private BlockEntity copyMovedBlockEntityData(BlockPos movedPos, BlockState movingPistonState, BlockState movedState, Direction moveDirection, boolean isExtending, boolean isSourcePiston, Level level, BlockPos pistonPos, Direction facing, boolean extending) {
        BlockPos sourcePos = movedPos.relative(extending ? facing.getOpposite() : facing);
        BlockState preservedSourceState = PistonChestSourceStateStore.get(level, sourcePos);
        BlockState effectiveMovedState = preservedSourceState != null ? preservedSourceState : movedState;
        BlockEntity movingBlockEntity = MovingPistonBlock.newMovingBlockEntity(movedPos, movingPistonState, effectiveMovedState, moveDirection, isExtending, isSourcePiston);

        if (!Fabsservertweaks.CONFIG.canPistonsPushBlockEntities || isSourcePiston || !effectiveMovedState.hasBlockEntity()) {
            return movingBlockEntity;
        }

        BlockEntity sourceBlockEntity = level.getBlockEntity(sourcePos);
        if (sourceBlockEntity != null && movingBlockEntity instanceof PistonBlockEntityMover pistonBlockEntityMover) {
            var customData = sourceBlockEntity.saveCustomOnly(level.registryAccess());
            var components = sourceBlockEntity.components();
            pistonBlockEntityMover.fabsservertweaks$setMovedBlockEntityData(customData, components);
            PistonBlockEntityTransferStore.put(level, movedPos, effectiveMovedState, customData, components);
            level.removeBlockEntity(sourcePos);
        }

        if (effectiveMovedState.getBlock() instanceof ChestBlock) {
            PistonChestStateStore.put(level, movedPos, effectiveMovedState);
        }

        return movingBlockEntity;
    }

    @Unique
    private boolean fabsservertweaks$validateChestMoves(Level level, List<BlockPos> toPush, Direction pushDirection) {
        Set<BlockPos> pushSet = new HashSet<>(toPush);

        for (BlockPos blockPos : toPush) {
            BlockState blockState = this.fabsservertweaks$getEffectiveChestState(level, blockPos);
            if (!(blockState.getBlock() instanceof ChestBlock) || !blockState.hasProperty(ChestBlock.TYPE)) {
                continue;
            }

            ChestType chestType = blockState.getValue(ChestBlock.TYPE);
            if (chestType == ChestType.SINGLE) {
                PistonChestSourceStateStore.put(level, blockPos, blockState);
                continue;
            }

            BlockPos partnerPos = ChestBlock.getConnectedBlockPos(blockPos, blockState);
            BlockState partnerState = this.fabsservertweaks$getEffectiveChestState(level, partnerPos);
            if (!partnerState.is(blockState.getBlock())
                || !partnerState.hasProperty(ChestBlock.TYPE)
                || partnerState.getValue(ChestBlock.TYPE) != chestType.getOpposite()
                || partnerState.getValue(ChestBlock.FACING) != blockState.getValue(ChestBlock.FACING)) {
                return false;
            }

            if (pushSet.contains(partnerPos)) {
                continue;
            }

            if (this.fabsservertweaks$wasMovedEarlierThisTick(level, partnerPos, pushDirection)) {
                PistonChestSourceStateStore.put(level, blockPos, blockState);
                PistonChestSourceStateStore.put(level, partnerPos, partnerState);
                continue;
            }

            if (!this.fabsservertweaks$isMovedByParallelPiston(level, partnerPos, pushDirection)) {
                return false;
            }

            PistonChestSourceStateStore.put(level, blockPos, blockState);
            PistonChestSourceStateStore.put(level, partnerPos, partnerState);
        }

        return true;
    }

    @Unique
    private BlockState fabsservertweaks$getEffectiveChestState(Level level, BlockPos blockPos) {
        BlockState preservedState = PistonChestSourceStateStore.get(level, blockPos);
        return preservedState != null ? preservedState : level.getBlockState(blockPos);
    }

    private boolean fabsservertweaks$wasMovedEarlierThisTick(Level level, BlockPos sourcePos, Direction pushDirection) {
        BlockPos destinationPos = sourcePos.relative(pushDirection);
        return PistonBlockEntityTransferStore.get(level, destinationPos) != null
            || PistonChestStateStore.get(level, destinationPos) != null;
    }

    @Unique
    private boolean fabsservertweaks$isMovedByParallelPiston(Level level, BlockPos chestPos, Direction pushDirection) {
        return this.fabsservertweaks$isMovedByParallelExtendingPiston(level, chestPos, pushDirection)
            || this.fabsservertweaks$isMovedByParallelRetractingPiston(level, chestPos, pushDirection);
    }

    @Unique
    private boolean fabsservertweaks$isMovedByParallelExtendingPiston(Level level, BlockPos chestPos, Direction pushDirection) {
        BlockPos pistonPos = chestPos.relative(pushDirection.getOpposite());
        BlockState pistonState = level.getBlockState(pistonPos);
        if (!(pistonState.getBlock() instanceof PistonBaseBlock)
            || pistonState.getValue(PistonBaseBlock.FACING) != pushDirection
            || pistonState.getValue(PistonBaseBlock.EXTENDED)
            || !this.fabsservertweaks$getNeighborSignal(level, pistonPos, pushDirection)) {
            return false;
        }

        PistonStructureResolver resolver = new PistonStructureResolver(level, pistonPos, pushDirection, true);
        return resolver.resolve() && resolver.getToPush().contains(chestPos);
    }

    @Unique
    private boolean fabsservertweaks$isMovedByParallelRetractingPiston(Level level, BlockPos chestPos, Direction pushDirection) {
        Direction pistonFacing = pushDirection.getOpposite();
        BlockPos pistonPos = chestPos.relative(pushDirection, 2);
        BlockState pistonState = level.getBlockState(pistonPos);

        if (pistonState.is(Blocks.STICKY_PISTON)) {
            if (pistonState.getValue(PistonBaseBlock.FACING) != pistonFacing
                || !pistonState.getValue(PistonBaseBlock.EXTENDED)) {
                return false;
            }
            return true;
        } else if (pistonState.is(Blocks.MOVING_PISTON)) {
            BlockEntity blockEntity = level.getBlockEntity(pistonPos);
            return blockEntity instanceof PistonMovingBlockEntity movingBlockEntity
                && !movingBlockEntity.isExtending()
                && movingBlockEntity.isSourcePiston()
                && movingBlockEntity.getDirection() == pistonFacing;
        } else {
            return false;
        }
    }

    @Unique
    private boolean fabsservertweaks$getNeighborSignal(SignalGetter signalGetter, BlockPos pos, Direction blockedDirection) {
        for (Direction direction : Direction.values()) {
            if (direction != blockedDirection && signalGetter.hasSignal(pos.relative(direction), direction)) {
                return true;
            }
        }

        if (signalGetter.hasSignal(pos, Direction.DOWN)) {
            return true;
        }

        BlockPos abovePos = pos.above();
        for (Direction direction : Direction.values()) {
            if (direction != Direction.DOWN && signalGetter.hasSignal(abovePos.relative(direction), direction)) {
                return true;
            }
        }

        return false;
    }
}
