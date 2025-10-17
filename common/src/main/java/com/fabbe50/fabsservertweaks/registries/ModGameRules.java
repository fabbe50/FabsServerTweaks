package com.fabbe50.fabsservertweaks.registries;

import com.fabbe50.fabsservertweaks.registries.gamerules.CropTrampleValue;
import com.fabbe50.fabsservertweaks.registries.gamerules.CropTrampleValue.CropTrampleMode;
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
    public static final GameRules.Key<CropTrampleValue> RULE_CROP_TRAMPLE_MODE;
    public static final GameRules.Key<BooleanValue> RULE_SHULKERS_RANDOM_COLOR;
    public static final GameRules.Key<BooleanValue> RULE_SHULKERS_CAN_TELEPORT;
    public static final GameRules.Key<IntegerValue> RULE_SHULKER_SHELL_DROP_AMOUNT;
    public static final GameRules.Key<BooleanValue> RULE_LOYALTY_TRIDENT_RETURNS_FROM_VOID;
    public static final GameRules.Key<BooleanValue> RULE_ALWAYS_CONVERT_VILLAGERS;
    public static final GameRules.Key<BooleanValue> RULE_MOBS_SPAWN_WITH_EFFECTS;
    public static final GameRules.Key<BooleanValue> RULE_SHARE_SEED;

    static {
        RULE_MOB_GRIEF_ENDERMAN = GameRules.register("mobGriefEnderman", Category.MOBS, BooleanValue.create(true));
        RULE_MOB_GRIEF_CREEPER = GameRules.register("mobGriefCreeper", Category.MOBS, BooleanValue.create(true));
        RULE_MOB_GRIEF_ZOMBIE = GameRules.register("mobGriefZombie", Category.MOBS, BooleanValue.create(true));
        RULE_MOB_DROP_EQUIPABLE = GameRules.register("mobDropEquipable", Category.DROPS, BooleanValue.create(true));
        RULE_MOB_DROP_FULL_DURABILITY = GameRules.register("mobDropFullDurability", Category.DROPS, BooleanValue.create(false));
        RULE_CROP_TRAMPLE_MODE = GameRules.register("cropTrampleMode", Category.MISC, CropTrampleValue.create(CropTrampleMode.NORMAL));
        RULE_SHULKERS_RANDOM_COLOR = GameRules.register("shulkerRandomColor", Category.MOBS, BooleanValue.create(false));
        RULE_SHULKERS_CAN_TELEPORT = GameRules.register("shulkersCanTeleport", Category.MOBS, BooleanValue.create(true));
        RULE_SHULKER_SHELL_DROP_AMOUNT = GameRules.register("shulkerShellDropAmount", Category.MOBS, IntegerValue.create(2));
        RULE_LOYALTY_TRIDENT_RETURNS_FROM_VOID = GameRules.register("tridentWithLoyaltyReturnsFromVoid", Category.MISC, BooleanValue.create(true));
        RULE_ALWAYS_CONVERT_VILLAGERS = GameRules.register("villagerAlwaysTurnIntoZombies", Category.MISC, BooleanValue.create(true));
        RULE_MOBS_SPAWN_WITH_EFFECTS = GameRules.register("mobsSpawnWithEffects", Category.MISC, BooleanValue.create(false));
        RULE_SHARE_SEED = GameRules.register("shareSeed", Category.MISC, BooleanValue.create(false));
    }

    public static void init() {}
}
