package com.fabbe50.fabsservertweaks.registries;

import net.minecraft.world.level.gamerules.*;

public class ModGameRules {
    public static final GameRule<Boolean> RULE_MOB_GRIEF_ENDERMAN;
    public static final GameRule<Boolean> RULE_MOB_GRIEF_CREEPER;
    public static final GameRule<Boolean> RULE_MOB_GRIEF_ZOMBIE;
    public static final GameRule<Boolean> RULE_MOB_DROP_EQUIPABLE;
    public static final GameRule<Boolean> RULE_MOB_DROP_FULL_DURABILITY;
    public static final GameRule<Boolean> RULE_MOB_DROPS_REQUIRE_PLAYER_KILL;
    public static final GameRule<Boolean> RULE_SHULKERS_RANDOM_COLOR;
    public static final GameRule<Boolean> RULE_SHULKERS_CAN_TELEPORT;
    public static final GameRule<Integer> RULE_SHULKER_SHELL_DROP_AMOUNT;
    public static final GameRule<Boolean> RULE_LOYALTY_TRIDENT_RETURNS_FROM_VOID;
    public static final GameRule<Boolean> RULE_ALWAYS_CONVERT_VILLAGERS;
    public static final GameRule<Boolean> RULE_MOBS_SPAWN_WITH_EFFECTS;
    public static final GameRule<Boolean> RULE_FRIENDLY_PHANTOMS;
    public static final GameRule<Boolean> RULE_NO_COBBLE_GEN;
    public static final GameRule<Boolean> RULE_BETTER_HOES;
    public static final GameRule<Boolean> RULE_WATER_DRIPSTONE_FILL_CAULDRON;
    public static final GameRule<Boolean> RULE_LAVA_DRIPSTONE_FILL_CAULDRON;
    public static final GameRule<Boolean> RULE_RAIN_FILLS_CAULDRON;
    public static final GameRule<Boolean> RULE_SNOW_FILLS_CAULDRON;
    public static final GameRule<Boolean> RULE_BETTER_RAIL_PLACEMENT;
    public static final GameRule<Boolean> RULE_SNOW_GOLEMS_SURVIVE_ON_ICE;
    public static final GameRule<Boolean> RULE_SNOW_GOLEMS_GENERATE_SNOW;
    public static final GameRule<Boolean> RULE_NO_SOLICITING_SIGN;
    public static final GameRule<Boolean> RULE_SILK_TOUCHABLE_AMETHYST_NODES;
    public static final GameRule<Boolean> RULE_SILK_TOUCHABLE_SPAWNERS;
    public static final GameRule<Boolean> RULE_SILK_TOUCHABLE_TRIAL_SPAWNERS;
    public static final GameRule<Boolean> RULE_SILK_TOUCHABLE_TRIAL_VAULTS;
    public static final GameRule<Boolean> RULE_EXPANDED_BONE_MEAL;
    public static final GameRule<Boolean> RULE_SLEEPING_BAGS_ENABLED;
    public static final GameRule<Boolean> RULE_SAFE_CANT_SLEEP;
    public static final GameRule<Boolean> RULE_PET_FRIENDLY_FIRE;
    public static final GameRule<Boolean> RULE_FORTUNE_ANCIENT_DEBRIS;
    public static final GameRule<Boolean> RULE_TORCHFLOWERS_GLOW;
    public static final GameRule<Integer> RULE_CACTUS_GROW_HEIGHT;
    public static final GameRule<Integer> RULE_SUGAR_CANE_GROW_HEIGHT;
    public static final GameRule<Boolean> RULE_PLANTS_GROW_FASTER_IN_RAIN;

    static {
        RULE_MOB_GRIEF_ENDERMAN = registerBoolean("mob_grief_enderman", GameRuleCategory.MOBS, true);
        RULE_MOB_GRIEF_CREEPER = registerBoolean("mob_grief_creeper", GameRuleCategory.MOBS, true);
        RULE_MOB_GRIEF_ZOMBIE = registerBoolean("mob_grief_zombie", GameRuleCategory.MOBS, true);
        RULE_MOB_DROP_EQUIPABLE = registerBoolean("mob_drop_equipable", GameRuleCategory.DROPS, true);
        RULE_MOB_DROP_FULL_DURABILITY = registerBoolean("mob_drop_full_durability", GameRuleCategory.DROPS, false);
        RULE_MOB_DROPS_REQUIRE_PLAYER_KILL = registerBoolean("mob_drops_require_player_kill", GameRuleCategory.DROPS, false);
        RULE_SHULKERS_RANDOM_COLOR = registerBoolean("shulker_random_color", GameRuleCategory.MOBS, false);
        RULE_SHULKERS_CAN_TELEPORT = registerBoolean("shulkers_can_teleport", GameRuleCategory.MOBS, true);
        RULE_SHULKER_SHELL_DROP_AMOUNT = registerInteger("shulker_shell_drop_amount", GameRuleCategory.MOBS, 2, -1);
        RULE_LOYALTY_TRIDENT_RETURNS_FROM_VOID = registerBoolean("trident_with_loyalty_returns_from_void", GameRuleCategory.MISC, true);
        RULE_ALWAYS_CONVERT_VILLAGERS = registerBoolean("villager_always_turn_into_zombies", GameRuleCategory.MOBS, true);
        RULE_MOBS_SPAWN_WITH_EFFECTS = registerBoolean("mobs_spawn_with_effects", GameRuleCategory.MOBS, true);
        RULE_FRIENDLY_PHANTOMS = registerBoolean("friendly_phantoms", GameRuleCategory.MOBS, false);
        RULE_NO_COBBLE_GEN = registerBoolean("no_cobble_gen", GameRuleCategory.MISC, false);
        RULE_BETTER_HOES = registerBoolean("better_hoes", GameRuleCategory.MISC, true);
        RULE_WATER_DRIPSTONE_FILL_CAULDRON = registerBoolean("dripstone_fill_cauldron_water", GameRuleCategory.MISC, true);
        RULE_LAVA_DRIPSTONE_FILL_CAULDRON = registerBoolean("dripstone_fill_cauldron_lava", GameRuleCategory.MISC, true);
        RULE_RAIN_FILLS_CAULDRON = registerBoolean("rain_fills_cauldron", GameRuleCategory.MISC, true);
        RULE_SNOW_FILLS_CAULDRON = registerBoolean("snow_fills_cauldron", GameRuleCategory.MISC, true);
        RULE_BETTER_RAIL_PLACEMENT = registerBoolean("better_rail_placement", GameRuleCategory.MISC, false);
        RULE_SNOW_GOLEMS_SURVIVE_ON_ICE = registerBoolean("snow_golems_survive_on_ice", GameRuleCategory.MOBS, true);
        RULE_SNOW_GOLEMS_GENERATE_SNOW = registerBoolean("snow_golems_generate_snow", GameRuleCategory.MOBS, true);
        RULE_NO_SOLICITING_SIGN = registerBoolean("no_soliciting_sign", GameRuleCategory.MOBS, true);
        RULE_SILK_TOUCHABLE_AMETHYST_NODES = registerBoolean("silk_touchable_amethyst_nodes", GameRuleCategory.MISC, false);
        RULE_SILK_TOUCHABLE_SPAWNERS = registerBoolean("silk_touchable_spawners", GameRuleCategory.MISC, false);
        RULE_SILK_TOUCHABLE_TRIAL_SPAWNERS = registerBoolean("silk_touchable_trial_spawners", GameRuleCategory.MISC, false);
        RULE_SILK_TOUCHABLE_TRIAL_VAULTS = registerBoolean("silk_touchable_trial_vaults", GameRuleCategory.MISC, false);
        RULE_EXPANDED_BONE_MEAL = registerBoolean("expanded_bone_meal", GameRuleCategory.MISC, true);
        RULE_SLEEPING_BAGS_ENABLED = registerBoolean("sleeping_bags_enabled", GameRuleCategory.MISC, true);
        RULE_SAFE_CANT_SLEEP = registerBoolean("safe_cant_sleep", GameRuleCategory.MISC, false);
        RULE_PET_FRIENDLY_FIRE = registerBoolean("pet_friendly_fire", GameRuleCategory.MISC, false);
        RULE_FORTUNE_ANCIENT_DEBRIS = registerBoolean("fortune_ancient_debris", GameRuleCategory.DROPS, false);
        RULE_TORCHFLOWERS_GLOW = registerBoolean("torch_flowers_glow", GameRuleCategory.MISC, true);
        RULE_CACTUS_GROW_HEIGHT = registerInteger("cactus_grow_height", GameRuleCategory.MISC, 3, 1, 100);
        RULE_SUGAR_CANE_GROW_HEIGHT = registerInteger("sugar_cane_grow_height", GameRuleCategory.MISC, 3, 1, 100);
        RULE_PLANTS_GROW_FASTER_IN_RAIN = registerBoolean("plants_grow_faster_in_rain", GameRuleCategory.MISC, true);
    }

    public static GameRule<Boolean> registerBoolean(String name, GameRuleCategory category, boolean defaultValue) {
        return GameRules.registerBoolean(name, category, defaultValue);
    }

    public static GameRule<Integer> registerInteger(String name, GameRuleCategory category, int defaultValue, int minValue) {
        return registerInteger(name, category, defaultValue, minValue, Integer.MAX_VALUE);
    }

    public static GameRule<Integer> registerInteger(String name, GameRuleCategory category, int defaultValue, int minValue, int maxValue) {
        return GameRules.registerInteger(name, category, defaultValue, minValue, maxValue);
    }

    public static void init() {}
}
