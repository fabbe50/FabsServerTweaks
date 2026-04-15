package com.fabbe50.fabsservertweaks.util.interfaces;

import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.Nullable;

public interface PistonBlockEntityMover {
    void fabsservertweaks$setMovedBlockEntityData(@Nullable CompoundTag tag, DataComponentMap components);
}
