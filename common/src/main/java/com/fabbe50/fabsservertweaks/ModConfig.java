package com.fabbe50.fabsservertweaks;

import com.fabbe50.fabsservertweaks.registries.gamerules.DifficultyValue.Difficulty;
import com.fabbe50.fabsservertweaks.registries.gamerules.TrampleValue.TrampleMode;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry.BoundedDiscrete;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Category;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.RequiresRestart;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = Fabsservertweaks.MOD_NAME)
public class ModConfig implements ConfigData {
    @Comment("If true, debug messages will be logged.")
    @Category("Debug")
    public boolean debugMode = false;
    @Comment("If true, the event run will be logged.")
    @Category("Debug")
    public boolean debugEventRun = false;
    @Comment("If true, the item stack creation will be logged.")
    @Category("Debug")
    public boolean debugItemStackCreation = false;
    @Comment("If true, experimental features will be enabled. These features may not work as expected and may be removed at any time.")
    @Category("Debug")
    public boolean enableExperimentalFeatures = false;

    @Comment("The trample mode for crops. FEATHER_FALLING would prevent trampling when wearing feather falling. NO_TRAMPLE would prevent trampling all-together. Default=FEATHER_FALLING")
    public TrampleMode cropTrampleMode = TrampleMode.FEATHER_FALLING;
    @Comment("The trample mode for eggs. FEATHER_FALLING would prevent trampling when wearing feather falling. NO_TRAMPLE would prevent trampling all-together. Default=FEATHER_FALLING")
    public TrampleMode eggTrampleMode = TrampleMode.FEATHER_FALLING;
    @Comment("The custom difficulty setting on the server. Used for scaling difficulty in the mod. Default=SAME_ON_ALL_DIFFICULTIES")
    public Difficulty difficulty = Difficulty.SAME_ON_ALL_DIFFICULTIES;
    @Comment("If true, the level seed will be shared with all players.")
    public boolean shareSeed = false;

    @Comment("If true, the mod will warn players about the mod not being installed on the client.")
    public boolean warnPlayersAboutModNotOnClient = false;

    @Comment("If true, the lead item functionality will be overridden by the settings below.")
    @Category("Leads")
    public boolean overrideNormalLead = true;
    @Comment("If true, animals will be able to be leashed.")
    @Category("Leads")
    public boolean canLeashAnimals = true;
    @Comment("If true, monsters will be able to be leashed.")
    @Category("Leads")
    public boolean canLeashMonsters = true;
    @Comment("If true, bosses will be able to be leashed.")
    @Category("Leads")
    public boolean canLeashBosses = false;
    @Comment("If true, villagers and wandering traders will be able to be leashed.")
    @Category("Leads")
    public boolean canLeashVillagerTypes = true;
    @Comment("If true, golems will be able to be leashed.")
    @Category("Leads")
    public boolean canLeashGolems = true;
    @Comment("If true, pets will be able to be leashed.")
    @Category("Leads")
    public boolean canLeashPets = true;
    @Comment("If true, pistons will be able to push block entities like chests and furnaces for example.")
    @Category("Leads")
    public boolean canPistonsPushBlockEntities = true;

    @Comment("If true, tools will fit in bundles. If only installed on the server, the bundle will display as full but will still accept the item.")
    public boolean toolsInBundle = true;

    @Comment("The percentage chance for a plant to grow an extra step. Default=20")
    public int plantRainGrowthChance = 20;

    @Comment("The maximum amount of extra level cost in an anvil. The cost may still exceed this number as it's only affecting the extra cost.")
    public int maxAnvilCost = 100;

    @Comment("The chance in percent that creepers can spawn charged")
    @BoundedDiscrete(min = 0, max = 100)
    public int creeperChargedChance = 10;

    @Comment("If true, the ender dragon will always generate a spawn egg and drop xp like it would on the first defeat.")
    public boolean shouldEnderDragonAlwaysLootLikeFirst = true;

    @Comment("If true, the Lootr Polymer plugin will be enabled. If false, the plugin will be disabled. Requires \"Lootr\" to be installed.")
    @Category("Plugins")
    @RequiresRestart
    public boolean enableLootrPolymerPlugin = true;
    @Comment("If true, the Universal Ores Polymer plugin will be enabled. If false, the plugin will be disabled. Requires \"Universal Ores\" to be installed.")
    @Category("Plugins")
    @RequiresRestart
    public boolean enableUniversalOresPlugin = true;
    @Comment("If true, the Resource Nether Ores Polymer plugin will be enabled. If false, the plugin will be disabled. Requires \"Resource Nether Ores\" to be installed.")
    @Category("Plugins")
    @RequiresRestart
    public boolean enableResourceNetherOresPlugin = true;
}
