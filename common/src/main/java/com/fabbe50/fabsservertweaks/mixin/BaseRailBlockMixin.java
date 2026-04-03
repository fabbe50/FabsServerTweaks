package com.fabbe50.fabsservertweaks.mixin;

import com.fabbe50.fabsservertweaks.registries.ModGameRules;
import com.fabbe50.fabsservertweaks.util.RailUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BaseRailBlock.class)
public abstract class BaseRailBlockMixin {
    @Inject(method = "onPlace", at = @At("HEAD"), cancellable = true)
    private void injectOnPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl, CallbackInfo ci) {
        if (level instanceof ServerLevel serverLevel && serverLevel.getGameRules().get(ModGameRules.RULE_BETTER_RAIL_PLACEMENT)) {
            ci.cancel();
        }
    }

    @Inject(method = "getStateForPlacement", at = @At("RETURN"), cancellable = true)
    private void injectStateForPlacement(BlockPlaceContext blockPlaceContext, CallbackInfoReturnable<BlockState> cir) {
        Level level = blockPlaceContext.getLevel();
        if (level instanceof ServerLevel serverLevel && serverLevel.getGameRules().get(ModGameRules.RULE_BETTER_RAIL_PLACEMENT)) {
            cir.setReturnValue(RailUtil.getBetterPlacement(cir.getReturnValue(), blockPlaceContext));
        }
    }

    @Inject(method = "updateDir", at = @At("HEAD"), cancellable = true)
    private void injectUpdateDir(Level level, BlockPos blockPos, BlockState blockState, boolean bl, CallbackInfoReturnable<BlockState> cir) {
        if (level instanceof ServerLevel serverLevel && serverLevel.getGameRules().get(ModGameRules.RULE_BETTER_RAIL_PLACEMENT)) {
            RailShape railShape = RailUtil.getShapeFromBlockState(blockState);
            if (railShape != null && RailUtil.sidesAreParallel(level, blockPos, railShape)) {
                cir.setReturnValue(blockState);
            }
        }
    }
}
