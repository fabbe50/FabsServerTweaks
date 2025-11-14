package com.fabbe50.fabsservertweaks.registries;

import com.fabbe50.fabsservertweaks.commands.GotoCommand;
import com.fabbe50.fabsservertweaks.commands.PresetCommand;
import com.fabbe50.fabsservertweaks.network.packets.SeedPacket;
import com.fabbe50.fabsservertweaks.registries.gamerules.DifficultyValue;
import com.fabbe50.fabsservertweaks.util.*;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.*;
import dev.architectury.networking.NetworkManager;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class EventRegistry {
    public static void init() {
        EntityEvent.ADD.register((entity, level) -> {
            if (level instanceof ServerLevel serverLevel) {
                if (serverLevel.getGameRules().getBoolean(ModGameRules.RULE_MOBS_SPAWN_WITH_EFFECTS)) {
                    if (entity instanceof Monster monster) {
                        if (entity.getType().is(ModRegistry.MOBS_WITH_POTION_EFFECTS_BLACKLIST)) {
                            return EventResult.pass();
                        }
                        RandomSource random = entity.getRandom();
                        DifficultyValue.Difficulty difficulty = serverLevel.getGameRules().getRule(ModGameRules.RULE_SPAWN_WITH_EFFECT_MODE).getValue();
                        if (shouldApplyEffect(serverLevel, random, difficulty)) {
                            if (serverLevel.dimension().equals(Level.NETHER)) {
                                if (random.nextDouble() < 0.75d) {
                                    monster.addEffect(EffectUtil.getRandomNetherEffect(random));
                                    return EventResult.interruptTrue();
                                }
                            } else if (serverLevel.dimension().equals(Level.END)) {
                                if (random.nextDouble() < 0.25d) {
                                    monster.addEffect(EffectUtil.getRandomEndEffect(random));
                                    return EventResult.interruptTrue();
                                }
                            } else {
                                if (ChanceUtil.rollAtY(level, monster.getBlockY(), 0.01, 0.75, 5.25, random)) {
                                    monster.addEffect(EffectUtil.getRandomOverworldEffect(random));
                                    return EventResult.interruptTrue();
                                }
                            }
                        }
                    }
                }
            }
            return EventResult.pass();
        });
        EntityEvent.LIVING_DEATH.register((livingEntity, damageSource) -> {
            if (livingEntity instanceof Mob mob) {
                Level level = mob.level();
                if (level instanceof ServerLevel serverLevel) {
                    GameRules gameRules = serverLevel.getGameRules();
                    if (!gameRules.getBoolean(ModGameRules.RULE_MOB_DROP_EQUIPABLE)) {
                        for (EquipmentSlot slot : EquipmentSlot.values()) {
                            if (!mob.getDropChances().isPreserved(slot)) {
                                ItemStack stack = mob.getItemBySlot(slot);
                                if (stack.has(DataComponents.EQUIPPABLE) || stack.has(DataComponents.TOOL) || stack.has(DataComponents.WEAPON) || stack.getItem() instanceof ProjectileWeaponItem) {
                                    stack.setCount(0);
                                }
                            }
                        }
                    }
                    if (mob instanceof Shulker shulker) {
                        int shulker_shells = gameRules.getInt(ModGameRules.RULE_SHULKER_SHELL_DROP_AMOUNT);
                        if (shulker_shells > 0) {
                            shulker.spawnAtLocation(serverLevel, new ItemStack(Items.SHULKER_SHELL, shulker_shells));
                            shulker.remove(Entity.RemovalReason.DISCARDED);
                            return EventResult.interruptTrue();
                        }
                    }
                }
            }
            return EventResult.pass();
        });
        CommandRegistrationEvent.EVENT.register((commandDispatcher, commandBuildContext, commandSelection) -> {
            GotoCommand.register(commandDispatcher);
            PresetCommand.register(commandDispatcher);
        });
        PlayerEvent.PLAYER_JOIN.register(serverPlayer -> {
            if (serverPlayer.level().getGameRules().getBoolean(ModGameRules.RULE_SHARE_SEED)) {
                long seed = serverPlayer.level().getSeed();
                try {
                    NetworkManager.sendToPlayer(serverPlayer, new SeedPacket.Client.PacketPayload(seed));
                } catch (UnsupportedOperationException ignored) {
                    serverPlayer.sendSystemMessage(Component.literal("Server Seed: " + serverPlayer.level().getSeed()));
                }
            }
        });
        InteractionEvent.LEFT_CLICK_BLOCK.register((player, hand, pos, face) -> {
            Level level = player.level();
            ItemStack stack = player.getItemInHand(hand);
            if (level instanceof ServerLevel serverLevel && serverLevel.getGameRules().getBoolean(ModGameRules.RULE_BETTER_HOES) && !player.isShiftKeyDown()) {
                AtomicBoolean flag = new AtomicBoolean(false);
                WorldUtil.getBlocksInSphericalRadius(pos, ToolUtil.getScytheRadiusFromHoe(stack))
                        .forEach(blockPos1 -> {
                            BlockState state = level.getBlockState(blockPos1);
                            if (state.is(ModRegistry.SCYTHE_ABLE)) {
                                List<ItemStack> stacks = state.getDrops(new LootParams.Builder(serverLevel).withParameter(LootContextParams.TOOL, stack).withParameter(LootContextParams.ORIGIN, blockPos1.getCenter()));
                                for (ItemStack dropStack : stacks) {
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
            }
            return InteractionResult.PASS;
        });
        BlockEvent.BREAK.register((level, pos, state, player, xp) -> {
            if (level instanceof ServerLevel serverLevel) {
                ItemStack toolStack = player.getItemInHand(player.getUsedItemHand());
                if (EnchantmentUtil.hasTreeChopper(player, toolStack)) {
                    EnchantmentUtil.performTreeChop(serverLevel, pos, player, toolStack);
                }
                if (EnchantmentUtil.hasSilkTouch(player, toolStack)) {
                    if (state.is(Blocks.SPAWNER) && serverLevel.getGameRules().getBoolean(ModGameRules.RULE_SILK_TOUCHABLE_SPAWNERS)) {
                        if (dropItemWithData(level, pos, new ItemStack(Blocks.SPAWNER))) {
                            return EventResult.interruptTrue();
                        }
                    }
                    if (state.is(Blocks.BUDDING_AMETHYST) && serverLevel.getGameRules().getBoolean(ModGameRules.RULE_SILK_TOUCHABLE_AMETHYST_NODES)) {
                        Block.popResource(level, pos, new ItemStack(Items.BUDDING_AMETHYST));
                        return EventResult.interruptTrue();
                    }
                    if (state.is(Blocks.TRIAL_SPAWNER) && serverLevel.getGameRules().getBoolean(ModGameRules.RULE_SILK_TOUCHABLE_TRIAL_SPAWNERS)) {
                        if (dropItemWithData(level, pos, new ItemStack(Blocks.TRIAL_SPAWNER))) {
                            return EventResult.interruptTrue();
                        }
                    }
                    if (state.is(Blocks.VAULT) && serverLevel.getGameRules().getBoolean(ModGameRules.RULE_SILK_TOUCHABLE_TRIAL_VAULTS)) {
                        if (dropItemWithData(level, pos, new ItemStack(Blocks.VAULT))) {
                            return EventResult.interruptTrue();
                        }
                    }
                }
            }
            return EventResult.pass();
        });
        BlockEvent.PLACE.register((level, pos, state, placer) -> {
            if (level instanceof ServerLevel && placer instanceof Player player && state.is(Blocks.SPAWNER)) {
                ItemStack stack = player.getItemInHand(player.getUsedItemHand());
                CustomData customData = stack.getOrDefault(DataComponents.BLOCK_ENTITY_DATA, CustomData.EMPTY);
                if (!customData.isEmpty()) {
                    BlockEntityType<?> blockEntityType = customData.parseEntityType(level.registryAccess(), Registries.BLOCK_ENTITY_TYPE);
                    if (blockEntityType == null) {
                        return EventResult.pass();
                    }
                    level.setBlockAndUpdate(pos, state);

                    BlockEntity blockEntity = level.getBlockEntity(pos);
                    if (blockEntity != null) {
                        BlockEntityType<?> blockEntityType2 = blockEntity.getType();
                        if (blockEntityType != blockEntityType2) {
                            return EventResult.pass();
                        }

                        if (customData.loadInto(blockEntity, level.registryAccess())) {
                            blockEntity.applyComponentsFromItemStack(stack);
                            blockEntity.setChanged();
                            stack.shrink(1);
                            return EventResult.interruptTrue();
                        }
                    }
                }
            }
            return EventResult.pass();
        });
    }

    private static boolean dropItemWithData(Level level, BlockPos pos, ItemStack stack) {
        BlockEntity spawnerBlockEntity = level.getBlockEntity(pos);
        if (spawnerBlockEntity != null) {
            TagValueOutput tagValueOutput = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, level.registryAccess());
            spawnerBlockEntity.saveWithId(tagValueOutput);
            stack.set(DataComponents.BLOCK_ENTITY_DATA, CustomData.of(tagValueOutput.buildResult()));
            Block.popResource(level, pos, stack);
            level.removeBlockEntity(pos);
            level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
            return true;
        }
        return false;
    }

    private static boolean dropItem(Level level, BlockPos pos, ItemStack stack) {
        ItemEntity itemEntity = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack);
        return level.addFreshEntity(itemEntity);
    }

    private static boolean shouldApplyEffect(ServerLevel level, RandomSource random, DifficultyValue.Difficulty difficulty) {
        if (difficulty.equals(DifficultyValue.Difficulty.SCALE_BY_DIFFICULTY)) {
            return switch (level.getDifficulty()) {
                case PEACEFUL, EASY -> false;
                case NORMAL -> random.nextBoolean();
                case HARD -> true;
            };
        } else return difficulty.equals(DifficultyValue.Difficulty.SAME_ON_ALL_DIFFICULTIES);
    }
}
