package com.fabbe50.fabsservertweaks.util;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;

public class PillarGrowUtil {
    public static int getPillarHeight(final BlockState state, final ServerLevel level, final BlockPos pos) {
        BlockPos above = pos.above();
        if (level.isEmptyBlock(above)) {
            int height = 1;

            while(level.getBlockState(pos.below(height)).is(state.getBlock())) {
                ++height;
            }
            return height;
        }
        return -1;
    }

    public static boolean canPillarGrow(final int height, final int maxHeight, final int age) {
        return height != maxHeight || age != 15;
    }
}
