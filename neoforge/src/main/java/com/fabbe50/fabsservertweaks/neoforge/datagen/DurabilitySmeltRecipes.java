package com.fabbe50.fabsservertweaks.neoforge.datagen;

import com.fabbe50.fabsservertweaks.Fabsservertweaks;
import com.fabbe50.fabsservertweaks.LogUtil;
import com.fabbe50.fabsservertweaks.data.DurabilitySmeltData;
import com.fabbe50.fabsservertweaks.data.DurabilitySmeltData.Tier;
import com.mojang.serialization.Codec;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.data.JsonCodecProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class DurabilitySmeltRecipes extends JsonCodecProvider<DurabilitySmeltData> {
    public DurabilitySmeltRecipes(PackOutput output, PackOutput.Target target, Codec<DurabilitySmeltData> codec, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, target, Fabsservertweaks.location("durability_smelting").getPath(), codec, lookupProvider, Fabsservertweaks.MOD_ID);
    }

    @Override
    protected void gather() {
        toolGroupRecipe("chainmail", List.of(
                new ToolValueData(Items.CHAINMAIL_HELMET, "helmet", 2, 9),
                new ToolValueData(Items.CHAINMAIL_CHESTPLATE, "chestplate", 4, 9),
                new ToolValueData(Items.CHAINMAIL_LEGGINGS, "leggings", 3, 9),
                new ToolValueData(Items.CHAINMAIL_BOOTS, "boots", 2, 9)
        ), Set.of("furnace", "blast_furnace"), 200, 0.7f, Items.IRON_INGOT, Items.IRON_NUGGET);
        toolGroupRecipe("iron", List.of(
                new ToolValueData(Items.IRON_SWORD, "sword", 2, 9),
                new ToolValueData(Items.IRON_PICKAXE, "pickaxe", 3, 9),
                new ToolValueData(Items.IRON_AXE, "axe", 3, 9),
                new ToolValueData(Items.IRON_SHOVEL, "shovel", 1, 9),
                new ToolValueData(Items.IRON_HOE, "hoe", 2, 9),
                new ToolValueData(Items.IRON_HELMET, "helmet", 5, 9),
                new ToolValueData(Items.IRON_CHESTPLATE, "chestplate", 8, 9),
                new ToolValueData(Items.IRON_LEGGINGS, "leggings", 7, 9),
                new ToolValueData(Items.IRON_BOOTS, "boots", 4, 9)
        ), Set.of("furnace", "blast_furnace"), 200, 0.7f, Items.IRON_INGOT, Items.IRON_NUGGET);
        toolGroupRecipe("golden", List.of(
                new ToolValueData(Items.GOLDEN_SWORD, "sword", 2, 9),
                new ToolValueData(Items.GOLDEN_PICKAXE, "pickaxe", 3, 9),
                new ToolValueData(Items.GOLDEN_AXE, "axe", 3, 9),
                new ToolValueData(Items.GOLDEN_SHOVEL, "shovel", 1, 9),
                new ToolValueData(Items.GOLDEN_HOE, "hoe", 2, 9),
                new ToolValueData(Items.GOLDEN_HELMET, "helmet", 5, 9),
                new ToolValueData(Items.GOLDEN_CHESTPLATE, "chestplate", 8, 9),
                new ToolValueData(Items.GOLDEN_LEGGINGS, "leggings", 7, 9),
                new ToolValueData(Items.GOLDEN_BOOTS, "boots", 4, 9)
        ), Set.of("furnace", "blast_furnace"), 200, 0.7f, Items.GOLD_INGOT, Items.GOLD_NUGGET);
    }

    private void toolGroupRecipe(String name, List<ToolValueData> toolValueDataList, Set<String> furnaceType, int cookTime, float xp, Item whole, Item part) {
        for (ToolValueData toolValueData : toolValueDataList) {
            toolRecipe(name + "_" + toolValueData.tool_type() + "_smelt", toolValueData.tool(), furnaceType, cookTime, xp, toolValueData.ingredientCount(), toolValueData.partCount(), whole, part);
        }
    }

    private void toolRecipe(String name, Item input, Set<String> furnaceType, int cookTime, float xp, int ingredientCount, int part, Item wholeOutput, Item partOutput) {
        List<Tier> tiers = new ArrayList<>();
        int count = ingredientCount * part;
        double partPercent = 100d / (count - 1);
        int cookingTime = cookTime / count;
        for (int i = count; i > 0; i--) {
            int percent = (int) Math.clamp((partPercent * (i - 1)), 0, 100);
            Item output = (i + 1) % 9 == 1 ? wholeOutput : partOutput;
            int amount = Math.clamp(((i + 1) % 9 == 1 ? i / 9 : i), 0, 64);
            int partCookTime = Math.clamp(((long)cookingTime * (i - 1)), 0, cookTime - 20) + 20;
            LogUtil.log("Part: [ Percent: " + percent + " ], [ output: " + output + " ], [ amount: " + amount + " ], [ Cook Time: " + partCookTime + " ]");
            tiers.add(tier(percent, output, amount, partCookTime));
        }

        durabilityRecipe(name, input, furnaceType, cookTime, xp, tiers);
    }

    private void durabilityRecipe(String name, Item item, Set<String> furnaceType, int cookTime, float xp, List<Tier> tiers) {
        this.unconditional(Fabsservertweaks.location(name), new DurabilitySmeltData(Ingredient.of(item), furnaceType, cookTime, xp, tiers));
    }

    private void durabilityRecipe(String name, Item item, Set<String> furnaceType, List<Tier> tiers) {
        this.unconditional(Fabsservertweaks.location(name), new DurabilitySmeltData(Ingredient.of(item), furnaceType, 200, 0.7f, tiers));
    }

    private Tier tier(int minPercent, Item item, int amount) {
        return tier(minPercent, item, amount, null);
    }

    private Tier tier(int minPercent, Item item, int amount, Integer cookTime) {
        return tier(minPercent, item, amount, cookTime, null);
    }

    private Tier tier(int minPercent, Item item, int amount, Integer cookTime, Float xp) {
        return new Tier(minPercent, new ItemStack(item, amount), cookTime, xp);
    }

    private record ToolValueData(Item tool, String tool_type, int ingredientCount, int partCount) {
    }
}
