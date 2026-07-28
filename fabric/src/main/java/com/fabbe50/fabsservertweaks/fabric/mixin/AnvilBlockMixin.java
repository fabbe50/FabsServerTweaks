package com.fabbe50.fabsservertweaks.fabric.mixin;

import com.fabbe50.fabsservertweaks.fabric.FabricPluginHelper;
import com.fabbe50.fabsservertweaks.fabric.plugins.fabs.FabsPolymerPlugin;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AnvilBlock.class)
public class AnvilBlockMixin {
    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    private static void injectDamage(BlockState blockState, CallbackInfoReturnable<BlockState> cir) {
        if (FabricPluginHelper.arePolymerComponentsLoaded()) {
            if (blockState.is(Blocks.DAMAGED_ANVIL)) {
                cir.setReturnValue(FabsPolymerPlugin.BROKEN_ANVIL_BLOCK.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, blockState.getValue(BlockStateProperties.HORIZONTAL_FACING)));
            }
        }
    }
}
