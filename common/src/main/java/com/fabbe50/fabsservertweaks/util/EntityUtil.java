package com.fabbe50.fabsservertweaks.util;

import com.fabbe50.fabsservertweaks.Fabsservertweaks;
import com.fabbe50.fabsservertweaks.LogUtil;
import com.fabbe50.fabsservertweaks.data.MobLoot;
import com.fabbe50.fabsservertweaks.registries.ModGameRules;
import com.fabbe50.fabsservertweaks.registries.ModRegistry;
import com.fabbe50.fabsservertweaks.registries.gamerules.DifficultyValue;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class EntityUtil {
    public static boolean applyMobEffect(ServerLevel level, Entity entity) {
        if (entity instanceof Monster monster) {
            if (entity.is(ModRegistry.MOBS_WITH_POTION_EFFECTS_BLACKLIST)) {
                return false;
            }
            RandomSource random = entity.getRandom();
            DifficultyValue.Difficulty difficulty = Fabsservertweaks.CONFIG.difficulty;
            if (shouldApplyEffect(level, random, difficulty)) {
                if (level.dimension().equals(Level.NETHER)) {
                    if (random.nextDouble() < 0.75d) {
                        monster.addEffect(EffectUtil.getRandomNetherEffect(random));
                        return true;
                    }
                } else if (level.dimension().equals(Level.END)) {
                    if (random.nextDouble() < 0.25d) {
                        monster.addEffect(EffectUtil.getRandomEndEffect(random));
                        return true;
                    }
                } else {
                    if (ChanceUtil.rollAtY(level, monster.getBlockY(), 0.01, 0.75, 5.25, random)) {
                        monster.addEffect(EffectUtil.getRandomOverworldEffect(random));
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean shouldApplyEffect(ServerLevel level, RandomSource random, DifficultyValue.Difficulty difficulty) {
        if (difficulty.equals(DifficultyValue.Difficulty.SCALE_BY_DIFFICULTY)) {
            return switch (level.getDifficulty()) {
                case PEACEFUL, EASY -> false;
                case NORMAL -> random.nextBoolean();
                case HARD -> true;
            };
        } else return difficulty.equals(DifficultyValue.Difficulty.SAME_ON_ALL_DIFFICULTIES);
    }

    public static boolean canLeash(LivingEntity entity) {
        if (entity.is(ModRegistry.LEAD_BLACKLIST)) {
            return false;
        }
        if (entity.is(ModRegistry.PETS)) {
            return Fabsservertweaks.CONFIG.canLeashPets;
        }
        if (entity.is(ModRegistry.ANIMALS)) {
            return Fabsservertweaks.CONFIG.canLeashAnimals;
        }
        if (entity.is(ModRegistry.HOSTILES)) {
            return Fabsservertweaks.CONFIG.canLeashMonsters;
        }
        if (entity.is(ModRegistry.BOSSES)) {
            return Fabsservertweaks.CONFIG.canLeashBosses;
        }
        if (entity.is(ModRegistry.VILLAGER_TYPES)) {
            return Fabsservertweaks.CONFIG.canLeashVillagerTypes;
        }
        if (entity.is(ModRegistry.GOLEMS)) {
            return Fabsservertweaks.CONFIG.canLeashGolems;
        }
        return true;
    }

    public static void teleportPlayer(ServerLevel targetDimension, ServerPlayer serverPlayer, BlockPos teleportPosition, ItemStack compassItem) {
        serverPlayer.teleport(new TeleportTransition(targetDimension, teleportPosition.getBottomCenter(), Vec3.ZERO, serverPlayer.getYRot(), serverPlayer.getXRot(), Relative.union(Relative.DELTA, Relative.ROTATION), entity -> {}));
        serverPlayer.getCooldowns().addCooldown(compassItem, 600);
        serverPlayer.sendSystemMessage(Component.literal("Teleported!"), true);
    }

    /**
     * @param level Server Level instance
     * @param targetEntity The killed entity
     * @param damageSource The source of the damage
     * @return true if the entity should be destroyed, false if the event should be passed.
     */
    public static boolean handleMobDeath(ServerLevel level, LivingEntity targetEntity, DamageSource damageSource) {
        if (BuiltinDatapackUtil.isEnabled(level.getServer(), BuiltinDatapack.CUSTOM_ENCHANTMENTS)) {
            if (targetEntity instanceof Player player) {
                if (EnchantmentUtil.handleSoulBoundAfterDeath(player)) {
                    LogUtil.debug("Soul bound items are saved for player: " + player);
                    return false;
                }
            }
        }
        if (ModGameRules.getGameRuleBoolean(level, ModGameRules.RULE_MOB_DROPS_REQUIRE_PLAYER_KILL)) {
            if (!(damageSource.getEntity() instanceof Player)) {
                return true;
            }
        }
        if (!ModGameRules.getGameRuleBoolean(level, ModGameRules.RULE_MOB_DROP_EQUIPABLE)) {
            if (targetEntity instanceof Mob mob) {
                for (EquipmentSlot slot : EquipmentSlot.values()) {
                    if (!mob.getDropChances().isPreserved(slot)) {
                        ItemStack stack = mob.getItemBySlot(slot);
                        if (stack.has(DataComponents.EQUIPPABLE) || stack.has(DataComponents.TOOL) || stack.has(DataComponents.WEAPON) || stack.getItem() instanceof ProjectileWeaponItem) {
                            stack.setCount(0);
                        }
                    }
                }
            }
        } else if (ModGameRules.getGameRuleBoolean(level, ModGameRules.RULE_MOB_DROP_FULL_DURABILITY)) {
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                ItemStack stack = targetEntity.getItemBySlot(slot);
                if (stack.isDamageableItem()) {
                    stack.setDamageValue(0);
                }
            }
        }
        return dropCustomDeathLoot(level, targetEntity, damageSource.getEntity());
    }

    public static boolean dropCustomDeathLoot(ServerLevel level, LivingEntity targetEntity, Entity sourceEntity) {
        if (BuiltinDatapackUtil.isEnabled(level.getServer(), BuiltinDatapack.CUSTOM_ENCHANTMENTS)) {
            if (sourceEntity instanceof LivingEntity sourceLivingEntity) {
                if (EnchantmentUtil.performCapturing(level, targetEntity, sourceLivingEntity)) {
                    LogUtil.debug("Entity spawn egg dropped.");
                }
            }
        }
        if (ModGameRules.getGameRuleBoolean(level, ModGameRules.RULE_BETTER_MOB_LOOT)) {
            List<ItemStack> drops = MobLoot.getLootItems(level, targetEntity.getType());
            for (ItemStack drop : drops) {
                WorldUtil.dropItem(level, targetEntity.blockPosition(), drop);
            }
        }
        return dropShulkerItems(level, targetEntity);
    }

    public static boolean dropShulkerItems(ServerLevel level, LivingEntity targetEntity) {
        if (targetEntity instanceof Shulker) {
            int shells = ModGameRules.getGameRuleInteger(level, ModGameRules.RULE_SHULKER_SHELL_DROP_AMOUNT);
            if (shells > 0) {
                return WorldUtil.dropItem(level, targetEntity.blockPosition(), new ItemStack(Items.SHULKER_SHELL, shells));
            }
        }
        return false;
    }
}
