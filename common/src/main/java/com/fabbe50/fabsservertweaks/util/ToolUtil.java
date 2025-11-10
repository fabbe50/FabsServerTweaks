package com.fabbe50.fabsservertweaks.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ToolUtil {
    public static int getTillingRadiusFromHoe(ItemStack stack) {
        if (stack.is(Items.IRON_HOE)) {
            return 2;
        } else if (stack.is(Items.GOLDEN_HOE) || stack.is(Items.NETHERITE_HOE)) {
            return 5;
        } else if (stack.is(Items.DIAMOND_HOE)) {
            return 3;
        }
        return 0;
    }

    public static int getScytheRadiusFromHoe(ItemStack stack) {
        if (stack.is(Items.IRON_HOE)) {
            return 3;
        } else if (stack.is(Items.GOLDEN_HOE) || stack.is(Items.NETHERITE_HOE)) {
            return 7;
        } else if (stack.is(Items.DIAMOND_HOE)) {
            return 5;
        }
        return 0;
    }

    public static void hurtItem(int i, ServerLevel level, ItemStack stack, BlockPos pos) {
        stack.hurtAndBreak(i, level, null, item -> level.playSeededSound(null, pos.getX(), pos.getY(), pos.getZ(), item.components().get(DataComponents.BREAK_SOUND), SoundSource.BLOCKS, 1, 0.5f, 1));
    }

    public static void hurtItem(LivingEntity entity, ItemStack stack) {
        hurtItem(1, entity, stack);
    }

    public static void hurtItem(int i, LivingEntity entity, ItemStack stack) {
        stack.hurtAndBreak(i, entity, entity.getEquipmentSlotForItem(stack));
    }
}
