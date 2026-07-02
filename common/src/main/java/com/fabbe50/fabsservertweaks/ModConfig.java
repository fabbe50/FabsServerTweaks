package com.fabbe50.fabsservertweaks;

import com.fabbe50.fabsservertweaks.registries.gamerules.DifficultyValue.Difficulty;
import com.fabbe50.fabsservertweaks.registries.gamerules.TrampleValue.TrampleMode;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = Fabsservertweaks.MOD_NAME)
public class ModConfig implements ConfigData {
    public TrampleMode cropTrampleMode = TrampleMode.FEATHER_FALLING;
    public TrampleMode eggTrampleMode = TrampleMode.FEATHER_FALLING;
    public Difficulty difficulty = Difficulty.SAME_ON_ALL_DIFFICULTIES;
    public boolean shareSeed = false;
    public boolean overrideNormalLead = true;
    public boolean canLeashAnimals = true;
    public boolean canLeashMonsters = true;
    public boolean canLeashBosses = false;
    public boolean canLeashVillagerTypes = true;
    public boolean canLeashGolems = true;
    public boolean canLeashPets = true;
    public boolean canPistonsPushBlockEntities = true;

    @Comment("The percentage chance for a plant to grow an extra step. Default=20")
    public int plantRainGrowthChance = 20;

    public boolean enableLootrPolymerPlugin = true;
    public boolean enableUniversalOresPlugin = true;
    public boolean enableResourceNetherOresPlugin = true;
}
