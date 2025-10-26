package com.fabbe50.fabsservertweaks.util;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;

public class EnchantmentUtil {
    public static boolean hasFeatherFalling(LivingEntity livingEntity) {
        Holder<Enchantment> enchantmentHolder = livingEntity.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.FEATHER_FALLING);
        ItemStack equipmentStack = livingEntity.getItemBySlot(EquipmentSlot.FEET);
        if (equipmentStack.isEmpty()) {
            return false;
        }
        return EnchantmentHelper.getItemEnchantmentLevel(enchantmentHolder, equipmentStack) > 0;
    }

    public static boolean hasAquaAffinity(LivingEntity livingEntity) {
        Holder<Enchantment> enchantmentHolder = livingEntity.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.AQUA_AFFINITY);
        ItemStack headStack = livingEntity.getItemBySlot(EquipmentSlot.HEAD);
        if (headStack.isEmpty()) {
            return false;
        }
        return EnchantmentHelper.getItemEnchantmentLevel(enchantmentHolder, headStack) > 0;
    }
}
