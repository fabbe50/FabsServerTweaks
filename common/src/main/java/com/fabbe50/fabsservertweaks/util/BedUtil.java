package com.fabbe50.fabsservertweaks.util;

import com.fabbe50.fabsservertweaks.data.storage.BedNameStore;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
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

    public static boolean wakeUpFromSleepingBag(Level level, BlockPos pos, BlockState state) {
        BlockPos bedOrigin = BedUtil.getBaseBedPos(pos, state);
        Component customName = BedNameStore.getAndRemove(level, bedOrigin);
        if (customName != null) {
            ItemStack stack = new ItemStack(state.getBlock().asItem());
            if (stack.is(ItemTags.BEDS)) {
                BedUtil.breakBed(level, pos, state);
                stack.set(DataComponents.CUSTOM_NAME, customName);
                WorldUtil.dropItem(level, pos, stack);
            }
            return true;
        }
        return false;
    }
}

