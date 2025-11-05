package com.fabbe50.fabsservertweaks.neoforge.datagen;

import com.fabbe50.fabsservertweaks.Fabsservertweaks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class Recipes extends RecipeProvider {
    protected Recipes(HolderLookup.Provider provider, RecipeOutput recipeOutput) {
        super(provider, recipeOutput);
    }

    public static class Runner extends RecipeProvider.Runner {
        protected Runner(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> completableFuture) {
            super(packOutput, completableFuture);
        }

        @Override
        protected @NotNull RecipeProvider createRecipeProvider(HolderLookup.@NotNull Provider provider, @NotNull RecipeOutput recipeOutput) {
            return new Recipes(provider, recipeOutput);
        }

        @Override
        public @NotNull String getName() {
            return "FabsServerTweaksRecipeProvider";
        }
    }

    @Override
    protected void buildRecipes() {
        this.shapeless(RecipeCategory.MISC, Items.CALCITE, 8)
                .requires(Items.COAL_BLOCK, 4)
                .requires(Items.BONE_BLOCK, 4)
                .requires(Items.SNOW_BLOCK)
                .group(Fabsservertweaks.MOD_ID + "_calcite")
                .unlockedBy("has_calcite", has(Items.CALCITE))
                .save(this.output, Fabsservertweaks.MOD_ID + "_mass_calcite");
        this.shapeless(RecipeCategory.MISC, Items.CALCITE, 2)
                .requires(Items.COAL_BLOCK)
                .requires(Items.BONE_BLOCK)
                .requires(Items.SNOWBALL)
                .group(Fabsservertweaks.MOD_ID + "_calcite")
                .unlockedBy("has_calcite", has(Items.CALCITE))
                .save(this.output, Fabsservertweaks.MOD_ID + "_calcite");
    }
}
