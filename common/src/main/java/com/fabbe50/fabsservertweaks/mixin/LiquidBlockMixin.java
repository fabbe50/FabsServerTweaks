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

    @Shadow protected abstract void fizz(LevelAccessor level, BlockPos pos);

    @Shadow @Final protected FlowingFluid fluid;

    @Inject(method = "shouldSpreadLiquid", at = @At("HEAD"), cancellable = true)
    private void injectShouldSpreadLiquid(Level level, BlockPos pos, BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if (level instanceof ServerLevel serverLevel) {
            if (ModGameRules.getGameRuleBoolean(serverLevel, ModGameRules.RULE_NO_COBBLE_GEN)) {
                if (this.fluid.is(FluidTags.LAVA)) {
                    boolean isOverSoulSoil = level.getBlockState(pos.below()).is(Blocks.SOUL_SOIL);
                    for (Direction direction : POSSIBLE_FLOW_DIRECTIONS) {
                        BlockPos neighborPos = pos.relative(direction.getOpposite());
                        if (level.getFluidState(neighborPos).is(FluidTags.WATER)) {
                            Block block = null;
                            if (level.getFluidState(pos).isSource()) {
                                block = Blocks.OBSIDIAN;
                            } else if (level.getFluidState(neighborPos).isSource()) {
                                block = Blocks.COBBLESTONE;
                            }
                            if (block != null) {
                                level.setBlockAndUpdate(neighborPos, block.defaultBlockState());
                                this.fizz(level, neighborPos);
                            }
                            cir.setReturnValue(false);
                            return;
                        }

                        if (isOverSoulSoil && level.getBlockState(neighborPos).is(Blocks.BLUE_ICE)) {
                            cir.setReturnValue(true);
                            return;
                        }
                    }
                }
            } else if (ModGameRules.getGameRuleBoolean(serverLevel, ModGameRules.RULE_STONE_TYPE_GENERATORS)) {
                if (this.fluid.is(FluidTags.LAVA)) {
                    boolean isOverSoulSoil = level.getBlockState(pos.below()).is(Blocks.SOUL_SOIL);
                    for (Direction direction : POSSIBLE_FLOW_DIRECTIONS) {
                        BlockPos neighborPos = pos.relative(direction.getOpposite());
                        Block block = null;
                        if (isOverSoulSoil && level.getBlockState(pos.above()).is(Blocks.PACKED_ICE)) {
                            block = Blocks.CALCITE;
                        } else if (isOverSoulSoil && level.getBlockState(neighborPos).is(Blocks.PACKED_ICE)) {
                            block = Blocks.TUFF;
                        } else if (isOverSoulSoil && level.getBlockState(neighborPos).is(Blocks.MAGMA_BLOCK)) {
                            block = Blocks.DEEPSLATE;
                        }

                        if (block != null) {
                            level.setBlockAndUpdate(neighborPos, block.defaultBlockState());
                            this.fizz(level, neighborPos);
                            cir.setReturnValue(false);
                            return;
                        }
                    }
                }
            }
        }
    }
}
