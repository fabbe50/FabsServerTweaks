package com.fabbe50.fabsservertweaks.mixin;

import com.fabbe50.fabsservertweaks.registries.ModGameRules;
import com.fabbe50.fabsservertweaks.util.BedUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin {
    @Shadow
    public abstract void setRespawnPosition(@Nullable ServerPlayer.RespawnConfig respawnConfig, boolean bl);

    @Redirect(method = "startSleepInBed",  at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;setRespawnPosition(Lnet/minecraft/server/level/ServerPlayer$RespawnConfig;Z)V"))
    private void redirectSetRespawnPositionFromStartSleepingInBed(ServerPlayer instance, ServerPlayer.RespawnConfig respawnConfig, boolean bl) {
        ServerLevel level = instance.level();
        if ((!BedUtil.isSleepingBag(level, respawnConfig.pos(), level.getBlockState(respawnConfig.pos()))) || (!level.getGameRules().getBoolean(ModGameRules.RULE_SLEEPING_BAGS_ENABLED))) {
            instance.setRespawnPosition(respawnConfig, true);
        }
    }
}
