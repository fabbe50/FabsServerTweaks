package com.fabbe50.fabsservertweaks.mixin;

import com.fabbe50.fabsservertweaks.registries.ModGameRules;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Shulker.class)
public class ShulkerMixin {
    @Shadow @Final protected static EntityDataAccessor<Byte> DATA_COLOR_ID;

    @Inject(method = "finalizeSpawn", at = @At("RETURN"))
    private void injectFinalizeSpawn(ServerLevelAccessor serverLevelAccessor, DifficultyInstance difficultyInstance, EntitySpawnReason entitySpawnReason, SpawnGroupData spawnGroupData, CallbackInfoReturnable<SpawnGroupData> cir) {
        if (serverLevelAccessor.getLevel().getGameRules().getBoolean(ModGameRules.RULE_SHULKERS_RANDOM_COLOR)) {
            Shulker INSTANCE = ((Shulker) (Object) this);
            INSTANCE.getEntityData().set(DATA_COLOR_ID, (byte) DyeColor.byId(serverLevelAccessor.getRandom().nextInt(16)).getId());
        }
    }

    @Inject(method = "teleportSomewhere", at = @At("HEAD"), cancellable = true)
    private void injectTeleportSomewhere(CallbackInfoReturnable<Boolean> cir) {
        Shulker INSTANCE = ((Shulker) (Object) this);
        if (INSTANCE.level() instanceof ServerLevel level) {
            if (!level.getGameRules().getBoolean(ModGameRules.RULE_SHULKERS_CAN_TELEPORT)) {
                cir.setReturnValue(false);
            }
        }
    }
}
