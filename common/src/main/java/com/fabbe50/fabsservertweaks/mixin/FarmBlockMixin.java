package com.fabbe50.fabsservertweaks.mixin;

import com.fabbe50.fabsservertweaks.registries.ModGameRules;
import com.fabbe50.fabsservertweaks.registries.gamerules.CropTrampleValue;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FarmBlock.class)
public class FarmBlockMixin extends Block {
    public FarmBlockMixin(Properties properties) {
        super(properties);
    }

    @Inject(method = "fallOn", at = @At("HEAD"), cancellable = true)
    private void injectFallOn(Level level, BlockState blockState, BlockPos blockPos, Entity entity, double d, CallbackInfo ci) {
        if (level instanceof ServerLevel serverLevel) {
            CropTrampleValue cropTrampleValue = serverLevel.getGameRules().getRule(ModGameRules.RULE_CROP_TRAMPLE_MODE);
            if (cropTrampleValue.getValue().equals(CropTrampleValue.CropTrampleMode.FEATHER_FALLING)) {
                if (entity instanceof LivingEntity livingEntity) {
                    ItemStack boots = livingEntity.getItemBySlot(EquipmentSlot.FEET);
                    if (EnchantmentHelper.getItemEnchantmentLevel(serverLevel.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.FEATHER_FALLING), boots) > 0) {
                        super.fallOn(level, blockState, blockPos, entity, d);
                        ci.cancel();
                    }
                }
            } else if (cropTrampleValue.getValue().equals(CropTrampleValue.CropTrampleMode.NO_TRAMPLE)) {
                super.fallOn(level, blockState, blockPos, entity, d);
                ci.cancel();
            }
        }
    }
}
