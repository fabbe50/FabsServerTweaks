package com.fabbe50.fabsservertweaks.data.soulbound;

import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.UUID;

public record SoulBoundData(UUID playerID, List<ItemStack> soulBoundItems) {
}
