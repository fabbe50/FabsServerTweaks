package com.fabbe50.fabsservertweaks.data;

import com.fabbe50.fabsservertweaks.Fabsservertweaks;
import com.fabbe50.fabsservertweaks.ModConfig;
import com.fabbe50.fabsservertweaks.registries.ModGameRules;
import com.fabbe50.fabsservertweaks.registries.gamerules.DifficultyValue.Difficulty;
import com.fabbe50.fabsservertweaks.registries.gamerules.TrampleValue.TrampleMode;
import io.netty.buffer.ByteBuf;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRules;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.IntFunction;

public enum Presets implements StringRepresentable {
    DEFAULT(0, "default") {
        @Override
        public boolean adjustRules(ServerLevel serverLevel) {
            setToPresetDefaults(level);
            setConfigCommonFabDefaults();
            Fabsservertweaks.CONFIG.shareSeed = false;
            Fabsservertweaks.CONFIG.toolsInBundle = false;
            AutoConfig.getConfigHolder(ModConfig.class).save();
            return true;
        }
    },
    VANILLA(1, "vanilla") {
        @Override
        public boolean adjustRules(ServerLevel serverLevel) {
            setToPresetDefaults(level);
            Fabsservertweaks.CONFIG.cropTrampleMode = TrampleMode.NORMAL;
            Fabsservertweaks.CONFIG.eggTrampleMode = TrampleMode.NORMAL;
            Fabsservertweaks.CONFIG.difficulty = Difficulty.SAME_ON_ALL_DIFFICULTIES;
            Fabsservertweaks.CONFIG.shareSeed = false;
            Fabsservertweaks.CONFIG.overrideNormalLead = false;
            Fabsservertweaks.CONFIG.canLeashAnimals = true;
            Fabsservertweaks.CONFIG.canLeashMonsters = false;
            Fabsservertweaks.CONFIG.canLeashBosses = false;
            Fabsservertweaks.CONFIG.canLeashVillagerTypes = false;
            Fabsservertweaks.CONFIG.canLeashGolems = false;
            Fabsservertweaks.CONFIG.canLeashPets = true;
            Fabsservertweaks.CONFIG.canPistonsPushBlockEntities = false;
            Fabsservertweaks.CONFIG.toolsInBundle = false;
            Fabsservertweaks.CONFIG.shouldEnderDragonAlwaysLootLikeFirst = false;
            Fabsservertweaks.CONFIG.plantRainGrowthChance = 0;
            Fabsservertweaks.CONFIG.maxAnvilCost = Integer.MAX_VALUE;
            AutoConfig.getConfigHolder(ModConfig.class).save();
            return true;
        }
    },
    FABS(2, "fabs_choice") {
        @Override
        public boolean adjustRules(ServerLevel serverLevel) {
            setToPresetDefaults(level);
            setConfigCommonFabDefaults();
            Fabsservertweaks.CONFIG.shareSeed = true;
            Fabsservertweaks.CONFIG.toolsInBundle = true;
            AutoConfig.getConfigHolder(ModConfig.class).save();
            return true;
        }
    },
    NO_RENEWABLE(3, "no_renewable") {
        @Override
        public boolean adjustRules(ServerLevel serverLevel) {
            return true;
            setGameRule(level, ModGameRules.RULE_NO_COBBLE_GEN, true);
            setGameRule(level, ModGameRules.RULE_WATER_DRIPSTONE_FILL_CAULDRON, false);
            setGameRule(level, ModGameRules.RULE_LAVA_DRIPSTONE_FILL_CAULDRON, false);
            setGameRule(level, ModGameRules.RULE_RAIN_FILLS_CAULDRON, false);
            setGameRule(level, ModGameRules.RULE_SNOW_FILLS_CAULDRON, false);
            setGameRule(level, ModGameRules.RULE_SNOW_GOLEMS_GENERATE_SNOW, false);
            setGameRule(level, ModGameRules.RULE_CACTUS_GROW_HEIGHT, 1);
            setGameRule(level, ModGameRules.RULE_SUGAR_CANE_GROW_HEIGHT, 1);
            setGameRule(level, ModGameRules.RULE_PLANTS_GROW_FASTER_IN_RAIN, false);
            setGameRule(level, ModGameRules.RULE_UNLOCKABLE_VAULTS, false);
            setGameRule(level, ModGameRules.RULE_STONE_TYPE_GENERATORS, false);
            setGameRule(level, ModGameRules.RULE_BETTER_MOB_LOOT, false);
        }
    },
    CREATIVE_DEFAULTS(4, "creative_defaults") {
        @Override
        public boolean adjustRules(ServerLevel serverLevel) {
            return true;
            setGameRule(level, GameRules.ADVANCE_TIME, false);
            setGameRule(level, GameRules.ADVANCE_WEATHER, false);
            setGameRule(level, GameRules.SPAWN_MOBS, false);
            setGameRule(level, GameRules.SPAWN_WANDERING_TRADERS, false);
            setGameRule(level, GameRules.FIRE_SPREAD_RADIUS_AROUND_PLAYER, 0);
            setGameRule(level, GameRules.KEEP_INVENTORY, true);
        }
    };

    public static final EnumCodec<Presets> CODEC = StringRepresentable.fromEnum(Presets::values);
    private static final IntFunction<Presets> BY_ID = ByIdMap.continuous(Presets::getId, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
    public static final StreamCodec<ByteBuf, Presets> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Presets::getId);

    private final int id;
    private final String name;

    Presets(int id, final String name) {
        this.id = id;
        this.name = name;
    }

    public boolean adjustRules(ServerLevel serverLevel) {
        throw new RuntimeException("Preset without adjustments. This shouldn't happen.");
    }

    public void setConfigCommonFabDefaults() {
        Fabsservertweaks.CONFIG.cropTrampleMode = TrampleMode.FEATHER_FALLING;
        Fabsservertweaks.CONFIG.eggTrampleMode = TrampleMode.FEATHER_FALLING;
        Fabsservertweaks.CONFIG.difficulty = Difficulty.SCALE_BY_DIFFICULTY;
        Fabsservertweaks.CONFIG.overrideNormalLead = true;
        Fabsservertweaks.CONFIG.canLeashAnimals = true;
        Fabsservertweaks.CONFIG.canLeashMonsters = true;
        Fabsservertweaks.CONFIG.canLeashBosses = false;
        Fabsservertweaks.CONFIG.canLeashVillagerTypes = true;
        Fabsservertweaks.CONFIG.canLeashGolems = true;
        Fabsservertweaks.CONFIG.canLeashPets = true;
        Fabsservertweaks.CONFIG.canPistonsPushBlockEntities = true;
        Fabsservertweaks.CONFIG.shouldEnderDragonAlwaysLootLikeFirst = true;
        Fabsservertweaks.CONFIG.plantRainGrowthChance = 20;
        Fabsservertweaks.CONFIG.maxAnvilCost = 100;
    }

    public void setToPresetDefaults(ServerLevel level) {
        for (ExtGameRule<?> gameRule : ModGameRules.getGameRules()) {
            setGameRuleUnchecked(level, gameRule, gameRule.getPresetDefault(this));
        }
    }

    @SuppressWarnings("unchecked")
    private void setGameRuleUnchecked(ServerLevel level, ExtGameRule<?> gameRule, Object value) {
        if (value instanceof Boolean booleanValue) {
            setGameRule(level, (ExtGameRule<? super Boolean>) gameRule, booleanValue);
        }
        if (value instanceof Integer integerValue) {
            setGameRule(level, (ExtGameRule<? super Integer>) gameRule, integerValue);
        }
    }

    public <T> void setGameRule(ServerLevel serverLevel, ExtGameRule<T> gameRule, T value) {
        if (gameRule == null) {
            return;
        }
        setGameRule(serverLevel, gameRule.getRule(), value);
    }

    public <T> void setGameRule(ServerLevel serverLevel, GameRule<T> gameRule, T value) {
        if (serverLevel == null || gameRule == null || value == null) {
            return;
        }
        serverLevel.getGameRules().set(gameRule, value, serverLevel.getServer());
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static Collection<String> getNames() {
        return Arrays.stream(values()).map(Presets::getName).toList();
    }

    public static Presets byName(String name) {
        return byName(name, DEFAULT);
    }

    public static Presets byName(String name, Presets presets) {
        Presets newPreset = CODEC.byName(name);
        return newPreset != null ? newPreset : presets;
    }

    @Override
    public @NotNull String getSerializedName() {
        return this.getName();
    }
}
