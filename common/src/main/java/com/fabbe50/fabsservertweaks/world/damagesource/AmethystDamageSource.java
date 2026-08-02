package com.fabbe50.fabsservertweaks.world.damagesource;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;

public class AmethystDamageSource extends DamageSource {
    private final boolean dropPlayerLoot;

    private AmethystDamageSource(Holder<DamageType> type, boolean dropPlayerLoot) {
        super(type);
        this.dropPlayerLoot = dropPlayerLoot;
    }

    public boolean shouldDropPlayerLoot() {
        return dropPlayerLoot;
    }

    public static DamageSource source(ResourceKey<DamageType> type, ServerLevel level, boolean dropPlayerLoot) {
        Holder<DamageType> damageTypeHolder = level.registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(type);
        return new AmethystDamageSource(damageTypeHolder, dropPlayerLoot);
    }
}
