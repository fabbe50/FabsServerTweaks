package com.fabbe50.fabsservertweaks.fabsservertweaks;

import com.fabbe50.fabsservertweaks.fabsservertweaks.config.IConfigOption;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigScreen {
    public static final Map<String, SubCategoryBuilder> subCategory = new HashMap<>();
    public static final List<String> seenSubCategories = new ArrayList<>();

    public static Screen getConfigScreen(Screen parent) {
        Component title = Component.literal(FabsServerTweaks.MOD_NAME);

        var builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(title);

        var entryBuilder = builder.entryBuilder();

        for (IConfigOption<?, ?> configOption : ModConfig.getConfigOptions().values()) {
            var category = configOption.getConfigCategory(builder);
            var subCategoryBuilder = configOption.getSubCategory(entryBuilder);
            if (subCategoryBuilder != null) {
                subCategoryBuilder.add((AbstractConfigListEntry) configOption.buildClothEntry(entryBuilder));
                String subCategoryName = configOption.getSubCategory();
                if (!seenSubCategories.contains(subCategoryName)) {
                    category.addEntry(subCategoryBuilder.build());
                    seenSubCategories.add(subCategoryName);
                }
            } else {
                category.addEntry((AbstractConfigListEntry) configOption.buildClothEntry(entryBuilder));
            }
        }

        return builder.setSavingRunnable(() -> {
            try {
                ModConfig.save(ModConfig.getConfigFile());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            ModConfig.load(ModConfig.getConfigFile());
        }).build();
    }
}
