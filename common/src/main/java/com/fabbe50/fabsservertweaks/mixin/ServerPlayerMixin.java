package com.fabbe50.fabsservertweaks.mixin;

import com.fabbe50.fabsservertweaks.registries.ModGameRules;
import com.fabbe50.fabsservertweaks.util.BedUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayer.RespawnConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import com.llamalad7.mixinextras.injector.WrapWithCondition;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin {
    @WrapWithCondition(
            method = "startSleepInBed",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;setRespawnPosition(Lnet/minecraft/server/level/ServerPlayer$RespawnConfig;Z)V"),
            require = 0
    )
    private boolean fabsServerTweaks$skipRespawnForSleepingBags(ServerPlayer instance, RespawnConfig arg, boolean bl) {
        ServerLevel level = instance.level();
        return !BedUtil.isSleepingBag(level, arg.respawnData().pos(), level.getBlockState(arg.respawnData().pos()))
                || !level.getGameRules().get(ModGameRules.RULE_SLEEPING_BAGS_ENABLED);
    }
}
