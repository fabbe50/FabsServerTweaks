package com.fabbe50.fabsservertweaks.util;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemLore;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ItemStackUtil {
    public static void addLore(ItemStack stack, String loreText) {
        ItemLore lore = stack.getOrDefault(DataComponents.LORE, ItemLore.EMPTY);
        AtomicBoolean shouldAddLore = new AtomicBoolean(true);
        lore.lines().forEach(component -> {
            if (component.getString().equalsIgnoreCase(loreText)) {
                shouldAddLore.set(false);
            }
        });
        if (shouldAddLore.get()) {
            lore = lore.withLineAdded(Component.literal(loreText));
            stack.set(DataComponents.LORE, lore);
        }
    }

    public static void removeLore(ItemStack stack, String loreText) {
        ItemLore lore = stack.getOrDefault(DataComponents.LORE, ItemLore.EMPTY);
        List<Component> components = new ArrayList<>(lore.lines());
        if (components.removeIf(component -> component.getString().equalsIgnoreCase(loreText))) {
            lore = new ItemLore(components);
            stack.set(DataComponents.LORE, lore);
        }
    }
}
