package com.fabbe50.fabsservertweaks.util;

import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

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

    public static void removeLoreFuzzy(ItemStack stack, String loreText) {
        ItemLore lore = stack.getOrDefault(DataComponents.LORE, ItemLore.EMPTY);
        List<Component> components = new ArrayList<>(lore.lines());
        if (components.removeIf(component -> component.getString().contains(loreText))) {
            lore = new ItemLore(components);
            stack.set(DataComponents.LORE, lore);
        }
    }

    public static ItemStackTemplate createStackTemplateWithState(BlockState state) {
        BlockItemStateProperties properties = BlockItemStateProperties.EMPTY;
        for (Property<?> property : state.getProperties()) {
            properties = copyProperty(properties, state, property);
        }

        return new ItemStackTemplate(state.getBlock().asItem(), DataComponentPatch.builder().set(DataComponents.BLOCK_STATE, properties).build());
    }

    public static ItemStack createStackWithState(BlockState state) {
        ItemStack stack = new ItemStack(state.getBlock());
        BlockItemStateProperties properties = BlockItemStateProperties.EMPTY;

        for (Property<?> property : state.getProperties()) {
            properties = copyProperty(properties, state, property);
        }

        stack.set(DataComponents.BLOCK_STATE, properties);
        return stack;
    }

    private static BlockItemStateProperties copyProperty(BlockItemStateProperties properties, BlockState state, Property<?> property) {
        return properties.with(property, state);
    }
}
