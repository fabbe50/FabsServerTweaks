package com.fabbe50.fabsservertweaks.fabric.world.items;

import eu.pb4.polymer.core.api.item.PolymerItem;
import net.fabricmc.fabric.api.networking.v1.context.PacketContext;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public class SimplePolymerItem extends Item implements PolymerItem {
    private final Identifier identifier;
    private final Item polymerItem;

    public SimplePolymerItem(Identifier identifier, Item polymerItem, Properties properties) {
        super(properties);
        this.identifier = identifier;
        this.polymerItem = polymerItem;
    }

    @Override
    public Item getPolymerItem(ItemStack itemStack, PacketContext context) {
        return polymerItem;
    }

    @Override
    public @Nullable Identifier getPolymerItemModel(ItemStack stack, PacketContext context, Provider lookup) {
        return identifier;
    }
}
