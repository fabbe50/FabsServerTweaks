package com.fabbe50.fabsservertweaks.mixin;

import com.fabbe50.fabsservertweaks.registries.ModRegistry;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.piston.PistonStructureResolver;
import net.minecraft.world.level.material.PushReaction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PistonStructureResolver.class)
public abstract class PistonStructureResolverMixin {
    @Redirect(
        method = {"resolve", "addBlockLine"},
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/state/BlockState;getPistonPushReaction()Lnet/minecraft/world/level/material/PushReaction;"
        )
    )
    private PushReaction pistonMoveOverride(BlockState blockState) {
        PushReaction pushReaction = blockState.getPistonPushReaction();
        if (blockState.is(ModRegistry.PISTON_MOVE_OVERRIDE) && pushReaction == PushReaction.DESTROY) {
            return PushReaction.NORMAL;
        }

        return pushReaction;
    }
}
