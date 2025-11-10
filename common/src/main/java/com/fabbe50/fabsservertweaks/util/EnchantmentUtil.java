package com.fabbe50.fabsservertweaks.util;

import com.fabbe50.fabsservertweaks.LogUtil;
import com.fabbe50.fabsservertweaks.registries.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.logging.Log;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

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

    public static boolean hasTreeChopper(LivingEntity livingEntity, ItemStack stack) {
        Holder<Enchantment> enchantmentHolder = livingEntity.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ModRegistry.TREE_CHOPPER);
        if (stack.isEmpty()) {
            return false;
        }
        return EnchantmentHelper.getItemEnchantmentLevel(enchantmentHolder, stack) > 0;
    }

    public static void performVeinMining(ServerLevel level, BlockPos pos, int range, int maxAmount, ServerPlayer player, ItemStack toolStack, TagKey<Block> validTargets, TagKey<Block> requiredAttachments) {
        if (player.isShiftKeyDown()) {
            return;
        }
        BlockState blockState = level.getBlockState(pos);
        if (blockState.is(validTargets)) {
            Set<BlockPos> found = new HashSet<>();
            Set<BlockPos> checked = new HashSet<>();
            Queue<BlockPos> toCheck = new LinkedList<>();
            AtomicBoolean valid = new AtomicBoolean(requiredAttachments == null);

            found.add(pos);
            toCheck.add(pos);

            while (!toCheck.isEmpty()) {
                BlockPos blockPos = toCheck.poll();
                if (!checked.add(blockPos)) {
                    continue;
                }
                Set<BlockPos> matched = BlockPos.betweenClosedStream(blockPos.offset(-range, -range, -range), blockPos.offset(range, range, range))
                        .filter(aPos -> {
                            BlockState state = level.getBlockState(aPos);
                            if (requiredAttachments != null) {
                                if (state.is(requiredAttachments)) {
                                    if (state.getBlock() instanceof LeavesBlock) {
                                        boolean persistentLeaves = state.getValue(LeavesBlock.PERSISTENT);
                                        if (!persistentLeaves) {
                                            valid.set(true);
                                        }
                                    }
                                    if (state.getBlock() instanceof HugeMushroomBlock) {
                                        valid.set(true);
                                        return true;
                                    }
                                    if (state.is(Blocks.WARPED_WART_BLOCK) || state.is(Blocks.NETHER_WART_BLOCK) || state.is(Blocks.SHROOMLIGHT)) {
                                        valid.set(true);
                                        return true;
                                    }
                                }
                            }
                            return state.is(blockState.getBlock());
                        })
                        .map(BlockPos::immutable)
                        .collect(Collectors.toSet());

                for (BlockPos match : matched) {
                    if (found.size() < maxAmount) {
                        found.add(match);
                        if (!checked.contains(match)) {
                            toCheck.add(match);
                        }
                    } else {
                        if (valid.get()) {
                            WorldUtil.breakBlocks(level, pos, found, player, toolStack);
                            return;
                        }
                    }
                }
            }
            if (valid.get()) {
                WorldUtil.breakBlocks(level, pos, found, player, toolStack);
            }
        }
    }
}
