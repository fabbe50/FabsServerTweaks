package com.fabbe50.fabsservertweaks.data;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.gamerules.GameRule;

import java.util.Map;

public class ExtGameRule<T> {
    public final GameRule<T> rule;
    public final Map<Presets, T> presetDefaults;

    public ExtGameRule(GameRule<T> rule, Map<Presets, T> presetDefaults) {
        this.rule = rule;
        this.presetDefaults = presetDefaults;
    }

    public GameRule<T> getRule() {
        return rule;
    }

    public T getPresetDefault(Presets preset) {
        return presetDefaults.get(preset);
    }

    public T getValue(ServerLevel level) {
        if (level == null || rule == null) {
            return null;
        }
        return level.getGameRules().get(rule);
    }
}
