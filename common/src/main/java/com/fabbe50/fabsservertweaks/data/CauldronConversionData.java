package com.fabbe50.fabsservertweaks.data;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;

public record CauldronConversionData(ItemStackTemplate input, ItemStackTemplate output) {
    public static final MapCodec<CauldronConversionData> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    ItemStackTemplate.CODEC.fieldOf("input").forGetter(CauldronConversionData::input),
                    ItemStackTemplate.CODEC.fieldOf("output").forGetter(CauldronConversionData::output)
            ).apply(instance, CauldronConversionData::new));

    public boolean matches(ItemStack stack) {
        return ItemStack.isSameItemSameComponents(stack, this.input.create());
    }

    public ItemStack getInput() {
        return this.input.create();
    }

    public ItemStack getOutput() {
        return this.output.create();
    }
}
