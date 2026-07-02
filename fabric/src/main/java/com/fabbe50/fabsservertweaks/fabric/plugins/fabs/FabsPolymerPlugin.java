package com.fabbe50.fabsservertweaks.fabric.plugins.fabs;

import com.fabbe50.fabsservertweaks.Fabsservertweaks;
import com.fabbe50.fabsservertweaks.LogUtil;
import com.fabbe50.fabsservertweaks.fabric.plugins.base.Plugin;
import com.fabbe50.fabsservertweaks.fabric.plugins.polymer.PolymerBlockWithElementHolder;
import com.fabbe50.fabsservertweaks.fabric.plugins.polymer.PolymerPlugin;
import com.fabbe50.fabsservertweaks.fabric.registries.ModRegistry;
import com.fabbe50.fabsservertweaks.fabric.world.blocks.ChunkLoaderBlock;
import com.fabbe50.fabsservertweaks.fabric.world.items.SimplePolymerBlockItem;
import eu.pb4.polymer.core.api.item.PolymerCreativeModeTabUtils;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.fabric.api.resource.v1.pack.PackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTab.Row;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour.BlockStateBase;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class FabsPolymerPlugin extends Plugin {
    public static Block CHUNK_LOADER_BLOCK;
    public static Item CHUNK_LOADER_ITEM;

    @Override
    public void init() {
        LogUtil.log("Initializing Fab's Polymer Plugin");

        FabricLoader.getInstance().getModContainer(Fabsservertweaks.MOD_ID).ifPresent(modContainer ->
                ResourceLoader.registerBuiltinPack(
                        Fabsservertweaks.location("fabs_polymer_plugin", "fabs_polymer_plugin"),
                        modContainer,
                        Component.literal("Fab's Polymer Plugin"),
                        PackActivationType.ALWAYS_ENABLED
                )
        );

        PolymerResourcePackUtils.RESOURCE_PACK_CREATION_EVENT.register(resourcePackBuilder -> {
            PolymerPlugin.addAsset(resourcePackBuilder, "resourcepacks/fabs_polymer_plugin/assets", "fabs_polymer_plugin", "lang/en_us.json");
            PolymerPlugin.addAsset(resourcePackBuilder, "resourcepacks/fabs_polymer_plugin/assets", "fabs_polymer_plugin", "models/block/chunk_loader.json");
            PolymerPlugin.addAsset(resourcePackBuilder, "resourcepacks/fabs_polymer_plugin/assets", "fabs_polymer_plugin", "textures/block/chunk_loader.png");
            PolymerPlugin.addAsset(resourcePackBuilder, "resourcepacks/fabs_polymer_plugin/assets", "fabs_polymer_plugin", "textures/block/chunk_loader_e.png");
            PolymerPlugin.addAsset(resourcePackBuilder, "resourcepacks/fabs_polymer_plugin/assets", "fabs_polymer_plugin", "items/chunk_loader.json");
        });

        PolymerResourcePackUtils.markAsRequired();

        FabsPolymerStates.init();

        PolymerCreativeModeTabUtils.registerPolymerCreativeModeTab(Fabsservertweaks.location("fabs_polymer_plugin"), CreativeModeTab.builder(Row.TOP, -1)
                .title(Component.literal("Fab's Polymer Plugin"))
                .icon(() -> new ItemStack(CHUNK_LOADER_ITEM))
                .displayItems((parameters, output) -> output.accept(CHUNK_LOADER_ITEM))
                .build()
        );
    }

    @Override
    public void registerBlocks() {
        CHUNK_LOADER_BLOCK = ModRegistry.register(Fabsservertweaks.location("chunk_loader"), properties -> new ChunkLoaderBlock(properties, Blocks.TORCH, (state, context) -> FabsPolymerStates.CHUNK_LOADER_BLOCK), Properties.ofFullCopy(Blocks.TORCH).emissiveRendering(BlockStateBase::emissiveRendering));
    }

    @Override
    public void registerItems() {
        CHUNK_LOADER_ITEM = ModRegistry.register(Fabsservertweaks.location("chunk_loader"), properties -> new SimplePolymerBlockItem(Fabsservertweaks.location("fabs_polymer_plugin", "chunk_loader"), Items.TORCH, CHUNK_LOADER_BLOCK, properties), new Item.Properties());
    }

    @Override
    public void registerElementHolderOverlays() {
        PolymerPlugin.registerElementHolder(CHUNK_LOADER_BLOCK, new PolymerBlockWithElementHolder(new ChunkLoaderElementHolder(), true));
    }
}
