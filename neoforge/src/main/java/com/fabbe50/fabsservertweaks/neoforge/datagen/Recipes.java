package com.fabbe50.fabsservertweaks.neoforge.datagen;

import com.fabbe50.fabsservertweaks.Fabsservertweaks;
import com.fabbe50.fabsservertweaks.registries.ModRegistry;
import com.mojang.datafixers.util.Pair;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
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
        shapeless(RecipeCategory.MISC, Items.CALCITE, 8, "mass", "calcite", "has_calcite",
                new ModIngredient(Items.COAL_BLOCK, 4),
                new ModIngredient(Items.BONE_BLOCK, 4),
                new ModIngredient(Items.SNOW_BLOCK)
        );
        shapeless(RecipeCategory.MISC, Items.CALCITE, 2, "calcite", "has_calcite",
                new ModIngredient(Items.COAL_BLOCK),
                new ModIngredient(Items.BONE_BLOCK),
                new ModIngredient(Items.SNOWBALL)
        );
        shapeless(RecipeCategory.MISC, Items.GREEN_DYE, 2, "green_dye", "has_green_dye_ingredients",
                new ModIngredient(Items.BLUE_DYE),
                new ModIngredient(Items.YELLOW_DYE)
        );
        shapeless(RecipeCategory.MISC, Items.BROWN_DYE, 3, "brown_dye", "has_brown_dye_ingredients",
                new ModIngredient(Items.BLUE_DYE),
                new ModIngredient(Items.YELLOW_DYE),
                new ModIngredient(Items.RED_DYE)
        );
        shapeless(RecipeCategory.MISC, Items.LIGHT_GRAY_DYE, 2, "light_gray_dye", "has_light_gray_dye_ingredients",
                new ModIngredient(Items.LIGHT_BLUE_DYE),
                new ModIngredient(Items.RED_DYE)
        );
        shapeless(RecipeCategory.MISC, Items.MAGENTA_DYE, 3, "magenta_dye", "has_magenta_dye_ingredients",
                new ModIngredient(Items.PURPLE_DYE),
                new ModIngredient(Items.WHITE_DYE),
                new ModIngredient(Items.RED_DYE)
        );
        shapeless(RecipeCategory.MISC, Items.CYAN_DYE, 3, "cyan_dye", "has_cyan_dye_ingredients",
                new ModIngredient(Items.BLUE_DYE, 2),
                new ModIngredient(Items.YELLOW_DYE)
        );
        shapeless(RecipeCategory.MISC, Items.CLAY, 2, "clay", "has_dirt_and_sand",
                new ModIngredient(Items.DIRT),
                new ModIngredient(Items.SAND)
        );
        shapeless(RecipeCategory.MISC, Items.MAGENTA_DYE, 2, "spore_blossom", "magenta_dye", "has_spore_blossom",
                new ModIngredient(Items.SPORE_BLOSSOM)
        );
        shapeless9x9(RecipeCategory.MISC, Items.TUBE_CORAL_BLOCK, "has_tube_coral_block", ModRegistry.TUBE_CORALS);
        shapeless9x9(RecipeCategory.MISC, Items.BRAIN_CORAL_BLOCK, "has_brain_coral_block", ModRegistry.BRAIN_CORALS);
        shapeless9x9(RecipeCategory.MISC, Items.BUBBLE_CORAL_BLOCK, "has_bubble_coral_block", ModRegistry.BUBBLE_CORALS);
        shapeless9x9(RecipeCategory.MISC, Items.FIRE_CORAL_BLOCK, "has_fire_coral_block", ModRegistry.FIRE_CORALS);
        shapeless9x9(RecipeCategory.MISC, Items.HORN_CORAL_BLOCK, "has_horn_coral_block", ModRegistry.HORN_CORALS);

        woodOverrides();
        stoneTypeStairOverrides();
        potterySherdRecipes();
    }

    private final List<RecipeInfo> materialPairs = List.of(
            makeStairRecipe(Items.ANDESITE_STAIRS, Ingredient.of(Items.ANDESITE), "andesite", has(Items.ANDESITE), "andesite"),
            makeStairRecipe(Items.BAMBOO_MOSAIC_STAIRS, Ingredient.of(Items.BAMBOO_MOSAIC), "bamboo_mosaic", has(Items.BAMBOO_MOSAIC), "bamboo_mosaic"),
            makeStairRecipe(Items.BLACKSTONE_STAIRS, Ingredient.of(Items.BLACKSTONE), "blackstone", has(Items.BLACKSTONE), "blackstone"),
            makeStairRecipe(Items.BRICK_STAIRS, Ingredient.of(Items.BRICKS), "bricks", has(Items.BRICKS), "brick"),
            makeStairRecipe(Items.COBBLED_DEEPSLATE_STAIRS, Ingredient.of(Items.COBBLED_DEEPSLATE), "cobbled_deepslate", has(Items.COBBLED_DEEPSLATE), "cobbled_deepslate"),
            makeStairRecipe(Items.COBBLESTONE_STAIRS, Ingredient.of(Items.COBBLESTONE), "cobblestone", has(Items.COBBLESTONE), "cobblestone"),
            makeStairRecipe(Items.CUT_COPPER_STAIRS, Ingredient.of(Items.CUT_COPPER), "cut_copper", has(Items.CUT_COPPER), "cut_copper"),
            makeStairRecipe(Items.DARK_PRISMARINE_STAIRS, Ingredient.of(Items.DARK_PRISMARINE), "dark_prismarine", has(Items.DARK_PRISMARINE), "dark_prismarine"),
            makeStairRecipe(Items.DEEPSLATE_BRICK_STAIRS, Ingredient.of(Items.DEEPSLATE_BRICKS), "deepslate_bricks", has(Items.DEEPSLATE_BRICKS), "deepslate_brick"),
            makeStairRecipe(Items.DEEPSLATE_TILE_STAIRS, Ingredient.of(Items.DEEPSLATE_TILES), "deepslate_tiles", has(Items.DEEPSLATE_TILES), "deepslate_tile"),
            makeStairRecipe(Items.DIORITE_STAIRS, Ingredient.of(Items.DIORITE), "diorite", has(Items.DIORITE), "diorite"),
            makeStairRecipe(Items.END_STONE_BRICK_STAIRS, Ingredient.of(Items.END_STONE_BRICKS), "end_stone_bricks", has(Items.END_STONE_BRICKS), "end_stone_brick"),
            makeStairRecipe(Items.EXPOSED_CUT_COPPER_STAIRS, Ingredient.of(Items.EXPOSED_CUT_COPPER), "exposed_cut_copper", has(Items.EXPOSED_CUT_COPPER), "exposed_cut_copper"),
            makeStairRecipe(Items.GRANITE_STAIRS, Ingredient.of(Items.GRANITE), "granite", has(Items.GRANITE), "granite"),
            makeStairRecipe(Items.MOSSY_COBBLESTONE_STAIRS, Ingredient.of(Items.MOSSY_COBBLESTONE), "mossy_cobblestone", has(Items.MOSSY_COBBLESTONE), "mossy_cobblestone"),
            makeStairRecipe(Items.MOSSY_STONE_BRICK_STAIRS, Ingredient.of(Items.MOSSY_STONE_BRICKS), "mossy_stone_bricks", has(Items.MOSSY_STONE_BRICKS), "mossy_stone_brick"),
            makeStairRecipe(Items.MUD_BRICK_STAIRS, Ingredient.of(Items.MUD_BRICKS), "mud_bricks", has(Items.MUD_BRICKS), "mud_brick"),
            makeStairRecipe(Items.NETHER_BRICK_STAIRS, Ingredient.of(Items.NETHER_BRICK), "nether_bricks", has(Items.NETHER_BRICK), "nether_brick"),
            makeStairRecipe(Items.OXIDIZED_CUT_COPPER_STAIRS, Ingredient.of(Items.OXIDIZED_CUT_COPPER), "oxidized_cut_copper", has(Items.OXIDIZED_CUT_COPPER), "oxidized_cut_copper"),
            makeStairRecipe(Items.POLISHED_ANDESITE_STAIRS, Ingredient.of(Items.POLISHED_ANDESITE), "polished_andesite", has(Items.POLISHED_ANDESITE), "polished_andesite"),
            makeStairRecipe(Items.POLISHED_BLACKSTONE_BRICK_STAIRS, Ingredient.of(Items.POLISHED_BLACKSTONE_BRICKS), "polished_blackstone_bricks", has(Items.POLISHED_BLACKSTONE_BRICKS), "polished_blackstone_brick"),
            makeStairRecipe(Items.POLISHED_BLACKSTONE_STAIRS, Ingredient.of(Items.POLISHED_BLACKSTONE), "polished_blackstone", has(Items.POLISHED_BLACKSTONE), "polished_blackstone"),
            makeStairRecipe(Items.POLISHED_DEEPSLATE_STAIRS, Ingredient.of(Items.POLISHED_DEEPSLATE), "polished_deepslate", has(Items.POLISHED_DEEPSLATE), "polished_deepslate"),
            makeStairRecipe(Items.POLISHED_DIORITE_STAIRS, Ingredient.of(Items.POLISHED_DIORITE), "polished_diorite", has(Items.POLISHED_DIORITE), "polished_diorite"),
            makeStairRecipe(Items.POLISHED_GRANITE_STAIRS, Ingredient.of(Items.POLISHED_GRANITE), "polished_granite", has(Items.POLISHED_GRANITE), "polished_granite"),
            makeStairRecipe(Items.POLISHED_TUFF_STAIRS, Ingredient.of(Items.POLISHED_TUFF), "polished_tuff", has(Items.POLISHED_TUFF), "polished_tuff"),
            makeStairRecipe(Items.PRISMARINE_BRICK_STAIRS, Ingredient.of(Items.PRISMARINE_BRICKS), "prismarine_bricks", has(Items.PRISMARINE_BRICKS), "prismarine_brick"),
            makeStairRecipe(Items.PRISMARINE_STAIRS, Ingredient.of(Items.PRISMARINE), "prismarine", has(Items.PRISMARINE), "prismarine"),
            makeStairRecipe(Items.PURPUR_STAIRS, Ingredient.of(Items.PURPUR_BLOCK, Items.PURPUR_PILLAR), "purpur", has(Items.PURPUR_BLOCK, Items.PURPUR_PILLAR), "purpur"),
            makeStairRecipe(Items.QUARTZ_STAIRS, Ingredient.of(Items.CHISELED_QUARTZ_BLOCK, Items.QUARTZ_BLOCK, Items.QUARTZ_PILLAR), "quartz", has(Items.CHISELED_QUARTZ_BLOCK, Items.QUARTZ_BLOCK, Items.QUARTZ_PILLAR), "quartz"),
            makeStairRecipe(Items.RED_NETHER_BRICK_STAIRS, Ingredient.of(Items.RED_NETHER_BRICKS), "red_nether_bricks", has(Items.RED_NETHER_BRICKS), "red_nether_brick"),
            makeStairRecipe(Items.RED_SANDSTONE_STAIRS, Ingredient.of(Items.RED_SANDSTONE, Items.CHISELED_RED_SANDSTONE, Items.CUT_RED_SANDSTONE), "red_sandstone", has(Items.SANDSTONE, Items.CHISELED_RED_SANDSTONE, Items.CUT_RED_SANDSTONE), "red_sandstone"),
            makeStairRecipe(Items.RESIN_BRICK_STAIRS, Ingredient.of(Items.RESIN_BRICKS), "resin_bricks", has(Items.RESIN_BRICKS), "resin_brick"),
            makeStairRecipe(Items.SANDSTONE_STAIRS, Ingredient.of(Items.SANDSTONE, Items.CHISELED_SANDSTONE, Items.CUT_SANDSTONE), "sandstone", has(Items.SANDSTONE, Items.CHISELED_SANDSTONE, Items.CUT_SANDSTONE), "sandstone"),
            makeStairRecipe(Items.SMOOTH_QUARTZ_STAIRS, Ingredient.of(Items.SMOOTH_QUARTZ), "smooth_quartz", has(Items.SMOOTH_QUARTZ), "smooth_quartz"),
            makeStairRecipe(Items.SMOOTH_RED_SANDSTONE_STAIRS, Ingredient.of(Items.SMOOTH_RED_SANDSTONE), "smooth_red_sandstone", has(Items.SMOOTH_RED_SANDSTONE), "smooth_red_sandstone"),
            makeStairRecipe(Items.SMOOTH_SANDSTONE_STAIRS, Ingredient.of(Items.SMOOTH_SANDSTONE), "smooth_sandstone", has(Items.SMOOTH_SANDSTONE), "smooth_sandstone"),
            makeStairRecipe(Items.STONE_BRICK_STAIRS, Ingredient.of(Items.STONE_BRICKS), "stone_bricks", has(Items.STONE_BRICKS), "stone_brick"),
            makeStairRecipe(Items.STONE_STAIRS, Ingredient.of(Items.STONE), "stone", has(Items.STONE), "stone"),
            makeStairRecipe(Items.TUFF_STAIRS, Ingredient.of(Items.TUFF), "tuff", has(Items.TUFF), "tuff"),
            makeStairRecipe(Items.TUFF_BRICK_STAIRS, Ingredient.of(Items.TUFF_BRICKS), "tuff_bricks", has(Items.TUFF_BRICKS), "tuff_brick"),
            makeStairRecipe(Items.WAXED_CUT_COPPER_STAIRS, Ingredient.of(Items.WAXED_CUT_COPPER), "waxed_cut_copper", has(Items.WAXED_CUT_COPPER), "waxed_cut_copper"),
            makeStairRecipe(Items.WAXED_EXPOSED_CUT_COPPER_STAIRS, Ingredient.of(Items.WAXED_EXPOSED_CUT_COPPER), "waxed_exposed_cut_copper", has(Items.WAXED_EXPOSED_CUT_COPPER), "waxed_exposed_cut_copper"),
            makeStairRecipe(Items.WAXED_OXIDIZED_CUT_COPPER_STAIRS, Ingredient.of(Items.WAXED_OXIDIZED_CUT_COPPER), "waxed_oxidized_cut_copper", has(Items.WAXED_OXIDIZED_CUT_COPPER), "waxed_oxidized_cut_copper"),
            makeStairRecipe(Items.WAXED_WEATHERED_CUT_COPPER_STAIRS, Ingredient.of(Items.WAXED_WEATHERED_CUT_COPPER), "waxed_weathered_cut_copper", has(Items.WAXED_WEATHERED_CUT_COPPER), "waxed_weathered_cut_copper"),
            makeStairRecipe(Items.WEATHERED_CUT_COPPER_STAIRS, Ingredient.of(Items.WEATHERED_CUT_COPPER), "weathered_cut_copper", has(Items.WEATHERED_CUT_COPPER), "weathered_cut_copper")
    );
    private void stoneTypeStairOverrides() {
        for (RecipeInfo recipeInfo : materialPairs) {
            stairRecipe(recipeInfo.item(), recipeInfo.ingredient(), recipeInfo.unlockedByName(), recipeInfo.trigger(), recipeInfo.recipeName());
        }
    }

    private void woodOverrides() {
        woodStairRecipe(Items.OAK_STAIRS, Items.OAK_PLANKS, "wooden", "oak");
        woodStairRecipe(Items.SPRUCE_STAIRS, Items.SPRUCE_PLANKS, "wooden", "spruce");
        woodStairRecipe(Items.BIRCH_STAIRS, Items.BIRCH_PLANKS, "wooden", "birch");
        woodStairRecipe(Items.JUNGLE_STAIRS, Items.JUNGLE_PLANKS, "wooden", "jungle");
        woodStairRecipe(Items.ACACIA_STAIRS, Items.ACACIA_PLANKS, "wooden", "acacia");
        woodStairRecipe(Items.DARK_OAK_STAIRS, Items.DARK_OAK_PLANKS, "wooden", "dark_oak");
        woodStairRecipe(Items.MANGROVE_STAIRS, Items.MANGROVE_PLANKS, "wooden", "mangrove");
        woodStairRecipe(Items.CHERRY_STAIRS, Items.CHERRY_PLANKS, "wooden", "cherry");
        woodStairRecipe(Items.PALE_OAK_STAIRS, Items.PALE_OAK_PLANKS, "wooden", "pale_oak");
        woodStairRecipe(Items.CRIMSON_STAIRS, Items.CRIMSON_PLANKS, "wooden", "crimson");
        woodStairRecipe(Items.WARPED_STAIRS, Items.WARPED_PLANKS, "wooden", "warped");
        woodStairRecipe(Items.BAMBOO_STAIRS, Items.BAMBOO_PLANKS, "wooden", "bamboo");
        trapdoorRecipe(Items.OAK_TRAPDOOR, Items.OAK_PLANKS, "wooden", "oak");
        trapdoorRecipe(Items.SPRUCE_TRAPDOOR, Items.SPRUCE_PLANKS, "wooden", "spruce");
        trapdoorRecipe(Items.BIRCH_TRAPDOOR, Items.BIRCH_PLANKS, "wooden", "birch");
        trapdoorRecipe(Items.JUNGLE_TRAPDOOR, Items.JUNGLE_PLANKS, "wooden", "jungle");
        trapdoorRecipe(Items.ACACIA_TRAPDOOR, Items.ACACIA_PLANKS, "wooden", "acacia");
        trapdoorRecipe(Items.DARK_OAK_TRAPDOOR, Items.DARK_OAK_PLANKS, "wooden", "dark_oak");
        trapdoorRecipe(Items.MANGROVE_TRAPDOOR, Items.MANGROVE_PLANKS, "wooden", "mangrove");
        trapdoorRecipe(Items.CHERRY_TRAPDOOR, Items.CHERRY_PLANKS, "wooden", "cherry");
        trapdoorRecipe(Items.PALE_OAK_TRAPDOOR, Items.PALE_OAK_PLANKS, "wooden", "pale_oak");
        trapdoorRecipe(Items.CRIMSON_TRAPDOOR, Items.CRIMSON_PLANKS, "wooden", "crimson");
        trapdoorRecipe(Items.WARPED_TRAPDOOR, Items.WARPED_PLANKS, "wooden", "warped");
        trapdoorRecipe(Items.BAMBOO_TRAPDOOR, Items.BAMBOO_PLANKS, "wooden", "bamboo");
    }

    private final List<Item> sherds = List.of(
            Items.ANGLER_POTTERY_SHERD,
            Items.ARCHER_POTTERY_SHERD,
            Items.ARMS_UP_POTTERY_SHERD,
            Items.BLADE_POTTERY_SHERD,
            Items.BREWER_POTTERY_SHERD,
            Items.BURN_POTTERY_SHERD,
            Items.DANGER_POTTERY_SHERD,
            Items.EXPLORER_POTTERY_SHERD,
            Items.FLOW_POTTERY_SHERD,
            Items.FRIEND_POTTERY_SHERD,
            Items.GUSTER_POTTERY_SHERD,
            Items.HEART_POTTERY_SHERD,
            Items.HEARTBREAK_POTTERY_SHERD,
            Items.HOWL_POTTERY_SHERD,
            Items.MINER_POTTERY_SHERD,
            Items.MOURNER_POTTERY_SHERD,
            Items.PLENTY_POTTERY_SHERD,
            Items.PRIZE_POTTERY_SHERD,
            Items.SCRAPE_POTTERY_SHERD,
            Items.SHEAF_POTTERY_SHERD,
            Items.SHELTER_POTTERY_SHERD,
            Items.SKULL_POTTERY_SHERD,
            Items.SNORT_POTTERY_SHERD
    );
    private void potterySherdRecipes() {
        for (Item sherd : sherds) {
            String recipeName = sherd.getDescriptionId();
            recipeName = recipeName.substring(recipeName.lastIndexOf(".") + 1);
            shapeless(RecipeCategory.MISC, sherd, 2, recipeName,
                    new ModIngredient(sherd),
                    new ModIngredient(Items.CLAY_BALL, 2)
            );
        }
    }

    private void shapeless(RecipeCategory recipeCategory, Item result, int resultAmount, String group, String unlockedByName, ModIngredient... modIngredients) {
        shapeless(recipeCategory, result, resultAmount, "", group, unlockedByName, modIngredients);
    }

    private void shapeless(RecipeCategory recipeCategory, Item result, int resultAmount, String prefix, String group, String unlockedByName, ModIngredient... modIngredients) {
        ShapelessRecipeBuilder recipeBuilder = this.shapeless(recipeCategory, result, resultAmount);
        for (ModIngredient modIngredient : modIngredients) {
            recipeBuilder.requires(modIngredient.item(), modIngredient.amount());
        }
        recipeBuilder.group(Fabsservertweaks.MOD_ID + "_" + group);
        recipeBuilder.unlockedBy(unlockedByName, has(Arrays.stream(modIngredients).map(ModIngredient::item).toArray(ItemLike[]::new)));
        recipeBuilder.save(this.output, Fabsservertweaks.MOD_ID + (!prefix.isEmpty() ? "_" + prefix : "") + "_" + group);
    }

    private void shapeless(RecipeCategory recipeCategory, Item result, int resultAmount, String unlockedByName, ModIngredient... modIngredients) {
        ShapelessRecipeBuilder recipeBuilder = this.shapeless(recipeCategory, result, resultAmount);
        for (ModIngredient modIngredient : modIngredients) {
            recipeBuilder.requires(modIngredient.item(), modIngredient.amount());
        }
        recipeBuilder.unlockedBy(unlockedByName, has(Arrays.stream(modIngredients).map(ModIngredient::item).toArray(ItemLike[]::new)));
        recipeBuilder.save(this.output, Fabsservertweaks.MOD_ID + "_" + unlockedByName);
    }

    private void shapeless9x9(RecipeCategory recipeCategory, Item result, String unlockedByName, TagKey<Item> input) {
        String recipeName = result.getDescriptionId();
        recipeName = recipeName.substring(recipeName.lastIndexOf(".") + 1);
        this.shapeless(recipeCategory, result)
                .requires(input)
                .requires(input)
                .requires(input)
                .requires(input)
                .requires(input)
                .requires(input)
                .requires(input)
                .requires(input)
                .requires(input)
                .unlockedBy(unlockedByName, has(input))
                .save(this.output, Fabsservertweaks.MOD_ID + "_" + recipeName);
    }

    private RecipeInfo makeStairRecipe(Item item, Ingredient ingredient, String unlockedBy, Criterion<InventoryChangeTrigger.TriggerInstance> trigger, String recipeName) {
        return new RecipeInfo(item, ingredient, unlockedBy, trigger, recipeName);
    }

    private void woodStairRecipe(ItemLike item, Item ingredient, String groupPrefix, String recipeName) {
        stairRecipe(item, Ingredient.of(ingredient), "has_planks", has(ItemTags.PLANKS), groupPrefix, recipeName);
    }

    private void stairRecipe(ItemLike item, Ingredient ingredient, String unlockedBy, Criterion<InventoryChangeTrigger.TriggerInstance> trigger, String recipeName) {
        unlockedBy = "has_" + unlockedBy;
        recipeName = recipeName + "_stairs";
        smallStair(item, ingredient).unlockedBy(unlockedBy, trigger).save(this.output, recipeName + "_small");
        bigStair(item, ingredient).unlockedBy(unlockedBy, trigger).save(this.output);
    }

    private void stairRecipe(ItemLike item, Ingredient ingredient, String unlockedBy, Criterion<InventoryChangeTrigger.TriggerInstance> trigger, String groupPrefix, String recipeName) {
        String group = groupPrefix + "_stairs";
        recipeName = recipeName + "_stairs";
        smallStair(item, ingredient).group(group).unlockedBy(unlockedBy, trigger).save(this.output, recipeName + "_small");
        bigStair(item, ingredient).group(group).unlockedBy(unlockedBy, trigger).save(this.output);
    }

    protected @NotNull RecipeBuilder smallStair(@NotNull ItemLike item, @NotNull Ingredient ingredient) {
        return this.shaped(RecipeCategory.BUILDING_BLOCKS, item, 4)
                .define('#', ingredient)
                .pattern("#  ")
                .pattern("## ");
    }

    protected @NotNull RecipeBuilder bigStair(@NotNull ItemLike item, @NotNull Ingredient ingredient) {
        return this.shaped(RecipeCategory.BUILDING_BLOCKS, item, 8)
                .define('#', ingredient)
                .pattern("#  ")
                .pattern("## ")
                .pattern("###");
    }

    private void trapdoorRecipe(ItemLike item, Item ingredient, String groupPrefix, String recipeName) {
        String group = groupPrefix + "_trapdoor";
        recipeName = recipeName + "_trapdoor";
        trapdoor(item, Ingredient.of(ingredient)).group(group).unlockedBy("has_planks", has(ItemTags.PLANKS)).save(this.output);
    }

    protected @NotNull RecipeBuilder trapdoor(@NotNull ItemLike arg, @NotNull Ingredient arg2) {
        return this.shaped(RecipeCategory.REDSTONE, arg, 12)
                .define('#', arg2)
                .pattern("###")
                .pattern("###");
    }

    private Criterion<InventoryChangeTrigger.TriggerInstance> has(ItemLike... items) {
        return inventoryTrigger(ItemPredicate.Builder.item().of(this.items, items));
    }

    private record ModIngredient(Item item, int amount) {
        public ModIngredient(Item item) {
            this(item, 1);
        }
    }

    private record RecipeInfo(Item item, Ingredient ingredient, String unlockedByName, Criterion<InventoryChangeTrigger.TriggerInstance> trigger, String recipeName) {}
}
