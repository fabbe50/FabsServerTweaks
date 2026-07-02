package com.fabbe50.fabsservertweaks.fabric.plugins.resource_nether_ores;

import com.fabbe50.fabsservertweaks.Fabsservertweaks;
import com.fabbe50.fabsservertweaks.LogUtil;
import com.fabbe50.fabsservertweaks.fabric.FabricPluginHelper;
import com.fabbe50.fabsservertweaks.fabric.plugins.base.Plugin;
import com.fabbe50.fabsservertweaks.fabric.plugins.polymer.PolymerPlugin;
import com.fabbe50.fabsservertweaks.fabric.world.blocks.SuppliedPolymerTexturedBlock;
import eu.pb4.polymer.core.api.block.PolymerBlock;
import eu.pb4.polymer.core.api.item.PolymerCreativeModeTabUtils;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.xstopho.resource_nether_ores.registries.BlockRegistry;
import net.xstopho.resource_nether_ores.registries.CreativeTabRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ResourceNetherOresPlugin extends Plugin {
    public static final Map<Block, Block> RESOURCE_NETHER_ORES_BLOCKS_WITH_FALLBACK = new HashMap<>();
    public static final Map<Block, PolymerBlock> POLYMER_BLOCKS = new HashMap<>();

    @Override
    public void init() {
        LogUtil.log("Initializing Resource Nether Ores Plugin");

        PolymerResourcePackUtils.addModAssets(FabricPluginHelper.RESOURCE_NETHER_ORES_ID);
        PolymerResourcePackUtils.markAsRequired();

        makeBlockList();

        ResourceNetherOresPolymerStates.init();

        PolymerCreativeModeTabUtils.registerPolymerCreativeModeTab(Fabsservertweaks.location("resource_nether_ores_plugin"), CreativeTabRegistry.RESOURCE_NETHER_ORES.get());
    }

    private void makeBlockList() {
        RESOURCE_NETHER_ORES_BLOCKS_WITH_FALLBACK.put(BlockRegistry.NETHER_COAL_ORE.get(), Blocks.COAL_ORE);
        RESOURCE_NETHER_ORES_BLOCKS_WITH_FALLBACK.put(BlockRegistry.NETHER_COPPER_ORE.get(), Blocks.COPPER_ORE);
        RESOURCE_NETHER_ORES_BLOCKS_WITH_FALLBACK.put(BlockRegistry.NETHER_IRON_ORE.get(), Blocks.IRON_ORE);
        RESOURCE_NETHER_ORES_BLOCKS_WITH_FALLBACK.put(BlockRegistry.NETHER_DIAMOND_ORE.get(), Blocks.DIAMOND_ORE);
        RESOURCE_NETHER_ORES_BLOCKS_WITH_FALLBACK.put(BlockRegistry.NETHER_EMERALD_ORE.get(), Blocks.EMERALD_ORE);
        RESOURCE_NETHER_ORES_BLOCKS_WITH_FALLBACK.put(BlockRegistry.NETHER_LAPIS_ORE.get(), Blocks.LAPIS_ORE);
        RESOURCE_NETHER_ORES_BLOCKS_WITH_FALLBACK.put(BlockRegistry.NETHER_REDSTONE_ORE.get(), Blocks.REDSTONE_ORE);
    }

    @Override
    public void registerBlocks() {
        for (Entry<Block, Block> entry : RESOURCE_NETHER_ORES_BLOCKS_WITH_FALLBACK.entrySet()) {
            POLYMER_BLOCKS.put(entry.getKey(), new SuppliedPolymerTexturedBlock((state, context) -> ResourceNetherOresPolymerStates.getBlockState(state.getBlock()), entry.getValue()));
        }
    }

    @Override
    public void registerBlockOverlays() {
        for (Entry<Block, PolymerBlock> entry : POLYMER_BLOCKS.entrySet()) {
            PolymerPlugin.registerBlock(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void registerItemOverlays() {
        for (Entry<Block, Block> entry : RESOURCE_NETHER_ORES_BLOCKS_WITH_FALLBACK.entrySet()) {
            PolymerPlugin.registerItemOverlay(entry.getKey().asItem(), entry.getValue().asItem());
        }
    }
}
