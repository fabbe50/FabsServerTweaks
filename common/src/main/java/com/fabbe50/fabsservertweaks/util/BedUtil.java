package com.fabbe50.fabsservertweaks.util;

import com.fabbe50.fabsservertweaks.data.storage.BedNameStore;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;

public class BedUtil {
    public static BlockPos getBaseBedPos(BlockPos pos, BlockState state) {
        if (!(state.getBlock() instanceof BedBlock)) {
            return pos;
        }

        BedPart part = state.getValue(BedBlock.PART);
        if (part == BedPart.FOOT) {
            return pos;
        }

        // HEAD → go one block *back* along facing to get the FOOT
        return pos.relative(state.getValue(BedBlock.FACING).getOpposite());
    }

    public static void breakBed(Level level, BlockPos pos, BlockState state) {
        if (!(state.getBlock() instanceof BedBlock)) {
            return;
        }

        BedPart part = state.getValue(BedBlock.PART);
        if (part == BedPart.FOOT) {
            BlockPos headPart = pos.relative(state.getValue(BedBlock.FACING));
            level.removeBlock(headPart, false);
            level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
        } else {
            BlockPos footPart = pos.relative(state.getValue(BedBlock.FACING).getOpposite());
            level.removeBlock(pos, false);
            level.setBlockAndUpdate(footPart, Blocks.AIR.defaultBlockState());
        }
    }

    public static boolean isSleepingBag(Level level, BlockPos pos, BlockState state) {
        BlockPos bedOrigin = BedUtil.getBaseBedPos(pos, state);
        Component customName = BedNameStore.get(level, bedOrigin);
        return customName != null && customName.getString().equalsIgnoreCase("sleeping bag");
    }
}

