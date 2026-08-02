package com.fabbe50.fabsservertweaks.util;

import com.fabbe50.fabsservertweaks.LogUtil;
import com.mojang.logging.LogUtils;
import dev.architectury.event.EventResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ProblemReporter;
import net.minecraft.util.random.Weighted;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.sheep.Sheep;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.TypedEntityData;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.ValueInput;

import java.util.Optional;

public class SpawnerUtil {
    public static boolean tryChangeSpawner(Player player, BaseSpawner spawner, Item mainHandItem, boolean isHoldingQuartz) {
        Modifier modifier = Modifier.getModifier(mainHandItem);
        return switch (modifier) {
            case MIN_SPAWN_DELAY -> {
                int value = spawner.minSpawnDelay;
                int newValue = calculateNewValue(modifier, value, isHoldingQuartz);
                if (isNewValueOutOfRange(modifier, newValue)) {
                    yield false;
                }
                spawner.minSpawnDelay = newValue;
                player.sendOverlayMessage(Component.literal("Minimum Spawn Delay=" + newValue));
                yield true;
            }
            case MAX_SPAWN_DELAY -> {
                int value = spawner.maxSpawnDelay;
                int newValue = calculateNewValue(modifier, value, isHoldingQuartz);
                if (isNewValueOutOfRange(modifier, newValue)) {
                    yield false;
                }
                spawner.maxSpawnDelay = newValue;
                player.sendOverlayMessage(Component.literal("Maximum Spawn Delay=" + newValue));
                yield true;
            }
            case SPAWN_COUNT -> {
                int value = spawner.spawnCount;
                int newValue = calculateNewValue(modifier, value, isHoldingQuartz);
                if (isNewValueOutOfRange(modifier, newValue)) {
                    yield false;
                }
                spawner.spawnCount = newValue;
                player.sendOverlayMessage(Component.literal("Spawn Count=" + newValue));
                yield true;
            }
            case MAX_NEARBY_ENTITIES -> {
                int value = spawner.maxNearbyEntities;
                int newValue = calculateNewValue(modifier, value, isHoldingQuartz);
                if (isNewValueOutOfRange(modifier, newValue)) {
                    yield false;
                }
                spawner.maxNearbyEntities = newValue;
                player.sendOverlayMessage(Component.literal("Max Nearby Entities=" + newValue));
                yield true;
            }
            case REQUIRED_PLAYER_RANGE -> {
                int value = spawner.requiredPlayerRange;
                if (value == -1) {
                    yield false;
                }
                int newValue = calculateNewValue(modifier, value, isHoldingQuartz);
                if (isNewValueOutOfRange(modifier, newValue)) {
                    yield false;
                }
                spawner.requiredPlayerRange = newValue;
                player.sendOverlayMessage(Component.literal("Required Player Range=" + newValue));
                yield true;
            }
            case DISABLE_REQUIRED_RANGE -> {
                int value = spawner.requiredPlayerRange;
                if ((value == -1 && !isHoldingQuartz) || (value != -1 && isHoldingQuartz)) {
                    yield false;
                }
                if (isHoldingQuartz) {
                    spawner.requiredPlayerRange = modifier.getDefaultValue();
                    player.sendOverlayMessage(Component.literal("Required Player Range=" + modifier.getDefaultValue()));
                } else {
                    spawner.requiredPlayerRange = -1;
                    player.sendOverlayMessage(Component.literal("Required Player Range=Not Required"));
                }
                yield true;
            }
            case SPAWN_RANGE -> {
                int value = spawner.spawnRange;
                int newValue = calculateNewValue(modifier, value, isHoldingQuartz);
                if (isNewValueOutOfRange(modifier, newValue)) {
                    yield false;
                }
                spawner.spawnRange = newValue;
                player.sendOverlayMessage(Component.literal("Spawn Range=" + newValue));
                yield true;
            }
            case null -> false;
        };
    }

    private static int calculateNewValue(Modifier modifier, int originalValue, boolean isHoldingQuartz) {
        boolean minus = modifier.isNegativeIsBetter() && !isHoldingQuartz || !modifier.isNegativeIsBetter() && isHoldingQuartz;
        if (minus) {
            return originalValue - modifier.getStepAmount();
        } else {
            return originalValue + modifier.getStepAmount();
        }
    }

    private static boolean isNewValueOutOfRange(Modifier modifier, int newValue) {
        return newValue < modifier.getMin() || newValue > modifier.getMax();
    }

    public static boolean placeSpawnerWithData(ServerLevel level, BlockPos pos, BlockState state, ItemStack mainHandStack) {
        return WorldUtil.placeBlockWithData(level, pos, state, mainHandStack, BlockEntityType.MOB_SPAWNER);
    }

    public static boolean isSpawningAnimals(ServerLevel level, SpawnData spawnData) {
        ValueInput input = TagValueInput.create(ProblemReporter.DISCARDING, level.registryAccess(), spawnData.getEntityToSpawn());
        Optional<EntityType<?>> entityType = EntityType.by(input);
        return entityType.filter(type -> !(type.create(level, EntitySpawnReason.SPAWNER) instanceof Enemy)).isPresent();
    }

    public enum Modifier {
        MIN_SPAWN_DELAY(Items.SUGAR, 10, 1600, 4, true, 200),
        MAX_SPAWN_DELAY(Items.REDSTONE, 10, 1600, 16, true, 800),
        SPAWN_COUNT(Items.GHAST_TEAR, 1, 16, 1, false, 4),
        MAX_NEARBY_ENTITIES(Items.GLISTERING_MELON_SLICE, 1, 32, 1, false, 6),
        REQUIRED_PLAYER_RANGE(Items.ENDER_EYE, 3, 32, 1, false, 16),
        DISABLE_REQUIRED_RANGE(Items.ECHO_SHARD, 0, 0, 0, false, 0),
        SPAWN_RANGE(Items.AMETHYST_SHARD, 2, 16, 1, false, 4),
        ;

        private final Item item;
        private final int min;
        private final int max;
        private final int stepAmount;
        private final boolean negativeIsBetter;
        private final int defaultValue;
        Modifier(Item item, int min, int max, int stepAmount, boolean negativeIsBetter, int defaultValue) {
            this.item = item;
            this.min = min;
            this.max = max;
            this.stepAmount = stepAmount;
            this.negativeIsBetter = negativeIsBetter;
            this.defaultValue = defaultValue;
        }

        public Item getItem() {
            return item;
        }

        public int getMin() {
            return min;
        }

        public int getMax() {
            return max;
        }

        public int getStepAmount() {
            return stepAmount;
        }

        public boolean isNegativeIsBetter() {
            return negativeIsBetter;
        }

        public int getDefaultValue() {
            return defaultValue;
        }

        public static boolean isItemValid(Item item) {
            for (Modifier modifier : values()) {
                if (modifier.getItem() == item) {
                    return true;
                }
            }
            return false;
        }

        public static Modifier getModifier(Item item) {
            for (Modifier modifier : values()) {
                if (modifier.getItem() == item) {
                    return modifier;
                }
            }
            return null;
        }
    }
}
