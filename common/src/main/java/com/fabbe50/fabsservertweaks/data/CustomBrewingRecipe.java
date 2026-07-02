package com.fabbe50.fabsservertweaks.data;

import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;

public record CustomBrewingRecipe(Item inputItem, Holder<Potion> inputPotion, Item ingredient, Item outputItem, Holder<Potion> outputPotion) {
}
