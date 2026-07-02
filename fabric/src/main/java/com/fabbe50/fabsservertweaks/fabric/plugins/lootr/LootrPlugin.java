package com.fabbe50.fabsservertweaks.fabric.plugins.lootr;

import com.fabbe50.fabsservertweaks.Fabsservertweaks;
import com.fabbe50.fabsservertweaks.LogUtil;
import com.fabbe50.fabsservertweaks.fabric.FabricPluginHelper;
import com.fabbe50.fabsservertweaks.fabric.plugins.base.Plugin;
import com.fabbe50.fabsservertweaks.fabric.plugins.polymer.PolymerBlockWithElementHolder;
import com.fabbe50.fabsservertweaks.fabric.plugins.polymer.PolymerPlugin;
import com.fabbe50.fabsservertweaks.fabric.plugins.util.PathUtil;
import com.fabbe50.fabsservertweaks.fabric.plugins.util.PathUtil.FolderType;
import com.fabbe50.fabsservertweaks.fabric.registries.ModRegistry;
import com.fabbe50.fabsservertweaks.fabric.world.blocks.*;
import eu.pb4.polymer.core.api.block.PolymerBlock;
import eu.pb4.polymer.core.api.item.PolymerCreativeModeTabUtils;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import eu.pb4.polymer.rsm.api.RegistrySyncUtils;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.fabric.api.resource.v1.pack.PackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import noobanidus.mods.lootr.fabric.init.*;

import java.util.LinkedHashMap;

public class LootrPlugin extends Plugin {
    private static final PolymerStateSupplier DEFAULT_STATE_SUPPLIER = (state, context) -> LootrPolymerStates.getBlockState(state, false);
    private static final PolymerStateSupplier DEFAULT_STATE_SUPPLIER_OPEN = (state, context) -> LootrPolymerStates.getBlockState(state, true);
    private static final PolymerStateSupplier HORIZONTAL_ROTATABLE = (state, context) -> {
        Direction facing = state.hasProperty(BlockStateProperties.HORIZONTAL_FACING) ? state.getValue(BlockStateProperties.HORIZONTAL_FACING) : Direction.NORTH;
        return LootrPolymerStates.getBlockState(state, facing, false);
    };
    private static final PolymerStateSupplier HORIZONTAL_ROTATABLE_OPEN = (state, context) -> {
        Direction facing = state.hasProperty(BlockStateProperties.HORIZONTAL_FACING) ? state.getValue(BlockStateProperties.HORIZONTAL_FACING) : Direction.NORTH;
        return LootrPolymerStates.getBlockState(state, facing, true);
    };
    private static final PolymerStateSupplier ROTATABLE = (state, context) -> {
        Direction facing = state.hasProperty(BlockStateProperties.FACING) ? state.getValue(BlockStateProperties.FACING) : Direction.NORTH;
        return LootrPolymerStates.getBlockState(state, facing, false);
    };
    private static final PolymerStateSupplier ROTATABLE_OPEN = (state, context) -> {
        Direction facing = state.hasProperty(BlockStateProperties.FACING) ? state.getValue(BlockStateProperties.FACING) : Direction.NORTH;
        return LootrPolymerStates.getBlockState(state, facing, true);
    };
    private static final PolymerStateSupplier ROTATABLE_OPENABLE = (state, context) -> {
        Direction facing = state.hasProperty(BlockStateProperties.FACING) ? state.getValue(BlockStateProperties.FACING) : Direction.NORTH;
        Boolean open = state.hasProperty(BlockStateProperties.OPEN) ? state.getValue(BlockStateProperties.OPEN) : false;
        if (open) {
            return LootrPolymerStates.getSpecificState(LootrBlocks.OPENED_BARREL, facing, false);
        } else {
            return LootrPolymerStates.getSpecificState(LootrBlocks.BARREL, facing, false);
        }
    };
    private static final PolymerStateSupplier ROTATABLE_OPENABLE_OPEN = (state, context) -> {
        Direction facing = state.hasProperty(BlockStateProperties.FACING) ? state.getValue(BlockStateProperties.FACING) : Direction.NORTH;
        Boolean open = state.hasProperty(BlockStateProperties.OPEN) ? state.getValue(BlockStateProperties.OPEN) : false;
        if (open) {
            return LootrPolymerStates.getSpecificState(LootrBlocks.OPENED_BARREL, facing, true);
        } else {
            return LootrPolymerStates.getSpecificState(LootrBlocks.BARREL, facing, true);
        }
    };


    public static final PolymerBlock LOOTR_CHEST = new SuppliedPolymerTexturedBlock(HORIZONTAL_ROTATABLE);
    public static final PolymerBlock LOOTR_TRAPPED_CHEST = new SuppliedPolymerTexturedBlock(HORIZONTAL_ROTATABLE);
    public static final PolymerBlock LOOTR_BARREL = new SuppliedPolymerTexturedBlock(ROTATABLE_OPENABLE, Blocks.BARREL);
    public static final PolymerBlock LOOTR_SHULKER = new SuppliedPolymerTexturedBlock(ROTATABLE);
//    public static final PolymerBlock LOOTR_DECORATED_POT = new SuppliedPolymerTexturedBlock(HORIZONTAL_ROTATABLE);
    public static final PolymerBlock LOOTR_TROPHY = new SuppliedPolymerTexturedBlock(HORIZONTAL_ROTATABLE, Blocks.PLAYER_HEAD);
    public static Block LOOTR_CHEST_OPENED;
    public static Block LOOTR_TRAPPED_CHEST_OPENED;
    public static Block LOOTR_BARREL_OPENED;
    public static Block LOOTR_SHULKER_OPENED;
//    public static Block LOOTR_DECORATED_POT_OPENED;
    public static Block LOOTR_SUSPICIOUS_SAND_OPENED;
    public static Block LOOTR_SUSPICIOUS_GRAVEL_OPENED;

    private final static LinkedHashMap<LootrBlocks, Block> openedBlocksMap = new LinkedHashMap<>();

    @Override
    public void init() {
        LogUtil.log("Initializing Lootr Plugin");

        FabricLoader.getInstance().getModContainer(Fabsservertweaks.MOD_ID).ifPresent(modContainer ->
                ResourceLoader.registerBuiltinPack(
                        Fabsservertweaks.location("lootr_plugin", "lootr_plugin"),
                        modContainer,
                        Component.literal("Lootr Plugin"),
                        PackActivationType.ALWAYS_ENABLED
                )
        );

        LogUtil.log("Registering Lootr Assets");
        PolymerResourcePackUtils.addModAssets(FabricPluginHelper.LOOTR_ID);
        LogUtil.log("Setting up lootr resource events.");
        PolymerResourcePackUtils.RESOURCE_PACK_CREATION_EVENT.register(resourcePackBuilder -> {
            LogUtil.log("Adding custom lootr models to resource pack.");
            PolymerPlugin.addAsset(resourcePackBuilder, "resourcepacks/lootr_plugin/assets", "lootr_plugin", "models/block/lootr_chest.json");
            PolymerPlugin.addAsset(resourcePackBuilder, "resourcepacks/lootr_plugin/assets", "lootr_plugin", "models/block/lootr_chest_opened.json");
            PolymerPlugin.addAsset(resourcePackBuilder, "resourcepacks/lootr_plugin/assets", "lootr_plugin", "models/block/lootr_trapped_chest.json");
            PolymerPlugin.addAsset(resourcePackBuilder, "resourcepacks/lootr_plugin/assets", "lootr_plugin", "models/block/lootr_trapped_chest_opened.json");
            PolymerPlugin.addAsset(resourcePackBuilder, "resourcepacks/lootr_plugin/assets", "lootr_plugin", "models/block/lootr_shulker.json");
            PolymerPlugin.addAsset(resourcePackBuilder, "resourcepacks/lootr_plugin/assets", "lootr_plugin", "models/block/lootr_shulker_opened.json");
            LogUtil.log("Adding custom lootr item registration to resource pack.");
            PolymerPlugin.addAsset(resourcePackBuilder, "resourcepacks/lootr_plugin/assets", "lootr_plugin", "items/lootr_barrel.json");
            PolymerPlugin.addAsset(resourcePackBuilder, "resourcepacks/lootr_plugin/assets", "lootr_plugin", "items/lootr_chest.json");
//            PolymerPlugin.addAsset(resourcePackBuilder, "resourcepacks/lootr_plugin/assets", "lootr_plugin", "items/lootr_decorated_pot.json");
            PolymerPlugin.addAsset(resourcePackBuilder, "resourcepacks/lootr_plugin/assets", "lootr_plugin", "items/lootr_shulker.json");
            PolymerPlugin.addAsset(resourcePackBuilder, "resourcepacks/lootr_plugin/assets", "lootr_plugin", "items/lootr_trapped_chest.json");
        });
        PolymerResourcePackUtils.RESOURCE_PACK_AFTER_INITIAL_CREATION_EVENT.register(resourcePackBuilder -> {
            LogUtil.log("Moving lootr entity textures to plugin location in resource pack.");
            PolymerPlugin.copyAsset(resourcePackBuilder, PathUtil.getPath("lootr", FolderType.ENTITY_TEXTURES, "chest", "normal", "png"), PathUtil.getPath("lootr_plugin", FolderType.BLOCK_TEXTURES, "lootr_chest", "png"));
            PolymerPlugin.copyAsset(resourcePackBuilder, PathUtil.getPath("lootr", FolderType.ENTITY_TEXTURES, "chest", "normal_opened", "png"), PathUtil.getPath("lootr_plugin", FolderType.BLOCK_TEXTURES, "lootr_chest_opened", "png"));
            PolymerPlugin.copyAsset(resourcePackBuilder, PathUtil.getPath("lootr", FolderType.ENTITY_TEXTURES, "chest", "trapped", "png"), PathUtil.getPath("lootr_plugin", FolderType.BLOCK_TEXTURES, "lootr_trapped_chest", "png"));
            PolymerPlugin.copyAsset(resourcePackBuilder, PathUtil.getPath("lootr", FolderType.ENTITY_TEXTURES, "chest", "trapped_opened", "png"), PathUtil.getPath("lootr_plugin", FolderType.BLOCK_TEXTURES, "lootr_trapped_chest_opened", "png"));
            PolymerPlugin.copyAsset(resourcePackBuilder, PathUtil.getPath("lootr", FolderType.ENTITY_TEXTURES, "shulker_box", "normal", "png"), PathUtil.getPath("lootr_plugin", FolderType.BLOCK_TEXTURES, "lootr_shulker", "png"));
            PolymerPlugin.copyAsset(resourcePackBuilder, PathUtil.getPath("lootr", FolderType.ENTITY_TEXTURES, "shulker_box", "normal_opened", "png"), PathUtil.getPath("lootr_plugin", FolderType.BLOCK_TEXTURES, "lootr_shulker_opened", "png"));
            LogUtil.log("Moving lootr item registration to lootr location in resource pack.");
            PolymerPlugin.copyAsset(resourcePackBuilder, PathUtil.getPath("lootr_plugin", FolderType.ITEMS, "lootr_barrel", "json"), PathUtil.getPath("lootr", FolderType.ITEMS, "barrel", "json"));
            PolymerPlugin.copyAsset(resourcePackBuilder, PathUtil.getPath("lootr_plugin", FolderType.ITEMS, "lootr_chest", "json"), PathUtil.getPath("lootr", FolderType.ITEMS, "chest", "json"));
//            PolymerPlugin.copyAsset(resourcePackBuilder, PathUtil.getPath("lootr_plugin", FolderType.ITEMS, "lootr_decorated_pot", "json"), PathUtil.getPath("lootr", FolderType.ITEMS, "decorated_pot", "json"));
            PolymerPlugin.copyAsset(resourcePackBuilder, PathUtil.getPath("lootr_plugin", FolderType.ITEMS, "lootr_shulker", "json"), PathUtil.getPath("lootr", FolderType.ITEMS, "shulker_box", "json"));
            PolymerPlugin.copyAsset(resourcePackBuilder, PathUtil.getPath("lootr_plugin", FolderType.ITEMS, "lootr_trapped_chest", "json"), PathUtil.getPath("lootr", FolderType.ITEMS, "trapped_chest", "json"));
        });
        LogUtil.log("Marking lootr resource pack as required.");
        PolymerResourcePackUtils.markAsRequired();
        LogUtil.log("Registering lootr polymer block states.");
        LootrPolymerStates.init();

        LogUtil.log("Setting lootr registry entries to polymer server entry.");
        //noinspection unchecked,rawtypes
        RegistrySyncUtils.setServerEntry((Registry) BuiltInRegistries.CUSTOM_STAT, (Object) ModStats.LOOTED_LOCATION);
        RegistrySyncUtils.setServerEntry(BuiltInRegistries.PARTICLE_TYPE, ModParticles.UNOPENED_PARTCLE);
        RegistrySyncUtils.setServerEntry(BuiltInRegistries.PARTICLE_TYPE, ModParticles.REFRESH_PARTICLE);

        LogUtil.log("Registering lootr creative mode tabs.");
        PolymerCreativeModeTabUtils.registerPolymerCreativeModeTab(Fabsservertweaks.location("polymer_lootr"), ModTabs.LOOTR_TAB);
    }

    @Override
    public void registerBlocks() {
        LogUtil.log("Registering lootr polymer blocks.");
        LOOTR_CHEST_OPENED = registerBlock(LootrBlocks.CHEST);
        LOOTR_TRAPPED_CHEST_OPENED = registerBlock(LootrBlocks.TRAPPED_CHEST);
        LOOTR_BARREL_OPENED = registerBlock(LootrBlocks.BARREL);
        LOOTR_SHULKER_OPENED = registerBlock(LootrBlocks.SHULKER);
//        LOOTR_DECORATED_POT_OPENED = registerBlock(LootrBlocks.DECORATED_POT);
        LOOTR_SUSPICIOUS_SAND_OPENED = registerBlock(LootrBlocks.SUSPICIOUS_SAND);
        LOOTR_SUSPICIOUS_GRAVEL_OPENED = registerBlock(LootrBlocks.SUSPICIOUS_GRAVEL);
        openedBlocksMap.put(LootrBlocks.CHEST, LOOTR_CHEST_OPENED);
        openedBlocksMap.put(LootrBlocks.TRAPPED_CHEST, LOOTR_TRAPPED_CHEST_OPENED);
        openedBlocksMap.put(LootrBlocks.BARREL, LOOTR_BARREL_OPENED);
        openedBlocksMap.put(LootrBlocks.SHULKER, LOOTR_SHULKER_OPENED);
//        openedBlocksMap.put(LootrBlocks.DECORATED_POT, LOOTR_DECORATED_POT_OPENED);
        openedBlocksMap.put(LootrBlocks.SUSPICIOUS_SAND, LOOTR_SUSPICIOUS_SAND_OPENED);
        openedBlocksMap.put(LootrBlocks.SUSPICIOUS_GRAVEL, LOOTR_SUSPICIOUS_GRAVEL_OPENED);
    }

    private Block registerBlock(LootrBlocks block) {
        return switch (block.getDirectional()) {
            case NO -> registerBlock(block.getOpenedIdentifier(), block.getOriginalBlock(), block.getPolymerBlock());
            case HORIZONTAL -> registerHorizontalBlock(block.getOpenedIdentifier(), block.getOriginalBlock(), block.getPolymerBlock());
            case ALL -> {
                if (block.equals(LootrBlocks.BARREL)) {
                    yield registerDirectionalBarrelBlock(block.getOpenedIdentifier(), block.getOriginalBlock(), block.getPolymerBlock());
                } else {
                    yield registerDirectionalBlock(block.getOpenedIdentifier(), block.getOriginalBlock(), block.getPolymerBlock());
                }
            }
        };
    }

    private Block registerBlock(Identifier id, Block originalBlock, Block polymerBlock) {
        return ModRegistry.register(id, properties -> new PolymerSimpleTexturedBlock(properties, polymerBlock, DEFAULT_STATE_SUPPLIER_OPEN), Properties.ofFullCopy(originalBlock).noCollision().noOcclusion().strength(-1, 3600000));
    }

    private Block registerHorizontalBlock(Identifier id, Block originalBlock, Block polymerBlock) {
        return ModRegistry.register(id, properties -> new PolymerHorizontalDirectionalTexturedBlock(properties, polymerBlock, HORIZONTAL_ROTATABLE_OPEN), Properties.ofFullCopy(originalBlock).noCollision().noOcclusion().strength(-1, 3600000));
    }

    private Block registerDirectionalBlock(Identifier id, Block originalBlock, Block polymerBlock) {
        return ModRegistry.register(id, properties -> new PolymerDirectionalTexturedBlock(properties, polymerBlock, ROTATABLE_OPEN), Properties.ofFullCopy(originalBlock).noCollision().noOcclusion().strength(-1, 3600000));
    }

    private Block registerDirectionalBarrelBlock(Identifier id, Block originalBlock, Block polymerBlock) {
        return ModRegistry.register(id, properties -> new LootrPolymerBarrelBlock(properties, polymerBlock, ROTATABLE_OPENABLE_OPEN), Properties.ofFullCopy(originalBlock).noCollision().noOcclusion().strength(-1, 3600000));
    }

    @Override
    public void registerBlockOverlays() {
        LogUtil.log("Registering lootr polymer block overlays.");
        PolymerPlugin.registerBlock(ModBlocks.CHEST, LOOTR_CHEST);
        PolymerPlugin.registerBlock(ModBlocks.TRAPPED_CHEST, LOOTR_TRAPPED_CHEST);
        PolymerPlugin.registerBlock(ModBlocks.BARREL, LOOTR_BARREL);
        PolymerPlugin.registerBlock(ModBlocks.SHULKER_BOX, LOOTR_SHULKER);
        PolymerPlugin.registerBlockOverlay(ModBlocks.SUSPICIOUS_SAND, Blocks.SUSPICIOUS_SAND);
        PolymerPlugin.registerBlockOverlay(ModBlocks.SUSPICIOUS_GRAVEL, Blocks.SUSPICIOUS_GRAVEL);
        PolymerPlugin.registerBlockOverlay(ModBlocks.DECORATED_POT, Blocks.DECORATED_POT);
        PolymerPlugin.registerBlock(ModBlocks.TROPHY, LOOTR_TROPHY);
    }

    @Override
    public void registerElementHolderOverlays() {
        LogUtil.log("Registering lootr polymer element holders.");
        PolymerPlugin.registerElementHolder(ModBlocks.CHEST, new PolymerBlockWithElementHolder(new LootrElementHolder(), true));
        PolymerPlugin.registerElementHolder(ModBlocks.TRAPPED_CHEST, new PolymerBlockWithElementHolder(new LootrElementHolder(), true));
        PolymerPlugin.registerElementHolder(ModBlocks.BARREL, new PolymerBlockWithElementHolder(new LootrElementHolder(), true));
        PolymerPlugin.registerElementHolder(ModBlocks.SHULKER_BOX, new PolymerBlockWithElementHolder(new LootrElementHolder(), true));
        PolymerPlugin.registerElementHolder(ModBlocks.SUSPICIOUS_SAND, new PolymerBlockWithElementHolder(new LootrElementHolder(), true));
        PolymerPlugin.registerElementHolder(ModBlocks.SUSPICIOUS_GRAVEL, new PolymerBlockWithElementHolder(new LootrElementHolder(), true));
//        PolymerPlugin.registerElementHolder(ModBlocks.DECORATED_POT, new PolymerBlockWithElementHolder(new LootrElementHolder(), true));
    }

    @Override
    public void registerItemOverlays() {
        LogUtil.log("Registering lootr polymer item overlays.");
        PolymerPlugin.registerItemOverlay(ModItems.CHEST, Items.CHEST);
        PolymerPlugin.registerItemOverlay(ModItems.BARREL, Items.BARREL);
        PolymerPlugin.registerItemOverlay(ModItems.TRAPPED_CHEST, Items.TRAPPED_CHEST);
        PolymerPlugin.registerItemOverlay(ModItems.SHULKER_BOX, Items.SHULKER_BOX);
        PolymerPlugin.registerItemOverlay(ModItems.SUSPICIOUS_SAND, Items.SUSPICIOUS_SAND);
        PolymerPlugin.registerItemOverlay(ModItems.SUSPICIOUS_GRAVEL, Items.SUSPICIOUS_GRAVEL);
        PolymerPlugin.registerItemOverlay(ModItems.DECORATED_POT, Items.DECORATED_POT);
        PolymerPlugin.registerItemOverlay(ModItems.TROPHY, Items.BARRIER);
    }

    @Override
    public void registerBlockEntities() {
        LogUtil.log("Registering lootr polymer block entities.");
        PolymerPlugin.registerBlockEntity(ModBlockEntities.CHEST, BlockEntityType.BARREL);
        PolymerPlugin.registerBlockEntity(ModBlockEntities.BARREL, BlockEntityType.BARREL);
        PolymerPlugin.registerBlockEntity(ModBlockEntities.TRAPPED_CHEST, BlockEntityType.BARREL);
        PolymerPlugin.registerBlockEntity(ModBlockEntities.SHULKER_BOX, BlockEntityType.BARREL);
        PolymerPlugin.registerBlockEntity(ModBlockEntities.BRUSHABLE_BLOCK, BlockEntityType.BRUSHABLE_BLOCK);
        PolymerPlugin.registerBlockEntity(ModBlockEntities.DECORATED_POT, BlockEntityType.DECORATED_POT);
    }

    @Override
    public void registerEntities() {
        LogUtil.log("Registering lootr polymer entities.");
        PolymerPlugin.registerEntity(ModEntities.MINECART_WITH_CHEST, EntityType.CHEST_MINECART);
        PolymerPlugin.registerEntity(ModEntities.ITEM_FRAME, EntityType.ITEM_FRAME);
    }

    public static Block getOpenedBlock(LootrBlocks block) {
        return openedBlocksMap.get(block);
    }
}
