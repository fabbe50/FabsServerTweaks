package com.fabbe50.fabsservertweaks.mixin;

import com.fabbe50.fabsservertweaks.registries.ModGameRules;
import com.google.common.collect.ImmutableList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LiquidBlock.class)
public abstract class LiquidBlockMixin {
    @Shadow @Final public static ImmutableList<Direction> POSSIBLE_FLOW_DIRECTIONS;

    @Shadow protected abstract void fizz(LevelAccessor levelAccessor, BlockPos blockPos);

    @Shadow @Final protected FlowingFluid fluid;

    @Inject(method = "shouldSpreadLiquid", at = @At("HEAD"), cancellable = true)
    private void injectShouldSpreadLiquid(Level level, BlockPos blockPos, BlockState blockState, CallbackInfoReturnable<Boolean> cir) {
        if (level instanceof ServerLevel serverLevel) {
            if (serverLevel.getGameRules().getBoolean(ModGameRules.RULE_NO_COBBLE_GEN)) {
                if (this.fluid.is(FluidTags.LAVA)) {
                    boolean bl = level.getBlockState(blockPos.below()).is(Blocks.SOUL_SOIL);
                    if (!bl) {
                        for (Direction direction : POSSIBLE_FLOW_DIRECTIONS) {
                            BlockPos blockPos2 = blockPos.relative(direction.getOpposite());
                            if (level.getFluidState(blockPos2).is(FluidTags.WATER)) {
                                Block block = null;
                                if (level.getFluidState(blockPos).isSource()) {
                                    block = Blocks.OBSIDIAN;
                                    level.setBlockAndUpdate(blockPos, block.defaultBlockState());
                                    this.fizz(level, blockPos);
                                } else if (level.getFluidState(blockPos2).isSource()) {
                                    block = Blocks.COBBLESTONE;
                                    level.setBlockAndUpdate(blockPos2, block.defaultBlockState());
                                    this.fizz(level, blockPos2);
                                }
                                cir.setReturnValue(false);
                                return;
                            }
                        }
                    }
                }
            }
        }
    }
}
