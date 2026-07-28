package com.fabbe50.fabsservertweaks.fabric.plugins;

import com.fabbe50.fabsservertweaks.Fabsservertweaks;
import com.fabbe50.fabsservertweaks.LogUtil;
import com.fabbe50.fabsservertweaks.fabric.FabricPluginHelper;
import com.fabbe50.fabsservertweaks.fabric.plugins.base.Plugin;
import com.fabbe50.fabsservertweaks.fabric.plugins.base.PolymerPlugin;
import com.fabbe50.fabsservertweaks.fabric.plugins.fabs.FabsPolymerPlugin;
import com.fabbe50.fabsservertweaks.fabric.plugins.lootr.LootrPlugin;
import com.fabbe50.fabsservertweaks.fabric.plugins.resource_nether_ores.ResourceNetherOresPlugin;
import com.fabbe50.fabsservertweaks.fabric.plugins.universal_ores.UniversalOresPlugin;
import com.fabbe50.fabsservertweaks.fabric.plugins.vanilla.VanillaPlugin;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.LinkedList;

public class Plugins {
    private static final LinkedList<Plugin> plugins = new LinkedList<>();

    public static void init() {
        if (FabricPluginHelper.arePolymerComponentsLoaded()) {
            plugins.add(new FabsPolymerPlugin());
            plugins.add(new VanillaPlugin());
        }
        if (FabricPluginHelper.areLootrPluginComponentsLoaded() && Fabsservertweaks.CONFIG.enableLootrPolymerPlugin) {
            plugins.add(new LootrPlugin());
        } else {
            LogUtil.warn("Lootr plugin disabled or components are not loaded, disabling Lootr support.");
        }
        if (FabricPluginHelper.areUniversalOresComponentsLoaded() && Fabsservertweaks.CONFIG.enableUniversalOresPlugin) {
            plugins.add(new UniversalOresPlugin());
        } else {
            LogUtil.warn("Universal Ores plugin disabled or components are not loaded, disabling Universal Ores support.");
        }
        if (FabricPluginHelper.areResourceNetherOresComponentsLoaded() && Fabsservertweaks.CONFIG.enableResourceNetherOresPlugin) {
            plugins.add(new ResourceNetherOresPlugin());
        } else {
            LogUtil.warn("Resource Nether Ores plugin disabled or components are not loaded, disabling Resource Nether Ores support.");
        }

        plugins.forEach(Plugin::initialize);
    }

    public static boolean breakBlockEvent(Level level, BlockPos pos, BlockState state, ServerPlayer player) {
        boolean success = false;
        for (Plugin plugin : plugins) {
            if (plugin instanceof PolymerPlugin polymerPlugin) {
                if (polymerPlugin.breakBlockEvent(level, pos, state, player)) {
                    success = true;
                }
            }
        }
        return success;
    }

    public static boolean placeBlockEvent(Level level, BlockPos pos, BlockState state, Entity placer) {
        boolean success = false;
        for (Plugin plugin : plugins) {
            if (plugin instanceof PolymerPlugin polymerPlugin) {
                if (polymerPlugin.placeBlockEvent(level, pos, state, placer)) {
                    success = true;
                }
            }
        }
        return success;
    }

    public static boolean rightClickBlockEvent(Player player, InteractionHand hand, BlockPos pos, Direction face) {
        boolean success = false;
        for (Plugin plugin : plugins) {
            if (plugin instanceof PolymerPlugin polymerPlugin) {
                if (polymerPlugin.rightClickBlockEvent(player, hand, pos, face)) {
                    success = true;
                }
            }
        }
        return success;
    }
}
