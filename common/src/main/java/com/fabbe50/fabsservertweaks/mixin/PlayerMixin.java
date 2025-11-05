package com.fabbe50.fabsservertweaks.mixin;

import com.fabbe50.fabsservertweaks.util.EnchantmentUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class PlayerMixin {
    @Inject(method = "getDestroySpeed(Lnet/minecraft/world/level/block/state/BlockState;)F", at = @At("RETURN"))
    private void ft_removeSwimPenaltyWithAquaAffinity(BlockState state, CallbackInfoReturnable<Float> cir) {
        Player instance = (Player)(Object) this;
        AttributeInstance miningSpeed = instance.getAttribute(Attributes.BLOCK_BREAK_SPEED);
        AttributeInstance submergedMiningSpeedAttribute = instance.getAttribute(Attributes.SUBMERGED_MINING_SPEED);
        if (miningSpeed != null && submergedMiningSpeedAttribute != null) {
            miningSpeed.addOrReplacePermanentModifier(new AttributeModifier(ResourceLocation.parse("minecraft:enchantment.aqua_affinity"), 1.0, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
            if (instance.isInWater() && EnchantmentUtil.hasAquaAffinity(instance) && !instance.onGround()) {
                if (!instance.isEyeInFluid(FluidTags.WATER)) {
                    miningSpeed.addOrReplacePermanentModifier(new AttributeModifier(ResourceLocation.parse("minecraft:enchantment.aqua_affinity"), 5.0, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
                }
                submergedMiningSpeedAttribute.addOrReplacePermanentModifier(new AttributeModifier(ResourceLocation.parse("minecraft:enchantment.aqua_affinity"), 3.0, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
            } else {
                submergedMiningSpeedAttribute.addOrReplacePermanentModifier(new AttributeModifier(ResourceLocation.parse("minecraft:enchantment.aqua_affinity"), 0.2, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
            }
        }
    }
}
