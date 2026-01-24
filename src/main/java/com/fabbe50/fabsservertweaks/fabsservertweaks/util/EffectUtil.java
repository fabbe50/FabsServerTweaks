package com.fabbe50.fabsservertweaks.fabsservertweaks.util;

import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

import java.util.List;

public class EffectUtil {
    public static List<Holder<MobEffect>> overworldMobEffects = List.of(MobEffects.RESISTANCE, MobEffects.FIRE_RESISTANCE, MobEffects.ABSORPTION, MobEffects.INFESTED, MobEffects.SPEED, MobEffects.STRENGTH);
    public static List<Holder<MobEffect>> netherMobEffects = List.of(MobEffects.RESISTANCE, MobEffects.ABSORPTION, MobEffects.SPEED, MobEffects.STRENGTH);
    public static List<Holder<MobEffect>> endMobEffects = List.of(MobEffects.RESISTANCE, MobEffects.FIRE_RESISTANCE, MobEffects.ABSORPTION, MobEffects.SPEED, MobEffects.STRENGTH);

    public static MobEffectInstance getRandomOverworldEffect(RandomSource random) {
        Holder<MobEffect> mobEffect = overworldMobEffects.get(random.nextInt(overworldMobEffects.size()));
        return new MobEffectInstance(mobEffect, -1, random.nextInt(3));
    }

    public static MobEffectInstance getRandomNetherEffect(RandomSource random) {
        Holder<MobEffect> mobEffect = netherMobEffects.get(random.nextInt(netherMobEffects.size()));
        return new MobEffectInstance(mobEffect, -1, random.nextInt(3));
    }

    public static MobEffectInstance getRandomEndEffect(RandomSource random) {
        Holder<MobEffect> mobEffect = endMobEffects.get(random.nextInt(endMobEffects.size()));
        return new MobEffectInstance(mobEffect, -1, random.nextInt(3));
    }
}
