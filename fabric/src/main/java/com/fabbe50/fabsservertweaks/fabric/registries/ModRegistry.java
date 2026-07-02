package com.fabbe50.fabsservertweaks.fabric.registries;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

import java.util.function.Function;

public class ModRegistry {
    public static void init() {

    }

    public static Block register(Identifier identifier, Function<Properties, Block> blockFactory, BlockBehaviour.Properties properties) {
        ResourceKey<Block> key = ResourceKey.create(Registries.BLOCK, identifier);
        Block block = blockFactory.apply(properties.setId(key));

        return Registry.register(BuiltInRegistries.BLOCK, key, block);
    }

    public static <T extends Item> Item register(Identifier identifier, Function<Item.Properties, T> itemFactory, Item.Properties properties) {
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, identifier);
        T item = itemFactory.apply(properties.setId(key));

        return Registry.register(BuiltInRegistries.ITEM, key, item);
    }
}
