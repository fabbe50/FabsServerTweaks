package com.fabbe50.fabsservertweaks.fabric.plugins.polymer;

import com.fabbe50.fabsservertweaks.LogUtil;
import eu.pb4.polymer.virtualentity.api.BlockWithElementHolder;
import eu.pb4.polymer.virtualentity.api.ElementHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;

public class PolymerBlockWithElementHolder implements BlockWithElementHolder {
    private final PolymerElementHolder elementHolder;
    private final PolymerElementSupplier elementSupplier;
    private final boolean shouldTick;

    public PolymerBlockWithElementHolder(PolymerElementHolder elementHolder, boolean shouldTick) {
        this.elementHolder = elementHolder;
        this.elementSupplier = null;
        this.shouldTick = shouldTick;
    }

    public PolymerBlockWithElementHolder(PolymerElementHolder elementHolder, PolymerElementSupplier elementSupplier, boolean shouldTick) {
        this.elementHolder = elementHolder;
        this.elementSupplier = elementSupplier;
        this.shouldTick = shouldTick;
    }

    @Override
    public boolean tickElementHolder(ServerLevel world, BlockPos pos, BlockState initialBlockState) {
        return shouldTick;
    }

    @Override
    public @Nullable ElementHolder createElementHolder(ServerLevel world, BlockPos pos, BlockState initialBlockState) {
        try {
            PolymerElementHolder instancedElementHolder = elementHolder.getClass().getDeclaredConstructor().newInstance();
            if (elementSupplier != null) {
                return elementSupplier.createElementHolder(instancedElementHolder, world, pos, initialBlockState);
            }
            return instancedElementHolder;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            LogUtil.warn("Failed to create element-holder...");
            return new ElementHolder();
        }
    }
}
