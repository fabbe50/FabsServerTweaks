package com.fabbe50.fabsservertweaks.fabsservertweaks.config;

import com.fabbe50.fabsservertweaks.fabsservertweaks.ClothScreen;
import com.fabbe50.fabsservertweaks.fabsservertweaks.util.LangUtils;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;

public abstract class AbstractConfigOption<T, R extends AbstractConfigListEntry<T>> implements IConfigOption<T, R> {
    final String name;
    final T defaultValue;
    T value;
    boolean requiresRestart = false;
    String category = "general";
    String subCategory = "";

    public AbstractConfigOption(String name, T defaultValue) {
        this(name, defaultValue, defaultValue);
    }

    public AbstractConfigOption(String name, T defaultValue, T value) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.value = value;
    }

    @Override
    public String getKey() {
        return name;
    }

    @Override
    public ConfigCategory getConfigCategory(ConfigBuilder builder) {
        return builder.getOrCreateCategory(LangUtils.getConfig("category." + getCategory()));
    }

    @Override
    public SubCategoryBuilder getSubCategory(ConfigEntryBuilder builder) {
        if (!getSubCategory().isBlank()) {
            return ClothScreen.subCategory.computeIfAbsent(getSubCategory(), s -> builder.startSubCategory(LangUtils.getConfig("subcategory." + getSubCategory())));
        }
        return null;
    }

    @Override
    public T getDefaultValue() {
        return defaultValue;
    }

    @Override
    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public T getValue() {
        return value;
    }

    @Override
    public boolean requiresRestart() {
        return requiresRestart;
    }

    @Override
    public String getCategory() {
        return category;
    }

    @Override
    public String getSubCategory() {
        return subCategory;
    }

    public abstract class Builder<Z extends AbstractConfigOption<T, R>> {
        String name;
        T defaultValue;
        boolean requiresRestart;
        String category;
        String subCategory;

        public Builder(String name, T defaultValue) {
            this(name, defaultValue, true, "general", "");
        }

        public Builder(String name, T defaultValue, boolean requiresRestart, String category, String subCategory) {
            this.name = name;
            this.defaultValue = defaultValue;
            this.requiresRestart = requiresRestart;
            this.category = category;
            this.subCategory = subCategory;
        }

        public Builder<Z> requiresRestart() {
            this.requiresRestart = true;
            return this;
        }

        public Builder<Z> category(String category) {
            this.category = category;
            return this;
        }

        public Builder<Z> subCategory(String subCategory) {
            this.subCategory = subCategory;
            return this;
        }

        public abstract Z build();
    }
}
