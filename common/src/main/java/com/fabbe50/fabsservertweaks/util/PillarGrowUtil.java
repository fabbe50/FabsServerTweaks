package com.fabbe50.fabsservertweaks.util;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class PillarGrowUtil {
    public static BlockPos getPillarFoot(final Block block, final ServerLevel level, final BlockPos pos) {
        BlockPos interactionPos = pos;
        while (level.getBlockState(interactionPos.below()).is(block)) {
            interactionPos = interactionPos.below();
        }
        return interactionPos;
    }

    public static int getPillarHeight(final BlockState state, final ServerLevel level, final BlockPos pos) {
        return getPillarHeight(state.getBlock(), level, pos);
    }

    public static int getPillarHeight(final Block block, final ServerLevel level, final BlockPos pos) {
        BlockPos interactionPos = getPillarFoot(block, level, pos);
        int height = 1;
        while(level.getBlockState(interactionPos.above()).is(block)) {
            interactionPos = interactionPos.above();
            height++;
        }
        return height;
    }

    public static boolean canPillarGrow(final int height, final int maxHeight, final int age) {
        return height < maxHeight || age != 15;
    }
}
