package com.fabbe50.fabsservertweaks.registries;

import com.fabbe50.fabsservertweaks.commands.GotoCommand;
import com.fabbe50.fabsservertweaks.network.packets.SeedPacket;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.architectury.event.events.common.EntityEvent;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.networking.NetworkManager;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;

public class EventRegistry {
    public static void init() {
        EntityEvent.LIVING_DEATH.register((livingEntity, damageSource) -> {
            if (livingEntity instanceof Mob mob) {
                Level level = mob.level();
                if (level instanceof ServerLevel serverLevel) {
                    GameRules gameRules = serverLevel.getGameRules();
                    if (!gameRules.getBoolean(ModGameRules.RULE_MOB_DROP_EQUIPABLE)) {
                        for (EquipmentSlot slot : EquipmentSlot.values()) {
                            if (!mob.getDropChances().isPreserved(slot)) {
                                ItemStack stack = mob.getItemBySlot(slot);
                                if (stack.has(DataComponents.EQUIPPABLE) || stack.has(DataComponents.TOOL) || stack.has(DataComponents.WEAPON) || stack.getItem() instanceof ProjectileWeaponItem) {
                                    stack.setCount(0);
                                }
                            }
                        }
                    }
                    if (mob instanceof Shulker shulker) {
                        int shulker_shells = gameRules.getInt(ModGameRules.RULE_SHULKER_SHELL_DROP_AMOUNT);
                        if (shulker_shells > 0) {
                            shulker.spawnAtLocation(serverLevel, new ItemStack(Items.SHULKER_SHELL, shulker_shells));
                            shulker.remove(Entity.RemovalReason.DISCARDED);
                        }
                    }
                }
            }
            return EventResult.pass();
        });
        CommandRegistrationEvent.EVENT.register((commandDispatcher, commandBuildContext, commandSelection) -> {
            GotoCommand.register(commandDispatcher);
        });
        PlayerEvent.PLAYER_JOIN.register(serverPlayer -> {
            if (serverPlayer.level().getGameRules().getBoolean(ModGameRules.RULE_SHARE_SEED)) {
                long seed = serverPlayer.level().getSeed();
                try {
                    NetworkManager.sendToPlayer(serverPlayer, new SeedPacket.Client.PacketPayload(seed));
                } catch (UnsupportedOperationException ignored) {
                    serverPlayer.sendSystemMessage(Component.literal("Server Seed: " + serverPlayer.level().getSeed()));
                }
            }
        });
    }
}
