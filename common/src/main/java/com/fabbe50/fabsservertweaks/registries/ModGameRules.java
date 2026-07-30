package com.fabbe50.fabsservertweaks.registries;

import com.fabbe50.fabsservertweaks.Fabsservertweaks;
import com.fabbe50.fabsservertweaks.data.ExtGameRule;
import com.fabbe50.fabsservertweaks.data.Presets;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.gamerules.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ModGameRules {
    private static final List<ExtGameRule<?>> GAME_RULES = new ArrayList<>();

    private static final Map<Presets, Boolean> BOOLEAN_DEFAULT = Map.of(
            Presets.DEFAULT, true,
            Presets.VANILLA, false,
            Presets.FABS, true
    );
    private static final Map<Presets, Boolean> BOOLEAN_ALL_TRUE = Map.of(
            Presets.DEFAULT, true,
            Presets.VANILLA, true,
            Presets.FABS, true
    );
    private static final Map<Presets, Boolean> BOOLEAN_ALL_FALSE = Map.of(
            Presets.DEFAULT, false,
            Presets.VANILLA, false,
            Presets.FABS, false
    );
    private static final Map<Presets, Boolean> BOOLEAN_DEFAULT_VT = Map.of(
            Presets.DEFAULT, false,
            Presets.VANILLA, true,
            Presets.FABS, false
    );
    private static final Map<Presets, Boolean> BOOLEAN_DEFAULT_FF = Map.of(
            Presets.DEFAULT, true,
            Presets.VANILLA, true,
            Presets.FABS, false
    );
    private static final Map<Presets, Boolean> BOOLEAN_DEFAULT_FT = Map.of(
            Presets.DEFAULT, false,
            Presets.VANILLA, false,
            Presets.FABS, true
    );
    private static final Map<Presets, Integer> INTEGER_SHELL_DROP_DEFAULT = Map.of(
            Presets.DEFAULT, 2,
            Presets.VANILLA, 0,
            Presets.FABS, 2
    );
    private static final Map<Presets, Integer> INTEGER_GROWING_DEFAULT = Map.of(
            Presets.DEFAULT, 3,
            Presets.VANILLA, 3,
            Presets.FABS, 5
    );


    // Mob Griefing Rules
    public static final ExtGameRule<Boolean> RULE_MOB_GRIEF_ENDERMAN;
    public static final ExtGameRule<Boolean> RULE_MOB_GRIEF_CREEPER;
    public static final ExtGameRule<Boolean> RULE_MOB_GRIEF_ZOMBIE;

    // Mob Drops
    public static final ExtGameRule<Boolean> RULE_MOB_DROP_EQUIPABLE;
    public static final ExtGameRule<Boolean> RULE_MOB_DROP_FULL_DURABILITY;
    public static final ExtGameRule<Boolean> RULE_MOB_DROPS_REQUIRE_PLAYER_KILL;
    public static final ExtGameRule<Integer> RULE_SHULKER_SHELL_DROP_AMOUNT;

    // Mob Behavior
    public static final ExtGameRule<Boolean> RULE_FRIENDLY_PHANTOMS;
    public static final ExtGameRule<Boolean> RULE_MOBS_SPAWN_WITH_EFFECTS;
    public static final ExtGameRule<Boolean> RULE_ALWAYS_CONVERT_VILLAGERS;
    public static final ExtGameRule<Boolean> RULE_NO_SOLICITING_SIGN;
    public static final ExtGameRule<Boolean> RULE_SNOW_GOLEMS_SURVIVE_ON_ICE;
    public static final ExtGameRule<Boolean> RULE_SNOW_GOLEMS_GENERATE_SNOW;
    public static final ExtGameRule<Boolean> RULE_PET_FRIENDLY_FIRE;
    public static final ExtGameRule<Boolean> RULE_SHULKERS_CAN_TELEPORT;
    public static final ExtGameRule<Boolean> RULE_ENDERMAN_TELEPORT_INHIBITOR;

    // Block Behavior
    public static final ExtGameRule<Boolean> RULE_TORCHFLOWERS_GLOW;
    public static final ExtGameRule<Integer> RULE_CACTUS_GROW_HEIGHT;
    public static final ExtGameRule<Integer> RULE_SUGAR_CANE_GROW_HEIGHT;
    public static final ExtGameRule<Boolean> RULE_PLANTS_GROW_FASTER_IN_RAIN;
    public static final ExtGameRule<Boolean> RULE_NO_COBBLE_GEN;
    public static final ExtGameRule<Boolean> RULE_STONE_TYPE_GENERATORS;
    public static final ExtGameRule<Boolean> RULE_DISPENSERS_CAN_PLACE_BLOCKS;
    public static final ExtGameRule<Boolean> RULE_WATER_DRIPSTONE_FILL_CAULDRON;
    public static final ExtGameRule<Boolean> RULE_LAVA_DRIPSTONE_FILL_CAULDRON;
    public static final ExtGameRule<Boolean> RULE_RAIN_FILLS_CAULDRON;
    public static final ExtGameRule<Boolean> RULE_SNOW_FILLS_CAULDRON;
    public static final ExtGameRule<Boolean> RULE_BETTER_RAIL_PLACEMENT;
    public static final ExtGameRule<Boolean> RULE_AMETHYST_DOES_DAMAGE;

    // Block Interaction
    public static final ExtGameRule<Boolean> RULE_XP_TO_BOTTLES;
    public static final ExtGameRule<Boolean> RULE_ENCHANTMENT_TRANSFER_TO_BOOKS;
    public static final ExtGameRule<Boolean> RULE_SAFE_CANT_SLEEP;
    public static final ExtGameRule<Boolean> RULE_MODIFY_SPAWNERS;
    public static final ExtGameRule<Boolean> RULE_UNLOCKABLE_VAULTS;
    public static final ExtGameRule<Boolean> RULE_SLEEPING_BAGS_ENABLED;
    public static final ExtGameRule<Boolean> RULE_REPAIRABLE_ANVILS;

    // Block Breaking
    public static final ExtGameRule<Boolean> RULE_FORTUNE_ANCIENT_DEBRIS;
    public static final ExtGameRule<Boolean> RULE_SILK_TOUCHABLE_SPAWNERS;
    public static final ExtGameRule<Boolean> RULE_SILK_TOUCHABLE_TRIAL_SPAWNERS;
    public static final ExtGameRule<Boolean> RULE_SILK_TOUCHABLE_TRIAL_VAULTS;
    public static final ExtGameRule<Boolean> RULE_SILK_TOUCHABLE_AMETHYST_NODES;

    // Item Behavior
    public static final ExtGameRule<Boolean> RULE_LOYALTY_TRIDENT_RETURNS_FROM_VOID;
    public static final ExtGameRule<Boolean> RULE_BETTER_HOES;
    public static final ExtGameRule<Boolean> RULE_BETTER_BONE_MEAL;
    public static final ExtGameRule<Boolean> RULE_PREVENT_HOT_FLOOR_DAMAGE_ITEMS;
    public static final ExtGameRule<Boolean> RULE_TELEPORT_TO_DEATH_POINT_WITH_RECOVERY_COMPASS;

    // Fun
    public static final ExtGameRule<Boolean> RULE_SHULKERS_RANDOM_COLOR;

    // Experimental features
    public static final ExtGameRule<Boolean> RULE_PISTONS_CAN_BREAK_BLOCKS;
    public static final ExtGameRule<Boolean> RULE_BETTER_MOB_LOOT;

    static {
        RULE_MOB_GRIEF_ENDERMAN = registerBoolean("mob_grief_enderman", GameRuleCategory.MOBS, BOOLEAN_DEFAULT_FF);
        RULE_MOB_GRIEF_CREEPER = registerBoolean("mob_grief_creeper", GameRuleCategory.MOBS, BOOLEAN_ALL_TRUE);
        RULE_MOB_GRIEF_ZOMBIE = registerBoolean("mob_grief_zombie", GameRuleCategory.MOBS, BOOLEAN_ALL_TRUE);
        RULE_MOB_DROP_EQUIPABLE = registerBoolean("mob_drop_equipable", GameRuleCategory.DROPS, BOOLEAN_DEFAULT_FF);
        RULE_MOB_DROP_FULL_DURABILITY = registerBoolean("mob_drop_full_durability", GameRuleCategory.DROPS, BOOLEAN_ALL_FALSE);
        RULE_MOB_DROPS_REQUIRE_PLAYER_KILL = registerBoolean("mob_drops_require_player_kill", GameRuleCategory.DROPS, BOOLEAN_ALL_FALSE);
        RULE_SHULKERS_RANDOM_COLOR = registerBoolean("shulker_random_color", GameRuleCategory.MOBS, BOOLEAN_DEFAULT_FT);
        RULE_SHULKERS_CAN_TELEPORT = registerBoolean("shulkers_can_teleport", GameRuleCategory.MOBS, BOOLEAN_ALL_TRUE);
        RULE_SHULKER_SHELL_DROP_AMOUNT = registerInteger("shulker_shell_drop_amount", GameRuleCategory.MOBS, INTEGER_SHELL_DROP_DEFAULT, 0);
        RULE_LOYALTY_TRIDENT_RETURNS_FROM_VOID = registerBoolean("trident_with_loyalty_returns_from_void", GameRuleCategory.MISC, BOOLEAN_DEFAULT);
        RULE_ALWAYS_CONVERT_VILLAGERS = registerBoolean("villager_always_turn_into_zombies", GameRuleCategory.MOBS, BOOLEAN_DEFAULT);
        RULE_MOBS_SPAWN_WITH_EFFECTS = registerBoolean("mobs_spawn_with_effects", GameRuleCategory.MOBS, BOOLEAN_DEFAULT);
        RULE_FRIENDLY_PHANTOMS = registerBoolean("friendly_phantoms", GameRuleCategory.MOBS, BOOLEAN_DEFAULT_FT);
        RULE_NO_COBBLE_GEN = registerBoolean("no_cobble_gen", GameRuleCategory.MISC, BOOLEAN_ALL_FALSE);
        RULE_BETTER_HOES = registerBoolean("better_hoes", GameRuleCategory.MISC, BOOLEAN_DEFAULT);
        RULE_BETTER_BONE_MEAL = registerBoolean("better_bone_meal", GameRuleCategory.MISC, BOOLEAN_DEFAULT);
        RULE_WATER_DRIPSTONE_FILL_CAULDRON = registerBoolean("dripstone_fill_cauldron_water", GameRuleCategory.MISC, BOOLEAN_ALL_TRUE);
        RULE_LAVA_DRIPSTONE_FILL_CAULDRON = registerBoolean("dripstone_fill_cauldron_lava", GameRuleCategory.MISC, BOOLEAN_ALL_TRUE);
        RULE_RAIN_FILLS_CAULDRON = registerBoolean("rain_fills_cauldron", GameRuleCategory.MISC, BOOLEAN_ALL_TRUE);
        RULE_SNOW_FILLS_CAULDRON = registerBoolean("snow_fills_cauldron", GameRuleCategory.MISC, BOOLEAN_ALL_TRUE);
        RULE_BETTER_RAIL_PLACEMENT = registerBoolean("better_rail_placement", GameRuleCategory.MISC, BOOLEAN_DEFAULT_FT);
        RULE_SNOW_GOLEMS_SURVIVE_ON_ICE = registerBoolean("snow_golems_survive_on_ice", GameRuleCategory.MOBS, BOOLEAN_DEFAULT);
        RULE_SNOW_GOLEMS_GENERATE_SNOW = registerBoolean("snow_golems_generate_snow", GameRuleCategory.MOBS, BOOLEAN_ALL_TRUE);
        RULE_NO_SOLICITING_SIGN = registerBoolean("no_soliciting_sign", GameRuleCategory.MOBS, BOOLEAN_DEFAULT);
        RULE_SILK_TOUCHABLE_AMETHYST_NODES = registerBoolean("silk_touchable_amethyst_nodes", GameRuleCategory.MISC, BOOLEAN_DEFAULT_FT);
        RULE_SILK_TOUCHABLE_SPAWNERS = registerBoolean("silk_touchable_spawners", GameRuleCategory.MISC, BOOLEAN_DEFAULT_FT);
        RULE_SILK_TOUCHABLE_TRIAL_SPAWNERS = registerBoolean("silk_touchable_trial_spawners", GameRuleCategory.MISC, BOOLEAN_DEFAULT_FT);
        RULE_SILK_TOUCHABLE_TRIAL_VAULTS = registerBoolean("silk_touchable_trial_vaults", GameRuleCategory.MISC, BOOLEAN_DEFAULT_FT);
        RULE_SLEEPING_BAGS_ENABLED = registerBoolean("sleeping_bags_enabled", GameRuleCategory.MISC, BOOLEAN_DEFAULT);
        RULE_SAFE_CANT_SLEEP = registerBoolean("safe_cant_sleep", GameRuleCategory.MISC, BOOLEAN_ALL_FALSE);
        RULE_PET_FRIENDLY_FIRE = registerBoolean("pet_friendly_fire", GameRuleCategory.MISC, BOOLEAN_DEFAULT);
        RULE_FORTUNE_ANCIENT_DEBRIS = registerBoolean("fortune_ancient_debris", GameRuleCategory.DROPS, BOOLEAN_DEFAULT);
        RULE_TORCHFLOWERS_GLOW = registerBoolean("torch_flowers_glow", GameRuleCategory.MISC, BOOLEAN_DEFAULT);
        RULE_CACTUS_GROW_HEIGHT = registerInteger("cactus_grow_height", GameRuleCategory.MISC, INTEGER_GROWING_DEFAULT, 1, 100);
        RULE_SUGAR_CANE_GROW_HEIGHT = registerInteger("sugar_cane_grow_height", GameRuleCategory.MISC, INTEGER_GROWING_DEFAULT, 1, 100);
        RULE_PLANTS_GROW_FASTER_IN_RAIN = registerBoolean("plants_grow_faster_in_rain", GameRuleCategory.MISC, BOOLEAN_DEFAULT);
        RULE_UNLOCKABLE_VAULTS = registerBoolean("unlockable_vaults", GameRuleCategory.MISC, BOOLEAN_DEFAULT);
        RULE_MODIFY_SPAWNERS = registerBoolean("modify_spawners", GameRuleCategory.MISC, BOOLEAN_DEFAULT);
        RULE_ENCHANTMENT_TRANSFER_TO_BOOKS = registerBoolean("enchantment_transfer_to_books", GameRuleCategory.MISC, BOOLEAN_DEFAULT);
        RULE_XP_TO_BOTTLES = registerBoolean("xp_to_bottles", GameRuleCategory.MISC, BOOLEAN_DEFAULT);
        RULE_AMETHYST_DOES_DAMAGE = registerBoolean("amethyst_does_damage", GameRuleCategory.MOBS, BOOLEAN_DEFAULT);
        RULE_STONE_TYPE_GENERATORS = registerBoolean("stone_type_generators", GameRuleCategory.MISC, BOOLEAN_DEFAULT);
        RULE_DISPENSERS_CAN_PLACE_BLOCKS = registerBoolean("dispensers_can_place_blocks", GameRuleCategory.MISC, BOOLEAN_DEFAULT);
        RULE_PREVENT_HOT_FLOOR_DAMAGE_ITEMS = registerBoolean("prevent_hot_floor_damage_items", GameRuleCategory.MISC, BOOLEAN_DEFAULT);
        RULE_REPAIRABLE_ANVILS = registerBoolean("repairable_anvils", GameRuleCategory.MISC, BOOLEAN_DEFAULT);
        RULE_ENDERMAN_TELEPORT_INHIBITOR = registerBoolean("enderman_teleport_inhibitor", GameRuleCategory.MOBS, BOOLEAN_DEFAULT);
        RULE_TELEPORT_TO_DEATH_POINT_WITH_RECOVERY_COMPASS = registerBoolean("teleport_to_death_point_with_recovery_compass", GameRuleCategory.MISC, BOOLEAN_DEFAULT);
        if (Fabsservertweaks.CONFIG.enableExperimentalFeatures) {
            RULE_PISTONS_CAN_BREAK_BLOCKS = registerBoolean("pistons_can_break_blocks", GameRuleCategory.MISC, BOOLEAN_ALL_FALSE);
            RULE_BETTER_MOB_LOOT = registerBoolean("better_mob_loot", GameRuleCategory.MOBS, BOOLEAN_DEFAULT);
        } else {
            RULE_PISTONS_CAN_BREAK_BLOCKS = null;
            RULE_BETTER_MOB_LOOT = null;
        }
    }

    public static ExtGameRule<Boolean> registerBoolean(String name, GameRuleCategory category, Map<Presets, Boolean> presetDefaults) {
        ExtGameRule<Boolean> gameRule = new ExtGameRule<>(GameRules.registerBoolean(name, category, presetDefaults.get(Presets.DEFAULT)), presetDefaults);
        GAME_RULES.add(gameRule);
        return gameRule;
    }

    public static ExtGameRule<Integer> registerInteger(String name, GameRuleCategory category, Map<Presets, Integer> presetDefaults, int minValue) {
        return registerInteger(name, category, presetDefaults, minValue, Integer.MAX_VALUE);
    }

    public static ExtGameRule<Integer> registerInteger(String name, GameRuleCategory category, Map<Presets, Integer> presetDefaults, int minValue, int maxValue) {
        ExtGameRule<Integer> gameRule = new ExtGameRule<>(GameRules.registerInteger(name, category, presetDefaults.get(Presets.DEFAULT), minValue, maxValue), presetDefaults);
        GAME_RULES.add(gameRule);
        return gameRule;
    }

    public static List<ExtGameRule<?>> getGameRules() {
        return GAME_RULES;
    }

    private static Map<Presets, Boolean> getPresetDefaults(boolean defaultValue, boolean vanillaValue, boolean fabsValue) {
        return Map.of(
                Presets.DEFAULT, defaultValue,
                Presets.VANILLA, vanillaValue,
                Presets.FABS, fabsValue
        );
    }

    private static Map<Presets, Integer> getPresetDefaults(int defaultValue, int vanillaValue, int fabsValue) {
        return Map.of(
                Presets.DEFAULT, defaultValue,
                Presets.VANILLA, vanillaValue,
                Presets.FABS, fabsValue
        );
    }

    public static boolean getGameRuleBoolean(ServerLevel level, ExtGameRule<Boolean> rule) {
        if (rule == null) {
            return false;
        }
        return getGameRuleBoolean(level, rule.getRule());
    }

    public static boolean getGameRuleBoolean(ServerLevel level, GameRule<Boolean> rule) {
        if (rule == null || level == null) {
            return false;
        }
        return level.getGameRules().get(rule);
    }

    public static boolean getGameRuleIntLessThan(ServerLevel level, GameRule<Integer> rule, int lessThan) {
        if (rule == null || level == null) {
            return false;
        }
        return level.getGameRules().get(rule) < lessThan;
    }

    public static boolean getGameRuleIntGreaterThan(ServerLevel level, GameRule<Integer> rule, int greaterThan) {
        if (rule == null || level == null) {
            return false;
        }
        return level.getGameRules().get(rule) > greaterThan;
    }

    public static boolean getGameRuleIntBetween(ServerLevel level, GameRule<Integer> rule, int min, int max) {
        if (rule == null || level == null) {
            return false;
        }
        return level.getGameRules().get(rule) >= min && level.getGameRules().get(rule) <= max;
    }

    public static boolean getGameRuleIntEquals(ServerLevel level, GameRule<Integer> rule, int equals) {
        if (rule == null || level == null) {
            return false;
        }
        return level.getGameRules().get(rule) == equals;
    }

    public static int getGameRuleInteger(ServerLevel level, ExtGameRule<Integer> rule) {
        if (rule == null) {
            return -1;
        }
        return getGameRuleInteger(level, rule.getRule());
    }

    public static int getGameRuleInteger(ServerLevel level, GameRule<Integer> rule) {
        if (rule == null || level == null) {
            return -1;
        }
        return level.getGameRules().get(rule);
    }

    public static void init() {}
}
