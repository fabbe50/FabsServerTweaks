package com.fabbe50.fabsservertweaks.data;

import com.fabbe50.fabsservertweaks.registries.ModRegistry;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public record CauldronConversionData(ItemStack input, ItemStack output) {
    public static final MapCodec<CauldronConversionData> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    ItemStack.CODEC.fieldOf("input").forGetter(CauldronConversionData::input),
                    ItemStack.CODEC.fieldOf("output").forGetter(CauldronConversionData::output)
            ).apply(instance, CauldronConversionData::new));

    public static CauldronConversionData fromJson(JsonObject json) {
        ItemStack input = parseStack(json.get("input"));
        ItemStack output = parseStack(json.get("output"));
        return new CauldronConversionData(input, output);
    }

    public boolean matches(ItemStack stack) {
        return ItemStack.isSameItemSameComponents(stack, this.input);
    }

    private static ItemStack parseStack(JsonElement json) {
        if (json.isJsonPrimitive() && json.getAsJsonPrimitive().isString()) {
            Identifier id = Identifier.parse(json.getAsString());
            Item item = ModRegistry.ITEMS.get(id);
            return item == null ? ItemStack.EMPTY : new ItemStack(item);
        }
        return ItemStack.CODEC.parse(JsonOps.INSTANCE, json).getOrThrow();
    }
}
