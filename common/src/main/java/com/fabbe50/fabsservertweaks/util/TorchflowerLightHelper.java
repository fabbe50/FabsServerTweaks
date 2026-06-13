package com.fabbe50.fabsservertweaks.util;

import com.fabbe50.fabsservertweaks.registries.ModGameRules;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LightBlock;
import net.minecraft.world.level.block.state.BlockState;

public final class TorchflowerLightHelper {
    private static final BlockState TORCHFLOWER_LIGHT = Blocks.LIGHT.defaultBlockState().setValue(LightBlock.LEVEL, 8);

    private TorchflowerLightHelper() {
    }

    public static void refreshAdjacentLights(ServerLevel level, BlockPos torchflowerPos) {
        if (level.getGameRules().get(ModGameRules.RULE_TORCHFLOWERS_GLOW)) {
            for (Direction direction : Direction.values()) {
                BlockPos targetPos = torchflowerPos.relative(direction);
                if (level.getBlockState(targetPos).isAir()) {
                    level.setBlockAndUpdate(targetPos, TORCHFLOWER_LIGHT);
                }
            }
        }
    }

    public static void cleanupAdjacentLights(ServerLevel level, BlockPos torchflowerPos) {
        for (Direction direction : Direction.values()) {
            BlockPos targetPos = torchflowerPos.relative(direction);
            removeIfOrphaned(level, targetPos, torchflowerPos);
        }
    }

    public static void removeIfOrphaned(ServerLevel level, BlockPos pos) {
        removeIfOrphaned(level, pos, null);
    }

    public static void removeIfOrphaned(ServerLevel level, BlockPos pos, BlockPos ignoredTorchflowerPos) {
        if (level.getBlockState(pos).is(Blocks.LIGHT) && !hasAdjacentTorchflower(level, pos, ignoredTorchflowerPos)) {
            level.removeBlock(pos, false);
        }
    }

    private static boolean hasAdjacentTorchflower(ServerLevel level, BlockPos pos, BlockPos ignoredTorchflowerPos) {
        for (Direction direction : Direction.values()) {
            BlockPos targetPos = pos.relative(direction);
            if (!targetPos.equals(ignoredTorchflowerPos) && level.getBlockState(targetPos).is(Blocks.TORCHFLOWER)) {
                return true;
            }
        }
        return false;
    }
}
