package com.fabbe50.fabsservertweaks.data;

import com.fabbe50.fabsservertweaks.ModPlatform;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;

import java.util.HashSet;
import java.util.Set;

public class PotionBrewingRecipes {
    private static final Set<CustomBrewingRecipe> brewingRecipes = new HashSet<>();

    public static void register(PotionBrewing.Builder builder) {
        registerStarter(builder, Items.ROTTEN_FLESH, Potions.POISON);
        registerStarter(builder, Items.POISONOUS_POTATO, Potions.POISON);
    }

    private static void registerStarter(PotionBrewing.Builder builder, Item ingredient, Holder<Potion> output) {
        addBrewingRecipe(new CustomBrewingRecipe(Items.POTION, Potions.AWKWARD, ingredient, Items.POTION, output));
        addBrewingRecipe(new CustomBrewingRecipe(Items.SPLASH_POTION, Potions.AWKWARD, ingredient, Items.SPLASH_POTION, output));
        addBrewingRecipe(new CustomBrewingRecipe(Items.LINGERING_POTION, Potions.AWKWARD, ingredient, Items.LINGERING_POTION, output));
        ModPlatform.registerStarter(builder, ingredient, output);
    }

    private static void registerPotionRecipe(PotionBrewing.Builder builder, Holder<Potion> input, Item ingredient, Holder<Potion> output) {
        ModPlatform.registerPotion(builder, input, ingredient, output);
    }

    private static void addBrewingRecipe(CustomBrewingRecipe brewingRecipe) {
        brewingRecipes.add(brewingRecipe);
    }

    public static Set<CustomBrewingRecipe> getBrewingRecipes() {
        return brewingRecipes;
    }
}
