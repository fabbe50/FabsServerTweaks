package com.fabbe50.fabsservertweaks.registries;

import com.fabbe50.fabsservertweaks.registries.gamerules.DifficultyValue;
import com.fabbe50.fabsservertweaks.registries.gamerules.DifficultyValue.Difficulty;
import com.fabbe50.fabsservertweaks.registries.gamerules.TrampleValue;
import com.fabbe50.fabsservertweaks.registries.gamerules.TrampleValue.TrampleMode;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameRules.BooleanValue;
import net.minecraft.world.level.GameRules.Category;
import net.minecraft.world.level.GameRules.IntegerValue;

public class ModGameRules {
    public static final GameRules.Key<BooleanValue> RULE_MOB_GRIEF_ENDERMAN;
    public static final GameRules.Key<BooleanValue> RULE_MOB_GRIEF_CREEPER;
    public static final GameRules.Key<BooleanValue> RULE_MOB_GRIEF_ZOMBIE;
    public static final GameRules.Key<BooleanValue> RULE_MOB_DROP_EQUIPABLE;
    public static final GameRules.Key<BooleanValue> RULE_MOB_DROP_FULL_DURABILITY;
    public static final GameRules.Key<BooleanValue> RULE_MOB_DROPS_REQUIRE_PLAYER_KILL;
    public static final GameRules.Key<TrampleValue> RULE_CROP_TRAMPLE_MODE;
    public static final GameRules.Key<TrampleValue> RULE_TURTLE_EGG_TRAMPLE_MODE;
    public static final GameRules.Key<BooleanValue> RULE_SHULKERS_RANDOM_COLOR;
    public static final GameRules.Key<BooleanValue> RULE_SHULKERS_CAN_TELEPORT;
    public static final GameRules.Key<IntegerValue> RULE_SHULKER_SHELL_DROP_AMOUNT;
    public static final GameRules.Key<BooleanValue> RULE_LOYALTY_TRIDENT_RETURNS_FROM_VOID;
    public static final GameRules.Key<BooleanValue> RULE_ALWAYS_CONVERT_VILLAGERS;
    public static final GameRules.Key<BooleanValue> RULE_MOBS_SPAWN_WITH_EFFECTS;
    public static final GameRules.Key<DifficultyValue> RULE_SPAWN_WITH_EFFECT_MODE;
    public static final GameRules.Key<BooleanValue> RULE_FRIENDLY_PHANTOMS;
    public static final GameRules.Key<BooleanValue> RULE_NO_COBBLE_GEN;
    public static final GameRules.Key<BooleanValue> RULE_SHARE_SEED;
    public static final GameRules.Key<BooleanValue> RULE_BETTER_HOES;
    public static final GameRules.Key<BooleanValue> RULE_WATER_DRIPSTONE_FILL_CAULDRON;
    public static final GameRules.Key<BooleanValue> RULE_LAVA_DRIPSTONE_FILL_CAULDRON;
    public static final GameRules.Key<BooleanValue> RULE_RAIN_FILLS_CAULDRON;
    public static final GameRules.Key<BooleanValue> RULE_SNOW_FILLS_CAULDRON;
    public static final GameRules.Key<BooleanValue> RULE_BETTER_RAIL_PLACEMENT;
    public static final GameRules.Key<BooleanValue> RULE_SNOW_GOLEMS_SURVIVE_ON_ICE;
    public static final GameRules.Key<BooleanValue> RULE_SNOW_GOLEMS_GENERATE_SNOW;

    static {
        RULE_MOB_GRIEF_ENDERMAN = GameRules.register("mobGriefEnderman", Category.MOBS, BooleanValue.create(true));
        RULE_MOB_GRIEF_CREEPER = GameRules.register("mobGriefCreeper", Category.MOBS, BooleanValue.create(true));
        RULE_MOB_GRIEF_ZOMBIE = GameRules.register("mobGriefZombie", Category.MOBS, BooleanValue.create(true));
        RULE_MOB_DROP_EQUIPABLE = GameRules.register("mobDropEquipable", Category.DROPS, BooleanValue.create(true));
        RULE_MOB_DROP_FULL_DURABILITY = GameRules.register("mobDropFullDurability", Category.DROPS, BooleanValue.create(false));
        RULE_MOB_DROPS_REQUIRE_PLAYER_KILL = GameRules.register("mobDropsRequirePlayerKill", Category.DROPS, BooleanValue.create(false));
        RULE_CROP_TRAMPLE_MODE = GameRules.register("cropTrampleMode", Category.MISC, TrampleValue.create(TrampleMode.FEATHER_FALLING));
        RULE_TURTLE_EGG_TRAMPLE_MODE = GameRules.register("turtleEggTrampleMode", Category.MISC, TrampleValue.create(TrampleMode.FEATHER_FALLING));
        RULE_SHULKERS_RANDOM_COLOR = GameRules.register("shulkerRandomColor", Category.MOBS, BooleanValue.create(false));
        RULE_SHULKERS_CAN_TELEPORT = GameRules.register("shulkersCanTeleport", Category.MOBS, BooleanValue.create(true));
        RULE_SHULKER_SHELL_DROP_AMOUNT = GameRules.register("shulkerShellDropAmount", Category.MOBS, IntegerValue.create(2));
        RULE_LOYALTY_TRIDENT_RETURNS_FROM_VOID = GameRules.register("tridentWithLoyaltyReturnsFromVoid", Category.MISC, BooleanValue.create(true));
        RULE_ALWAYS_CONVERT_VILLAGERS = GameRules.register("villagerAlwaysTurnIntoZombies", Category.MISC, BooleanValue.create(true));
        RULE_MOBS_SPAWN_WITH_EFFECTS = GameRules.register("mobsSpawnWithEffects", Category.MISC, BooleanValue.create(true));
        RULE_SPAWN_WITH_EFFECT_MODE = GameRules.register("spawnWithEffectMode", Category.MISC, DifficultyValue.create(Difficulty.SCALE_BY_DIFFICULTY));
        RULE_FRIENDLY_PHANTOMS = GameRules.register("friendlyPhantoms", Category.MOBS, BooleanValue.create(false));
        RULE_NO_COBBLE_GEN = GameRules.register("noCobbleGen", Category.MISC, BooleanValue.create(false));
        RULE_SHARE_SEED = GameRules.register("shareSeed", Category.MISC, BooleanValue.create(false));
        RULE_BETTER_HOES = GameRules.register("betterHoes", Category.MISC, BooleanValue.create(true));
        RULE_WATER_DRIPSTONE_FILL_CAULDRON = GameRules.register("dripstoneFillCauldronWater", Category.MISC, BooleanValue.create(true));
        RULE_LAVA_DRIPSTONE_FILL_CAULDRON = GameRules.register("dripstoneFillCauldronLava", Category.MISC, BooleanValue.create(true));
        RULE_RAIN_FILLS_CAULDRON = GameRules.register("rainFillsCauldron", Category.MISC, BooleanValue.create(true));
        RULE_SNOW_FILLS_CAULDRON = GameRules.register("snowFillsCauldron", Category.MISC, BooleanValue.create(true));
        RULE_BETTER_RAIL_PLACEMENT = GameRules.register("betterRailPlacement", Category.MISC, BooleanValue.create(false));
        RULE_SNOW_GOLEMS_SURVIVE_ON_ICE = GameRules.register("snow_golems_survive_on_ice", Category.MISC, BooleanValue.create(true));
        RULE_SNOW_GOLEMS_GENERATE_SNOW = GameRules.register("snow_golems_generate_snow", Category.MISC, BooleanValue.create(true));
    }

    public static void init() {}
}
