package com.fabbe50.fabsservertweaks.mixin;

import com.fabbe50.fabsservertweaks.util.PistonBlockEntityTransferStore;
import com.fabbe50.fabsservertweaks.util.PistonChestStateStore;
import com.fabbe50.fabsservertweaks.util.interfaces.PistonBlockEntityMover;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.piston.PistonMovingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.util.ProblemReporter;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PistonMovingBlockEntity.class)
public abstract class PistonMovingBlockEntityMixin extends BlockEntity implements PistonBlockEntityMover {
    @Shadow private BlockState movedState;

    @Unique private static final String FABS_MOVED_BLOCK_ENTITY_TAG = "FabsMovedBlockEntity";
    @Unique private static final String FABS_MOVED_BLOCK_ENTITY_COMPONENTS = "FabsMovedBlockEntityComponents";
    @Unique @Nullable private CompoundTag fabsservertweaks$movedBlockEntityData;
    @Unique private DataComponentMap fabsservertweaks$movedBlockEntityComponents = DataComponentMap.EMPTY;

    public PistonMovingBlockEntityMixin(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    @Inject(method = "saveAdditional", at = @At("TAIL"))
    private void saveMovedBlockEntityData(ValueOutput valueOutput, CallbackInfo ci) {
        if (this.fabsservertweaks$movedBlockEntityData != null) {
            valueOutput.store(FABS_MOVED_BLOCK_ENTITY_TAG, CompoundTag.CODEC, this.fabsservertweaks$movedBlockEntityData);
        }
        if (!this.fabsservertweaks$movedBlockEntityComponents.isEmpty()) {
            valueOutput.store(FABS_MOVED_BLOCK_ENTITY_COMPONENTS, DataComponentMap.CODEC, this.fabsservertweaks$movedBlockEntityComponents);
        }
    }

    @Inject(method = "loadAdditional", at = @At("TAIL"))
    private void loadMovedBlockEntityData(ValueInput valueInput, CallbackInfo ci) {
        this.fabsservertweaks$movedBlockEntityData = valueInput.read(FABS_MOVED_BLOCK_ENTITY_TAG, CompoundTag.CODEC).map(CompoundTag::copy).orElse(null);
        this.fabsservertweaks$movedBlockEntityComponents = valueInput.read(FABS_MOVED_BLOCK_ENTITY_COMPONENTS, DataComponentMap.CODEC).orElse(DataComponentMap.EMPTY);
    }

    @Inject(
        method = "finalTick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z",
            shift = At.Shift.AFTER
        )
    )
    private void restoreMovedBlockEntityDataOnFinalTick(CallbackInfo ci) {
        this.fabsservertweaks$restoreMovedBlockEntityData();
    }

    @Inject(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z",
            ordinal = 0,
            shift = At.Shift.AFTER
        )
    )
    private static void restoreMovedBlockEntityDataOnTickAirBranch(Level level, BlockPos pos, BlockState state, PistonMovingBlockEntity blockEntity, CallbackInfo ci) {
        ((PistonMovingBlockEntityMixin)(Object)blockEntity).fabsservertweaks$restoreMovedBlockEntityData();
    }

    @Inject(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z",
            ordinal = 1,
            shift = At.Shift.AFTER
        )
    )
    private static void restoreMovedBlockEntityDataOnTickNormalBranch(Level level, BlockPos pos, BlockState state, PistonMovingBlockEntity blockEntity, CallbackInfo ci) {
        ((PistonMovingBlockEntityMixin)(Object)blockEntity).fabsservertweaks$restoreMovedBlockEntityData();
    }

    @Redirect(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/Block;updateFromNeighbourShapes(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;"
        )
    )
    private static BlockState keepChestStateDuringTick(BlockState movedState, net.minecraft.world.level.LevelAccessor levelAccessor, BlockPos pos) {
        return fabsservertweaks$preserveChestState(movedState, levelAccessor, pos);
    }

    @Redirect(
        method = "finalTick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/Block;updateFromNeighbourShapes(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;"
        )
    )
    private BlockState keepChestStateDuringFinalTick(BlockState movedState, net.minecraft.world.level.LevelAccessor levelAccessor, BlockPos pos) {
        return fabsservertweaks$preserveChestState(this.movedState, levelAccessor, pos);
    }

    @Override
    public void fabsservertweaks$setMovedBlockEntityData(@Nullable CompoundTag tag, DataComponentMap components) {
        this.fabsservertweaks$movedBlockEntityData = tag == null ? null : tag.copy();
        this.fabsservertweaks$movedBlockEntityComponents = components;
    }

    @Unique
    private void fabsservertweaks$restoreMovedBlockEntityData() {
        if (this.level == null) {
            return;
        }

        if (this.fabsservertweaks$movedBlockEntityData == null) {
            PistonBlockEntityTransferStore.Snapshot snapshot = PistonBlockEntityTransferStore.take(this.level, this.worldPosition);
            if (snapshot != null) {
                this.fabsservertweaks$movedBlockEntityData = snapshot.tag();
                this.fabsservertweaks$movedBlockEntityComponents = snapshot.components();
            }
        }

        if (this.fabsservertweaks$movedBlockEntityData == null) {
            return;
        }

        BlockState blockState = this.level.getBlockState(this.worldPosition);
        if (blockState.is(Blocks.MOVING_PISTON) || !blockState.hasBlockEntity()) {
            return;
        }

        BlockEntity blockEntity = this.level.getBlockEntity(this.worldPosition);
        if (blockEntity == null) {
            return;
        }
        blockEntity.loadCustomOnly(TagValueInput.create(ProblemReporter.DISCARDING, this.level.registryAccess(), this.fabsservertweaks$movedBlockEntityData));
        blockEntity.setComponents(this.fabsservertweaks$movedBlockEntityComponents);
        blockEntity.setChanged();
        this.fabsservertweaks$movedBlockEntityData = null;
        this.fabsservertweaks$movedBlockEntityComponents = DataComponentMap.EMPTY;
    }

    @Unique
    private static BlockState fabsservertweaks$preserveChestState(BlockState movedState, net.minecraft.world.level.LevelAccessor levelAccessor, BlockPos pos) {
        if (!(levelAccessor instanceof Level level) || !(movedState.getBlock() instanceof ChestBlock)) {
            return Block.updateFromNeighbourShapes(movedState, levelAccessor, pos);
        }

        BlockState lockedState = PistonChestStateStore.get(level, pos);
        if (lockedState != null) {
            return lockedState;
        }

        PistonBlockEntityTransferStore.Snapshot snapshot = PistonBlockEntityTransferStore.get(level, pos);
        if (snapshot != null && snapshot.movedState().getBlock() instanceof ChestBlock) {
            return snapshot.movedState();
        }

        return Block.updateFromNeighbourShapes(movedState, levelAccessor, pos);
    }
}
