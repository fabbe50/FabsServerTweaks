package com.fabbe50.fabsservertweaks.plugin.jei;

import com.fabbe50.fabsservertweaks.plugin.recipe_viewer_common.CauldronConversion;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeType;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import org.jspecify.annotations.NonNull;

import java.util.List;

public class CauldronConversionJEI extends CauldronConversion<IRecipeRegistration> {
    public static IRecipeType<CauldronRecipe> cauldronType = IRecipeType.create(CAULDRON_CONVERSION, CauldronRecipe.class);
    public static final CauldronRecipeCategory CAULDRON_CONVERSION_CATEGORY = new CauldronRecipeCategory(cauldronType, NAME);

    @Override
    protected void registerRecipes(IRecipeRegistration registry, List<CauldronRecipe> cauldronRecipes) {
        registry.addRecipes(cauldronType, cauldronRecipes);
    }

    public static class CauldronRecipeCategory extends AbstractRecipeCategory<CauldronRecipe> {
        public CauldronRecipeCategory(IRecipeType<CauldronRecipe> recipeType, Component title) {
            super(recipeType, title, new IDrawable() {
                @Override
                public int getWidth() {
                    return 18;
                }

                @Override
                public int getHeight() {
                    return 18;
                }

                @Override
                public void draw(@NonNull GuiGraphicsExtractor guiGraphics, int x, int y) {
                    guiGraphics.fakeItem(new ItemStack(Blocks.WATER_CAULDRON), x, y);
                }
            }, 108, 18);
        }

        @Override
        public void setRecipe(IRecipeLayoutBuilder builder, CauldronRecipe cauldronRecipe, @NonNull IFocusGroup iFocusGroup) {
            IRecipeSlotBuilder inputSlot = builder.addInputSlot(0, 0).setStandardSlotBackground();
            inputSlot.add(cauldronRecipe.input());
            IRecipeSlotBuilder outputSlot = builder.addOutputSlot(90, 0).setOutputSlotBackground();
            outputSlot.add(cauldronRecipe.output());
        }
    }
}
