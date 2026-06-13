package com.fabbe50.fabsservertweaks.mixin;

import com.fabbe50.fabsservertweaks.registries.ModGameRules;
import com.fabbe50.fabsservertweaks.util.PillarGrowUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SugarCaneBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SugarCaneBlock.class)
public class SugarCaneBlockMixin extends Block {
    @Shadow
    @Final
    public static IntegerProperty AGE;

    public SugarCaneBlockMixin(Properties properties) {
        super(properties);
    }

    @Inject(method = "randomTick", at = @At("HEAD"), cancellable = true)
    private void injectRandomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo ci) {
        int maxHeight = level.getGameRules().get(ModGameRules.RULE_SUGAR_CANE_GROW_HEIGHT);
        if (maxHeight != 3) {
            ci.cancel();
            BlockPos above = pos.above();
            if (level.isEmptyBlock(above)) {
                int height = PillarGrowUtil.getPillarHeight(state, level, pos);
                int age = state.getValue(AGE);

                if (PillarGrowUtil.canPillarGrow(height, maxHeight, age)) {
                    if (age == 15) {
                        level.setBlockAndUpdate(pos.above(), this.defaultBlockState());
                        level.setBlock(pos, state.setValue(AGE, 0), 260);
                    } else {
                        level.setBlock(pos, state.setValue(AGE, age + 1), 260);
                    }
                }
            }
        }
    }
}
