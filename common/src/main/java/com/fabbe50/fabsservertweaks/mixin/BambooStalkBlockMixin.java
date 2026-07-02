package com.fabbe50.fabsservertweaks.mixin;

import com.fabbe50.fabsservertweaks.util.WorldUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BambooStalkBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BambooStalkBlock.class)
public abstract class BambooStalkBlockMixin {
    @Shadow
    @Final
    public static IntegerProperty STAGE;

    @Shadow
    protected abstract int getHeightBelowUpToMax(BlockGetter level, BlockPos pos);

    @Shadow
    protected abstract void growBamboo(BlockState state, Level level, BlockPos pos, RandomSource random, int height);

    @Inject(method = "randomTick", at = @At("HEAD"))
    private void injectRandomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo ci) {
        int stage = state.getValue(STAGE);
        if (stage == 0 && level.isEmptyBlock(pos.above()) && level.getRawBrightness(pos.above(), 0) >= 9) {
            int height = getHeightBelowUpToMax(level, pos) + 1;
            if (WorldUtil.shouldPlantGrowExtra(level, pos, random, height, 17)) {
                this.growBamboo(state, level, pos, random, height);
            }
        }
    }
}
