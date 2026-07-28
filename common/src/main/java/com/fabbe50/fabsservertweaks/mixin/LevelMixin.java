package com.fabbe50.fabsservertweaks.mixin;

import com.fabbe50.fabsservertweaks.events.ExtendedBlockEvent;
import dev.architectury.event.EventResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Level.class)
public class LevelMixin {
    @Inject(method = "setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;II)Z", at = @At("RETURN"), cancellable = true)
    private void injectSetBlock(BlockPos pos, BlockState blockState, int updateFlags, int updateLimit, CallbackInfoReturnable<Boolean> cir) {
        EventResult result = ExtendedBlockEvent.BLOCK_UPDATE.invoker().blockUpdate((Level)(Object) this, pos, blockState);
        if (result.interruptsFurtherEvaluation()) {
            cir.setReturnValue(result.value());
        }
    }
}
