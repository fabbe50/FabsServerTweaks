package com.fabbe50.fabsservertweaks;

import com.fabbe50.fabsservertweaks.registries.gamerules.DifficultyValue.Difficulty;
import com.fabbe50.fabsservertweaks.registries.gamerules.TrampleValue.TrampleMode;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

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
}
