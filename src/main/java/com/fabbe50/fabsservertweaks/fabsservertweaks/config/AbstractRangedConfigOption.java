package com.fabbe50.fabsservertweaks.fabsservertweaks.config;

import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;

public abstract class AbstractRangedConfigOption<T extends Number, R extends AbstractConfigListEntry<T>> extends AbstractConfigOption<T, R> implements IRangedConfigOption<T, R> {
    private final T min;
    private final T max;

    public AbstractRangedConfigOption(String name, T defaultValue, T min, T max) {
        this(name, defaultValue, defaultValue, min, max);
    }

    public AbstractRangedConfigOption(String name, T defaultValue, T value, T min, T max) {
        super(name, defaultValue, value);
        this.min = min;
        this.max = max;
    }

    @Override
    public T min() {
        return min;
    }

    @Override
    public T max() {
        return max;
    }

    public abstract class Builder<Z extends AbstractRangedConfigOption<T, R>> extends AbstractConfigOption<T, R>.Builder<Z> {
        T min;
        T max;

        public Builder(String name, T defaultValue) {
            super(name, defaultValue, true, "general", "");
        }

        public Builder(String name, T defaultValue, T min, T max) {
            super(name, defaultValue, true, "general", "");
            this.min = min;
            this.max = max;
        }

        public Builder(String name, T defaultValue, boolean requiresRestart, String category, String subCategory) {
            super(name, defaultValue, requiresRestart, category, subCategory);
        }

        public Builder<Z> min(T min) {
            this.min = min;
            return this;
        }

        public Builder<Z> max(T max) {
            this.max = max;
            return this;
        }
    }
}
