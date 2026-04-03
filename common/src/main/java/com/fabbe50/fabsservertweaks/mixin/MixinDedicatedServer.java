package com.fabbe50.fabsservertweaks.mixin;

import net.minecraft.server.dedicated.DedicatedServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DedicatedServer.class)
public class MixinDedicatedServer {
    @Inject(method = "enforceSecureProfile", at = @At("RETURN"), cancellable = true)
    private void onEnforceSecureProfile(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }
}
