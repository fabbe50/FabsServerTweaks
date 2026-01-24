package com.fabbe50.fabsservertweaks.fabsservertweaks.config;

import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.SubCategoryListEntry;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;

public interface IClothBuilder<R, T> {
    T buildClothEntry(ConfigEntryBuilder builder);

    boolean requiresRestart();

    ConfigCategory getConfigCategory(ConfigBuilder builder);

    SubCategoryBuilder getSubCategory(ConfigEntryBuilder builder);
}
