package com.fabbe50.fabsservertweaks.data;

import com.fabbe50.fabsservertweaks.Fabsservertweaks;
import com.fabbe50.fabsservertweaks.ModConfig;
import com.fabbe50.fabsservertweaks.registries.ModGameRules;
import com.fabbe50.fabsservertweaks.registries.gamerules.DifficultyValue;
import com.fabbe50.fabsservertweaks.registries.gamerules.DifficultyValue.Difficulty;
import com.fabbe50.fabsservertweaks.registries.gamerules.TrampleValue;
import com.fabbe50.fabsservertweaks.registries.gamerules.TrampleValue.TrampleMode;
import io.netty.buffer.ByteBuf;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.gamerules.GameRules;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.IntFunction;

public enum Presets implements StringRepresentable {
    DEFAULT(0, "default") {
        @Override
        public boolean adjustRules(ServerLevel serverLevel) {
            MinecraftServer minecraftServer = serverLevel.getServer();
            serverLevel.getGameRules().set(ModGameRules.RULE_MOB_GRIEF_ENDERMAN, true, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_MOB_GRIEF_CREEPER, true, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_MOB_GRIEF_ZOMBIE, true, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_MOB_DROP_EQUIPABLE, true, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_MOB_DROP_FULL_DURABILITY, false, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_MOB_DROPS_REQUIRE_PLAYER_KILL, false, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_SHULKERS_RANDOM_COLOR, false, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_SHULKERS_CAN_TELEPORT, true, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_SHULKER_SHELL_DROP_AMOUNT, 2, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_LOYALTY_TRIDENT_RETURNS_FROM_VOID, true, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_ALWAYS_CONVERT_VILLAGERS, true, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_MOBS_SPAWN_WITH_EFFECTS, true, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_FRIENDLY_PHANTOMS, false, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_NO_COBBLE_GEN, false, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_BETTER_HOES, false, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_WATER_DRIPSTONE_FILL_CAULDRON, true, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_LAVA_DRIPSTONE_FILL_CAULDRON, true, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_RAIN_FILLS_CAULDRON, true, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_SNOW_FILLS_CAULDRON, true, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_BETTER_RAIL_PLACEMENT, false, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_SNOW_GOLEMS_SURVIVE_ON_ICE, true, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_SNOW_GOLEMS_GENERATE_SNOW, true, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_NO_SOLICITING_SIGN, true, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_SILK_TOUCHABLE_AMETHYST_NODES, false, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_SILK_TOUCHABLE_SPAWNERS, false, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_SILK_TOUCHABLE_TRIAL_SPAWNERS, false, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_SILK_TOUCHABLE_TRIAL_VAULTS, false, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_EXPANDED_BONE_MEAL, true, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_SLEEPING_BAGS_ENABLED, true, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_SAFE_CANT_SLEEP, false, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_TORCHFLOWERS_GLOW, true, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_CACTUS_GROW_HEIGHT, 3, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_SUGAR_CANE_GROW_HEIGHT, 3, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_PLANTS_GROW_FASTER_IN_RAIN, true, minecraftServer);
            Fabsservertweaks.CONFIG.cropTrampleMode = TrampleMode.FEATHER_FALLING;
            Fabsservertweaks.CONFIG.eggTrampleMode = TrampleMode.FEATHER_FALLING;
            Fabsservertweaks.CONFIG.difficulty = Difficulty.SCALE_BY_DIFFICULTY;
            Fabsservertweaks.CONFIG.shareSeed = false;
            Fabsservertweaks.CONFIG.overrideNormalLead = true;
            Fabsservertweaks.CONFIG.canLeashAnimals = true;
            Fabsservertweaks.CONFIG.canLeashMonsters = true;
            Fabsservertweaks.CONFIG.canLeashBosses = false;
            Fabsservertweaks.CONFIG.canLeashVillagerTypes = true;
            Fabsservertweaks.CONFIG.canLeashGolems = true;
            Fabsservertweaks.CONFIG.canLeashPets = true;
            Fabsservertweaks.CONFIG.canPistonsPushBlockEntities = true;
            AutoConfig.getConfigHolder(ModConfig.class).save();
            return true;
        }
    },
    VANILLA(1, "vanilla") {
        @Override
        public boolean adjustRules(ServerLevel serverLevel) {
            MinecraftServer minecraftServer = serverLevel.getServer();
            serverLevel.getGameRules().set(ModGameRules.RULE_MOB_GRIEF_ENDERMAN, true, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_MOB_GRIEF_CREEPER, true, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_MOB_GRIEF_ZOMBIE, true, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_MOB_DROP_EQUIPABLE, true, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_MOB_DROP_FULL_DURABILITY, false, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_MOB_DROPS_REQUIRE_PLAYER_KILL, false, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_SHULKERS_RANDOM_COLOR, false, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_SHULKERS_CAN_TELEPORT, true, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_SHULKER_SHELL_DROP_AMOUNT, 0, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_LOYALTY_TRIDENT_RETURNS_FROM_VOID, false, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_ALWAYS_CONVERT_VILLAGERS, false, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_MOBS_SPAWN_WITH_EFFECTS, false, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_FRIENDLY_PHANTOMS, false, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_NO_COBBLE_GEN, false, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_BETTER_HOES, false, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_WATER_DRIPSTONE_FILL_CAULDRON, true, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_LAVA_DRIPSTONE_FILL_CAULDRON, true, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_RAIN_FILLS_CAULDRON, true, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_SNOW_FILLS_CAULDRON, true, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_BETTER_RAIL_PLACEMENT, false, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_SNOW_GOLEMS_SURVIVE_ON_ICE, false, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_SNOW_GOLEMS_GENERATE_SNOW, true, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_NO_SOLICITING_SIGN, false, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_SILK_TOUCHABLE_AMETHYST_NODES, false, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_SILK_TOUCHABLE_SPAWNERS, false, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_SILK_TOUCHABLE_TRIAL_SPAWNERS, false, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_SILK_TOUCHABLE_TRIAL_VAULTS, false, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_EXPANDED_BONE_MEAL, false, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_SLEEPING_BAGS_ENABLED, false, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_SAFE_CANT_SLEEP, false, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_TORCHFLOWERS_GLOW, false, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_CACTUS_GROW_HEIGHT, 3, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_SUGAR_CANE_GROW_HEIGHT, 3, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_PLANTS_GROW_FASTER_IN_RAIN, false, minecraftServer);
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
            AutoConfig.getConfigHolder(ModConfig.class).save();
            return true;
        }
    },
    FABS(2, "fabs_choice") {
        @Override
        public boolean adjustRules(ServerLevel serverLevel) {
            MinecraftServer minecraftServer = serverLevel.getServer();
            serverLevel.getGameRules().set(ModGameRules.RULE_MOB_GRIEF_ENDERMAN, false, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_MOB_GRIEF_CREEPER, true, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_MOB_GRIEF_ZOMBIE, true, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_MOB_DROP_EQUIPABLE, false, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_MOB_DROP_FULL_DURABILITY, false, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_MOB_DROPS_REQUIRE_PLAYER_KILL, false, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_SHULKERS_RANDOM_COLOR, false, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_SHULKERS_CAN_TELEPORT, true, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_SHULKER_SHELL_DROP_AMOUNT, 2, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_LOYALTY_TRIDENT_RETURNS_FROM_VOID, true, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_ALWAYS_CONVERT_VILLAGERS, true, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_MOBS_SPAWN_WITH_EFFECTS, true, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_FRIENDLY_PHANTOMS, true, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_NO_COBBLE_GEN, false, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_BETTER_HOES, true, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_WATER_DRIPSTONE_FILL_CAULDRON, true, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_LAVA_DRIPSTONE_FILL_CAULDRON, true, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_RAIN_FILLS_CAULDRON, true, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_SNOW_FILLS_CAULDRON, true, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_BETTER_RAIL_PLACEMENT, true, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_SNOW_GOLEMS_SURVIVE_ON_ICE, true, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_SNOW_GOLEMS_GENERATE_SNOW, true, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_NO_SOLICITING_SIGN, true, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_SILK_TOUCHABLE_AMETHYST_NODES, true, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_SILK_TOUCHABLE_SPAWNERS, true, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_SILK_TOUCHABLE_TRIAL_SPAWNERS, true, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_SILK_TOUCHABLE_TRIAL_VAULTS, true, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_EXPANDED_BONE_MEAL, true, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_SLEEPING_BAGS_ENABLED, true, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_SAFE_CANT_SLEEP, false, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_TORCHFLOWERS_GLOW, true, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_CACTUS_GROW_HEIGHT, 5, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_SUGAR_CANE_GROW_HEIGHT, 5, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_PLANTS_GROW_FASTER_IN_RAIN, true, minecraftServer);
            Fabsservertweaks.CONFIG.cropTrampleMode = TrampleMode.FEATHER_FALLING;
            Fabsservertweaks.CONFIG.eggTrampleMode = TrampleMode.FEATHER_FALLING;
            Fabsservertweaks.CONFIG.difficulty = Difficulty.SCALE_BY_DIFFICULTY;
            Fabsservertweaks.CONFIG.shareSeed = true;
            Fabsservertweaks.CONFIG.overrideNormalLead = true;
            Fabsservertweaks.CONFIG.canLeashAnimals = true;
            Fabsservertweaks.CONFIG.canLeashMonsters = true;
            Fabsservertweaks.CONFIG.canLeashBosses = false;
            Fabsservertweaks.CONFIG.canLeashVillagerTypes = true;
            Fabsservertweaks.CONFIG.canLeashGolems = true;
            Fabsservertweaks.CONFIG.canLeashPets = true;
            Fabsservertweaks.CONFIG.canPistonsPushBlockEntities = true;
            AutoConfig.getConfigHolder(ModConfig.class).save();
            return true;
        }
    },
    NO_RENEWABLE(3, "no_renewable") {
        @Override
        public boolean adjustRules(ServerLevel serverLevel) {
            MinecraftServer minecraftServer = serverLevel.getServer();
            serverLevel.getGameRules().set(ModGameRules.RULE_NO_COBBLE_GEN, true, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_WATER_DRIPSTONE_FILL_CAULDRON, false, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_LAVA_DRIPSTONE_FILL_CAULDRON, false, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_RAIN_FILLS_CAULDRON, false, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_SNOW_FILLS_CAULDRON, false, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_SNOW_GOLEMS_GENERATE_SNOW, false, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_CACTUS_GROW_HEIGHT, 1, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_SUGAR_CANE_GROW_HEIGHT, 1, minecraftServer);
            serverLevel.getGameRules().set(ModGameRules.RULE_PLANTS_GROW_FASTER_IN_RAIN, false, minecraftServer);
            return true;
        }
    },
    CREATIVE_DEFAULTS(4, "creative_defaults") {
        @Override
        public boolean adjustRules(ServerLevel serverLevel) {
            MinecraftServer minecraftServer = serverLevel.getServer();
            serverLevel.getGameRules().set(GameRules.ADVANCE_TIME, false, minecraftServer);
            serverLevel.getGameRules().set(GameRules.ADVANCE_WEATHER, false, minecraftServer);
            serverLevel.getGameRules().set(GameRules.SPAWN_MOBS, false, minecraftServer);
            serverLevel.getGameRules().set(GameRules.SPAWN_WANDERING_TRADERS, false, minecraftServer);
            serverLevel.getGameRules().set(GameRules.FIRE_DAMAGE, false, minecraftServer);
            serverLevel.getGameRules().set(GameRules.KEEP_INVENTORY, true, minecraftServer);
            return true;
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
