package com.fabbe50.fabsservertweaks.util;

import com.fabbe50.fabsservertweaks.LogUtil;
import com.fabbe50.fabsservertweaks.data.soulbound.SoulBoundRegistry;
import com.fabbe50.fabsservertweaks.registries.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Holder.Reference;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EnchantmentUtil {
    private static final Map<UUID, List<ItemStack>> SOUL_BOUND_SAVED = new HashMap<>();

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

    public static boolean hasOreMiner(LivingEntity livingEntity, ItemStack stack) {
        return hasEnchantment(livingEntity, stack, ModRegistry.ORE_MINER);
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

    public static boolean hasSoulBound(LivingEntity livingEntity, ItemStack stack) {
        return hasEnchantment(livingEntity, stack, ModRegistry.SOUL_BOUND);
    }

    public static boolean hasIceTouch(LivingEntity livingEntity, ItemStack stack) {
        return hasEnchantment(livingEntity, stack, ModRegistry.ICE_TOUCH);
    }

    public static boolean hasEnchantment(LivingEntity livingEntity, ItemStack stack, ResourceKey<Enchantment> enchantmentKey) {
        Optional<Reference<Enchantment>> optionalEnchantmentHolder = livingEntity.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).get(enchantmentKey);
        if (stack.isEmpty()) {
            return false;
        }
        return optionalEnchantmentHolder.filter(enchantmentHolder -> EnchantmentHelper.getItemEnchantmentLevel(enchantmentHolder, stack) > 0).isPresent();
    }

    public static int getEnchantmentLevel(LivingEntity livingEntity, ItemStack stack, ResourceKey<Enchantment> enchantmentKey) {
        Optional<Reference<Enchantment>> optionalEnchantmentHolder = livingEntity.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).get(enchantmentKey);
        if (stack.isEmpty()) {
            return 0;
        }
        return optionalEnchantmentHolder.map(enchantmentHolder -> EnchantmentHelper.getItemEnchantmentLevel(enchantmentHolder, stack)).orElse(0);
    }

    public static void handleCustomEnchantments(ServerLevel level, BlockPos pos, ItemStack stack, ServerPlayer player) {
        if (BuiltinDatapackUtil.isEnabled(level.getServer(), BuiltinDatapack.CUSTOM_ENCHANTMENTS)) {
            if (EnchantmentUtil.hasIceTouch(player, stack)) {
                performIceTouch(level, pos, player, stack);
            }
        }
    }

    public static boolean performIceTouch(ServerLevel level, BlockPos pos, ServerPlayer player, ItemStack stack) {
        int eLevel = EnchantmentUtil.getEnchantmentLevel(player, stack, ModRegistry.ICE_TOUCH);
        boolean stateChanged = false;
        List<BlockPos> positions = WorldUtil.getAdjacentPositions(pos);
        for (BlockPos position : positions) {
            BlockState state = level.getBlockState(position);
            FluidState fluidState = level.getFluidState(position);
            if (state.is(Blocks.LAVA) && fluidState.is(Fluids.LAVA)) {
                Block block = Blocks.COBBLESTONE;
                if (eLevel == 2) {
                    block = Blocks.BASALT;
                }
                if (eLevel == 3) {
                    block = Blocks.OBSIDIAN;
                }
                level.setBlockAndUpdate(position, block.defaultBlockState());
                stateChanged = true;
            }
            if (state.canBeReplaced() && fluidState.is(Fluids.WATER)) {
                Block block = Blocks.ICE;
                if (eLevel >= 2) {
                    block = Blocks.PACKED_ICE;
                }
                level.setBlockAndUpdate(position, block.defaultBlockState());
                stateChanged = true;
            }
        }
        return stateChanged;
    }

    public static boolean performCapturing(ServerLevel level, LivingEntity entity, LivingEntity sourceEntity) {
        ItemStack toolStack = sourceEntity.getMainHandItem();
        if (EnchantmentUtil.hasEnchantment(sourceEntity, toolStack, ModRegistry.CAPTURING)) {
            Optional<Holder<Item>> optionalItemHolder = SpawnEggItem.byId(entity.getType());
            if (optionalItemHolder.isPresent()) {
                Holder<Item> itemHolder = optionalItemHolder.get();
                int capturingLevel = EnchantmentUtil.getEnchantmentLevel(sourceEntity, toolStack, ModRegistry.CAPTURING);
                double chance = Math.min(50d / (1000 / Math.pow(10, Math.clamp(capturingLevel, 1, 3))), 50) * 10;
                if (level.getRandom().nextInt(0, 1000) < chance) {
                    return WorldUtil.dropItem(level, entity.blockPosition(), new ItemStack(itemHolder.value()));
                }
            }
        }
        return false;
    }

    public static boolean performHammer(ServerLevel level, BlockPos pos, Direction face, ServerPlayer player, ItemStack toolStack) {
        BlockState blockState = level.getBlockState(pos);
        int hammerLevel = EnchantmentUtil.getEnchantmentLevel(player, toolStack, ModRegistry.HAMMER);
        float baseSpeed = blockState.getDestroySpeed(level, pos);
        if (!blockState.hasBlockEntity()) {
            BlockPos usedPos = pos;
            if (hammerLevel == 1) {
                if (player.getBlockY() + 1 == pos.getY()) {
                    usedPos = pos.below();
                }
            }
            if (hammerLevel == 3) {
                if (player.getBlockY() + 1 == pos.getY()) {
                    usedPos = pos.above();
                }
            }
            AABB aabb = new AABB(usedPos.getX(), usedPos.getY(), usedPos.getZ(), usedPos.getX(), usedPos.getY(), usedPos.getZ());
            if (hammerLevel == 1) {
                aabb = aabb.expandTowards(0, 1, 0);
            }
            if (face != null) {
                aabb = WorldUtil.inflateFromBreakAxis(aabb, face.getAxis(), (hammerLevel - 1));
            } else {
                Direction direction = player.getDirection();
                aabb = WorldUtil.inflateFromBreakAxis(aabb, direction.getAxis(), (hammerLevel - 1));
            }
            LogUtil.debug("Hammer operation will attempt to break blocks within the following AABB: " + aabb);
            Set<BlockPos> toBreak = BlockPos.betweenClosedStream(aabb)
                    .filter(blockPos -> {
                        BlockState state1 = level.getBlockState(blockPos);
                        if ((state1.is(Blocks.LAVA) || state1.is(Blocks.WATER)) && EnchantmentUtil.hasIceTouch(player, toolStack)) {
                            return true;
                        }
                        if (state1.isAir() || state1.hasBlockEntity() || (!toolStack.isCorrectToolForDrops(state1) && state1.requiresCorrectToolForDrops())) {
                            return false;
                        }
                        float speed = state1.getDestroySpeed(level, blockPos);
                        if (speed < 0) {
                            return false;
                        }
                        if (speed <= 1.5f) {
                            return true;
                        }
                        return speed <= baseSpeed;
                    })
                    .map(BlockPos::immutable)
                    .collect(Collectors.toSet());
            if (toBreak.isEmpty()) {
                LogUtil.debug("No blocks to break.");
                return false;
            }
            WorldUtil.breakBlocks(level, pos, toBreak, player, toolStack);
            LogUtil.debug("Hammer operation performed successfully.");
            return true;
        }
        return false;
    }

    public static boolean performTreeChop(ServerLevel level, BlockPos pos, ServerPlayer player, ItemStack toolStack) {
        BlockState blockState = level.getBlockState(pos);
        if (isNetherTree(blockState)) {
            return performVeinMining(level, pos, 2, 256, player, toolStack, ModRegistry.TREE_CHOPPER_WHITELIST,
                    blockStates -> isNetherTree(blockStates.checkingState()) && blockStates.checkingState().is(blockStates.originalState().getBlock()),
                    ModRegistry.TREE_CHOPPER_ATTACHMENTS,
                    states -> {
                        BlockState state = states.checkingState();
                        if (state.is(Blocks.WARPED_WART_BLOCK) || state.is(Blocks.NETHER_WART_BLOCK) || state.is(Blocks.SHROOMLIGHT)) {
                            return new VeinMinerResult(true, true);
                        }
                        return new VeinMinerResult(false, false);
                    });
        }
        if (isGiantMushroomBlock(blockState)) {
            return performVeinMining(level, pos, 2, 256, player, toolStack, ModRegistry.TREE_CHOPPER_WHITELIST,
                    blockStates -> isGiantMushroomBlock(blockStates.checkingState()) && blockStates.checkingState().is(blockStates.originalState().getBlock()),
                    ModRegistry.TREE_CHOPPER_ATTACHMENTS,
                    states -> {
                        BlockState state = states.checkingState();
                        if (state.getBlock() instanceof HugeMushroomBlock) {
                            return new VeinMinerResult(true, true);
                        }
                        return new VeinMinerResult(false, false);
                    });
        }
        return performVeinMining(level, pos, 2, 256, player, toolStack, ModRegistry.TREE_CHOPPER_WHITELIST,
                blockStates -> isOverworldTree(blockStates.checkingState()) && blockStates.checkingState().is(blockStates.originalState().getBlock()),
                ModRegistry.TREE_CHOPPER_ATTACHMENTS,
                states -> {
                    BlockState state = states.checkingState();
                    if (state.getBlock() instanceof LeavesBlock) {
                        boolean persistentLeaves = state.getValue(LeavesBlock.PERSISTENT);
                        if (!persistentLeaves) {
                            return new VeinMinerResult(true, false);
                        }
                    }
                    return new VeinMinerResult(false, false);
                });
    }

    private static boolean isNetherTree(BlockState state) {
        return state.is(BlockTags.WARPED_STEMS) || state.is(BlockTags.CRIMSON_STEMS);
    }

    private static boolean isOverworldTree(BlockState state) {
        return state.is(BlockTags.LOGS_THAT_BURN);
    }

    private static boolean isGiantMushroomBlock(BlockState state) {
        return state.is(Blocks.BROWN_MUSHROOM_BLOCK) || state.is(Blocks.RED_MUSHROOM_BLOCK);
    }

    public static boolean performOreMiner(ServerLevel level, BlockPos pos, ServerPlayer player, ItemStack toolStack) {
        return performVeinMining(level, pos, 2, 512, player, toolStack, ModRegistry.ORE_MINER_WHITELIST, null, null, null);
    }

    public static boolean performVeinMining(ServerLevel level, BlockPos pos, int range, int maxAmount, ServerPlayer player, ItemStack toolStack, TagKey<Block> validTargets, Function<BlockStates, Boolean> targetFunction, TagKey<Block> requiredAttachments, Function<BlockStates, VeinMinerResult> attachmentFunction) {
        if (player.isShiftKeyDown()) {
            return false;
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
                            if (requiredAttachments != null && attachmentFunction != null) {
                                if (state.is(requiredAttachments)) {
                                    VeinMinerResult result = attachmentFunction.apply(new BlockStates(state, blockState));
                                    if (result.valid()) {
                                        valid.set(true);
                                    }
                                    if (result.harvest()) {
                                        return true;
                                    }
                                }
                            }
                            if (targetFunction != null) {
                                return targetFunction.apply(new BlockStates(state, blockState));
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
                            return true;
                        }
                    }
                }
            }
            if (valid.get()) {
                WorldUtil.breakBlocks(level, pos, found, player, toolStack);
                return true;
            }
        }
        return false;
    }

    public static boolean handleSoulBoundAfterDeath(Player player) {
        LogUtil.debug("Player has died. Checking for soul bound items...");
        Iterator<ItemStack> stackIterator = player.getInventory().iterator();
        List<ItemStack> soulBoundItems = new ArrayList<>();
        while (stackIterator.hasNext()) {
            ItemStack stack = stackIterator.next();
            if (EnchantmentUtil.hasSoulBound(player, stack)) {
                LogUtil.debug("Soul bound item found. Adding to list and removing from inventory to prevent item drop...");
                soulBoundItems.add(stack.copy());
                player.getInventory().removeItem(stack);
            }
        }
        if (soulBoundItems.isEmpty()) {
            return false;
        }
        LogUtil.debug("Soul bound items found, saving to storage... (items: " + soulBoundItems + ")");
        SoulBoundRegistry.setSoulBoundItems(player, soulBoundItems);
        return true;
    }

    public static boolean handleSoulBoundAfterRespawn(Player player) {
        List<ItemStack> soulBoundItems = SoulBoundRegistry.getAndClearSoulBoundItems(player);
        if (!soulBoundItems.isEmpty()) {
            LogUtil.debug("Player has soul bound items. Adding them to their inventory... (items: " + soulBoundItems + ")");
            for (ItemStack soulBoundItem : soulBoundItems) {
                player.getInventory().add(soulBoundItem.copy());
                LogUtil.debug("Adding soul bound item to player inventory: " + soulBoundItem);
            }
            return true;
        }
        return false;
    }

    public static DisenchantingEntityResult disenchantEntity(ItemEntity itemEntity, int enchantmentsToRemove) {
        if (itemEntity == null) {
            return new DisenchantingEntityResult(null, new ArrayList<>());
        }
        ItemStack stack = itemEntity.getItem();
        DisenchantingItemStackResult result = disenchantItemStack(stack, enchantmentsToRemove);
        if (result.itemStack() != null) {
            ItemEntity newItemEntity = new ItemEntity(itemEntity.level(), itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(), result.itemStack());
            newItemEntity.setDeltaMovement(Vec3.ZERO);
            newItemEntity.absSnapRotationTo(itemEntity.yRotO, itemEntity.xRotO);
            itemEntity.discard();
            return new DisenchantingEntityResult(newItemEntity, result.enchantments());
        }
        return new DisenchantingEntityResult(itemEntity, new ArrayList<>());
    }

    public static DisenchantingItemStackResult disenchantItemStack(ItemStack stack, int enchantmentsToRemove) {
        if (stack == null || stack.isEmpty()) {
            return new DisenchantingItemStackResult(null, new ArrayList<>());
        }
        if (stack.isEnchanted()) {
            ItemEnchantments enchantments = stack.getEnchantments();
            List<Holder<Enchantment>> enchantmentHolderList = enchantments.keySet().stream().toList();
            List<EnchantmentResult> removedEnchantments = new ArrayList<>();
            for (int i = 0; i < enchantmentHolderList.size(); i++) {
                if (i >= enchantmentsToRemove) {
                    break;
                }
                Holder<Enchantment> enchantmentHolder = enchantmentHolderList.get(i);
                int level = enchantments.getLevel(enchantmentHolder);
                removedEnchantments.add(new EnchantmentResult(enchantmentHolder, level));
                EnchantmentHelper.updateEnchantments(stack, mutable -> mutable.set(enchantmentHolder, 0));
            }
            int enchantmentsLeft = stack.getEnchantments().size();
            if (enchantmentsLeft == 0) {
                stack.set(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
                stack.set(DataComponents.REPAIR_COST, 0);
            } else {
                int baseCost = 0;
                for (int i = 0; i < enchantmentsLeft; i++) {
                    baseCost = AnvilMenu.calculateIncreasedRepairCost(baseCost);
                }
                stack.set(DataComponents.REPAIR_COST, baseCost);
            }
            return new DisenchantingItemStackResult(stack, removedEnchantments);
        }
        return new DisenchantingItemStackResult(stack, new ArrayList<>());
    }

    public record DisenchantingEntityResult(ItemEntity itemEntity, List<EnchantmentResult> enchantments) {}

    public record DisenchantingItemStackResult(ItemStack itemStack, List<EnchantmentResult> enchantments) {}

    public record EnchantmentResult(Holder<Enchantment> enchantmentHolder, int level) {}

    public record BlockStates(BlockState checkingState, BlockState originalState) {
    }

    public record VeinMinerResult(boolean valid, boolean harvest) {}
}
