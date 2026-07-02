package com.fabbe50.fabsservertweaks.fabric.plugins.polymer;

import eu.pb4.polymer.core.api.block.PolymerBlock;
import eu.pb4.polymer.core.api.block.PolymerBlockUtils;
import eu.pb4.polymer.core.api.entity.PolymerEntity;
import eu.pb4.polymer.core.api.entity.PolymerEntityUtils;
import eu.pb4.polymer.core.api.item.PolymerItem;
import eu.pb4.polymer.core.api.item.PolymerItemUtils;
import eu.pb4.polymer.resourcepack.api.PackResource;
import eu.pb4.polymer.resourcepack.api.ResourcePackBuilder;
import eu.pb4.polymer.virtualentity.api.BlockWithElementHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

import java.io.IOException;
import java.io.InputStream;

public class PolymerPlugin {
    public static void registerBlock(Block block, PolymerBlock polymerBlock) {
        PolymerBlockUtils.registerOverlay(block, polymerBlock);
    }

    public static void registerBlockOverlay(Block block, Block fallback) {
        PolymerBlockUtils.registerOverlay(block, (state, context) -> copyCommonProperties(state, fallback.defaultBlockState()));
    }

    public static void registerElementHolder(Block block, BlockWithElementHolder elementHolder) {
        BlockWithElementHolder.registerOverlay(block, elementHolder);
    }

    public static void registerItem(Item item, PolymerItem polymerItem) {
        PolymerItemUtils.registerOverlay(item, polymerItem);
    }

    public static void registerItemOverlay(Item item, Item fallback) {
        PolymerItemUtils.registerOverlay(item, (itemStack, context) -> fallback);
    }

    public static void registerBlockEntity(BlockEntityType<?> blockEntityType, BlockEntityType<?> fallback) {
        PolymerBlockUtils.registerBlockEntity(blockEntityType, (object, context) -> fallback);
    }

    public static <T extends Entity, R extends Entity> void registerEntity(EntityType<T> entityType, EntityType<R> fallback) {
        PolymerEntityUtils.registerType(entityType, (object, context) -> fallback);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static BlockState copyCommonProperties(BlockState source, BlockState target) {
        for (Property<?> sourceProperty : source.getProperties()) {
            for (Property<?> targetProperty : target.getProperties()) {
                if (sourceProperty.getName().equals(targetProperty.getName())
                        && sourceProperty.getValueClass() == targetProperty.getValueClass()) {
                    target = target.setValue((Property) targetProperty, source.getValue((Property) sourceProperty));
                    break;
                }
            }
        }
        return target;
    }

    public static void addAsset(ResourcePackBuilder builder, String prefix, String pluginPath, String localPath) {
        String resourcePath = "/" + prefix + "/" + pluginPath + "/" + localPath;
        try (InputStream in = PolymerPlugin.class.getResourceAsStream(resourcePath)) {
            if (in == null) {
                throw new IllegalStateException("Missing polymer asset: " + resourcePath);
            }

            builder.addData("assets/" + pluginPath + "/" + localPath, in.readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException("Failed to add polymer asset: " + resourcePath, e);
        }
    }

    public static void copyAsset(ResourcePackBuilder builder, String startPath, String endPath) {
        PackResource resource = builder.getResource(startPath);
        if (resource == null) {
            throw new RuntimeException("Failed to find polymer asset: " + startPath);
        }
        builder.addData(endPath, resource);
    }
}
