package com.fabbe50.fabsservertweaks.mixin;

import com.fabbe50.fabsservertweaks.LogUtil;
import com.fabbe50.fabsservertweaks.data.PlantGrowth;
import com.fabbe50.fabsservertweaks.registries.ModGameRules;
import com.fabbe50.fabsservertweaks.registries.ModRegistry;
import com.fabbe50.fabsservertweaks.util.PillarGrowUtil;
import com.fabbe50.fabsservertweaks.util.PlantUtil;
import com.fabbe50.fabsservertweaks.util.ToolUtil;
import com.fabbe50.fabsservertweaks.util.WorldUtil;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
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
        if (level instanceof ServerLevel serverLevel && ModGameRules.getGameRuleBoolean(serverLevel, ModGameRules.RULE_BETTER_HOES) && !player.isShiftKeyDown()) {
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
            } else if (originState.is(ModRegistry.HARVESTABLE)) {
                Direction harvestDirection = face;
                if (originState.is(ModRegistry.FIELD_GROWABLE)) {
                    harvestDirection = Direction.UP;
                }
                PlantGrowth ogGrowth = PlantGrowth.getPlantAge(originState);
                WorldUtil.getBlocksInRadius(harvestDirection, blockPos, ToolUtil.getTillingRadiusFromHoe(toolStack)).forEach(pos -> {
                    BlockState state = serverLevel.getBlockState(pos);
                    if (ogGrowth != null) {
                        BlockPos abovePos = pos.above();
                        BlockState aboveState = serverLevel.getBlockState(abovePos);
                        for (Block block : ogGrowth.getAttachments()) {
                            if (aboveState.is(block)) {
                                dropItems(serverLevel, blockPos, abovePos, aboveState, toolStack);
                                serverLevel.setBlockAndUpdate(abovePos, Blocks.AIR.defaultBlockState());
                                break;
                            }
                        }
                    }
                    if (!(state.is(Blocks.MELON_STEM) || state.is(Blocks.PUMPKIN_STEM) || !state.is(ModRegistry.HARVESTABLE))) {
                        IntegerProperty ageProperty = PlantUtil.getAgeProperty(state);
                        PlantGrowth plantAge = PlantGrowth.getPlantAge(state);
                        if ((ageProperty != null && plantAge != null) || (plantAge != null && plantAge.hasDifferentBlock())) {
                            if (PlantUtil.isMaxAge(state, ageProperty) || plantAge.shouldIgnoreMaxAge() || !plantAge.isAgingPlant()) {
                                int resettingAge = plantAge.getResettingAge();
                                boolean tallPlant = plantAge.isTallPlant();
                                if (tallPlant) {
                                    int height = PillarGrowUtil.getPillarHeight(state, serverLevel, pos);
                                    if (height != 1) {
                                        BlockPos above = pos.above();
                                        for (int i = 0; i < height; i++) {
                                            BlockState tallPlantState = serverLevel.getBlockState(above);
                                            if (tallPlantState.is(state.getBlock())) {
                                                if (plantAge.tallPlantDropsForEverySegment()) {
                                                    dropItems(serverLevel, blockPos.relative(face), pos, state, toolStack);
                                                }
                                                serverLevel.setBlockAndUpdate(above, Blocks.AIR.defaultBlockState());
                                                above = above.above();
                                            }
                                        }
                                        PlantUtil.setPlantWithAge(serverLevel, pos, state, plantAge, ageProperty, resettingAge);
                                    }
                                } else if (resettingAge != -1) {
                                    dropItems(serverLevel, blockPos, pos, state, toolStack);
                                    PlantUtil.setPlantWithAge(serverLevel, pos, state, plantAge, ageProperty, resettingAge);
                                }
                            }
                        }
                    }
                });
            }
            if (flag.get()) {
                cir.setReturnValue(InteractionResult.SUCCESS);
            }
        }
    }

    @Unique
    private void dropItems(ServerLevel serverLevel, BlockPos blockPos, BlockPos blockPos1, BlockState state, ItemStack toolStack) {
        List<ItemStack> stacks = state.getDrops(new LootParams.Builder(serverLevel).withParameter(LootContextParams.TOOL, toolStack).withParameter(LootContextParams.ORIGIN, blockPos1.getCenter()));
        for (ItemStack dropStack : stacks) {
            ItemEntity itemEntity = new ItemEntity(serverLevel, blockPos.getX(), blockPos.getY(), blockPos.getZ(), dropStack);
            serverLevel.addFreshEntity(itemEntity);
        }
    }
}
