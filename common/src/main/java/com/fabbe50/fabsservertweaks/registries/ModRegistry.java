package com.fabbe50.fabsservertweaks.registries;

import com.fabbe50.fabsservertweaks.Fabsservertweaks;
import com.fabbe50.fabsservertweaks.LogUtil;
import com.fabbe50.fabsservertweaks.ModPlatform;
import com.fabbe50.fabsservertweaks.data.loader.CauldronConversionLoader;
import com.fabbe50.fabsservertweaks.data.loader.DurabilitySmeltLoader;
import dev.architectury.platform.Platform;
import dev.architectury.registry.ReloadListenerRegistry;
import dev.architectury.registry.registries.Registrar;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.PackType;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;

public class ModRegistry {
    public static final Registrar<Item> ITEMS = Fabsservertweaks.MANAGER.get().get(Registries.ITEM);

    public static final TagKey<EntityType<?>> MOBS_WITH_POTION_EFFECTS_BLACKLIST = TagKey.create(Registries.ENTITY_TYPE, Fabsservertweaks.location("mobs_with_potion_effects_blacklist"));
    public static final TagKey<EntityType<?>> ANIMALS = TagKey.create(Registries.ENTITY_TYPE, Fabsservertweaks.location("animals"));
    public static final TagKey<EntityType<?>> HOSTILES = TagKey.create(Registries.ENTITY_TYPE, Fabsservertweaks.location("hostiles"));
    public static final TagKey<EntityType<?>> BOSSES = TagKey.create(Registries.ENTITY_TYPE, Fabsservertweaks.location("bosses"));
    public static final TagKey<EntityType<?>> GOLEMS = TagKey.create(Registries.ENTITY_TYPE, Fabsservertweaks.location("golems"));
    public static final TagKey<EntityType<?>> PETS = TagKey.create(Registries.ENTITY_TYPE, Fabsservertweaks.location("pets"));
    public static final TagKey<EntityType<?>> VILLAGER_TYPES = TagKey.create(Registries.ENTITY_TYPE, Fabsservertweaks.location("villager_types"));
    public static final TagKey<EntityType<?>> LEAD_BLACKLIST = TagKey.create(Registries.ENTITY_TYPE, Fabsservertweaks.location("lead_blacklist"));
    public static final TagKey<Item> STACK_16 = TagKey.create(Registries.ITEM, Fabsservertweaks.location("stack_16"));
    public static final TagKey<Item> STACK_64 = TagKey.create(Registries.ITEM, Fabsservertweaks.location("stack_64"));
    public static final TagKey<Item> IMMUNE_TO_CACTUS_DAMAGE = TagKey.create(Registries.ITEM, Fabsservertweaks.location("immune_to_cactus"));
    public static final TagKey<Item> FITS_IN_BUNDLE_16 = TagKey.create(Registries.ITEM, Fabsservertweaks.location("fits_in_bundle_16"));
    public static final TagKey<Item> FITS_IN_BUNDLE_64 = TagKey.create(Registries.ITEM, Fabsservertweaks.location("fits_in_bundle_64"));
    public static final TagKey<Item> MUSHROOM_HEAD_BLOCKS = TagKey.create(Registries.ITEM, Fabsservertweaks.location("mushroom_head_blocks"));
    public static final TagKey<Item> VAULT_KEY = TagKey.create(Registries.ITEM, Fabsservertweaks.location("vault_key"));
    public static final TagKey<Item> BLOCK_DISPENSE_BLACKLIST = TagKey.create(Registries.ITEM, Fabsservertweaks.location("block_dispense_blacklist"));
    public static final TagKey<Item> PREVENTS_HOT_FLOOR_DAMAGE = TagKey.create(Registries.ITEM, Fabsservertweaks.location("prevents_hot_floor_damage"));
    public static final TagKey<Item> INFINITY_COMPATIBLE = TagKey.create(Registries.ITEM, Fabsservertweaks.location("enchantable/infinity_compatible"));
    public static final TagKey<Item> CHEST_PIECES = TagKey.create(Registries.ITEM, Fabsservertweaks.location("enchantable/chest_pieces"));
    public static final TagKey<Item> SOULBOUND_COMPATIBLE = TagKey.create(Registries.ITEM, Fabsservertweaks.location("enchantable/soulbound_compatible"));
    public static final TagKey<Item> SWIFTNESS_COMPATIBLE = TagKey.create(Registries.ITEM, Fabsservertweaks.location("enchantable/swiftness_compatible"));
    public static final TagKey<Block> SCYTHE_ABLE = TagKey.create(Registries.BLOCK, Fabsservertweaks.location("scythe-able"));
    public static final TagKey<Block> TREE_CHOPPER_WHITELIST = TagKey.create(Registries.BLOCK, Fabsservertweaks.location("tree_chopper_whitelist"));
    public static final TagKey<Block> TREE_CHOPPER_ATTACHMENTS = TagKey.create(Registries.BLOCK, Fabsservertweaks.location("tree_chopper_attachments"));
    public static final TagKey<Block> SPIDER_NOT_CLIMBABLE = TagKey.create(Registries.BLOCK, Fabsservertweaks.location("spider_not_climbable"));
    public static final TagKey<Block> MOD_BONE_MEALABLE = TagKey.create(Registries.BLOCK, Fabsservertweaks.location("bone_mealable"));
    public static final TagKey<Block> ORE_MINER_WHITELIST = TagKey.create(Registries.BLOCK, Fabsservertweaks.location("ore_miner_whitelist"));
    public static final TagKey<Block> PISTON_MOVE_OVERRIDE = TagKey.create(Registries.BLOCK, Fabsservertweaks.location("piston_move_override"));
    public static final TagKey<Block> PISTON_PUSH_BLACKLIST = TagKey.create(Registries.BLOCK, Fabsservertweaks.location("piston_push_blacklist"));
    public static final TagKey<Block> PISTON_PUSH_WHITELIST = TagKey.create(Registries.BLOCK, Fabsservertweaks.location("piston_push_whitelist"));
    public static final TagKey<Block> PISTON_BREAKER_RODS = TagKey.create(Registries.BLOCK, Fabsservertweaks.location("piston_breaker_rods"));
    public static final TagKey<Block> FIELD_GROWABLE = TagKey.create(Registries.BLOCK, Fabsservertweaks.location("field_growable"));
    public static final TagKey<Block> HARVESTABLE = TagKey.create(Registries.BLOCK, Fabsservertweaks.location("harvestable"));

    public static final TagKey<Item> TUBE_CORALS = TagKey.create(Registries.ITEM, Fabsservertweaks.location("c", "tube_corals"));
    public static final TagKey<Item> BRAIN_CORALS = TagKey.create(Registries.ITEM, Fabsservertweaks.location("c", "brain_corals"));
    public static final TagKey<Item> BUBBLE_CORALS = TagKey.create(Registries.ITEM, Fabsservertweaks.location("c", "bubble_corals"));
    public static final TagKey<Item> FIRE_CORALS = TagKey.create(Registries.ITEM, Fabsservertweaks.location("c", "fire_corals"));
    public static final TagKey<Item> HORN_CORALS = TagKey.create(Registries.ITEM, Fabsservertweaks.location("c", "horn_corals"));

    public static final ResourceKey<Enchantment> TREE_CHOPPER = ResourceKey.create(Registries.ENCHANTMENT, Fabsservertweaks.location("tree_chopper"));
    public static final ResourceKey<Enchantment> CAPTURING = ResourceKey.create(Registries.ENCHANTMENT, Fabsservertweaks.location("capturing"));
    public static final ResourceKey<Enchantment> HAMMER = ResourceKey.create(Registries.ENCHANTMENT, Fabsservertweaks.location("hammer"));
    public static final ResourceKey<Enchantment> ENDER = ResourceKey.create(Registries.ENCHANTMENT, Fabsservertweaks.location("ender"));
    public static final ResourceKey<Enchantment> ORE_MINER = ResourceKey.create(Registries.ENCHANTMENT, Fabsservertweaks.location("ore_miner"));
    public static final ResourceKey<Enchantment> SOUL_BOUND = ResourceKey.create(Registries.ENCHANTMENT, Fabsservertweaks.location("soul_bound"));
    public static final ResourceKey<Enchantment> ICE_TOUCH = ResourceKey.create(Registries.ENCHANTMENT, Fabsservertweaks.location("ice_touch"));

    public static void init() {
        LogUtil.log("Setting up registry...");
        ReloadListenerRegistry.register(PackType.SERVER_DATA, CauldronConversionLoader.INSTANCE, Fabsservertweaks.location("cauldron_conversion"));
        ReloadListenerRegistry.register(PackType.SERVER_DATA, DurabilitySmeltLoader.INSTANCE, Fabsservertweaks.location("durability_smelting"));
        if (Platform.isFabric() || ModPlatform.isDataGen()) {
            registerCompostables();
        }
    }

    public static void registerCompostables() {
        ModPlatform.registerCompostable(0.5f, Items.ROTTEN_FLESH);
        ModPlatform.registerCompostable(1.0f, Items.POISONOUS_POTATO);
        ModPlatform.registerCompostable(0.3f, Items.SPIDER_EYE);
        ModPlatform.registerCompostable(0.3f, Items.CHORUS_FRUIT);
        ModPlatform.registerCompostable(0.3f, Items.CHORUS_FLOWER);
        ModPlatform.registerCompostable(0.3f, Items.BAMBOO);
    }
}
