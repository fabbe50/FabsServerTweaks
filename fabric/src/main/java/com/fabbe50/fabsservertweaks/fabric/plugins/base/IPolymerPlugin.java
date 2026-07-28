package com.fabbe50.fabsservertweaks.fabric.plugins.base;

public interface IPolymerPlugin {
    default void registerBlocks() {}
    default void registerBlockOverlays() {}
    default void registerItems() {}
    default void registerItemOverlays() {}
    default void registerElementHolderOverlays() {}
    default void registerBlockEntities() {}
    default void registerEntities() {}
}
