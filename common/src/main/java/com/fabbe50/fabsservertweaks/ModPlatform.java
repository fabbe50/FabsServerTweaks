package com.fabbe50.fabsservertweaks;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;

public class ModPlatform {
    @ExpectPlatform
    public static boolean isModLoaded(String modId) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void registerStarter(PotionBrewing.Builder builder, Item ingredient, Holder<Potion> output) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void registerPotion(PotionBrewing.Builder builder, Holder<Potion> input, Item ingredient, Holder<Potion> output) {
        throw new AssertionError();
    }

    public static void registerCompostable(float chance, Item item) {
        registerCompostable(chance, item, false);
    }

    @ExpectPlatform
    public static void registerCompostable(float chance, Item item, boolean canVillagerCompost) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean isDataGen() {
        throw new AssertionError();
    }
}
