package com.fabbe50.fabsservertweaks.data;

import com.fabbe50.fabsservertweaks.registries.ModRegistry;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderSet;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public record DurabilitySmeltData(Ingredient ingredient, Set<String> furnaceType, Integer defaultCookTime, Float defaultXp, List<Tier> tiers) {
    public static final Codec<Set<String>> STRING_SET_CODEC =
            Codec.STRING.listOf().xmap(
                    LinkedHashSet::new,
                    List::copyOf
            );

    public static final MapCodec<DurabilitySmeltData> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Ingredient.CODEC.fieldOf("ingredient").forGetter(DurabilitySmeltData::ingredient),
                    STRING_SET_CODEC.fieldOf("furnaceType").forGetter(DurabilitySmeltData::furnaceType),
                    Codec.INT.fieldOf("defaultCookTime").forGetter(DurabilitySmeltData::defaultCookTime),
                    Codec.FLOAT.fieldOf("defaultXp").forGetter(DurabilitySmeltData::defaultXp),
                    Codec.list(Tier.CODEC.codec()).fieldOf("tiers").forGetter(DurabilitySmeltData::tiers)
            ).apply(instance, DurabilitySmeltData::new)
    );

    public static DurabilitySmeltData fromJson(JsonObject json) {
        String resourceString = json.get("ingredient").toString();
        resourceString = resourceString.replaceAll("\"", "");
        Ingredient ingredient1 = Ingredient.of(HolderSet.direct(ModRegistry.ITEMS.getHolder(Identifier.parse(resourceString))));
        Set<String> furnaceTypes = STRING_SET_CODEC.parse(JsonOps.INSTANCE, json.getAsJsonArray("furnaceType")).getOrThrow();
        Integer defaultCookTime = json.get("defaultCookTime").getAsInt();
        Float defaultXp = json.getAsJsonPrimitive("defaultXp").getAsFloat();
        JsonArray tierArray = json.getAsJsonArray("tiers");
        List<Tier> tiers1 = new ArrayList<>();
        for (JsonElement tierLocation : tierArray.asList()) {
            tiers1.add(Tier.fromJson(tierLocation.getAsJsonObject()));
        }
        return new DurabilitySmeltData(ingredient1, furnaceTypes, defaultCookTime, defaultXp, tiers1);
    }

    public record Tier(int minPercent, ItemStack result, @Nullable Integer cookTime, @Nullable Float xp) {
        public static final MapCodec<Tier> CODEC = RecordCodecBuilder.mapCodec(instance ->
                instance.group(
                        Codec.INT.fieldOf("minPercent").forGetter(Tier::minPercent),
                        ItemStack.CODEC.fieldOf("result").forGetter(Tier::result),
                        Codec.INT.optionalFieldOf("cookTime").forGetter(tier -> Optional.ofNullable(tier.cookTime())),
                        Codec.FLOAT.optionalFieldOf("xp").forGetter(tier -> Optional.ofNullable(tier.xp()))
                ).apply(instance, (min, result, optionalCookTime, optionalXp) ->
                        new Tier(min, result, optionalCookTime.orElse(null), optionalXp.orElse(null))
                )
        );

        public static Tier fromJson(JsonObject json) {
            int minPercent = json.get("minPercent").getAsInt();
            ItemStack result = ItemStack.CODEC.parse(JsonOps.INSTANCE, json.getAsJsonObject("result")).getOrThrow();
            Integer cookTime = json.has("cookTime") ? json.get("cookTime").getAsInt() : null;
            Float xp = json.has("xp") ? json.get("xp").getAsFloat() : null;
            return new Tier(minPercent, result, cookTime, xp);
        }
    }
}
