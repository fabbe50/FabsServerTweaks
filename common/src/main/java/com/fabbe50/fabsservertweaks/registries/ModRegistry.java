package com.fabbe50.fabsservertweaks.registries;

import com.fabbe50.fabsservertweaks.Fabsservertweaks;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModRegistry {
    public static final TagKey<EntityType<?>> MOBS_WITH_POTION_EFFECTS_BLACKLIST = TagKey.create(Registries.ENTITY_TYPE, Fabsservertweaks.location("mobs_with_potion_effects_blacklist"));
    public static final TagKey<Item> STACK_16 = TagKey.create(Registries.ITEM, Fabsservertweaks.location("stack_16"));
    public static final TagKey<Item> STACK_64 = TagKey.create(Registries.ITEM, Fabsservertweaks.location("stack_64"));
    public static final TagKey<Block> SCYTHE_ABLE = TagKey.create(Registries.BLOCK, Fabsservertweaks.location("scythe-able"));

    public static void init() {}
}
