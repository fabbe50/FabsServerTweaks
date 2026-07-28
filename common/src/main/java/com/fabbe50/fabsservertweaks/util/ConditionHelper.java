package com.fabbe50.fabsservertweaks.util;

import com.fabbe50.fabsservertweaks.registries.ModGameRules;
import com.fabbe50.fabsservertweaks.registries.ModRegistry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;

public class ConditionHelper {
    public static boolean preventsHotFloorDamage(ServerLevel level, LivingEntity entity, ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        if (!ModGameRules.getGameRuleBoolean(level, ModGameRules.RULE_PREVENT_HOT_FLOOR_DAMAGE_ITEMS)) {
            return false;
        }
        if (stack.is(ModRegistry.PREVENTS_HOT_FLOOR_DAMAGE)) {
            return true;
        }
        if (stack.is(ItemTags.FOOT_ARMOR) && EnchantmentUtil.hasEnchantment(entity, stack, Enchantments.FROST_WALKER)) {
            return true;
        }
        return false;
    }
}
