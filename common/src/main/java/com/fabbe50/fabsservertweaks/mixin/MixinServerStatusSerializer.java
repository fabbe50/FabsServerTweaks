package com.fabbe50.fabsservertweaks.mixin;

import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.status.ServerStatus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(ServerStatus.class)
public class MixinServerStatusSerializer {
    @Inject(method = "<init>", at = @At("TAIL"))
    public void injectInit(Component component, Optional optional, Optional optional2, Optional optional3, boolean bl, CallbackInfo ci) {
        bl = true;
    }
}
