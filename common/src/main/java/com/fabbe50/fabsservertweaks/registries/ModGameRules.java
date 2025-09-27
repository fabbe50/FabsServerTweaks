package com.fabbe50.fabsservertweaks.registries;

import com.fabbe50.fabsservertweaks.registries.gamerules.CropTrampleValue;
import com.fabbe50.fabsservertweaks.registries.gamerules.CropTrampleValue.CropTrampleMode;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameRules.BooleanValue;
import net.minecraft.world.level.GameRules.Category;

public class ModGameRules {
    public static final GameRules.Key<BooleanValue> RULE_MOB_GRIEF_ENDERMAN;
    public static final GameRules.Key<BooleanValue> RULE_MOB_GRIEF_CREEPER;
    public static final GameRules.Key<BooleanValue> RULE_MOB_GRIEF_ZOMBIE;
        RULE_MOB_DROP_EQUIPABLE = GameRules.register("mobDropEquipable", Category.DROPS, BooleanValue.create(true));
        RULE_MOB_DROP_FULL_DURABILITY = GameRules.register("mobDropFullDurability", Category.DROPS, BooleanValue.create(false));
        RULE_CROP_TRAMPLE_MODE = GameRules.register("cropTrampleMode", Category.MISC, CropTrampleValue.create(CropTrampleMode.NORMAL));
