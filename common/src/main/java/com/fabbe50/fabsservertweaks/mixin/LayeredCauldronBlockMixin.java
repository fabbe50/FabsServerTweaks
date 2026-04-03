package com.fabbe50.fabsservertweaks.mixin;

import com.fabbe50.fabsservertweaks.data.CauldronConversionData;
import com.fabbe50.fabsservertweaks.data.loader.CauldronConversionLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LayeredCauldronBlock.class)
public abstract class LayeredCauldronBlockMixin {
    @Shadow
    public abstract boolean isFull(BlockState arg);

    @Inject(method = "entityInside", at = @At("HEAD"))
    private void injectEntityInside(BlockState blockState, Level level, BlockPos blockPos, Entity entity, InsideBlockEffectApplier insideBlockEffectApplier, boolean bl, CallbackInfo ci) {
        if (level instanceof ServerLevel) {
            if (this.isFull(blockState)) {
                if (entity instanceof ItemEntity itemEntity) {
                    ItemStack inputStack = itemEntity.getItem();
                    for (CauldronConversionData conversionData : CauldronConversionLoader.INSTANCE.getDataMap().values()) {
                        Identifier inputLocation = conversionData.input();
                        Identifier outputLocation = conversionData.output();
                        if (inputLocation != null && outputLocation != null) {
                            Identifier itemInside = inputStack.getItem().arch$registryName();
                            if (itemInside != null) {
                                if (itemInside.toString().equals(inputLocation.toString())) {
                                    Item outputItem = level.registryAccess().lookup(Registries.ITEM).orElseThrow().getValue(outputLocation);
                                    if (outputItem != null) {
                                        itemEntity.setItem(new ItemStack(outputItem, inputStack.getCount()));
                                        RandomSource random = level.getRandom();
                                        itemEntity.addDeltaMovement(new Vec3((random.nextDouble() - 0.5d) * 0.08d, 0.4d, (random.nextDouble() - 0.5d) * 0.08d));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
