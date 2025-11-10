package com.fabbe50.fabsservertweaks.registries;

import com.fabbe50.fabsservertweaks.Fabsservertweaks;
import com.fabbe50.fabsservertweaks.LogUtil;
import com.fabbe50.fabsservertweaks.data.loader.CauldronConversionLoader;
import com.fabbe50.fabsservertweaks.data.loader.DurabilitySmeltLoader;
import dev.architectury.registry.ReloadListenerRegistry;
import dev.architectury.registry.registries.Registrar;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.packs.PackType;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModRegistry {
    public static final Registrar<Item> ITEMS = Fabsservertweaks.MANAGER.get().get(Registries.ITEM);

    public static final TagKey<EntityType<?>> MOBS_WITH_POTION_EFFECTS_BLACKLIST = TagKey.create(Registries.ENTITY_TYPE, Fabsservertweaks.location("mobs_with_potion_effects_blacklist"));
    public static final TagKey<Item> STACK_16 = TagKey.create(Registries.ITEM, Fabsservertweaks.location("stack_16"));
    public static final TagKey<Item> STACK_64 = TagKey.create(Registries.ITEM, Fabsservertweaks.location("stack_64"));
    public static final TagKey<Block> SCYTHE_ABLE = TagKey.create(Registries.BLOCK, Fabsservertweaks.location("scythe-able"));
    public static final TagKey<Item> IMMUNE_TO_CACTUS_DAMAGE = TagKey.create(Registries.ITEM, Fabsservertweaks.location("immune_to_cactus"));
    public static final TagKey<Block> SPIDER_NOT_CLIMBABLE = TagKey.create(Registries.BLOCK, Fabsservertweaks.location("spider_not_climbable"));

    public static void init() {
        LogUtil.log("Setting up registry...");
        ReloadListenerRegistry.register(PackType.SERVER_DATA, CauldronConversionLoader.INSTANCE, Fabsservertweaks.location("cauldron_conversion"));
        ReloadListenerRegistry.register(PackType.SERVER_DATA, DurabilitySmeltLoader.INSTANCE, Fabsservertweaks.location("durability_smelting"));
    }
}
