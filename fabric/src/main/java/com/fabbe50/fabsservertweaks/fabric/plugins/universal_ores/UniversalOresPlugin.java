package com.fabbe50.fabsservertweaks.fabric.plugins.universal_ores;

import com.fabbe50.fabsservertweaks.LogUtil;
import com.fabbe50.fabsservertweaks.fabric.FabricPluginHelper;
import com.fabbe50.fabsservertweaks.fabric.plugins.base.Plugin;
import com.fabbe50.fabsservertweaks.fabric.plugins.polymer.PolymerPlugin;
import com.fabbe50.fabsservertweaks.fabric.world.blocks.SuppliedPolymerTexturedBlock;
import eu.pb4.polymer.core.api.block.PolymerBlock;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import fr.hugman.universal_ores.block.DropExperienceRotatedPillarBlock;
import fr.hugman.universal_ores.block.UniversalOresBlocks;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class UniversalOresPlugin extends Plugin {
    public static final Map<Block, Block> UNIVERSAL_ORES_BLOCKS_WITH_FALLBACK = new HashMap<>();
    public static final Map<Block, PolymerBlock> POLYMER_BLOCKS = new HashMap<>();

    @Override
    public void init() {
        LogUtil.log("Initializing Universal Ores Plugin");

        PolymerResourcePackUtils.addModAssets(FabricPluginHelper.UNIVERSAL_ORES_ID);
        PolymerResourcePackUtils.markAsRequired();

        makeBlockList();

        UniversalOresPolymerStates.init();


    }

    private static void makeBlockList() {
        for (Block block : UniversalOresBlocks.COAL_ORES) {
            UNIVERSAL_ORES_BLOCKS_WITH_FALLBACK.put(block, Blocks.COAL_ORE);
        }
        for (Block block : UniversalOresBlocks.IRON_ORES) {
            UNIVERSAL_ORES_BLOCKS_WITH_FALLBACK.put(block, Blocks.IRON_ORE);
        }
        for (Block block : UniversalOresBlocks.GOLD_ORES) {
            UNIVERSAL_ORES_BLOCKS_WITH_FALLBACK.put(block, Blocks.GOLD_ORE);
        }
        for (Block block : UniversalOresBlocks.COPPER_ORES) {
            UNIVERSAL_ORES_BLOCKS_WITH_FALLBACK.put(block, Blocks.COPPER_ORE);
        }
        for (Block block : UniversalOresBlocks.LAPIS_ORES) {
            UNIVERSAL_ORES_BLOCKS_WITH_FALLBACK.put(block, Blocks.LAPIS_ORE);
        }
        for (Block block : UniversalOresBlocks.REDSTONE_ORES) {
            UNIVERSAL_ORES_BLOCKS_WITH_FALLBACK.put(block, Blocks.REDSTONE_ORE);
        }
        for (Block block : UniversalOresBlocks.EMERALD_ORES) {
            UNIVERSAL_ORES_BLOCKS_WITH_FALLBACK.put(block, Blocks.EMERALD_ORE);
        }
        for (Block block : UniversalOresBlocks.DIAMOND_ORES) {
            UNIVERSAL_ORES_BLOCKS_WITH_FALLBACK.put(block, Blocks.DIAMOND_ORE);
        }
        for (Block block : UniversalOresBlocks.NETHER_GOLD_ORES) {
            UNIVERSAL_ORES_BLOCKS_WITH_FALLBACK.put(block, Blocks.NETHER_GOLD_ORE);
        }
        for (Block block : UniversalOresBlocks.QUARTZ_ORES) {
            UNIVERSAL_ORES_BLOCKS_WITH_FALLBACK.put(block, Blocks.NETHER_QUARTZ_ORE);
        }
    }

    @Override
    public void registerBlocks() {
        for (Entry<Block, Block> entry : UNIVERSAL_ORES_BLOCKS_WITH_FALLBACK.entrySet()) {
            if (entry.getKey() instanceof DropExperienceRotatedPillarBlock) {
                POLYMER_BLOCKS.put(entry.getKey(), new SuppliedPolymerTexturedBlock((state, context) -> {
                    Axis axis = state.hasProperty(BlockStateProperties.AXIS) ? state.getValue(BlockStateProperties.AXIS) : Axis.Y;
                    return UniversalOresPolymerStates.getBlockState(state.getBlock(), axis);
                }, entry.getValue()));
            } else {
                POLYMER_BLOCKS.put(entry.getKey(), new SuppliedPolymerTexturedBlock((state, context) -> UniversalOresPolymerStates.getBlockState(state.getBlock()), entry.getValue()));
            }
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
        for (Entry<Block, Block> entry : UNIVERSAL_ORES_BLOCKS_WITH_FALLBACK.entrySet()) {
            PolymerPlugin.registerItemOverlay(entry.getKey().asItem(), entry.getValue().asItem());
        }
    }
}
