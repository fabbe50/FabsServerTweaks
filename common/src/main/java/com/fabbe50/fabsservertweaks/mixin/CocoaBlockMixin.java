package com.fabbe50.fabsservertweaks.mixin;

import com.fabbe50.fabsservertweaks.util.WorldUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.CocoaBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CocoaBlock.class)
public class CocoaBlockMixin {
    @Shadow
    @Final
    public static IntegerProperty AGE;

    @Inject(method = "randomTick", at = @At("HEAD"))
    private void injectRandomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo ci) {
        int age = state.getValue(AGE);
        if (WorldUtil.shouldPlantGrowExtra(level, pos, random, age, 2)) {
            level.setBlock(pos, state.setValue(AGE, age + 1), 2);
        }
    }
}
