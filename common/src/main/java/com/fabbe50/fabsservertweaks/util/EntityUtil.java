package com.fabbe50.fabsservertweaks.util;

import com.fabbe50.fabsservertweaks.Fabsservertweaks;
import com.fabbe50.fabsservertweaks.registries.ModRegistry;
import com.fabbe50.fabsservertweaks.registries.gamerules.DifficultyValue;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

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
}
