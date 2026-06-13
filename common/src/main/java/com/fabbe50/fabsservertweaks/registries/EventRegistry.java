package com.fabbe50.fabsservertweaks.registries;

import com.fabbe50.fabsservertweaks.Fabsservertweaks;
import com.fabbe50.fabsservertweaks.LogUtil;
import com.fabbe50.fabsservertweaks.commands.GotoCommand;
import com.fabbe50.fabsservertweaks.commands.NicknameCommand;
import com.fabbe50.fabsservertweaks.commands.PresetCommand;
import com.fabbe50.fabsservertweaks.commands.ServerTweaksCommand;
import com.fabbe50.fabsservertweaks.data.nickname.NicknameRegistry;
import com.fabbe50.fabsservertweaks.data.storage.BedNameStore;
import com.fabbe50.fabsservertweaks.events.BedEvents;
import com.fabbe50.fabsservertweaks.events.ItemStackEvent;
import com.fabbe50.fabsservertweaks.network.packets.SeedPacket;
import com.fabbe50.fabsservertweaks.registries.gamerules.DifficultyValue;
import com.fabbe50.fabsservertweaks.util.*;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.*;
import dev.architectury.event.events.common.EntityEvent;
import dev.architectury.networking.NetworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.*;
import net.minecraft.network.chat.HoverEvent.ShowText;
import net.minecraft.network.protocol.game.ClientboundDisguisedChatPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ProblemReporter;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.animal.feline.Cat;
import net.minecraft.world.entity.animal.parrot.Parrot;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.TypedEntityData;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class EventRegistry {
    private static final ThreadLocal<Boolean> REBROADCASTING_CHAT = ThreadLocal.withInitial(() -> false);
    private static final ThreadLocal<Boolean> PLACING_MOB_FROM_LEAD = ThreadLocal.withInitial(() -> false);

    public static void init() {
        EntityEvent.ADD.register((entity, level) -> {
            if (level instanceof ServerLevel serverLevel) {
                if (PLACING_MOB_FROM_LEAD.get()) {
                    PLACING_MOB_FROM_LEAD.set(false);
                    return EventResult.pass();
                }
                if (serverLevel.getGameRules().get(ModGameRules.RULE_MOBS_SPAWN_WITH_EFFECTS)) {
                    if (entity instanceof Monster monster) {
                        if (entity.getType().is(ModRegistry.MOBS_WITH_POTION_EFFECTS_BLACKLIST)) {
                            return EventResult.pass();
                        }
                        RandomSource random = entity.getRandom();
                        DifficultyValue.Difficulty difficulty = Fabsservertweaks.CONFIG.difficulty;
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
            Level level = livingEntity.level();
            if (BuiltinDatapackUtil.isEnabled(level.getServer(), BuiltinDatapack.CUSTOM_ENCHANTMENTS)) {
                if (damageSource.getEntity() instanceof Player player) {
                    ItemStack toolStack = player.getItemInHand(player.getUsedItemHand());
                    if (EnchantmentUtil.hasEnchantment(player, toolStack, ModRegistry.CAPTURING)) {
                        SpawnEggItem item = SpawnEggItem.byId(livingEntity.getType());
                        if (item != null) {
                            int capturingLevel = EnchantmentUtil.getEnchantmentLevel(player, toolStack, ModRegistry.CAPTURING);
                            double chance = Math.min(50d / (1000 / Math.pow(10, Math.clamp(capturingLevel, 1, 3))), 50);
                            if (level.random.nextInt(0, 100) < chance) {
                                dropItem(level, livingEntity.blockPosition(), new ItemStack(item));
                            }
                        }
                    }
                }
            }
            if (livingEntity instanceof Mob mob) {
                if (level instanceof ServerLevel serverLevel) {
                    GameRules gameRules = serverLevel.getGameRules();
                    if (!gameRules.get(ModGameRules.RULE_MOB_DROP_EQUIPABLE)) {
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
                        int shulker_shells = gameRules.get(ModGameRules.RULE_SHULKER_SHELL_DROP_AMOUNT);
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
            NicknameCommand.register(commandDispatcher);
            ServerTweaksCommand.register(commandDispatcher, commandBuildContext);
        });
        PlayerEvent.PLAYER_JOIN.register(serverPlayer -> {
            if (Fabsservertweaks.CONFIG.shareSeed) {
                long seed = serverPlayer.level().getSeed();
                try {
                    NetworkManager.sendToPlayer(serverPlayer, new SeedPacket.Client.PacketPayload(seed));
                } catch (UnsupportedOperationException ignored) {
                    serverPlayer.sendSystemMessage(Component.literal("Server Seed: " + serverPlayer.level().getSeed()));
                }
            }
        });
        InteractionEvent.LEFT_CLICK_BLOCK.register((player, hand, pos, face) -> {
            BreakContextStore.recordFace(player, pos, face);
            Level level = player.level();
            ItemStack stack = player.getItemInHand(hand);
            if (level instanceof ServerLevel serverLevel) {
                if (serverLevel.getGameRules().get(ModGameRules.RULE_BETTER_HOES) && !player.isShiftKeyDown()) {
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
            }
            return InteractionResult.PASS;
        });
        InteractionEvent.RIGHT_CLICK_BLOCK.register((player, hand, pos, face) -> {
            if (player.level() instanceof ServerLevel serverLevel) {
                ItemStack stack = player.getItemInHand(player.getUsedItemHand());
                BlockState state = serverLevel.getBlockState(pos);
                if (stack.is(Items.GLASS_BOTTLE) && state.is(Blocks.ENCHANTING_TABLE) && !player.isShiftKeyDown()) {
                    ItemStack xpBottle = new ItemStack(Items.EXPERIENCE_BOTTLE);
                    CompoundTag tag = new CompoundTag();
                    int exp = XPUtil.removeLevels(player, 1);
                    tag.putInt("xp", exp);
                    xpBottle.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
                    stack.shrink(1);
                    player.addItem(xpBottle);
                    return InteractionResult.SUCCESS;
                }
                if (stack.is(Items.BONE_MEAL) && !player.getCooldowns().isOnCooldown(stack)) {
                    if (state.is(ModRegistry.MOD_BONE_MEALABLE)) {
                        RandomSource random = serverLevel.getRandom();
                        if (state.is(Blocks.LILY_PAD)) {
                            List<BlockPos> blockPositions = WorldUtil.getBlockPositions(new AABB(pos).inflate(1));
                            for (BlockPos blockPos : blockPositions) {
                                if (serverLevel.getRandom().nextInt(3) == 0) {
                                    BlockState checkState = serverLevel.getBlockState(blockPos);
                                    if (checkState.isAir() && (serverLevel.getBlockState(blockPos.below()).is(Blocks.WATER) && serverLevel.getFluidState(blockPos.below()).is(Fluids.WATER))) {
                                        serverLevel.setBlockAndUpdate(blockPos, state);
                                        handleBoneMealUsed(serverLevel, pos, player, stack, true);
                                        return InteractionResult.SUCCESS;
                                    }
                                }
                            }
                        } else if (state.is(Blocks.SEA_PICKLE)) {
                            int pickles = state.getValue(SeaPickleBlock.PICKLES);
                            if (state.getValue(SeaPickleBlock.WATERLOGGED) && pickles < 4) {
                                serverLevel.setBlockAndUpdate(pos, state.setValue(SeaPickleBlock.PICKLES, pickles + 1));
                                handleBoneMealUsed(serverLevel, pos.above(), player, stack, true);
                                return InteractionResult.SUCCESS;
                            }
                        } else if ((state.is(Blocks.SUGAR_CANE) || state.is(Blocks.CACTUS))) {
                            if (growInColumn(serverLevel, pos, state.getBlock(), 3)) {
                                handleBoneMealUsed(serverLevel, pos, player, stack, false);
                                return InteractionResult.SUCCESS;
                            }
                        } else if (state.is(Blocks.PUMPKIN_STEM) || state.is(Blocks.MELON_STEM)) {
                            Block fruit = state.is(Blocks.PUMPKIN_STEM) ? Blocks.PUMPKIN : Blocks.MELON;
                            Block stem = state.is(Blocks.PUMPKIN_STEM) ? Blocks.ATTACHED_PUMPKIN_STEM : Blocks.ATTACHED_MELON_STEM;
                            if (growFromStem(serverLevel, pos, state, random, fruit, stem)) {
                                handleBoneMealUsed(serverLevel, pos, player, stack, true);
                                return InteractionResult.SUCCESS;
                            }
                        } else {
                            if (dropItem(serverLevel, pos, new ItemStack(state.getBlock()))) {
                                handleBoneMealUsed(serverLevel, pos, player, stack, true);
                                return InteractionResult.SUCCESS;
                            }
                        }
                    }
                }
                if (stack.is(Items.LEAD) && EnchantmentUtil.hasEnchantment(player, stack, ModRegistry.ENDER)) {
                    if (stack.has(DataComponents.ENTITY_DATA)) {
                        TypedEntityData<EntityType<?>> entityData = stack.get(DataComponents.ENTITY_DATA);
                        if (entityData != null) {
                            EntityType<?> type = entityData.type();
                            Entity entity = type.create(serverLevel, EntitySpawnReason.EVENT);
                            if (entity != null) {
                                PLACING_MOB_FROM_LEAD.set(true);
                                entityData.loadInto(entity);
                                entity.setPos(pos.relative(face).getBottomCenter());
                                serverLevel.addFreshEntity(entity);
                                stack.remove(DataComponents.ENTITY_DATA);
                                return InteractionResult.SUCCESS;
                            }
                        }
                    }
                }
            }
            return InteractionResult.PASS;
        });
        InteractionEvent.INTERACT_ENTITY.register((player, entity, hand) -> {
            if (player.level() instanceof ServerLevel serverLevel) {
                ItemStack stack = player.getItemInHand(player.getUsedItemHand());
                if (entity instanceof Mob mob) {
                    if (stack.is(Items.LEAD) && EnchantmentUtil.hasEnchantment(player, stack, ModRegistry.ENDER)) {
                        if (!stack.has(DataComponents.ENTITY_DATA) && canLeash(mob)) {
                            TagValueOutput valueOutput = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, serverLevel.registryAccess());
                            mob.save(valueOutput);
                            stack.set(DataComponents.ENTITY_DATA, TypedEntityData.of(mob.getType(), valueOutput.buildResult()));
                            mob.remove(RemovalReason.UNLOADED_WITH_PLAYER);
                        }
                        return EventResult.interruptFalse();
                    } else if (stack.is(Items.LEAD) && Fabsservertweaks.CONFIG.overrideNormalLead) {
                        if (canLeash(mob) && !mob.isLeashed()) {
                            mob.setLeashedTo(player, true);
                        } else {
                            mob.dropLeash();
                        }
                        return EventResult.interruptFalse();
                    }
                }
            }
            return EventResult.pass();
        });
        BlockEvent.BREAK.register((level, pos, state, player, xp) -> {
            Direction breakFace = BreakContextStore.consumeFace(player, pos);
            if (level instanceof ServerLevel serverLevel) {
                ItemStack toolStack = player.getItemInHand(player.getUsedItemHand());
                if (BuiltinDatapackUtil.isEnabled(serverLevel.getServer(), BuiltinDatapack.CUSTOM_ENCHANTMENTS)) {
                    if (EnchantmentUtil.hasTreeChopper(player, toolStack)) {
                        EnchantmentUtil.performTreeChop(serverLevel, pos, player, toolStack);
                    }
                    if (EnchantmentUtil.hasHammer(player, toolStack) && !player.isShiftKeyDown()) {
                        BlockState blockState = serverLevel.getBlockState(pos);
                        int hammerLevel = EnchantmentUtil.getEnchantmentLevel(player, toolStack, ModRegistry.HAMMER);
                        float baseSpeed = blockState.getDestroySpeed(serverLevel, pos);
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
                            if (breakFace != null) {
                                aabb = inflateFromBreakAxis(aabb, breakFace.getAxis(), (hammerLevel - 1));
                            } else {
                                Direction direction = player.getDirection();
                                aabb = inflateFromBreakAxis(aabb, direction.getAxis(), (hammerLevel - 1));
                            }
                            Set<BlockPos> toBreak = BlockPos.betweenClosedStream(aabb)
                                    .filter(blockPos -> {
                                        BlockState state1 = serverLevel.getBlockState(blockPos);
                                        if (state1.isAir() || state1.hasBlockEntity() || (!toolStack.isCorrectToolForDrops(state1) && state1.requiresCorrectToolForDrops())) {
                                            return false;
                                        }
                                        float speed = state1.getDestroySpeed(serverLevel, blockPos);
                                        if (speed <= baseSpeed) {
                                            return true;
                                        }
                                        return false;
                                    })
                                    .map(BlockPos::immutable)
                                    .collect(Collectors.toSet());
                            if (toBreak.isEmpty()) {
                                return EventResult.pass();
                            }
                            WorldUtil.breakBlocks(serverLevel, pos, toBreak, player, toolStack);
                            return EventResult.interruptFalse();
                        }
                    }
                }
                if (BuiltinDatapackUtil.isEnabled(serverLevel.getServer(), BuiltinDatapack.ORE_MINER)) {
                    if (EnchantmentUtil.hasOreMiner(player, toolStack)) {
                        EnchantmentUtil.performOreMiner(serverLevel, pos, player, toolStack);
                    }
                }
                if (EnchantmentUtil.hasSilkTouch(player, toolStack) && !WorldUtil.isPlayerInstaBuild(player)) {
                    if (state.is(Blocks.SPAWNER) && serverLevel.getGameRules().get(ModGameRules.RULE_SILK_TOUCHABLE_SPAWNERS)) {
                        if (dropItemWithData(level, pos, new ItemStack(Blocks.SPAWNER))) {
                            return EventResult.interruptTrue();
                        }
                    }
                    if (state.is(Blocks.BUDDING_AMETHYST) && serverLevel.getGameRules().get(ModGameRules.RULE_SILK_TOUCHABLE_AMETHYST_NODES)) {
                        Block.popResource(level, pos, new ItemStack(Items.BUDDING_AMETHYST));
                        return EventResult.interruptTrue();
                    }
                    if (state.is(Blocks.TRIAL_SPAWNER) && serverLevel.getGameRules().get(ModGameRules.RULE_SILK_TOUCHABLE_TRIAL_SPAWNERS)) {
                        if (dropItemWithData(level, pos, new ItemStack(Blocks.TRIAL_SPAWNER))) {
                            return EventResult.interruptTrue();
                        }
                    }
                    if (state.is(Blocks.VAULT) && serverLevel.getGameRules().get(ModGameRules.RULE_SILK_TOUCHABLE_TRIAL_VAULTS)) {
                        if (dropItemWithData(level, pos, new ItemStack(Blocks.VAULT))) {
                            return EventResult.interruptTrue();
                        }
                    }
                    if (state.is(Blocks.ANCIENT_DEBRIS) && serverLevel.getGameRules().get(ModGameRules.RULE_FORTUNE_ANCIENT_DEBRIS)) {
                        if (dropItem(level, pos, new ItemStack(Items.ANCIENT_DEBRIS))) {
                            serverLevel.destroyBlock(pos, false);
                            return EventResult.interruptFalse();
                        }
                    }
                }
                if (EnchantmentUtil.hasFortune(player, toolStack) && !WorldUtil.isPlayerInstaBuild(player)) {
                    int fortuneLevel = EnchantmentUtil.getEnchantmentLevel(player, toolStack, Enchantments.FORTUNE);
                    if (state.is(Blocks.ANCIENT_DEBRIS) && serverLevel.getGameRules().get(ModGameRules.RULE_FORTUNE_ANCIENT_DEBRIS)) {
                        if (dropItem(level, pos, new ItemStack(Items.NETHERITE_SCRAP, level.random.nextInt(fortuneLevel) + 1))) {
                            serverLevel.destroyBlock(pos, false);
                            return EventResult.interruptFalse();
                        }
                    }
                }
                if (state.is(Blocks.ANCIENT_DEBRIS) && serverLevel.getGameRules().get(ModGameRules.RULE_FORTUNE_ANCIENT_DEBRIS)) {
                    if (dropItem(level, pos, new ItemStack(Items.NETHERITE_SCRAP))) {
                        serverLevel.destroyBlock(pos, false);
                        return EventResult.interruptFalse();
                    }
                }
                if (state.is(BlockTags.BEDS) && !WorldUtil.isPlayerInstaBuild(player)) {
                    if (wakeUpFromSleepingBag(level, pos, state)) {
                        return EventResult.interruptTrue();
                    }
                }
            }
            return EventResult.pass();
        });
        BlockEvent.PLACE.register((level, pos, state, placer) -> {
            if (level instanceof ServerLevel && placer instanceof Player player) {
                ItemStack stack = player.getItemInHand(player.getUsedItemHand());
                ItemStack offhandStack = player.getOffhandItem();
                if (offhandStack.getItem() instanceof BlockItem && stack.has(DataComponents.FOOD)) {
                    return EventResult.interruptFalse();
                }
                if (state.is(BlockTags.BEDS)) {
                    Component component = stack.getCustomName();
                    if (component != null) {
                        BlockPos bedOrigin = BedUtil.getBaseBedPos(pos, state);
                        BedNameStore.put(level, bedOrigin, component);
                    }
                }
                if (state.is(Blocks.SPAWNER)) {
                    TypedEntityData<BlockEntityType<?>> customData = stack.getOrDefault(DataComponents.BLOCK_ENTITY_DATA, TypedEntityData.of(BlockEntityType.MOB_SPAWNER, new CompoundTag()));
                    BlockEntityType<?> blockEntityType = customData.type();
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
        InteractionEvent.RIGHT_CLICK_ITEM.register((player, hand) -> {
            ItemStack stack = player.getItemInHand(hand);
            if (stack.is(Items.EXPERIENCE_BOTTLE)) {
                CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
                if (tag.contains("xp")) {
                    int xpPoints = tag.getInt("xp").get();
                    XPUtil.addExperiencePoints(player, xpPoints);
                    stack.shrink(1);
                    return InteractionResult.SUCCESS;
                }
            }
            return InteractionResult.PASS;
        });
        BedEvents.START_SLEEPING.register((livingEntity, pos) -> {
            if (livingEntity.level() instanceof ServerLevel serverLevel) {
                if (serverLevel.getGameRules().get(ModGameRules.RULE_SAFE_CANT_SLEEP) && !serverLevel.canSleepThroughNights()) {
                    return EventResult.interruptTrue();
                }
            }
            return EventResult.pass();
        });
        BedEvents.STOP_SLEEPING.register(livingEntity -> {
            if (livingEntity instanceof ServerPlayer player && livingEntity.level() instanceof ServerLevel serverLevel) {
                if (serverLevel.getGameRules().get(ModGameRules.RULE_SLEEPING_BAGS_ENABLED)) {
                    BlockPos pos = livingEntity.getOnPos();
                    BlockState state = serverLevel.getBlockState(pos);
                    if (state.is(BlockTags.BEDS) && !WorldUtil.isPlayerInstaBuild(player)) {
                        if (wakeUpFromSleepingBag(serverLevel, pos, state)) {
                            return EventResult.interruptTrue();
                        }
                    }
                }
            }
            return EventResult.pass();
        });
        PlayerEvent.ATTACK_ENTITY.register((player, level, target, hand, result) -> {
            if (level instanceof ServerLevel serverLevel) {
                if (!serverLevel.getGameRules().get(ModGameRules.RULE_PET_FRIENDLY_FIRE) && target instanceof TamableAnimal tamableAnimal) {
                    if (tamableAnimal.isOwnedBy(player)) {
                        switch (tamableAnimal) {
                            case Wolf wolf -> {
                                ServerUtil.sendSound(serverLevel, player, wolf, wolf.getSoundVariant().value().pantSound(), SoundSource.NEUTRAL, 1.0f, 1.0f);
                                ServerUtil.sendParticle(serverLevel, ParticleTypes.HEART, wolf, 15, 0.5, 0.3);
                            }
                            case Cat cat -> {
                                ServerUtil.sendSound(serverLevel, player, cat, SoundEvents.CAT_PURR, SoundSource.NEUTRAL, 1.0f, 1.0f);
                                ServerUtil.sendParticle(serverLevel, ParticleTypes.HEART, cat, new Vec3(0, -0.2, 0), 15, 0.5, 0.3);
                            }
                            case Parrot parrot -> {
                                ServerUtil.sendSound(serverLevel, player, parrot, parrot.getAmbientSound(), SoundSource.NEUTRAL, 1.0f, 1.0f);
                                ServerUtil.sendParticle(serverLevel, ParticleTypes.HEART, parrot, new Vec3(0, -0.3, 0), 15, 0.3, 0.3);
                            }
                            default -> {
                            }
                        }
                        return EventResult.interruptTrue();
                    }
                }
                if (player.isCreative() && target instanceof Mob && !player.getItemInHand(hand).is(ItemTags.WEAPON_ENCHANTABLE)) {
                    target.kill(serverLevel);
                }
            }
            return EventResult.pass();
        });
        ItemStackEvent.CREATED.register(stack -> {
            if (stack.is(ItemTags.BEDS)) {
                ItemStackUtil.addLore(stack, "Naming a bed \"Sleeping Bag\", let's you sleep in it without setting your spawn.");
            }
            if (stack.is(ModRegistry.IMMUNE_TO_CACTUS_DAMAGE)) {
                ItemStackUtil.addLore(stack, "Immune to cactus damage.");
            }
            if (stack.is(Items.COMPASS)) {
                if (stack.get(DataComponents.LODESTONE_TRACKER) == null) {
                    ItemStackUtil.addLore(stack, "Bind to a lodestone to be able to teleport to it using ender pearls.");
                } else {
                    ItemStackUtil.removeLore(stack, "Bind to a lodestone to be able to teleport to it using ender pearls.");
                    ItemStackUtil.addLore(stack, "Right click to teleport using ender pearls.");
                }
            }
            if (stack.is(Items.LEAD)) {
                if (stack.getEnchantments().keySet().stream().anyMatch(enchantmentHolder -> enchantmentHolder.is(ModRegistry.ENDER))) {
                    TypedEntityData<EntityType<?>> entityData = stack.get(DataComponents.ENTITY_DATA);
                    if (entityData != null) {
                        ItemStackUtil.removeLoreFuzzy(stack, "Holding Entity: ");
                        ItemStackUtil.addLore(stack, "Holding Entity: " + Component.translatable(entityData.type().getDescriptionId()).getString());
                    } else {
                        ItemStackUtil.removeLoreFuzzy(stack, "Holding Entity: ");
                        ItemStackUtil.addLore(stack, "Holding Entity: None");
                    }
                }
            }
        });
        ChatEvent.RECEIVED.register((player, component) -> {
            if (!(player instanceof ServerPlayer serverPlayer)) {
                return EventResult.pass();
            }
            if (REBROADCASTING_CHAT.get()) {
                return EventResult.pass();
            }
            String nickname = NicknameRegistry.getNickname(serverPlayer);
            if (nickname.equals(serverPlayer.getName().getString())) {
                return EventResult.pass();
            }

            try {
                REBROADCASTING_CHAT.set(true);
                Component nicknameComponent = Component.literal(nickname).withStyle(style -> style.withHoverEvent(new ShowText(serverPlayer.getName())).withColor(NicknameRegistry.getNicknameColor(serverPlayer)));
//                PlayerChatMessage unsignedMessage = PlayerChatMessage.unsigned(serverPlayer.getUUID(), component.getString()).withUnsignedContent(component);
                ChatType.Bound bound = ChatType.bind(ChatType.CHAT, serverPlayer.registryAccess(), nicknameComponent);
//                serverPlayer.level().getServer().getPlayerList().broadcastChatMessage(unsignedMessage, serverPlayer, bound);
                ClientboundDisguisedChatPacket packet = new ClientboundDisguisedChatPacket(component, bound);
                for (ServerPlayer target : serverPlayer.level().getServer().getPlayerList().getPlayers()) {
                    target.connection.send(packet);
                }

                return EventResult.interruptFalse();
            } finally {
                REBROADCASTING_CHAT.set(false);
            }
        });
    }

    private static AABB inflateFromBreakAxis(AABB aabb, Axis axis, int inflateAmount) {
        return switch (axis) {
            case X -> aabb.inflate(0, inflateAmount, inflateAmount);
            case Y -> aabb.inflate(inflateAmount, 0, inflateAmount);
            case Z -> aabb.inflate(inflateAmount, inflateAmount, 0);
        };
    }

    private static boolean wakeUpFromSleepingBag(Level level, BlockPos pos, BlockState state) {
        BlockPos bedOrigin = BedUtil.getBaseBedPos(pos, state);
        Component customName = BedNameStore.getAndRemove(level, bedOrigin);
        if (customName != null) {
            ItemStack stack = new ItemStack(state.getBlock().asItem());
            if (stack.is(ItemTags.BEDS)) {
                BedUtil.breakBed(level, pos, state);
                stack.set(DataComponents.CUSTOM_NAME, customName);
                dropItem(level, pos, stack);
            }
            return true;
        }
        return false;
    }

    private static void handleBoneMealUsed(ServerLevel serverLevel, BlockPos pos, Player player, ItemStack stack, boolean doParticle) {
        if (doParticle) {
            boneMealParticle(serverLevel, pos);
        }
        stack.shrink(1);
        player.getCooldowns().addCooldown(stack, 4);
    }

    private static void boneMealParticle(ServerLevel serverLevel, BlockPos pos) {
        RandomSource random = serverLevel.getRandom();
        double offset = 0.5;
        double posX = pos.getX() + offset;
        double posY = pos.getY() + offset;
        double posZ = pos.getZ() + offset;
        double distX = random.nextDouble() / 2.5;
        double distY = random.nextDouble() / 2.5;
        double distZ = random.nextDouble() / 2.5;
        serverLevel.sendParticles(ParticleTypes.HAPPY_VILLAGER, posX, posY, posZ, 10, distX, distY, distZ, 1);
    }

    private static boolean growFromStem(ServerLevel serverLevel, BlockPos pos, BlockState state, RandomSource random, Block fruit, Block stem) {
        if (state.getValue(StemBlock.AGE) == StemBlock.MAX_AGE) {
            if (random.nextInt(4) == 0) {
                for (int i = 0; i < 6; i++) {
                    Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection(random);
                    BlockPos blockPos2 = pos.relative(direction);
                    BlockState blockState2 = serverLevel.getBlockState(blockPos2.below());
                    if (serverLevel.getBlockState(blockPos2).isAir() && (blockState2.is(Blocks.FARMLAND) || blockState2.is(BlockTags.DIRT))) {
                        serverLevel.setBlockAndUpdate(blockPos2, fruit.defaultBlockState());
                        serverLevel.setBlockAndUpdate(pos, stem.defaultBlockState().setValue(HorizontalDirectionalBlock.FACING, direction));
                        return true;
                    }
                }
            }
            return true;
        }
        return false;
    }

    private static boolean growInColumn(ServerLevel serverLevel, BlockPos pos, Block block, int maxHeight) {
        List<BlockPos> positions = WorldUtil.getBlockPositions(new AABB(pos).inflate(0, maxHeight - 1, 0));
        int canesInColumn = 0;
        for (BlockPos blockPos : positions) {
            if (serverLevel.getBlockState(blockPos).is(block)) {
                canesInColumn++;
            }
        }
        RandomSource random = serverLevel.getRandom();
        if (canesInColumn < maxHeight) {
            BlockPos above = pos.above();
            for (int i = 1; i < maxHeight; i++) {
                BlockState newState = serverLevel.getBlockState(above);
                if (newState.is(Blocks.CACTUS_FLOWER)) {
                    return false;
                }
                if (newState.isAir()) {
                    if (block == Blocks.CACTUS && random.nextInt(10) == 0) {
                        block = Blocks.CACTUS_FLOWER;
                    }
                    if (random.nextBoolean()) {
                        serverLevel.setBlockAndUpdate(above, block.defaultBlockState());
                    }
                    boneMealParticle(serverLevel, above);
                    return true;
                }
                above = above.above();
            }
        } else if (block == Blocks.CACTUS && canesInColumn == maxHeight) {
            return tryToGrowCactusFlower(serverLevel, pos, random, maxHeight);
        }
        return false;
    }

    private static boolean tryToGrowCactusFlower(ServerLevel serverLevel, BlockPos pos, RandomSource random, int maxHeight) {
        BlockPos newPos = pos.above();
        BlockState newState = serverLevel.getBlockState(newPos);
        int tries = maxHeight + 1;
        while (!newState.isAir()) {
            if (newState.is(Blocks.CACTUS_FLOWER)) {
                return false;
            }
            newPos = newPos.above();
            newState = serverLevel.getBlockState(newPos);
            if (tries == 0) {
                break;
            }
            tries--;
        }
        if (random.nextInt(4) == 0) {
            serverLevel.setBlockAndUpdate(newPos, Blocks.CACTUS_FLOWER.defaultBlockState());
        }
        boneMealParticle(serverLevel, newPos);
        return true;
    }

    private static boolean dropItemWithData(Level level, BlockPos pos, ItemStack stack) {
        BlockEntity spawnerBlockEntity = level.getBlockEntity(pos);
        if (spawnerBlockEntity != null) {
            TagValueOutput tagValueOutput = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, level.registryAccess());
            spawnerBlockEntity.saveWithId(tagValueOutput);
            stack.set(DataComponents.BLOCK_ENTITY_DATA, TypedEntityData.of(spawnerBlockEntity.getType(), tagValueOutput.buildResult()));
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

    private static boolean canLeash(LivingEntity entity) {
        if (entity.getType().is(ModRegistry.LEAD_BLACKLIST)) {
            return false;
        }
        if (entity.getType().is(ModRegistry.PETS)) {
            return Fabsservertweaks.CONFIG.canLeashPets;
        }
        if (entity.getType().is(ModRegistry.ANIMALS)) {
            return Fabsservertweaks.CONFIG.canLeashAnimals;
        }
        if (entity.getType().is(ModRegistry.HOSTILES)) {
            return Fabsservertweaks.CONFIG.canLeashMonsters;
        }
        if (entity.getType().is(ModRegistry.BOSSES)) {
            return Fabsservertweaks.CONFIG.canLeashBosses;
        }
        if (entity.getType().is(ModRegistry.VILLAGER_TYPES)) {
            return Fabsservertweaks.CONFIG.canLeashVillagerTypes;
        }
        if (entity.getType().is(ModRegistry.GOLEMS)) {
            return Fabsservertweaks.CONFIG.canLeashGolems;
        }
        return true;
    }
}
