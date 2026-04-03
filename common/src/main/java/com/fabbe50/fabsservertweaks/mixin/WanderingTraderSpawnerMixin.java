package com.fabbe50.fabsservertweaks.mixin;

import com.fabbe50.fabsservertweaks.LogUtil;
import com.fabbe50.fabsservertweaks.registries.ModGameRules;
import com.fabbe50.fabsservertweaks.util.WorldUtil;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.npc.wanderingtrader.WanderingTraderSpawner;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(WanderingTraderSpawner.class)
public class WanderingTraderSpawnerMixin {
    @Inject(method = "spawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;getBiome(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/core/Holder;"), cancellable = true)
    private void injectSpawn(ServerLevel serverLevel, CallbackInfoReturnable<Boolean> cir, @Local(ordinal = 2) BlockPos pos) {
        LogUtil.log("Attempting to spawn wandering trader at: " + pos);
        if (serverLevel.getGameRules().get(ModGameRules.RULE_NO_SOLICITING_SIGN)) {
            List<BlockPos> blockPositions = WorldUtil.getBlockPositions(new AABB(pos).inflate(96, 48, 96));
            blockPositions.forEach(blockPos -> {
                BlockState state = serverLevel.getBlockState(blockPos);
                if (state.hasBlockEntity() && serverLevel.getBlockEntity(blockPos) instanceof SignBlockEntity signBlockEntity) {
                    Component[] frontText = signBlockEntity.getFrontText().getMessages(false);
                    Component[] backText = signBlockEntity.getBackText().getMessages(false);
                    if (checkText(frontText) || checkText(backText)) {
                        LogUtil.log("Prevented trader spawn.");
                        cir.setReturnValue(false);
                    }
                }
            });
        }
    }

    @Unique
    private boolean checkText(Component[] textLines) {
        for (Component component : textLines) {
            if (component.getString().equalsIgnoreCase("No Soliciting")) {
                return true;
            }
        }
        return false;
    }
}
