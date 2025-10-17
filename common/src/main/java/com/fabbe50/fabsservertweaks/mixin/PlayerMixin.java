package com.fabbe50.fabsservertweaks.mixin;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class PlayerMixin {
    @Inject(method = "getDestroySpeed(Lnet/minecraft/world/level/block/state/BlockState;)F", at = @At("RETURN"), cancellable = true)
    private void ft_removeSwimPenaltyWithAquaAffinity(BlockState state, CallbackInfoReturnable<Float> cir) {
        Player instance = (Player)(Object) this;
        if (instance.isInWater() && hasAquaAffinity(instance) && !instance.onGround()) {
            cir.setReturnValue(cir.getReturnValueF() * 5.0F);
        }
    }

    @Unique
    private static boolean hasAquaAffinity(Player player) {
        Holder<Enchantment> enchantmentHolder = player.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.AQUA_AFFINITY);
        ItemStack headStack = player.getItemBySlot(EquipmentSlot.HEAD);
        if (headStack.isEmpty()) {
            return false;
        }
        return EnchantmentHelper.getItemEnchantmentLevel(enchantmentHolder, headStack) > 0;
    }
}
