package com.fabbe50.fabsservertweaks.fabric.plugins.base;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public abstract class PolymerPlugin extends Plugin implements IPolymerPlugin {
    @Override
    public void initialize() {
        super.initialize();
        bootstrap();
    }

    public void bootstrap() {
        if (bootstrapped) {
            return;
        }
        bootstrapped = true;

        registerBlocks();
        registerBlockOverlays();
        registerItems();
        registerItemOverlays();
        registerElementHolderOverlays();
        registerBlockEntities();
        registerEntities();
    }

    public boolean breakBlockEvent(Level level, BlockPos pos, BlockState state, ServerPlayer player) {
        return false;
    }

    public boolean placeBlockEvent(Level level, BlockPos pos, BlockState state, Entity placer) {
        return false;
    }

    public boolean rightClickBlockEvent(Player player, InteractionHand hand, BlockPos pos, Direction face) {
        return false;
    }
}
