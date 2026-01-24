package com.fabbe50.fabsservertweaks.fabsservertweaks.registries;

import com.fabbe50.fabsservertweaks.fabsservertweaks.FabsServerTweaks;
import com.fabbe50.fabsservertweaks.fabsservertweaks.data.loader.CauldronConversionLoader;
import com.fabbe50.fabsservertweaks.fabsservertweaks.data.loader.DurabilitySmeltLoader;
import com.fabbe50.fabsservertweaks.fabsservertweaks.util.LogUtil;
import dev.architectury.registry.ReloadListenerRegistry;
import dev.architectury.registry.registries.Registrar;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.PackType;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;

public class ModRegistry {
    public static final Registrar<Item> ITEMS = FabsServerTweaks.MANAGER.get().get(Registries.ITEM);

    public static final TagKey<EntityType<?>> MOBS_WITH_POTION_EFFECTS_BLACKLIST = TagKey.create(Registries.ENTITY_TYPE, FabsServerTweaks.location("mobs_with_potion_effects_blacklist"));
    public static final TagKey<Item> STACK_16 = TagKey.create(Registries.ITEM, FabsServerTweaks.location("stack_16"));
    public static final TagKey<Item> STACK_64 = TagKey.create(Registries.ITEM, FabsServerTweaks.location("stack_64"));
    public static final TagKey<Block> SCYTHE_ABLE = TagKey.create(Registries.BLOCK, FabsServerTweaks.location("scythe-able"));
    public static final TagKey<Item> IMMUNE_TO_CACTUS_DAMAGE = TagKey.create(Registries.ITEM, FabsServerTweaks.location("immune_to_cactus"));
    public static final TagKey<Block> TREE_CHOPPER_WHITELIST = TagKey.create(Registries.BLOCK, FabsServerTweaks.location("tree_chopper_whitelist"));
    public static final TagKey<Block> TREE_CHOPPER_ATTACHMENTS = TagKey.create(Registries.BLOCK, FabsServerTweaks.location("tree_chopper_attachments"));
    public static final TagKey<Block> SPIDER_NOT_CLIMBABLE = TagKey.create(Registries.BLOCK, FabsServerTweaks.location("spider_not_climbable"));
    public static final TagKey<Block> MOD_BONE_MEALABLE = TagKey.create(Registries.BLOCK, FabsServerTweaks.location("bone_mealable"));

    public static final TagKey<Item> TUBE_CORALS = TagKey.create(Registries.ITEM, FabsServerTweaks.location("c", "tube_corals"));
    public static final TagKey<Item> BRAIN_CORALS = TagKey.create(Registries.ITEM, FabsServerTweaks.location("c", "brain_corals"));
    public static final TagKey<Item> BUBBLE_CORALS = TagKey.create(Registries.ITEM, FabsServerTweaks.location("c", "bubble_corals"));
    public static final TagKey<Item> FIRE_CORALS = TagKey.create(Registries.ITEM, FabsServerTweaks.location("c", "fire_corals"));
    public static final TagKey<Item> HORN_CORALS = TagKey.create(Registries.ITEM, FabsServerTweaks.location("c", "horn_corals"));

    public static final ResourceKey<Enchantment> TREE_CHOPPER = ResourceKey.create(Registries.ENCHANTMENT, FabsServerTweaks.location("tree_chopper"));

    public static void init() {
        LogUtil.log("Setting up registry...");
        ReloadListenerRegistry.register(PackType.SERVER_DATA, CauldronConversionLoader.INSTANCE, FabsServerTweaks.location("cauldron_conversion"));
        ReloadListenerRegistry.register(PackType.SERVER_DATA, DurabilitySmeltLoader.INSTANCE, FabsServerTweaks.location("durability_smelting"));
    }
}
