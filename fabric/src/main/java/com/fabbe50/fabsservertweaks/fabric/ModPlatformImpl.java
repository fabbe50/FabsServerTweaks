package com.fabbe50.fabsservertweaks.fabric;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.ComposterBlock;

public class ModPlatformImpl {
    public static boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    public static void registerStarter(PotionBrewing.Builder builder, Item ingredient, Holder<Potion> output) {
        builder.registerPotionRecipe(Potions.AWKWARD, Ingredient.of(ingredient), output);
        builder.registerPotionRecipe(Potions.WATER, Ingredient.of(ingredient), Potions.MUNDANE);
    }

    public static void registerPotion(PotionBrewing.Builder builder, Holder<Potion> input, Item ingredient, Holder<Potion> output) {
        builder.registerPotionRecipe(input, Ingredient.of(ingredient), output);
    }

    public static void registerCompostable(float chance, Item item, boolean canVillagerCompost) {
        ComposterBlock.COMPOSTABLES.put(item, chance);
    }

    public static boolean isDataGen() {
        return false;
    }
}
