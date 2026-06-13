package com.fabbe50.fabsservertweaks.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    public record Tier(int minPercent, ItemStackTemplate result, @Nullable Integer cookTime, @Nullable Float xp) {
        public static final MapCodec<Tier> CODEC = RecordCodecBuilder.mapCodec(instance ->
                instance.group(
                        Codec.INT.fieldOf("minPercent").forGetter(Tier::minPercent),
                        ItemStackTemplate.CODEC.fieldOf("result").forGetter(Tier::result),
                        Codec.INT.optionalFieldOf("cookTime").forGetter(tier -> Optional.ofNullable(tier.cookTime())),
                        Codec.FLOAT.optionalFieldOf("xp").forGetter(tier -> Optional.ofNullable(tier.xp()))
                ).apply(instance, (min, result, optionalCookTime, optionalXp) ->
                        new Tier(min, result, optionalCookTime.orElse(null), optionalXp.orElse(null))
                )
        );

        public ItemStack getResult() {
            return result.create();
        }
    }
}
