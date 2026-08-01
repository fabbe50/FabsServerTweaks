package com.fabbe50.fabsservertweaks.registries;

import com.fabbe50.fabsservertweaks.Fabsservertweaks;
import com.fabbe50.fabsservertweaks.LogUtil;
import com.fabbe50.fabsservertweaks.ModPlatform;
import com.fabbe50.fabsservertweaks.commands.*;
import com.fabbe50.fabsservertweaks.data.nickname.NicknameRegistry;
import com.fabbe50.fabsservertweaks.data.soulbound.SoulBoundRegistry;
import com.fabbe50.fabsservertweaks.data.stats.PlayerOnline;
import com.fabbe50.fabsservertweaks.data.stats.StatsRegistry;
import com.fabbe50.fabsservertweaks.data.storage.BedNameStore;
import com.fabbe50.fabsservertweaks.events.BedEvents;
import com.fabbe50.fabsservertweaks.events.CollisionEvent;
import com.fabbe50.fabsservertweaks.events.ExtendedBlockEvent;
import com.fabbe50.fabsservertweaks.events.ItemStackEvent;
import com.fabbe50.fabsservertweaks.network.packets.SeedPacket;
import com.fabbe50.fabsservertweaks.util.*;
import com.fabbe50.fabsservertweaks.util.EnchantmentUtil.DisenchantingEntityResult;
import com.fabbe50.fabsservertweaks.util.EnchantmentUtil.EnchantmentResult;
import com.fabbe50.fabsservertweaks.util.EventUtil.BlockEventLogic;
import com.fabbe50.fabsservertweaks.util.SpawnerUtil.Modifier;
import com.fabbe50.fabsservertweaks.util.json.JsonUtil;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.*;
import dev.architectury.event.events.common.EntityEvent;
import dev.architectury.networking.NetworkManager;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.*;
import net.minecraft.network.chat.ClickEvent.OpenUrl;
import net.minecraft.network.chat.HoverEvent.ShowText;
import net.minecraft.network.protocol.game.ClientboundDisguisedChatPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ProblemReporter;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.animal.feline.Cat;
import net.minecraft.world.entity.animal.parrot.Parrot;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.TypedEntityData;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.entity.vault.VaultBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.*;

public class EventRegistry {
    private static final ThreadLocal<Boolean> REBROADCASTING_CHAT = ThreadLocal.withInitial(() -> false);
    private static final ThreadLocal<Boolean> PLACING_MOB_FROM_LEAD = ThreadLocal.withInitial(() -> false);

    public static void init() {
        EntityEvent.ADD.register((entity, level) -> {
            LogUtil.debugEventRun(String.format("Executed by entity '%s'", entity.getName().getString()));
            if (level instanceof ServerLevel serverLevel) {
                if (PLACING_MOB_FROM_LEAD.get()) {
                    LogUtil.debug("Entity is being placed from lead. Skipping further processing...");
                    PLACING_MOB_FROM_LEAD.set(false);
                    return EventResult.pass();
                }
                if (ModGameRules.getGameRuleBoolean(serverLevel, ModGameRules.RULE_MOBS_SPAWN_WITH_EFFECTS)) {
                    if (EntityUtil.applyMobEffect(serverLevel, entity)) {
                        LogUtil.debug("Entity got effects applied.");
                    }
                }
                if (ModGameRules.getGameRuleBoolean(serverLevel, ModGameRules.RULE_CREEPERS_CAN_SPAWN_CHARGED)) {
                    if (entity instanceof Creeper creeper) {
                        int chance = Fabsservertweaks.CONFIG.creeperChargedChance;
                        if (serverLevel.getRandom().nextInt(100) + 1 < chance) {
                            creeper.getEntityData().set(Creeper.DATA_IS_POWERED, true);
                        }
                    }
                }
            }
            return EventResult.pass();
        });
        EntityEvent.LIVING_HURT.register((entity, source, amount) -> {
            if (entity.level() instanceof ServerLevel serverLevel) {
                ItemStack boots = entity.getItemBySlot(EquipmentSlot.FEET);
                if (ConditionHelper.preventsHotFloorDamage(serverLevel, entity, boots) && source.is(DamageTypes.HOT_FLOOR)) {
                    return EventResult.interruptTrue();
                }
            }
            return EventResult.pass();
        });
        EntityEvent.LIVING_DEATH.register((livingEntity, damageSource) -> {
            LogUtil.debugEventRun(String.format("Executed by entity '%s' by %s", livingEntity.getName().getString(), damageSource));
            Level level = livingEntity.level();
            if (level instanceof ServerLevel serverLevel) {
                if (EntityUtil.handleMobDeath(serverLevel, livingEntity, damageSource)) {
                    livingEntity.discard();
                    return EventResult.interruptTrue();
                }
            }
            return EventResult.pass();
        });
        CommandRegistrationEvent.EVENT.register((commandDispatcher, commandBuildContext, commandSelection) -> {
            GotoCommand.register(commandDispatcher);
            PresetCommand.register(commandDispatcher);
            NicknameCommand.register(commandDispatcher);
            ServerTweaksCommand.register(commandDispatcher, commandBuildContext);
            StatsCommand.register(commandDispatcher);
        });
        PlayerEvent.PLAYER_JOIN.register(serverPlayer -> {
            LogUtil.debugEventRun(String.format("Executed by player '%s'", serverPlayer.getName().getString()));
            LocalDateTime lastOnline = StatsRegistry.getLastOnline(serverPlayer.getUUID());
            if (lastOnline != null) {
                LogUtil.log("Player " + serverPlayer.getName().getString() + " joined the server and was last online " + StringUtil.formatLocalDateTime(lastOnline));
            } else {
                LogUtil.log("Player " + serverPlayer.getName().getString() + " joined the server and has never been online.");
            }
            if (Fabsservertweaks.CONFIG.warnPlayersAboutModNotOnClient) {
                try {
                    if (!NetworkManager.canPlayerReceive(serverPlayer, SeedPacket.PACKET_ID)) {
                        serverPlayer.sendSystemMessage(Component.literal("Fab's Server Tweaks is not installed on your client."));
                        serverPlayer.sendSystemMessage(Component.translatable("See %s for more information.", Component.literal("https://github.com/fabbe50/FabsServerTweaks").withStyle(Style.EMPTY.withUnderlined(true).withClickEvent(new OpenUrl(URI.create("https://github.com/fabbe50/FabsServerTweaks"))))));
                    }
                } catch (UnsupportedOperationException ignored) {}
            }
            if (Fabsservertweaks.CONFIG.shareSeed) {
                long seed = serverPlayer.level().getSeed();
                try {
                    NetworkManager.sendToPlayer(serverPlayer, new SeedPacket.Client.PacketPayload(seed));
                } catch (UnsupportedOperationException ignored) {
                    serverPlayer.sendSystemMessage(Component.literal("Server Seed: " + serverPlayer.level().getSeed()));
                }
            }
        });
        PlayerEvent.PLAYER_QUIT.register(serverPlayer -> {
            LogUtil.debugEventRun(String.format("Executed by player '%s'", serverPlayer.getName().getString()));
            if (serverPlayer.level() instanceof ServerLevel serverLevel) {
                StatsRegistry.setPlayerLastOnline(serverPlayer);
            }
            PlayerOnline playerStat = StatsRegistry.getPlayerOnlineStat(serverPlayer.getUUID());
            if (playerStat != null) {
                LogUtil.log("Player " + serverPlayer.getName().getString() + " left the server with data: {" + playerStat + "}");
            } else {
                LogUtil.log("Player " + serverPlayer.getName().getString() + " left the server without data.");
            }
        });
        PlayerEvent.PLAYER_RESPAWN.register((player, conqueredEnd, removalReason) -> {
            LogUtil.debugEventRun(String.format("Executed by player '%s'", player.getName().getString()));
            if (player.level() instanceof ServerLevel level) {
                LogUtil.debug("Player spawned!");
                if (BuiltinDatapackUtil.isEnabled(level.getServer(), BuiltinDatapack.CUSTOM_ENCHANTMENTS)) {
                    if (EnchantmentUtil.handleSoulBoundAfterRespawn(player)) {
                        LogUtil.debug("Soul bound items were returned to player: " + player);
                    }
                }
            }
        });
        InteractionEvent.LEFT_CLICK_BLOCK.register((player, hand, pos, face) -> {
            LogUtil.debugEventRun(String.format("Executed by player '%s' at %s using hand %s", player.getName().getString(), pos, hand));
            BreakContextStore.recordFace(player, pos, face);
            Level level = player.level();
            if (level instanceof ServerLevel serverLevel && player instanceof ServerPlayer serverPlayer) {
                BlockState state = serverLevel.getBlockState(pos);
                BlockEventLogic.leftClickBlock(serverLevel, serverPlayer, pos, state, face);
            }
            return InteractionResult.PASS;
        });
        InteractionEvent.RIGHT_CLICK_BLOCK.register((player, hand, pos, face) -> {
            LogUtil.debugEventRun(String.format("Executed by player '%s' at %s using hand %s", player.getName().getString(), pos, hand));
            if (player.level() instanceof ServerLevel serverLevel && player instanceof ServerPlayer serverPlayer) {
                ItemStack mainHandStack = player.getMainHandItem();
                ItemStack offHandStack = player.getOffhandItem();
                BlockState state = serverLevel.getBlockState(pos);
                if (ModGameRules.getGameRuleBoolean(serverLevel, ModGameRules.RULE_BETTER_BONE_MEAL) && mainHandStack.is(Items.BONE_MEAL) && !player.getCooldowns().isOnCooldown(mainHandStack)) {
                    if (state.is(ModRegistry.MOD_BONE_MEALABLE)) {
                        LogUtil.debug("Bone meal used on bone-mealable block. Attempting growth task.");
                        if (PlantUtil.boneMealPlant(serverLevel, pos, state, serverPlayer, mainHandStack)) {
                            LogUtil.debug("Operation seems successful. Passing success result.");
                            return InteractionResult.SUCCESS;
                        }
                    }
                }
                if (BuiltinDatapackUtil.isEnabled(serverLevel.getServer(), BuiltinDatapack.CUSTOM_ENCHANTMENTS)) {
                    if (mainHandStack.is(Items.LEAD) && EnchantmentUtil.hasEnchantment(player, mainHandStack, ModRegistry.ENDER)) {
                        LogUtil.debug("Lead with Ender used on block. Checking entity data on stack.");
                        if (mainHandStack.has(DataComponents.ENTITY_DATA)) {
                            TypedEntityData<EntityType<?>> entityData = mainHandStack.get(DataComponents.ENTITY_DATA);
                            if (entityData != null) {
                                LogUtil.debug("Lead has entity data. Attempting mob placement.");
                                EntityType<?> type = entityData.type();
                                Entity entity = type.create(serverLevel, EntitySpawnReason.EVENT);
                                if (entity != null) {
                                    LogUtil.debug("Entity on lead is present and valid. Placing entity on block...");
                                    PLACING_MOB_FROM_LEAD.set(true);
                                    entityData.loadInto(entity);
                                    entity.setPos(pos.relative(face).getBottomCenter());
                                    serverLevel.addFreshEntity(entity);
                                    mainHandStack.remove(DataComponents.ENTITY_DATA);
                                    LogUtil.debug("Operation seems successful. Passing success result.");
                                    return InteractionResult.SUCCESS;
                                }
                            }
                        } else {
                            LogUtil.debug("Lead does not have entity data. Operation failed.");
                        }
                    }
                }
                if (ModGameRules.getGameRuleBoolean(serverLevel, ModGameRules.RULE_ENCHANTMENT_TRANSFER_TO_BOOKS) && mainHandStack.is(Items.BOOK) && state.is(Blocks.STONECUTTER)) {
                    LogUtil.debug("Book used on stonecutter. Checking items on top...");
                    AABB aabb = new AABB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 2, pos.getZ() + 1);
                    List<ItemEntity> entities = serverLevel.getEntitiesOfClass(ItemEntity.class, aabb);
                    LogUtil.debug("Item entities found: " + entities + ". Checking if any of them are enchanted...");
                    for (ItemEntity entity : entities) {
                        if (!mainHandStack.isEnchanted()) {
                            DisenchantingEntityResult result = EnchantmentUtil.disenchantEntity(entity, 1);
                            if (result.itemEntity() != null) {
                                boolean success = false;
                                for (int i = 0; i < result.enchantments().size(); i++) {
                                    EnchantmentResult enchantmentResult = result.enchantments().get(i);
                                    ItemStack book = EnchantmentHelper.createBook(new EnchantmentInstance(enchantmentResult.enchantmentHolder(), enchantmentResult.level()));
                                    mainHandStack.shrink(1);
                                    player.addItem(book);
                                    success = true;
                                }
                                serverLevel.addFreshEntity(result.itemEntity());
                                player.getCooldowns().addCooldown(mainHandStack, 10);
                                if (success) {
                                    ParticleUtil.spawnParticleExplodeUpperSphere(serverLevel, entity.position().add(new Vec3(0, 0.5, 0)), ParticleTypes.REVERSE_PORTAL, 100, 0.1, 0.7d);
                                    serverLevel.playSound(null, pos, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS, 1, 1);
                                    serverLevel.playSound(null, pos, SoundEvents.FIREWORK_ROCKET_BLAST, SoundSource.BLOCKS, 0.5f, 0.5f);
                                    LogUtil.debug("Operation seems successful. Passing success result.");
                                    return InteractionResult.SUCCESS;
                                } else {
                                    serverLevel.playSound(null, pos, SoundEvents.VAULT_INSERT_ITEM_FAIL, SoundSource.BLOCKS, 1, 1);
                                    LogUtil.debug("Operation failed. No enchanted items found.");
                                    return InteractionResult.FAIL;
                                }
                            }
                        }
                    }
                    LogUtil.debug("No enchanted items found. Operation failed.");
                    return InteractionResult.FAIL;
                }
                if (ModGameRules.getGameRuleBoolean(serverLevel, ModGameRules.RULE_UNLOCKABLE_VAULTS) && mainHandStack.is(ModRegistry.VAULT_KEY) && state.is(Blocks.VAULT)) {
                    LogUtil.debug("Vault key used on vault. Checking if vault is locked for player...");
                    BlockEntity blockEntity = serverLevel.getBlockEntity(pos);
                    if (blockEntity instanceof VaultBlockEntity vaultBlockEntity) {
                        if (WorldUtil.unlockVault(serverLevel, pos, player, mainHandStack, vaultBlockEntity)) {
                            LogUtil.debug("Vault is unlocked for player. Passing success result.");
                            return InteractionResult.SUCCESS;
                        }
                    }
                }
                if (ModGameRules.getGameRuleBoolean(serverLevel, ModGameRules.RULE_MODIFY_SPAWNERS) && state.is(Blocks.SPAWNER)) {
                    LogUtil.debug("Block is spawner. Attempting modification operation on spawner...");
                    BlockEntity blockEntity = serverLevel.getBlockEntity(pos);
                    if (blockEntity instanceof SpawnerBlockEntity spawnerBE) {
                        BaseSpawner spawner = spawnerBE.getSpawner();
                        if (mainHandStack.isEmpty()) {
                            LogUtil.debug("Stack is empty. Attempting to show spawner information...");
                            int minSpawnDelay = spawner.minSpawnDelay;
                            int maxSpawnDelay = spawner.maxSpawnDelay;
                            int spawnCount = spawner.spawnCount;
                            int maxNearbyEntities = spawner.maxNearbyEntities;
                            int requiredPlayerRange = spawner.requiredPlayerRange;
                            int spawnRange = spawner.spawnRange;
                            String requiredPlayerRangeString = requiredPlayerRange == -1 ? "Not required" : String.valueOf(requiredPlayerRange);
                            player.sendOverlayMessage(Component.literal("Spawner: minSpawnDelay=" + minSpawnDelay + ", maxSpawnDelay=" + maxSpawnDelay + ", spawnCount=" + spawnCount + ", maxNearbyEntities=" + maxNearbyEntities + ", requiredPlayerRange=" + requiredPlayerRangeString + ", spawnRange=" + spawnRange));
                            LogUtil.debug("Operation seems successful. Passing success result.");
                            return InteractionResult.SUCCESS;
                        } else if (Modifier.isItemValid(mainHandStack.getItem())) {
                            LogUtil.debug("Stack is valid modification item. Attempting modification operation on spawner...");
                            boolean offHandStackIsQuartz = offHandStack.is(Items.QUARTZ);
                            if (SpawnerUtil.tryChangeSpawner(player, spawner, mainHandStack.getItem(), offHandStackIsQuartz)) {
                                ParticleUtil.spawnParticlesOnBlockFaces(serverLevel, pos, ParticleTypes.HAPPY_VILLAGER, UniformInt.of(2, 5));
                                ItemStackUtil.shrink(mainHandStack, player);
                                if (offHandStackIsQuartz) {
                                    ItemStackUtil.shrink(offHandStack, player);
                                }
                                LogUtil.debug("Operation seems successful. Passing success result.");
                                return InteractionResult.SUCCESS;
                            }
                            LogUtil.debug("Operation failed. This is possibly due to the modifier being at it's min/max value or that the change didn't complete. Passing fail result.");
                            return InteractionResult.FAIL;
                        }
                    } else {
                        LogUtil.debug("Block entity is not a spawner. Operation failed.");
                    }
                }
                if (ModGameRules.getGameRuleBoolean(serverLevel, ModGameRules.RULE_REPAIRABLE_ANVILS) && mainHandStack.is(Items.IRON_BLOCK) && state.is(BlockTags.ANVIL)) {
                    if (state.is(Blocks.DAMAGED_ANVIL)) {
                        serverLevel.setBlockAndUpdate(pos, Blocks.CHIPPED_ANVIL.defaultBlockState().setValue(AnvilBlock.FACING, state.getValue(AnvilBlock.FACING)));
                        serverLevel.playSound(null, pos, SoundEvents.ANVIL_USE, SoundSource.BLOCKS, 1, 1);
                        ItemStackUtil.shrink(mainHandStack, player);
                        return InteractionResult.SUCCESS;
                    }
                    if (state.is(Blocks.CHIPPED_ANVIL)) {
                        serverLevel.setBlockAndUpdate(pos, Blocks.ANVIL.defaultBlockState().setValue(AnvilBlock.FACING, state.getValue(AnvilBlock.FACING)));
                        serverLevel.playSound(null, pos, SoundEvents.ANVIL_USE, SoundSource.BLOCKS, 1, 1);
                        ItemStackUtil.shrink(mainHandStack, player);
                        return InteractionResult.SUCCESS;
                    }
                }
            }
            if (ModPlatform.rightClickBlockEvent(player, hand, pos, face)) {
                return InteractionResult.SUCCESS_SERVER;
            }
            return InteractionResult.PASS;
        });
        InteractionEvent.INTERACT_ENTITY.register((player, entity, hand) -> {
            LogUtil.debugEventRun(String.format("Executed by player '%s' on entity %s using hand %s", player.getName().getString(), entity, hand));
            if (player.level() instanceof ServerLevel serverLevel) {
                ItemStack mainHandStack = player.getMainHandItem();
                if (entity instanceof Mob mob) {
                    if (BuiltinDatapackUtil.isEnabled(serverLevel.getServer(), BuiltinDatapack.CUSTOM_ENCHANTMENTS) && mainHandStack.is(Items.LEAD) && EnchantmentUtil.hasEnchantment(player, mainHandStack, ModRegistry.ENDER)) {
                        if (!mainHandStack.has(DataComponents.ENTITY_DATA) && EntityUtil.canLeash(mob)) {
                            TagValueOutput valueOutput = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, serverLevel.registryAccess());
                            mob.save(valueOutput);
                            mainHandStack.set(DataComponents.ENTITY_DATA, TypedEntityData.of(mob.getType(), valueOutput.buildResult()));
                            mob.remove(RemovalReason.UNLOADED_WITH_PLAYER);
                            return EventResult.interruptFalse();
                        }
                    } else if (mainHandStack.is(Items.LEAD) && Fabsservertweaks.CONFIG.overrideNormalLead) {
                        if (EntityUtil.canLeash(mob) && !mob.isLeashed()) {
                            mob.setLeashedTo(player, true);
                        } else {
                            mob.dropLeash();
                        }
                        return EventResult.pass();
                    }
                }
            }
            return EventResult.pass();
        });
        BlockEvent.BREAK.register((level, pos, state, player, xp) -> {
            LogUtil.debugEventRun(String.format("Executed by player '%s' at %s on block %s using hand %s", player.getName().getString(), pos, state, player.getUsedItemHand()));
            Direction breakFace = BreakContextStore.consumeFace(player, pos);
            if (level instanceof ServerLevel serverLevel) {
                ItemStack toolStack = player.getMainHandItem();
                if (BuiltinDatapackUtil.isEnabled(serverLevel.getServer(), BuiltinDatapack.ORE_MINER)) {
                    if (EnchantmentUtil.hasOreMiner(player, toolStack)) {
                        LogUtil.debug("Ore Miner used on block. Attempting to perform ore miner operation.");
                        if (EnchantmentUtil.performOreMiner(serverLevel, pos, player, toolStack)) {
                            LogUtil.debug("Ore Miner operation performed successfully.");
                            return EventResult.interruptFalse();
                        }
                    }
                }
                if (BuiltinDatapackUtil.isEnabled(serverLevel.getServer(), BuiltinDatapack.CUSTOM_ENCHANTMENTS)) {
                    if (EnchantmentUtil.hasTreeChopper(player, toolStack)) {
                        LogUtil.debug("Tree Chopper used on block. Attempting to perform tree chopper operation...");
                        if (EnchantmentUtil.performTreeChop(serverLevel, pos, player, toolStack)) {
                            LogUtil.debug("Tree Chopper operation performed successfully.");
                            return EventResult.interruptFalse();
                        }
                    }
                    if (EnchantmentUtil.hasHammer(player, toolStack) && !player.isShiftKeyDown()) {
                        LogUtil.debug("Hammer used on block. Attempting to perform hammer operation...");
                        if (EnchantmentUtil.performHammer(serverLevel, pos, breakFace, player, toolStack)) {
                            return EventResult.interruptFalse();
                        }
                    }
                    if (EnchantmentUtil.hasIceTouch(player, toolStack)) {
                        LogUtil.debug("Ice Touch used on block. Attempting to perform ice touch operation...");
                        if (EnchantmentUtil.performIceTouch(serverLevel, pos, player, toolStack)) {
                            LogUtil.debug("Ice Touch operation performed successfully.");
                            return EventResult.pass();
                        }
                    }
                }
                if (EnchantmentUtil.hasSilkTouch(player, toolStack) && !WorldUtil.isPlayerInstaBuild(player)) {
                    if (ModGameRules.getGameRuleBoolean(serverLevel, ModGameRules.RULE_SILK_TOUCHABLE_SPAWNERS) && state.is(Blocks.SPAWNER)) {
                        LogUtil.debug("Detected silk touch used on spawner.");
                        if (WorldUtil.dropItemWithData(level, pos, new ItemStack(Blocks.SPAWNER))) {
                            LogUtil.debug("Dropped spawner with data.");
                            return EventResult.interruptTrue();
                        }
                    }
                    if (ModGameRules.getGameRuleBoolean(serverLevel, ModGameRules.RULE_SILK_TOUCHABLE_AMETHYST_NODES) && state.is(Blocks.BUDDING_AMETHYST)) {
                        LogUtil.debug("Detected silk touch used on amethyst node. Dropping item.");
                        if (WorldUtil.dropItem(level, pos, new ItemStack(Items.BUDDING_AMETHYST))) {
                            LogUtil.debug("Dropped budding amethyst.");
                            return EventResult.interruptTrue();
                        }
                    }
                    if (ModGameRules.getGameRuleBoolean(serverLevel, ModGameRules.RULE_SILK_TOUCHABLE_TRIAL_SPAWNERS) && state.is(Blocks.TRIAL_SPAWNER)) {
                        LogUtil.debug("Detected silk touch used on trial spawner.");
                        if (WorldUtil.dropItemWithData(level, pos, new ItemStack(Blocks.TRIAL_SPAWNER))) {
                            LogUtil.debug("Dropped trial spawner with data.");
                            return EventResult.interruptTrue();
                        }
                    }
                    if (ModGameRules.getGameRuleBoolean(serverLevel, ModGameRules.RULE_SILK_TOUCHABLE_TRIAL_VAULTS) && state.is(Blocks.VAULT)) {
                        LogUtil.debug("Detected silk touch used on vault.");
                        if (WorldUtil.dropItemWithData(level, pos, new ItemStack(Blocks.VAULT))) {
                            LogUtil.debug("Dropped vault with data.");
                            return EventResult.interruptTrue();
                        }
                    }
                }
                if (WorldUtil.handleSpecialBreakingConditions(serverLevel, pos, state, player, toolStack)) {
                    LogUtil.debug("Handled special breaking conditions.");
                    return EventResult.interruptFalse();
                }
            }
            if (ModPlatform.breakBlockEvent(level, pos, state, player)) {
                return EventResult.interruptFalse();
            }
            return EventResult.pass();
        });
        BlockEvent.PLACE.register((level, pos, state, placer) -> {
            if (level instanceof ServerLevel serverLevel && placer instanceof Player player) {
                LogUtil.debugEventRun(String.format("Executed by player '%s' at %s with block %s using hand %s", player.getName().getString(), pos, state, player.getUsedItemHand()));
                ItemStack mainHandStack = player.getMainHandItem();
                ItemStack offhandStack = player.getOffhandItem();
                if (offhandStack.getItem() instanceof BlockItem && mainHandStack.has(DataComponents.FOOD)) {
                    LogUtil.debug("Blocking off-hand block placement due to food item in main hand.");
                    return EventResult.interruptFalse();
                }
                if (ModGameRules.getGameRuleBoolean(serverLevel, ModGameRules.RULE_SLEEPING_BAGS_ENABLED) && state.is(BlockTags.BEDS)) {
                    LogUtil.debug("Bed placement detected. Checking for sleeping bag.");
                    Component component = mainHandStack.getCustomName();
                    if (component != null) {
                        LogUtil.debug("Bed is sleeping bag. Saving location.");
                        BlockPos bedOrigin = BedUtil.getBaseBedPos(pos, state);
                        BedNameStore.put(level, bedOrigin, component);
                    } else {
                        LogUtil.debug("Bed is not sleeping bag. Ignoring.");
                    }
                }
                if (state.is(Blocks.SPAWNER)) {
                    if (SpawnerUtil.placeSpawnerWithData(serverLevel, pos, state, mainHandStack)) {
                        LogUtil.debug("Spawner placed with data.");
                        return EventResult.interruptTrue();
                    }
                }
                if (state.is(Blocks.TRIAL_SPAWNER)) {
                    if (WorldUtil.placeBlockWithData(serverLevel, pos, state, mainHandStack, BlockEntityType.TRIAL_SPAWNER)) {
                        LogUtil.debug("Trial Spawner placed with data.");
                        return EventResult.interruptFalse();
                    }
                }
                if (state.is(Blocks.VAULT)) {
                    if (WorldUtil.placeBlockWithData(serverLevel, pos, state, mainHandStack, BlockEntityType.VAULT)) {
                        LogUtil.debug("Vault placed with data.");
                        return EventResult.interruptFalse();
                    }
                }
            }
            if (ModPlatform.placeBlockEvent(level, pos, state, placer)) {
                return EventResult.interruptFalse();
            }
            return EventResult.pass();
        });
        ExtendedBlockEvent.BLOCK_UPDATE.register((level, pos, state) -> {
            LogUtil.debugEventRun("Executed at " + pos + " with state " + state);
            if (level instanceof ServerLevel serverLevel && state != null) {
                LogUtil.debug("Block update detected at " + pos + " with state " + state);
                if (state.is(Blocks.LAVA_CAULDRON)) {
                    BlockState below = serverLevel.getBlockState(pos.below());
                    if (below.is(Blocks.BLUE_ICE)) {
                        LogUtil.debug("Cauldron is on top of blue ice. Attempting to turn back into cauldron and dropping obsidian...");
                        if (level.setBlockAndUpdate(pos, Blocks.CAULDRON.defaultBlockState())) {
                            LogUtil.debug("Lava cooled to obsidian. Dropping item.");
                            WorldUtil.dropItem(level, pos.above(), new ItemStack(Blocks.OBSIDIAN));
                            return EventResult.interruptTrue();
                        } else {
                            LogUtil.debug("Failed to turn back into cauldron. Ignoring...");
                        }
                    }
                }
            }
            return EventResult.pass();
        });
        InteractionEvent.RIGHT_CLICK_ITEM.register((player, hand) -> {
            ItemStack mainHandStack = player.getMainHandItem();
            LogUtil.debugEventRun(String.format("Executed by player '%s' with stack %s using hand %s", player.getName().getString(), mainHandStack, hand));
            if (mainHandStack.is(Items.EXPERIENCE_BOTTLE)) {
                CompoundTag tag = mainHandStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
                if (tag.contains("xp")) {
                    int xpPoints = tag.getInt("xp").orElse(0);
                    XPUtil.addExperiencePoints(player, xpPoints);
                    mainHandStack.shrink(1);
                    return InteractionResult.SUCCESS;
                }
            }
            if (player instanceof ServerPlayer serverPlayer) {
                if (mainHandStack.is(Items.RECOVERY_COMPASS)) {
                    Optional<GlobalPos> optionalDeathPoint = player.getLastDeathLocation();
                    if (optionalDeathPoint.isPresent()) {
                        GlobalPos deathPoint = optionalDeathPoint.get();
                        if (ItemStackUtil.takeItemFromPlayerInventory(player, Items.ENDER_PEARL, true, true)) {
                            EntityUtil.teleportPlayer(serverPlayer.level().getServer().getLevel(deathPoint.dimension()), serverPlayer, deathPoint.pos(), mainHandStack);
                            return InteractionResult.SUCCESS;
                        } else {
                            serverPlayer.sendSystemMessage(Component.literal("Missing ender pearl.").withStyle(ChatFormatting.RED), true);
                        }
                    } else {
                        serverPlayer.sendSystemMessage(Component.literal("No valid teleport location.").withStyle(ChatFormatting.RED), true);
                    }
                }
            }
            return InteractionResult.PASS;
        });
        BedEvents.START_SLEEPING.register((livingEntity, pos) -> {
            LogUtil.debugEventRun(String.format("Executed by entity '%s' at %s", livingEntity.getName().getString(), pos));
            if (livingEntity.level() instanceof ServerLevel serverLevel) {
                if (ModGameRules.getGameRuleBoolean(serverLevel, ModGameRules.RULE_SAFE_CANT_SLEEP) && !serverLevel.canSleepThroughNights()) {
                    LogUtil.debug("Player is not allowed to sleep. Preventing sleep to avoid explosion.");
                    return EventResult.interruptTrue();
                }
            }
            return EventResult.pass();
        });
        BedEvents.STOP_SLEEPING.register(livingEntity -> {
            LogUtil.debugEventRun(String.format("Executed by entity '%s'", livingEntity.getName().getString()));
            if (livingEntity instanceof ServerPlayer player && livingEntity.level() instanceof ServerLevel serverLevel) {
                if (ModGameRules.getGameRuleBoolean(serverLevel, ModGameRules.RULE_SLEEPING_BAGS_ENABLED)) {
                    BlockPos pos = livingEntity.getOnPos();
                    BlockState state = serverLevel.getBlockState(pos);
                    if (state.is(BlockTags.BEDS) && !WorldUtil.isPlayerInstaBuild(player)) {
                        LogUtil.debug("Bed is sleeping bag. Attempting wake-up from sleeping bag operation...");
                        if (BedUtil.wakeUpFromSleepingBag(serverLevel, pos, state)) {
                            LogUtil.debug("Wake-up from sleeping bag operation performed successfully.");
                            return EventResult.interruptTrue();
                        }
                    }
                }
            }
            return EventResult.pass();
        });
        PlayerEvent.ATTACK_ENTITY.register((player, level, target, hand, result) -> {
            LogUtil.debugEventRun(String.format("Executed by player '%s' on entity %s using hand %s", player.getName().getString(), target, hand));
            if (level instanceof ServerLevel serverLevel) {
                if (!ModGameRules.getGameRuleBoolean(serverLevel, ModGameRules.RULE_PET_FRIENDLY_FIRE) && target instanceof TamableAnimal tamableAnimal) {
                    if (tamableAnimal.isOwnedBy(player)) {
                        LogUtil.debug("Tamable animal is owned by player. Preventing damage...");
                        switch (tamableAnimal) {
                            case Wolf wolf -> {
                                if (wolf.isBaby()) {
                                    ServerUtil.sendSound(serverLevel, player, wolf, wolf.getSoundVariant().value().babySounds().pantSound(), SoundSource.NEUTRAL, 1.0f, 1.0f);
                                } else {
                                    ServerUtil.sendSound(serverLevel, player, wolf, wolf.getSoundVariant().value().adultSounds().pantSound(), SoundSource.NEUTRAL, 1.0f, 1.0f);
                                }
                                ServerUtil.sendParticle(serverLevel, ParticleTypes.HEART, wolf, 15, 0.5, 0.3);
                            }
                            case Cat cat -> {
                                ServerUtil.sendSound(serverLevel, player, cat, cat.getSoundSet().purrSound(), SoundSource.NEUTRAL, 1.0f, 1.0f);
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
                if (player.isCreative() && target instanceof Mob && !player.getMainHandItem().is(ItemTags.WEAPON_ENCHANTABLE)) {
                    target.kill(serverLevel);
                }
            }
            return EventResult.pass();
        });
        ItemStackEvent.CREATED.register(stack -> {
            LogUtil.debugItemStackCreation(String.format("Executed with stack %s", stack), true);
            int repairCost = stack.getOrDefault(DataComponents.REPAIR_COST, 0);
            if (repairCost > Fabsservertweaks.CONFIG.maxAnvilCost) {
                stack.set(DataComponents.REPAIR_COST, Fabsservertweaks.CONFIG.maxAnvilCost);
            }

            if (LoreRegistry.updateLore(stack)) {
                LogUtil.debug("Updated lore for stack: " + stack);
            }

            if (ItemNameRegistry.setItemName(stack)) {
                LogUtil.debug("Updated item name for stack: " + stack);
            }
        });
        ChatEvent.RECEIVED.register((player, component) -> {
            if (player == null) {
                return EventResult.pass();
            }
            LogUtil.debugEventRun(String.format("Executed by player '%s' with component: %s", player.getName().getString(), component.getString()));
            //noinspection ConstantValue
            if (!(player instanceof ServerPlayer serverPlayer)) {
                LogUtil.debug("Player is not a server player. Skipping...");
                return EventResult.pass();
            }
            if (REBROADCASTING_CHAT.get()) {
                LogUtil.debug("The message is being rebroadcasted. Skipping...");
                return EventResult.pass();
            }
            String nickname = NicknameRegistry.getNickname(serverPlayer);
            if (nickname.equals(serverPlayer.getName().getString())) {
                LogUtil.debug("Player isn't using a nickname. Skipping...");
                return EventResult.pass();
            }

            try {
                LogUtil.debug("Player is using a nickname. Broadcasting message...");
                REBROADCASTING_CHAT.set(true);
                Component nicknameComponent = Component.literal(nickname).withStyle(style -> style.withHoverEvent(new ShowText(serverPlayer.getName())).withColor(NicknameRegistry.getNicknameColor(serverPlayer)));
//                PlayerChatMessage unsignedMessage = PlayerChatMessage.unsigned(serverPlayer.getUUID(), component.getString()).withUnsignedContent(component);
                ChatType.Bound bound = ChatType.bind(ChatType.CHAT, serverPlayer.registryAccess(), nicknameComponent);
//                serverPlayer.level().getServer().getPlayerList().broadcastChatMessage(unsignedMessage, serverPlayer, bound);
                ClientboundDisguisedChatPacket packet = new ClientboundDisguisedChatPacket(component, bound);
                for (ServerPlayer target : serverPlayer.level().getServer().getPlayerList().getPlayers()) {
                    target.connection.send(packet);
                }
                LogUtil.debug("Message broadcasted successfully.");
                return EventResult.interruptFalse();
            } finally {
                REBROADCASTING_CHAT.set(false);
                LogUtil.debug("Failed to rebroadcast message. Resetting state and skipping...");
            }
        });
        CollisionEvent.COLLISION_EVENT.register((level, pos, state, entity) -> {
            LogUtil.debugEventRun(String.format("Executed by entity '%s' at %s with state %s", entity.getName().getString(), pos, state));
            if (level instanceof ServerLevel serverLevel) {
                if (ModGameRules.getGameRuleBoolean(serverLevel, ModGameRules.RULE_AMETHYST_DOES_DAMAGE)) {
                    if (entity instanceof LivingEntity livingEntity) {
                        float health = livingEntity.getHealth();
                        if (state.is(Blocks.SMALL_AMETHYST_BUD)) {
                            if (health > 1) {
                                LogUtil.debug("Amethyst bud is damaging entity. Damage dealt: 1");
                                entity.hurtServer(serverLevel, level.damageSources().cactus(), 1);
                            }
                        }
                        if (state.is(Blocks.MEDIUM_AMETHYST_BUD)) {
                            LogUtil.debug("Amethyst bud is damaging entity. Damage dealt: 2");
                            entity.hurtServer(serverLevel, level.damageSources().cactus(), 2);
                        }
                        if (state.is(Blocks.LARGE_AMETHYST_BUD)) {
                            LogUtil.debug("Amethyst bud is damaging entity. Damage dealt: 3");
                            entity.hurtServer(serverLevel, level.damageSources().cactus(), 3);
                        }
                        if (state.is(Blocks.AMETHYST_CLUSTER)) {
                            Player player = serverLevel.getNearestPlayer(entity, 16);
                            if (player == null) {
                                player = serverLevel.getRandomPlayer();
                            }
                            if (player != null) {
                                LogUtil.debug("Amethyst cluster is performing player damage. Used player=\"" + player.getName().getString() + "\" Damage dealt: 4");
                                entity.hurtServer(serverLevel, level.damageSources().playerAttack(player), 4);
                            } else {
                                LogUtil.debug("Amethyst cluster is performing normal damage. Damage dealt: 4");
                                entity.hurtServer(serverLevel, level.damageSources().cactus(), 4);
                            }
                        }
                    }
                }
            }
            return EventResult.pass();
        });
        LifecycleEvent.SERVER_STARTING.register(instance -> {
            JsonUtil.init(instance.registryAccess());

            NicknameRegistry.loadNicknames();
            StatsRegistry.loadStats();
            SoulBoundRegistry.loadSoulBoundData();
        });
        PlayerEvent.FILL_BUCKET.register((player, level, stack, target) -> {
            if (EnchantmentUtil.hasEnchantment(player, stack, Enchantments.INFINITY)) {
                if (target instanceof BlockHitResult blockHitResult) {
                    BlockPos pos = blockHitResult.getBlockPos();
                    Direction direction = blockHitResult.getDirection();
                    BlockState state = level.getBlockState(pos);
                    FluidState fluidState = state.getFluidState();
                    if (stack.getItem() instanceof BucketItem bucketItem) {
                        Fluid fluid = bucketItem.getContent();
                        if (state.is(Blocks.CAULDRON) && (fluid.equals(Fluids.WATER) || fluid.equals(Fluids.LAVA))) {
                            SoundEvent empty = SoundEvents.BUCKET_EMPTY;
                            if (fluid == Fluids.WATER) {
                                level.setBlockAndUpdate(pos, Blocks.WATER_CAULDRON.defaultBlockState());
                            }
                            if (fluid == Fluids.LAVA) {
                                level.setBlockAndUpdate(pos, Blocks.LAVA_CAULDRON.defaultBlockState());
                                empty = SoundEvents.BUCKET_EMPTY_LAVA;
                            }
                            level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), empty, SoundSource.BLOCKS, 1, 1);
                            return InteractionResult.SUCCESS_SERVER;
                        } else if (state.is(Blocks.WATER_CAULDRON) && fluid.equals(Fluids.EMPTY)) {
                            level.setBlockAndUpdate(pos, Blocks.CAULDRON.defaultBlockState());
                            level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1, 1);
                            return InteractionResult.SUCCESS_SERVER;
                        } else if (state.is(Blocks.LAVA_CAULDRON) && fluid.equals(Fluids.EMPTY)) {
                            level.setBlockAndUpdate(pos, Blocks.CAULDRON.defaultBlockState());
                            level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BUCKET_FILL_LAVA, SoundSource.BLOCKS, 1, 1);
                            return InteractionResult.SUCCESS_SERVER;
                        }
                        if (fluid == Fluids.EMPTY) {
                            if (!fluidState.isEmpty()) {
                                level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                                SoundEvent fill = SoundEvents.BUCKET_FILL;
                                if (fluidState.getType().equals(Fluids.LAVA)) {
                                    fill = SoundEvents.BUCKET_FILL_LAVA;
                                }
                                level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), fill, SoundSource.BLOCKS, 1, 1);
                                return InteractionResult.SUCCESS_SERVER;
                            } else if (state.getBlock() instanceof SimpleWaterloggedBlock simpleWaterloggedBlock) {
                                simpleWaterloggedBlock.pickupBlock(player, level, pos, state);
                                return InteractionResult.SUCCESS_SERVER;
                            }
                        } else {
                            if (!fluidState.isEmpty() && !fluidState.is(fluid)) {
                                level.setBlockAndUpdate(pos, fluid.defaultFluidState().createLegacyBlock());
                                SoundEvent empty = SoundEvents.BUCKET_EMPTY;
                                if (fluidState.getType().equals(Fluids.LAVA)) {
                                    empty = SoundEvents.BUCKET_EMPTY_LAVA;
                                }
                                level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), empty, SoundSource.BLOCKS, 1, 1);
                                return InteractionResult.SUCCESS_SERVER;
                            } else if (state.getBlock() instanceof SimpleWaterloggedBlock simpleWaterloggedBlock) {
                                if (simpleWaterloggedBlock.canPlaceLiquid(player, level, pos, state, fluid)) {
                                    simpleWaterloggedBlock.placeLiquid(level, pos, state, fluid.defaultFluidState());
                                    return InteractionResult.SUCCESS_SERVER;
                                } else {
                                    level.setBlockAndUpdate(pos.relative(direction), fluid.defaultFluidState().createLegacyBlock());
                                    SoundEvent empty = SoundEvents.BUCKET_EMPTY;
                                    if (fluidState.getType().equals(Fluids.LAVA)) {
                                        empty = SoundEvents.BUCKET_EMPTY_LAVA;
                                    }
                                    level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), empty, SoundSource.BLOCKS, 1, 1);
                                    return InteractionResult.SUCCESS_SERVER;
                                }
                            } else {
                                level.setBlockAndUpdate(pos.relative(direction), fluid.defaultFluidState().createLegacyBlock());
                                SoundEvent empty = SoundEvents.BUCKET_EMPTY;
                                if (fluidState.getType().equals(Fluids.LAVA)) {
                                    empty = SoundEvents.BUCKET_EMPTY_LAVA;
                                }
                                level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), empty, SoundSource.BLOCKS, 1, 1);
                                return InteractionResult.SUCCESS_SERVER;
                            }
                        }
                    }
                }
            }
            return InteractionResult.PASS;
        });
    }
}
