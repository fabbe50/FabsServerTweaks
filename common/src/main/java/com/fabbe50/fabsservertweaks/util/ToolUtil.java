package com.fabbe50.fabsservertweaks.util;

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
}
