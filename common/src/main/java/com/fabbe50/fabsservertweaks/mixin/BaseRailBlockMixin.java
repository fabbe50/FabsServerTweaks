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
    private void injectOnPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston, CallbackInfo ci) {
        if (level instanceof ServerLevel serverLevel && ModGameRules.getGameRuleBoolean(serverLevel, ModGameRules.RULE_BETTER_RAIL_PLACEMENT)) {
            ci.cancel();
        }
    }

    @Inject(method = "getStateForPlacement", at = @At("RETURN"), cancellable = true)
    private void injectStateForPlacement(BlockPlaceContext context, CallbackInfoReturnable<BlockState> cir) {
        Level level = context.getLevel();
        if (level instanceof ServerLevel serverLevel && ModGameRules.getGameRuleBoolean(serverLevel, ModGameRules.RULE_BETTER_RAIL_PLACEMENT)) {
            cir.setReturnValue(RailUtil.getBetterPlacement(cir.getReturnValue(), context));
        }
    }

    @Inject(method = "updateDir", at = @At("HEAD"), cancellable = true)
    private void injectUpdateDir(Level level, BlockPos pos, BlockState state, boolean first, CallbackInfoReturnable<BlockState> cir) {
        if (level instanceof ServerLevel serverLevel && ModGameRules.getGameRuleBoolean(serverLevel, ModGameRules.RULE_BETTER_RAIL_PLACEMENT)) {
            RailShape railShape = RailUtil.getShapeFromBlockState(state);
            if (railShape != null && RailUtil.sidesAreParallel(level, pos, railShape)) {
                cir.setReturnValue(state);
            }
        }
    }
}
