package com.fabbe50.fabsservertweaks.mixin;

import com.fabbe50.fabsservertweaks.registries.ModGameRules;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.protocol.game.ClientboundExplodePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerExplosion;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(ServerLevel.class)
public class ServerLevelMixin {
    @Inject(method = "explode", at = @At(value = "HEAD"), cancellable = true)
    private void injectExplode(Entity entity, DamageSource damageSource, ExplosionDamageCalculator explosionDamageCalculator, double d, double e, double f, float g, boolean bl, Level.ExplosionInteraction explosionInteraction, ParticleOptions particleOptions, ParticleOptions particleOptions2, Holder<SoundEvent> holder, CallbackInfo ci) {
        if (explosionInteraction.equals(Level.ExplosionInteraction.MOB)) {
            ServerLevel instance = ((ServerLevel) (Object) this);
            if (!(instance.getGameRules().getBoolean(ModGameRules.RULE_MOB_GRIEF_CREEPER)) && entity instanceof Creeper) {
                Explosion.BlockInteraction blockInteraction = Explosion.BlockInteraction.KEEP;
                Vec3 vec3 = new Vec3(d, e, f);
                ServerExplosion serverExplosion = new ServerExplosion(instance, entity, damageSource, explosionDamageCalculator, vec3, g, bl, blockInteraction);
                serverExplosion.explode();
                ParticleOptions particleOptions3 = serverExplosion.isSmall() ? particleOptions : particleOptions2;

                for(ServerPlayer serverPlayer : instance.players()) {
                    if (serverPlayer.distanceToSqr(vec3) < (double)4096.0F) {
                        Optional<Vec3> optional = Optional.ofNullable((Vec3)serverExplosion.getHitPlayers().get(serverPlayer));
                        serverPlayer.connection.send(new ClientboundExplodePacket(vec3, optional, particleOptions3, holder));
                    }
                }
                ci.cancel();
            }
        }
    }
}
