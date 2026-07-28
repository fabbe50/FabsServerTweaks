package com.fabbe50.fabsservertweaks.util;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public record ItemStacks(ItemStack mainHand, ItemStack offHand) {
    public ItemStacks(Player player) {
        this(player.getMainHandItem(), player.getOffhandItem());
    }
}
