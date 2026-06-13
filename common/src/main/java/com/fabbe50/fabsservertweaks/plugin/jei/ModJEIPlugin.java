package com.fabbe50.fabsservertweaks.plugin.jei;

import com.fabbe50.fabsservertweaks.Fabsservertweaks;
import com.fabbe50.fabsservertweaks.LogUtil;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

@JeiPlugin
public class ModJEIPlugin implements IModPlugin {
    @Override
    public @NonNull Identifier getPluginUid() {
        return Fabsservertweaks.location("jei_plugin");
    }

    @Override
    public void registerCategories(@NonNull IRecipeCategoryRegistration registration) {
        LogUtil.log("JEI is loaded! Registering categories for " + Fabsservertweaks.MOD_NAME + "...");
        registration.addRecipeCategories(CauldronConversionJEI.CAULDRON_CONVERSION_CATEGORY);
    }

    @Override
    public void registerRecipes(@NonNull IRecipeRegistration registration) {
        LogUtil.log("JEI is loaded! Registering recipes for " + Fabsservertweaks.MOD_NAME + "...");
        CauldronConversionJEI conversion = new CauldronConversionJEI();
        conversion.registerRecipes(registration);
    }
}
