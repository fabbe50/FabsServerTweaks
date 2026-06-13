package com.fabbe50.fabsservertweaks.mixin;

import com.fabbe50.fabsservertweaks.registries.ModGameRules;
import com.fabbe50.fabsservertweaks.registries.ModRegistry;
import com.fabbe50.fabsservertweaks.util.PillarGrowUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CactusBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.redstone.Orientation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CactusBlock.class)
public abstract class CactusBlockMixin extends Block {
    public CactusBlockMixin(Properties properties) {
        super(properties);
    }

    @Shadow
    protected abstract boolean canSurvive(BlockState state, LevelReader level, BlockPos pos);

    @Shadow
    @Final
    public static IntegerProperty AGE;

    @Inject(method = "entityInside", at = @At("HEAD"), cancellable = true)
    private void injectEntityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise, CallbackInfo ci) {
        if (entity instanceof ItemEntity itemEntity) {
            if (itemEntity.getItem().is(ModRegistry.IMMUNE_TO_CACTUS_DAMAGE)) {
                ci.cancel();
            }
        }
    }

    @Inject(method = "randomTick", at = @At("HEAD"), cancellable = true)
    private void injectRandomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo ci) {
        int maxHeight = level.getGameRules().get(ModGameRules.RULE_CACTUS_GROW_HEIGHT);
        if (maxHeight != 3) {
            ci.cancel();
            BlockPos above = pos.above();
            if (level.isEmptyBlock(above)) {
                int height = PillarGrowUtil.getPillarHeight(state, level, pos);
                int age = state.getValue(AGE);

                if (!PillarGrowUtil.canPillarGrow(height, maxHeight, age)) {
                    return;
                }

                if (age == 8 && this.canSurvive(this.defaultBlockState(), level, pos.above())) {
                    double chanceToGrowFlower = height >= maxHeight ? (double)0.25F : 0.1;
                    if (random.nextDouble() <= chanceToGrowFlower) {
                        level.setBlockAndUpdate(above, Blocks.CACTUS_FLOWER.defaultBlockState());
                    }
                } else if (age == 15 && height < maxHeight) {
                    level.setBlockAndUpdate(above, this.defaultBlockState());
                    BlockState aboveBlock = state.setValue(AGE, 0);
                    level.setBlock(pos, aboveBlock, 260);
                    level.neighborChanged(aboveBlock, above, this, null, false);
                }

                if (age < 15) {
                    level.setBlock(pos, state.setValue(AGE, age + 1), 260);
                }
            }
        }
    }
}
