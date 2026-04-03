package com.fabbe50.fabsservertweaks.util;

import com.fabbe50.fabsservertweaks.Fabsservertweaks;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.repository.Pack;

public enum BuiltinDatapack {
    VANILLA_ENCHANTMENTS_OVERRIDES("vanilla_enchantments_overrides", "Vanilla Enchantments Overrides", Pack.Position.TOP, false, true),
    CUSTOM_ENCHANTMENTS("custom_enchantments", "Custom Enchantments", Pack.Position.TOP, true, true),
    CUSTOM_RECIPES("custom_recipes", "Custom Recipes", Pack.Position.BOTTOM, true, false),
    CAULDRON_CONVERSIONS("cauldron_conversions", "Cauldron Conversions", Pack.Position.TOP, true, false),
    DURABILITY_SMELTING("durability_smelting", "Durability Smelting", Pack.Position.TOP, true, false),
    MOUNT_ARMOR_RECIPES("mount_armor_recipes", "Mount Armor Recipes", Pack.Position.TOP, true, false),
    RAW_BLOCK_SMELTING("raw_block_smelting", "Raw Block Smelting", Pack.Position.TOP, true, false),
    STONE_STAIRS_RECIPES("stone_stairs_recipes", "Stone Stairs Recipes", Pack.Position.TOP, true, false),
    WOOD_OVERRIDE_RECIPES("wood_override_recipes", "Wood Override Recipes", Pack.Position.TOP, true, false),
    COMBINE_SLABS_RECIPES("combine_slabs_recipes", "Combine Slab Recipes", Pack.Position.TOP, true, false),
    POTTERY_SHERD_DUPLICATION("pottery_sherd_duplication", "Pottery Sherd Duplication", Pack.Position.TOP, true, false),
    ORE_MINER("ore_miner", "Ore Miner", Pack.Position.TOP, false, true),
    ;

    private final String path;
    private final String displayName;
    private final Pack.Position position;
    private final boolean enabledByDefault;
    private final boolean requiresRestart;

    BuiltinDatapack(String path, String displayName, Pack.Position position, boolean enabledByDefault, boolean requiresRestart) {
        this.path = path;
        this.displayName = displayName;
        this.position = position;
        this.enabledByDefault = enabledByDefault;
        this.requiresRestart = requiresRestart;
    }

    public String path() {
        return path;
    }

    public Identifier id() {
        return Fabsservertweaks.location(path);
    }

    public String fabricPackId() {
        return id().toString();
    }

    public String neoForgePackId() {
        return "mod/" + id();
    }

    public Component displayName() {
        return Component.literal(Fabsservertweaks.MOD_NAME + " " + displayName);
    }

    public Pack.Position position() {
        return position;
    }

    public boolean enabledByDefault() {
        return enabledByDefault;
    }

    public boolean requiresRestart() {
        return requiresRestart;
    }
}
