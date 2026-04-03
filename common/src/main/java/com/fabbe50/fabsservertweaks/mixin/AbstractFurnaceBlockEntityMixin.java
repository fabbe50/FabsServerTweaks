package com.fabbe50.fabsservertweaks.mixin;

import com.fabbe50.fabsservertweaks.data.DurabilitySmeltData;
import com.fabbe50.fabsservertweaks.data.loader.DurabilitySmeltLoader;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.Reference2IntMap;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class AbstractFurnaceBlockEntityMixin {
    @Shadow
    private static void createExperience(ServerLevel arg, Vec3 arg2, int j, float g) {
    }

    @Redirect(method = "burn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/crafting/AbstractCookingRecipe;assemble(Lnet/minecraft/world/item/crafting/SingleRecipeInput;Lnet/minecraft/core/HolderLookup$Provider;)Lnet/minecraft/world/item/ItemStack;"))
    private static ItemStack redirectAssemble(AbstractCookingRecipe recipe, SingleRecipeInput singleRecipeInput, HolderLookup.Provider provider) {
        ItemStack vanilla = recipe.assemble(singleRecipeInput, provider);

        ItemStack input = singleRecipeInput.item();
        if (input.isEmpty()) {
            return vanilla;
        }

        DurabilitySmeltData data = DurabilitySmeltLoader.find(input, kind(recipe));
        if (data == null) {
            return vanilla;
        }

        DurabilitySmeltData.Tier tier = DurabilitySmeltLoader.pickTier(data, input);
        if (tier == null) {
            return vanilla;
        }

        return tier.result().copy();
    }

    @Inject(method = "getTotalCookTime", at = @At("HEAD"), cancellable = true)
    private static void injectGetTotalCookTime(ServerLevel serverLevel, AbstractFurnaceBlockEntity container, CallbackInfoReturnable<Integer> cir) {
        ItemStack input = container.getItem(0);
        RecipeHolder<?> recipeHolder = container.getRecipeUsed();
        if (recipeHolder != null) {
            if (recipeHolder.value() instanceof AbstractCookingRecipe recipe) {
                final String furnaceKind = kind(recipe);
                DurabilitySmeltData data = DurabilitySmeltLoader.find(input, furnaceKind);
                if (data != null) {
                    DurabilitySmeltData.Tier tier = DurabilitySmeltLoader.pickTier(data, input);
                    if (tier != null && tier.cookTime() != null) {
                        cir.setReturnValue(tier.cookTime());
                        return;
                    }
                    if (data.defaultCookTime() != null) {
                        cir.setReturnValue(data.defaultCookTime());
                    }
                }
            }
        }
    }

    @Redirect(method = "awardUsedRecipesAndPopExperience", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/entity/AbstractFurnaceBlockEntity;getRecipesToAwardAndPopExperience(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/phys/Vec3;)Ljava/util/List;"))
    private static List<RecipeHolder<?>> redirectAwardUsedRecipesAndPopExperience(AbstractFurnaceBlockEntity container, ServerLevel serverLevel, Vec3 list) {
        List<RecipeHolder<?>> recipeList = Lists.newArrayList();

        for (Reference2IntMap.Entry<ResourceKey<Recipe<?>>> entry : container.recipesUsed.reference2IntEntrySet()) {
            serverLevel.recipeAccess().byKey(entry.getKey()).ifPresent((recipeHolder) -> {
                recipeList.add(recipeHolder);
                AbstractCookingRecipe recipe = getCookingRecipe(recipeHolder);
                final String furnaceKind = kind(recipe);
                ItemStack input = container.getItem(0);
                DurabilitySmeltData data = DurabilitySmeltLoader.find(input, furnaceKind);
                if (data != null) {
                    DurabilitySmeltData.Tier tier = DurabilitySmeltLoader.pickTier(data, input);
                    if (tier != null && tier.xp() != null) {
                        createExperience(serverLevel, list, entry.getIntValue(), tier.xp());
                    } else if (data.defaultXp() != null) {
                        createExperience(serverLevel, list, entry.getIntValue(), data.defaultXp());
                    } else {
                        createExperience(serverLevel, list, entry.getIntValue(), getCookingRecipe(recipeHolder).experience());
                    }
                } else {
                    createExperience(serverLevel, list, entry.getIntValue(), getCookingRecipe(recipeHolder).experience());
                }
            });
        }

        return recipeList;
    }

    @Unique
    private static AbstractCookingRecipe getCookingRecipe(RecipeHolder<?> recipeHolder) {
        if (recipeHolder != null) {
            if (recipeHolder.value() instanceof AbstractCookingRecipe recipe) {
                return recipe;
            }
        }
        return null;
    }

    @Unique
    private static String kind(AbstractCookingRecipe recipe) {
        return (recipe instanceof BlastingRecipe) ? "blast_furnace" :
                ((recipe instanceof SmokingRecipe) ? "smoker" :
                "furnace");
    }
}
