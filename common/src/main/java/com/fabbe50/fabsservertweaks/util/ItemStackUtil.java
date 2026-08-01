package com.fabbe50.fabsservertweaks.util;

import com.fabbe50.fabsservertweaks.registries.ModRegistry;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.item.component.BundleContents;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    public static void addLore(ItemStack stack, Component loreText) {
        ItemLore lore = stack.getOrDefault(DataComponents.LORE, ItemLore.EMPTY);
        AtomicBoolean shouldAddLore = new AtomicBoolean(true);
        lore.lines().forEach(component -> {
            if (component.getString().equalsIgnoreCase(loreText.getString())) {
                shouldAddLore.set(false);
            }
        });
        if (shouldAddLore.get()) {
            lore = lore.withLineAdded(loreText);
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

    public static void shrink(ItemStack stack, Player player) {
        if (player.isCreative() || player.isSpectator()) {
            return;
        }
        stack.shrink(1);
    }

    private static final List<Item> NON_TAGGED_CUSTOM_STACK_SIZE_CHECK = List.of(
            Items.LEAD
    );

    private static final List<Item> STACKABLE_WHEN_ENCHANTED = List.of(
            Items.ENCHANTED_BOOK
    );

    public static int getCustomMaxStackSize(ItemInstance itemInstance) {
        if (itemInstance instanceof ItemStack stack) {
            return getCustomMaxStackSize(stack);
        }
        return -1;
    }

    /**
     * @param stack The stack to check
     * @return Returns the custom max stack size of the stack, or -1 if the stack doesn't have a custom max stack size.
     */
    public static int getCustomMaxStackSize(ItemStack stack) {
        if (hasCustomStackSize(stack) || NON_TAGGED_CUSTOM_STACK_SIZE_CHECK.contains(stack.getItem())) {
            int targetMax = 1;
            if (stack.isEnchanted() && !STACKABLE_WHEN_ENCHANTED.contains(stack.getItem())) {
                return 1;
            }
            if (stack.is(ModRegistry.STACK_4)) {
                targetMax = 4;
            } else if (stack.is(ModRegistry.STACK_8)) {
                targetMax = 8;
            } else if (stack.is(ModRegistry.STACK_16)) {
                targetMax = 16;
            } else if (stack.is(ModRegistry.STACK_64)) {
                targetMax = 64;
            }
            return targetMax;
        }
        return -1;
    }

    public static boolean hasCustomStackSize(ItemStack stack) {
        return stack.is(ModRegistry.STACK_4) || stack.is(ModRegistry.STACK_8) || stack.is(ModRegistry.STACK_16) || stack.is(ModRegistry.STACK_64);
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

    public static BlockItemStateProperties copyProperty(BlockItemStateProperties properties, BlockState state, Property<?> property) {
        return properties.with(property, state);
    }

    public static boolean takeItemFromPlayerInventory(Player player, ItemLike item, boolean checkBundles, boolean resultOnCreative) {
        if (player.isCreative() || player.isSpectator()) {
            return resultOnCreative;
        }
        for (ItemStack stack : player.getInventory()) {
            if (checkBundles && stack.is(ItemTags.BUNDLES)) {
                if (stack.getItem() instanceof BundleItem) {
                    BundleContents contents = stack.getOrDefault(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY);
                    List<ItemStack> stacks = new ArrayList<>(contents.itemCopyStream().toList());
                    for (ItemStack itemStack : new ArrayList<>(stacks)) {
                        if (itemStack.is(item.asItem())) {
                            int index = stacks.indexOf(itemStack);
                            stacks.remove(itemStack);
                            shrink(stack, player);
                            stacks.add(index, itemStack);
                            break;
                        }
                    }
                    contents = new BundleContents(stacks.stream()
                            .map(stack1 -> {
                                if (!stack1.isEmpty()) {
                                    return ItemStackTemplate.fromNonEmptyStack(stack1);
                                }
                                return null;
                            })
                            .filter(Objects::nonNull).toList()
                    );
                    stack.set(DataComponents.BUNDLE_CONTENTS, contents);
                    return true;
                }
            }
            if (stack.is(item.asItem())) {
                shrink(stack, player);
                return true;
            }
        }
        return false;
    }
}
