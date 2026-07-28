package com.fabbe50.fabsservertweaks.mixin;

import com.fabbe50.fabsservertweaks.registries.ModGameRules;
import com.fabbe50.fabsservertweaks.util.ToolUtil;
import com.fabbe50.fabsservertweaks.util.WorldUtil;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CocoaBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
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
        if (level instanceof ServerLevel serverLevel && serverLevel.getGameRules().get(ModGameRules.RULE_BETTER_HOES) && !player.isShiftKeyDown()) {
            BlockPos blockPos = useOnContext.getClickedPos();
            ItemStack toolStack = useOnContext.getItemInHand();
            Direction face = useOnContext.getClickedFace();
            AtomicBoolean flag = new AtomicBoolean(false);
            BlockState originState = serverLevel.getBlockState(blockPos);
            if (TILLABLES.get(originState.getBlock()) != null) {
                WorldUtil.getBlocksInRadius(blockPos, ToolUtil.getTillingRadiusFromHoe(toolStack)).forEach(blockPos1 -> {
                    BlockState state = serverLevel.getBlockState(blockPos1);
                    Pair<Predicate<UseOnContext>, Consumer<UseOnContext>> pair = TILLABLES.get(state.getBlock());
                    if (pair != null) {
                        Predicate<UseOnContext> predicate = pair.getFirst();
                        Consumer<UseOnContext> consumer = pair.getSecond();
                        UseOnContext context = new UseOnContext(player, useOnContext.getHand(), useOnContext.getHitResult().withPosition(blockPos1));
                        if (predicate.test(context)) {
                            serverLevel.playSound(player, blockPos1, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                            if (!serverLevel.isClientSide()) {
                                consumer.accept(context);
                                context.getItemInHand().hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
                                flag.set(true);
                            }
                        }
                    }
                });
            } else if (originState.is(BlockTags.CROPS)) {
                WorldUtil.getBlocksInRadius(blockPos, ToolUtil.getTillingRadiusFromHoe(toolStack)).forEach(blockPos1 -> {
                    BlockState state = level.getBlockState(blockPos1);
                    if (state.getBlock() instanceof CropBlock cropBlock && cropBlock.isMaxAge(state)) {
                        List<ItemStack> stacks = state.getDrops(new LootParams.Builder(serverLevel).withParameter(LootContextParams.TOOL, toolStack).withParameter(LootContextParams.ORIGIN, blockPos1.getCenter()));
                        for (ItemStack dropStack : stacks) {
                            ItemEntity itemEntity = new ItemEntity(level, blockPos.getX(), blockPos.getY(), blockPos.getZ(), dropStack);
                            level.addFreshEntity(itemEntity);
                        }
                        level.setBlockAndUpdate(blockPos1, cropBlock.getStateForAge(0));
                    }
                });
            } else if (originState.is(Blocks.COCOA)) {
                WorldUtil.getBlocksInRadius(face, blockPos, ToolUtil.getTillingRadiusFromHoe(toolStack)).forEach(pos -> {
                    BlockState state = level.getBlockState(pos);
                    if (state.getBlock() instanceof CocoaBlock) {
                        int age = state.getValue(CocoaBlock.AGE);
                        if (age == CocoaBlock.MAX_AGE) {
                            List<ItemStack> stacks = state.getDrops(new LootParams.Builder(serverLevel).withParameter(LootContextParams.TOOL, toolStack).withParameter(LootContextParams.ORIGIN, pos.getCenter()));
                            for (ItemStack dropStack : stacks) {
                                ItemEntity itemEntity = new ItemEntity(level, blockPos.getX(), blockPos.getY(), blockPos.getZ(), dropStack);
                                level.addFreshEntity(itemEntity);
                            }
                            level.setBlockAndUpdate(pos, state.setValue(CocoaBlock.AGE, 0));
                        }
                    }
                });
            }
            if (flag.get()) {
                cir.setReturnValue(InteractionResult.SUCCESS);
            }
        }
    }
}
