package com.fabbe50.fabsservertweaks.mixin;

import com.fabbe50.fabsservertweaks.registries.ModGameRules;
import com.fabbe50.fabsservertweaks.util.ToolUtil;
import com.fabbe50.fabsservertweaks.util.WorldUtil;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Predicate;

@Mixin(HoeItem.class)
public abstract class HoeItemMixin extends Item {
    @Shadow
    @Final
    public static Map<Block, Pair<Predicate<UseOnContext>, Consumer<UseOnContext>>> TILLABLES;

    public HoeItemMixin(Properties properties) {
        super(properties);
    }

    @Inject(method = "useOn", at = @At("HEAD"), cancellable = true)
    private void injectUseOn(UseOnContext useOnContext, CallbackInfoReturnable<InteractionResult> cir) {
        Level level = useOnContext.getLevel();
        Player player = useOnContext.getPlayer();
        if (player == null) {
            return;
        }
        if (level instanceof ServerLevel serverLevel && serverLevel.getGameRules().getBoolean(ModGameRules.RULE_BETTER_HOES) && !player.isShiftKeyDown()) {
            BlockPos blockPos = useOnContext.getClickedPos();
            ItemStack toolStack = useOnContext.getItemInHand();
            AtomicBoolean flag = new AtomicBoolean(false);
            WorldUtil.getBlocksInRadius(blockPos, ToolUtil.getTillingRadiusFromHoe(toolStack)).forEach(blockPos1 -> {
                Pair<Predicate<UseOnContext>, Consumer<UseOnContext>> pair = TILLABLES.get(level.getBlockState(blockPos1).getBlock());
                if (pair != null) {
                    Predicate<UseOnContext> predicate = pair.getFirst();
                    Consumer<UseOnContext> consumer = pair.getSecond();
                    UseOnContext context = new UseOnContext(player, useOnContext.getHand(), useOnContext.getHitResult().withPosition(blockPos1));
                    if (predicate.test(context)) {
                        level.playSound(player, blockPos1, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                        if (!level.isClientSide) {
                            consumer.accept(context);
                            context.getItemInHand().hurtAndBreak(1, player, LivingEntity.getSlotForHand(context.getHand()));
                            flag.set(true);
                        }
                    }
                }
            });
            if (flag.get()) {
                cir.setReturnValue(InteractionResult.SUCCESS);
            }
        }
    }
}
