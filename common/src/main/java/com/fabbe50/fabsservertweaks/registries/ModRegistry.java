package com.fabbe50.fabsservertweaks.registries;

import com.fabbe50.fabsservertweaks.Fabsservertweaks;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public class ModRegistry {
    public static TagKey<EntityType<?>> MOBS_WITH_POTION_EFFECTS_BLACKLIST = TagKey.create(Registries.ENTITY_TYPE, Fabsservertweaks.location("mobs_with_potion_effects_blacklist"));

    public static void init() {}
}
