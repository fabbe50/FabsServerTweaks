package com.fabbe50.fabsservertweaks.fabric.plugins.lootr;

import com.fabbe50.fabsservertweaks.LogUtil;
import com.fabbe50.fabsservertweaks.fabric.plugins.polymer.PolymerElementHolder;
import com.mojang.datafixers.util.Pair;
import eu.pb4.polymer.virtualentity.api.ElementHolder;
import eu.pb4.polymer.virtualentity.api.elements.BlockDisplayElement;
import eu.pb4.polymer.virtualentity.api.elements.VirtualElement;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.util.Brightness;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import noobanidus.mods.lootr.common.api.data.blockentity.ILootrBlockEntity;
import org.joml.Vector3f;

import java.util.*;

public class LootrElementHolder extends PolymerElementHolder {
    private final LinkedHashMap<UUID, VirtualElement> openedVisuals = new LinkedHashMap<>();

    @Override
    protected void onTick() {
        var attachment = getAttachment();
        if (attachment == null) {
            return;
        }

        ServerLevel level = attachment.getWorld();
        BlockPos pos = BlockPos.containing(attachment.getPos());

        BlockState state = level.getBlockState(pos);

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof ILootrBlockEntity lootrBlockEntity)) {
            return;
        }

        List<ServerPlayer> watchingPlayers = getWatchingPlayers().stream().map(ServerGamePacketListenerImpl::getPlayer).toList();

        for (ServerPlayer player : watchingPlayers) {
            if (lootrBlockEntity.hasServerOpened(player)) {
                showOpenedVisual(level, player, pos, state);
            } else {
                hideOpenedVisual(player);
            }
        }
    }

    private void showOpenedVisual(ServerLevel level, ServerPlayer player, BlockPos pos, BlockState state) {
        UUID uuid = player.getUUID();
        if (openedVisuals.containsKey(uuid)) {
            return;
        }
        BlockState stateToRender = LootrPolymerStates.getOpenStateFromState(state);
        if (stateToRender == null) {
            return;
        }
        LogUtil.log("Making element for state " + state + " / " + stateToRender + " at position " + pos + " for player: " + player.getDisplayName().getString());
        BlockDisplayElement element = new BlockDisplayElement(stateToRender);
        element.setOffset(new Vec3(-0.505, -0.505, -0.505));
        element.setScale(new Vector3f(1.01f, 1.01f, 1.01f));

        if (level == null) {
            return;
        }
        int brightnessBlock = 0;
        int brightnessSky = 0;
        for (Direction direction : Direction.values()) {
            int brightnessNeighbor = level.getBrightness(LightLayer.BLOCK, pos.relative(direction));
            if (brightnessBlock < brightnessNeighbor) {
                brightnessBlock = brightnessNeighbor;
            }
            brightnessNeighbor = level.getBrightness(LightLayer.SKY, pos.relative(direction));
            if (brightnessSky < brightnessNeighbor) {
                brightnessSky = brightnessNeighbor;
            }
        }
        Brightness brightness = new Brightness(brightnessBlock, brightnessSky);
        element.setBrightness(brightness);
        if (element != null) {
            BlockDisplayElement blockDisplayElement = addElement(element);
            openedVisuals.put(uuid, blockDisplayElement);
            LogUtil.log("Showing element to player: " + player.getDisplayName().getString());
        }
    }

    private void hideOpenedVisual(ServerPlayer player) {
        UUID uuid = player.getUUID();
        if (!openedVisuals.containsKey(uuid)) {
            return;
        }
        VirtualElement element = openedVisuals.remove(uuid);
        if (element != null) {
            removeElement(element);
            LogUtil.log("Removing element for player: " + player.getDisplayName().getString());
        }
    }
}
