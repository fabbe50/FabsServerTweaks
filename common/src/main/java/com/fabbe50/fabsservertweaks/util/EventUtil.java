package com.fabbe50.fabsservertweaks.util;

import com.fabbe50.fabsservertweaks.LogUtil;
import com.fabbe50.fabsservertweaks.data.ExtGameRule;
import com.fabbe50.fabsservertweaks.registries.ModGameRules;
import com.fabbe50.fabsservertweaks.registries.ModRegistry;
import dev.architectury.event.EventResult;
import dev.architectury.utils.value.IntValue;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class EventUtil {
    public enum BlockEventLogic {
        HOES(new BlockOrBlockTag(ModRegistry.SCYTHE_ABLE), ModGameRules.RULE_BETTER_HOES, false) {
            @Override
            protected InteractionResult internalLeftClickBlock(ServerLevel level, ServerPlayer player, BlockPos pos, BlockState state, ItemStacks stacks, Direction face) {
                if (player.isShiftKeyDown()) {
                    return InteractionResult.PASS;
                }
                AtomicBoolean flag = new AtomicBoolean(false);
                WorldUtil.getBlocksInSphericalRadius(pos, ToolUtil.getScytheRadiusFromHoe(stacks.mainHand()))
                        .forEach(blockPos1 -> {
                            BlockState state1 = level.getBlockState(blockPos1);
                            if (state1.is(ModRegistry.SCYTHE_ABLE)) {
                                List<ItemStack> loot = state.getDrops(new LootParams.Builder(level).withParameter(LootContextParams.TOOL, stacks.mainHand()).withParameter(LootContextParams.ORIGIN, blockPos1.getCenter()));
                                for (ItemStack dropStack : loot) {
                                    ItemEntity itemEntity = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), dropStack);
                                    level.addFreshEntity(itemEntity);
                                }
                                level.setBlockAndUpdate(blockPos1, Blocks.AIR.defaultBlockState());
                                flag.set(true);
                            }
                        });
                if (flag.get()) {
                    return InteractionResult.SUCCESS;
                }
                return InteractionResult.PASS;
            }
        },
        BOTTLE_XP(new BlockOrBlockTag(Blocks.ENCHANTING_TABLE), ModGameRules.RULE_XP_TO_BOTTLES, false) {
            @Override
            protected InteractionResult internalRightClickBlock(ServerLevel level, ServerPlayer player, BlockPos pos, BlockState state, ItemStacks stacks, Direction face) {
                if (player.isShiftKeyDown()) {
                    return InteractionResult.PASS;
                }
                ItemStack mainHand = stacks.mainHand();
                if (mainHand.is(Items.GLASS_BOTTLE)) {
                    ItemStack xpBottle = new ItemStack(Items.EXPERIENCE_BOTTLE);
                    CompoundTag tag = new CompoundTag();
                    int exp = XPUtil.removeLevels(player, 1);
                    tag.putInt("xp", exp);
                    xpBottle.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
                    mainHand.shrink(1);
                    player.addItem(xpBottle);
                    return InteractionResult.SUCCESS;
                }
                return InteractionResult.PASS;
            }
        }
        ;

        private final BlockOrBlockTag blockOrBlockTag;
        private final GameRule<Boolean> rule;
        private final ExtGameRule<Boolean> extRule;
        private final boolean gameRuleInverted;
        BlockEventLogic(BlockOrBlockTag blockOrBlockTag, ExtGameRule<Boolean> extRule, boolean gameRuleInverted) {
            this.blockOrBlockTag = blockOrBlockTag;
            this.extRule = extRule;
            this.rule = extRule.getRule();
            this.gameRuleInverted = gameRuleInverted;
        }

        BlockEventLogic(BlockOrBlockTag blockOrBlockTag, GameRule<Boolean> rule, boolean gameRuleInverted) {
            this.blockOrBlockTag = blockOrBlockTag;
            this.extRule = null;
            this.rule = rule;
            this.gameRuleInverted = gameRuleInverted;
        }

        public BlockOrBlockTag getBlockOrBlockTag() {
            return blockOrBlockTag;
        }

        public Optional<GameRule<Boolean>> getRule() {
            return Optional.ofNullable(rule);
        }

        public boolean isGameRuleInverted() {
            return gameRuleInverted;
        }

        public static EventResult placeBlock(ServerLevel level, BlockPos pos, BlockState state, Entity placer) {
            BlockEventLogic logic = getEventLogic(state);
            if (validateEventRun(logic, level)) {
                if (placer instanceof ServerPlayer player) {
                    return logic.internalPlaceBlock(level, pos, state, player, new ItemStacks(player));
                } else {
                    return logic.internalPlaceBlock(level, pos, state, placer, null);
                }
            }
            return EventResult.pass();
        }

        protected EventResult internalPlaceBlock(ServerLevel level, BlockPos pos, BlockState state, Entity placer, ItemStacks stacks) {
            return EventResult.pass();
        }

        public static EventResult breakBlock(ServerLevel level, BlockPos pos, BlockState state, ServerPlayer player, IntValue xp) {
            BlockEventLogic logic = getEventLogic(state);
            if (validateEventRun(logic, level)) {
                return logic.internalBreakBlock(level, pos, state, player, new ItemStacks(player), xp);
            }
            return EventResult.pass();
        }

        protected EventResult internalBreakBlock(ServerLevel level, BlockPos pos, BlockState state, ServerPlayer player, ItemStacks stacks, IntValue xp) {
            return EventResult.pass();
        }

        public static InteractionResult rightClickBlock(ServerLevel level, ServerPlayer player, BlockPos pos, BlockState state, Direction face) {
            BlockEventLogic logic = getEventLogic(state);
            if (validateEventRun(logic, level)) {
                return logic.internalRightClickBlock(level, player, pos, state, new ItemStacks(player), face);
            }
            return InteractionResult.PASS;
        }

        protected InteractionResult internalRightClickBlock(ServerLevel level, ServerPlayer player, BlockPos pos, BlockState state, ItemStacks stacks, Direction face) {
            return InteractionResult.PASS;
        }

        public static InteractionResult leftClickBlock(ServerLevel level, ServerPlayer player, BlockPos pos, BlockState state, Direction face) {
            BlockEventLogic logic = getEventLogic(state);
            if (validateEventRun(logic, level)) {
                return logic.internalLeftClickBlock(level, player, pos, state, new ItemStacks(player), face);
            }
            return InteractionResult.PASS;
        }

        protected InteractionResult internalLeftClickBlock(ServerLevel level, ServerPlayer player, BlockPos pos, BlockState state, ItemStacks stacks, Direction face) {
            return InteractionResult.PASS;
        }

        private static boolean validateEventRun(BlockEventLogic logic, ServerLevel level) {
            if (logic == null) {
                return false;
            }
            if (logic.getRule().isPresent()) {
                if (logic.isGameRuleInverted()) {
                    return !ModGameRules.getGameRuleBoolean(level, logic.getRule().get());
                } else {
                    return ModGameRules.getGameRuleBoolean(level, logic.getRule().get());
                }
            }
            return true;
        }

        public static BlockEventLogic getEventLogic(BlockState state) {
            Block block = state.getBlock();
            for (BlockEventLogic logic : values()) {
                Block logicBlock = logic.getBlockOrBlockTag().getBlock();
                if (logicBlock != null) {
                    if (logicBlock == block) {
                        return logic;
                    }
                }
                TagKey<Block> blockTag = logic.getBlockOrBlockTag().getBlockTag();
                if (blockTag != null) {
                    if (state.is(blockTag)) {
                        return logic;
                    }
                }
            }
            return null;
        }
    }
}
