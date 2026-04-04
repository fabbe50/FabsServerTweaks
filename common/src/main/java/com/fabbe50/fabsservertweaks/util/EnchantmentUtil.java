package com.fabbe50.fabsservertweaks.util;

import com.fabbe50.fabsservertweaks.registries.ModRegistry;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EnchantmentUtil {
    public static boolean hasFeatherFalling(LivingEntity livingEntity) {
        ItemStack stack = livingEntity.getItemBySlot(EquipmentSlot.FEET);
        return hasEnchantment(livingEntity, stack, Enchantments.FEATHER_FALLING);
    }

    public static boolean hasAquaAffinity(LivingEntity livingEntity) {
        ItemStack stack = livingEntity.getItemBySlot(EquipmentSlot.HEAD);
        return hasEnchantment(livingEntity, stack, Enchantments.AQUA_AFFINITY);
    }

    public static boolean hasTreeChopper(LivingEntity livingEntity, ItemStack stack) {
        return hasEnchantment(livingEntity, stack, ModRegistry.TREE_CHOPPER);
    }

    public static boolean hasHammer(LivingEntity livingEntity, ItemStack stack) {
        return hasEnchantment(livingEntity, stack, ModRegistry.HAMMER);
    }

    public static boolean hasSilkTouch(LivingEntity livingEntity, ItemStack stack) {
        return hasEnchantment(livingEntity, stack, Enchantments.SILK_TOUCH);
    }

    public static boolean hasFortune(LivingEntity livingEntity, ItemStack stack) {
        return hasEnchantment(livingEntity, stack, Enchantments.FORTUNE);
    }

    public static boolean hasEnchantment(LivingEntity livingEntity, ItemStack stack, ResourceKey<Enchantment> enchantmentKey) {
        Holder<Enchantment> enchantmentHolder = livingEntity.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(enchantmentKey);
        if (stack.isEmpty()) {
            return false;
        }
        return EnchantmentHelper.getItemEnchantmentLevel(enchantmentHolder, stack) > 0;
    }

    public static int getEnchantmentLevel(LivingEntity livingEntity, ItemStack stack, ResourceKey<Enchantment> enchantmentKey) {
        Holder<Enchantment> enchantmentHolder = livingEntity.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(enchantmentKey);
        if (stack.isEmpty()) {
            return 0;
        }
        return EnchantmentHelper.getItemEnchantmentLevel(enchantmentHolder, stack);
    }

    public static void performTreeChop(ServerLevel level, BlockPos pos, ServerPlayer player, ItemStack toolStack) {
        performVeinMining(level, pos, 2, 256, player, toolStack, ModRegistry.TREE_CHOPPER_WHITELIST, ModRegistry.TREE_CHOPPER_ATTACHMENTS, state -> {
            if (state.getBlock() instanceof LeavesBlock) {
                boolean persistentLeaves = state.getValue(LeavesBlock.PERSISTENT);
                if (!persistentLeaves) {
                    return Pair.of(true, false);
                }
            }
            if (state.getBlock() instanceof HugeMushroomBlock) {
                return Pair.of(true, true);
            }
            if (state.is(Blocks.WARPED_WART_BLOCK) || state.is(Blocks.NETHER_WART_BLOCK) || state.is(Blocks.SHROOMLIGHT)) {
                return Pair.of(true, true);
            }
            return Pair.of(false, false);
        });
    }

    public static void performVeinMining(ServerLevel level, BlockPos pos, int range, int maxAmount, ServerPlayer player, ItemStack toolStack, TagKey<Block> validTargets, TagKey<Block> requiredAttachments, Function<BlockState, Pair<Boolean, Boolean>> attachmentFunction) {
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
                                    Pair<Boolean, Boolean> result = attachmentFunction.apply(state);
                                    if (result.getFirst()) {
                                        valid.set(true);
                                    }
                                    if (result.getSecond()) {
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
