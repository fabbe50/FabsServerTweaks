package com.fabbe50.fabsservertweaks.util;

import com.fabbe50.fabsservertweaks.Fabsservertweaks;
import com.fabbe50.fabsservertweaks.LogUtil;
import com.fabbe50.fabsservertweaks.data.ExperienceData;
import com.fabbe50.fabsservertweaks.registries.EventRegistry;
import com.fabbe50.fabsservertweaks.registries.ModGameRules;
import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ProblemReporter;
import net.minecraft.util.RandomSource;
import net.minecraft.util.datafix.fixes.BlockStateData;
import net.minecraft.util.random.Weighted;
import net.minecraft.util.random.WeightedList;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.item.component.TypedEntityData;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GameMasterBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.TrialSpawnerBlockEntity;
import net.minecraft.world.level.block.entity.trialspawner.TrialSpawner;
import net.minecraft.world.level.block.entity.vault.VaultBlockEntity;
import net.minecraft.world.level.block.entity.vault.VaultServerData;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class WorldUtil {
    public static List<BlockPos> getBlocksInRadius(BlockPos center, int radius) {
        return getBlocksInRadius(Direction.UP, center, radius);
    }

    public static List<BlockPos> getBlocksInRadius(Direction facing, BlockPos center, int radius) {
        List<BlockPos> blocks = new ArrayList<>();

        int cx = center.getX();
        int cy = center.getY();
        int cz = center.getZ();

        int rSq = radius * radius;

        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                if (x * x + z * z <= rSq) {
                    switch (facing) {
                        case NORTH, SOUTH -> blocks.add(new BlockPos(cx + x, cy + z, cz));
                        case EAST, WEST -> blocks.add(new BlockPos(cx, cy + x, cz + z));
                        case null, default -> blocks.add(new BlockPos(cx + x, cy, cz + z));
                    }
                }
            }
        }

        return blocks;
    }

    public static List<BlockPos> getBlocksInSphericalRadius(BlockPos center, int radius) {
        List<BlockPos> blocks = new ArrayList<>();

        int cx = center.getX();
        int cy = center.getY();
        int cz = center.getZ();

        int rSq = radius * radius;

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    if (x * x + y * y + z * z <= rSq) {
                        blocks.add(new BlockPos(cx + x, cy + y, cz + z));
                    }
                }
            }
        }

        return blocks;
    }

    public static List<BlockPos> getBlockPositions(AABB box) {
        List<BlockPos> positions = new ArrayList<>();

        int minX = (int) Math.floor(box.minX);
        int minY = (int) Math.floor(box.minY);
        int minZ = (int) Math.floor(box.minZ);
        int maxX = (int) Math.floor(box.maxX);
        int maxY = (int) Math.floor(box.maxY);
        int maxZ = (int) Math.floor(box.maxZ);

        for (BlockPos pos : BlockPos.betweenClosed(minX, minY, minZ, maxX, maxY, maxZ)) {
            positions.add(pos.immutable()); // avoid mutable references
        }

        return positions;
    }

    public static AABB inflateFromBreakAxis(AABB aabb, Axis axis, int inflateAmount) {
        return switch (axis) {
            case X -> aabb.inflate(0, inflateAmount, inflateAmount);
            case Y -> aabb.inflate(inflateAmount, 0, inflateAmount);
            case Z -> aabb.inflate(inflateAmount, inflateAmount, 0);
        };
    }

    public static void breakBlocks(ServerLevel level, BlockPos dropPos, Set<BlockPos> positions, ServerPlayer player, ItemStack stack) {
        List<ItemStack> drops = new ArrayList<>();
        int xpToDrop = 0;
        for (BlockPos pos : positions) {
            BlockState state = level.getBlockState(pos);
            BlockEntity blockEntity = level.getBlockEntity(pos);
            Block block = state.getBlock();
            if (checkGameMasterCondition(block, player)) {
                level.sendBlockUpdated(pos, state, state, 3);
                continue;
            }
            if (checkGameTypeCondition(player, level, pos)) {
                continue;
            }
            if (!isPlayerInstaBuild(player)) {
                Pair<Item, Integer> specialDrops = BlockUtil.getSpecialDropsAmount(level, pos, state, player, stack);
                if (specialDrops != null) {
                    drops.add(new ItemStack(specialDrops.getFirst(), specialDrops.getSecond()));
                } else {
                    drops.addAll(state.getDrops(new LootParams.Builder(level).withParameter(LootContextParams.TOOL, stack).withParameter(LootContextParams.ORIGIN, pos.getCenter())));
                }
                if (!EnchantmentUtil.hasSilkTouch(player, stack)) {
                    xpToDrop += ExperienceData.getExp(block);
                }
                ToolUtil.hurtItem(1, level, stack, pos);
            }
            if (blockEntity != null) {
                level.removeBlockEntity(pos);
            }
            level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
        }
        Vec3 newDropPos = dropPos.getCenter();
        for (ItemStack drop : drops) {
            ItemEntity itemEntity = new ItemEntity(level, newDropPos.x(), newDropPos.y(), newDropPos.z(), drop);
            itemEntity.setDeltaMovement(Vec3.ZERO);
            level.addFreshEntity(itemEntity);
        }
        if (xpToDrop > 0) {
            ExperienceOrb.award(level, newDropPos, xpToDrop);
        }
    }

    public static boolean dropItem(Level level, BlockPos pos, ItemStack stack) {
        ItemEntity itemEntity = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack);
        return level.addFreshEntity(itemEntity);
    }

    public static boolean dropItemWithData(Level level, BlockPos pos, ItemStack stack) {
        BlockState state = level.getBlockState(pos);
        if (!state.isAir()) {
            BlockItemStateProperties properties = BlockItemStateProperties.EMPTY;
            for (Property<?> property : state.getProperties()) {
                properties = ItemStackUtil.copyProperty(properties, state, property);
            }
            stack.set(DataComponents.BLOCK_STATE, properties);
            LogUtil.debug("Block state found. Adding block state data to stack: " + stack + ", properties: " + properties);
        }
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity != null) {
            TagValueOutput tagValueOutput = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, level.registryAccess());
            blockEntity.saveWithId(tagValueOutput);
            if (handleSpecialDataItemDrop(stack, blockEntity)) {
                LogUtil.debug("The block entity required special handling, the output stack had this data applied: " + stack);
            }
            stack.set(DataComponents.BLOCK_ENTITY_DATA, TypedEntityData.of(blockEntity.getType(), tagValueOutput.buildResult()));
            LogUtil.debug("Added data to stack: " + stack + ", data: " + tagValueOutput);
        }
        if (stack.has(DataComponents.BLOCK_STATE) || stack.has(DataComponents.BLOCK_ENTITY_DATA)) {
            LogUtil.debug("Dropping item " + stack);
            Block.popResource(level, pos, stack);
            level.removeBlockEntity(pos);
            level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
            return true;
        }
        return false;
    }

    public static boolean handleSpecialDataItemDrop(ItemStack stack, BlockEntity blockEntity) {
        if (blockEntity instanceof TrialSpawnerBlockEntity trialSpawnerBlockEntity) {
            TrialSpawner trialSpawner = trialSpawnerBlockEntity.getTrialSpawner();
            WeightedList<SpawnData> spawnDataWeightedList = trialSpawner.activeConfig().spawnPotentialsDefinition();
            List<Weighted<SpawnData>> weightedSpawnDataList = spawnDataWeightedList.unwrap();
            List<String> entityNames = new ArrayList<>();
            for (Weighted<SpawnData> weightedSpawnData : weightedSpawnDataList) {
                CompoundTag tag = weightedSpawnData.value().entityToSpawn();
                LogUtil.debug("EntityNBT: " + tag);
                if (tag.contains("id")) {
                    Optional<String> entityID = tag.getString("id");
                    entityID.ifPresent(s -> {
                        String[] splitID = s.split(":");
                        if (splitID.length == 2) {
                            String name = StringUtil.capitalizeFirstLetter(splitID[1]);
                            entityNames.add(name);
                        } else {
                            entityNames.add(s);
                        }
                    });
                }
            }
            boolean flag = false;
            for (String entityName : entityNames) {
                ItemStackUtil.addLore(stack, Component.literal(entityName).withStyle(style -> style.withItalic(false).withColor(ChatFormatting.GRAY)));
                flag = true;
            }
            return flag;
        }
        return false;
    }

    public static boolean handleSpecialBreakingConditions(ServerLevel level, BlockPos pos, BlockState state, ServerPlayer player, ItemStack toolStack) {
        Pair<Item, Integer> item = BlockUtil.getSpecialDropsAmount(level, pos, state, player, toolStack);
        if (item != null) {
            if (WorldUtil.dropItem(level, pos, new ItemStack(item.getFirst(), item.getSecond()))) {
                if (!level.destroyBlock(pos, false)) {
                    level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                }
                return true;
            }
        }

        if (state.is(BlockTags.BEDS) && !WorldUtil.isPlayerInstaBuild(player)) {
            return BedUtil.wakeUpFromSleepingBag(level, pos, state);
        }
        return false;
    }

    public static boolean placeBlockWithData(ServerLevel level, BlockPos pos, BlockState state, ItemStack mainHandStack, BlockEntityType<?> blockEntityTypeToPlace) {
        LogUtil.debug("Attempting to load custom data for block placement.");
        TypedEntityData<BlockEntityType<?>> customData = mainHandStack.getOrDefault(DataComponents.BLOCK_ENTITY_DATA, TypedEntityData.of(blockEntityTypeToPlace, new CompoundTag()));
        BlockEntityType<?> blockEntityType = customData.type();
        ItemStack copy = mainHandStack.copy();
        mainHandStack.shrink(1);

        if (copy.has(DataComponents.BLOCK_STATE)) {
            BlockItemStateProperties properties = copy.get(DataComponents.BLOCK_STATE);
            if (properties != null) {
                LogUtil.debug("Block state properties found. Applying..." + properties);
                if (state.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
                    properties = properties.with(BlockStateProperties.HORIZONTAL_FACING, state.getValue(BlockStateProperties.HORIZONTAL_FACING));
                }
                if (state.hasProperty(BlockStateProperties.FACING)) {
                    properties = properties.with(BlockStateProperties.FACING, state.getValue(BlockStateProperties.FACING));
                }
                state = properties.apply(state);
                LogUtil.debug("Block state properties applied successfully.");
            }
        }

        LogUtil.debug("Placing block with state: " + state);
        level.setBlockAndUpdate(pos, state);

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity != null) {
            BlockEntityType<?> blockEntityType2 = blockEntity.getType();
            if (blockEntityType != blockEntityType2) {
                LogUtil.debug("Block entities are different types. Ignoring.");
                return false;
            }

            if (customData.loadInto(blockEntity, level.registryAccess())) {
                blockEntity.applyComponentsFromItemStack(copy);
                blockEntity.setChanged();
                LogUtil.debug("Block entity data loaded successfully. State after load: " + level.getBlockState(pos));
            }
        } else {
            LogUtil.debug("Block entity is null. Ignoring.");
            return false;
        }
        return true;
    }

    public static int getAncientDebrisDrops(ServerLevel level, BlockPos pos, Player player, ItemStack tool) {
        int max = 2;
        int randomAmountBase = level.getRandom().nextInt(1, max);
        double randomAmount = randomAmountBase;
        if (EnchantmentUtil.hasFortune(player, tool)) {
            int fortuneLevel = EnchantmentUtil.getEnchantmentLevel(player, tool, Enchantments.FORTUNE);
            double fortuneMultiplier = level.getRandom().nextInt(1, fortuneLevel - 1);
            randomAmount += randomAmountBase * fortuneMultiplier;
        }
        return Math.toIntExact(Math.round(randomAmount));
    }

    public static boolean checkGameMasterCondition(Block block, ServerPlayer player) {
        if (block instanceof GameMasterBlock) {
            if (player == null) {
                return false;
            }
            return !player.canUseGameMasterBlocks();
        }
        return false;
    }

    public static boolean checkGameTypeCondition(ServerPlayer player, Level level, BlockPos pos) {
        if (player == null) {
            return false;
        }
        GameType type = isPlayerInstaBuild(player) ? GameType.CREATIVE : GameType.SURVIVAL;
        return player.blockActionRestricted(level, pos, type);
    }

    public static boolean isPlayerInstaBuild(ServerPlayer player) {
        if (player == null) {
            return false;
        }
        return player.getAbilities().instabuild;
    }

    public static boolean shouldPlantGrowExtra(ServerLevel level, BlockPos pos, RandomSource random, int age, int maxAge) {
        int randomChance = random.nextInt(100);
        int percentageChance = Fabsservertweaks.CONFIG.plantRainGrowthChance;
        if (age < maxAge - 1 && randomChance < percentageChance) {
            return level.getGameRules().get(ModGameRules.RULE_PLANTS_GROW_FASTER_IN_RAIN) && isRainingAtLocation(level, pos);
        }
        return false;
    }

    public static boolean isRainingAtLocation(Level level, BlockPos pos) {
        return level.isRainingAt(pos);
    }

    public static boolean isChunkLoaded(@NonNull Level level, @NonNull BlockPos pos) {
        return level.getChunkSource().getForceLoadedChunks().contains(level.getChunkAt(pos).getPos().pack());
    }

    public static List<BlockState> getAdjacentBlockStates(ServerLevel level, BlockPos pos) {
        List<BlockState> states = new ArrayList<>();
        for (BlockPos position : getAdjacentPositions(pos)) {
            states.add(level.getBlockState(position));
        }
        return states;
    }

    public static List<BlockPos> getAdjacentPositions(BlockPos pos) {
        List<BlockPos> positions = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            positions.add(pos.relative(direction));
        }
        return positions;
    }

    public static boolean unlockVault(ServerLevel level, BlockPos pos, Player player, ItemStack stack, VaultBlockEntity vaultBlockEntity) {
        VaultServerData serverData = vaultBlockEntity.getServerData();
        if (serverData == null) {
            LogUtil.debug("Vault doesn't have server data. Operation failed.");
            return false;
        }
        if (!serverData.rewardedPlayers.contains(player.getUUID())) {
            LogUtil.debug("Vault is not locked for player. Operation failed.");
            return false;
        }
        serverData.rewardedPlayers.remove(player.getUUID());
        serverData.markChanged();
        if (!player.isCreative()) {
            stack.shrink(1);
        }
        LogUtil.debug("Operation seems successful. Passing success result.");
        level.playLocalSound(pos, SoundEvents.VAULT_INSERT_ITEM, SoundSource.BLOCKS, 1.0F, 1.0F, false);
        ParticleUtil.spawnParticlesOnBlockFaces(level, pos, ParticleTypes.HAPPY_VILLAGER, UniformInt.of(2, 5));
        return true;
    }

    public enum RelativePosition {
        NORTH(Direction.NORTH) {
            @Override
            public BlockPos getRelativePosition(BlockPos pos) {
                return pos.relative(Direction.NORTH);
            }
        },
        NORTH_EAST {
            @Override
            public BlockPos getRelativePosition(BlockPos pos) {
                return pos.relative(Direction.NORTH).relative(Direction.EAST);
            }
        },
        NORTH_WEST {
            @Override
            public BlockPos getRelativePosition(BlockPos pos) {
                return pos.relative(Direction.NORTH).relative(Direction.WEST);
            }
        },
        NORTH_UP {
            @Override
            public BlockPos getRelativePosition(BlockPos pos) {
                return pos.relative(Direction.NORTH).relative(Direction.UP);
            }
        },
        NORTH_DOWN {
            @Override
            public BlockPos getRelativePosition(BlockPos pos) {
                return pos.relative(Direction.NORTH).relative(Direction.DOWN);
            }
        },
        SOUTH(Direction.SOUTH) {
            @Override
            public BlockPos getRelativePosition(BlockPos pos) {
                return pos.relative(Direction.SOUTH);
            }
        },
        SOUTH_EAST {
            @Override
            public BlockPos getRelativePosition(BlockPos pos) {
                return pos.relative(Direction.SOUTH).relative(Direction.EAST);
            }
        },
        SOUTH_WEST {
            @Override
            public BlockPos getRelativePosition(BlockPos pos) {
                return pos.relative(Direction.SOUTH).relative(Direction.WEST);
            }
        },
        SOUTH_UP {
            @Override
            public BlockPos getRelativePosition(BlockPos pos) {
                return pos.relative(Direction.SOUTH).relative(Direction.UP);
            }
        },
        SOUTH_DOWN {
            @Override
            public BlockPos getRelativePosition(BlockPos pos) {
                return pos.relative(Direction.SOUTH).relative(Direction.DOWN);
            }
        },
        WEST(Direction.WEST) {
            @Override
            public BlockPos getRelativePosition(BlockPos pos) {
                return pos.relative(Direction.WEST);
            }
        },
        WEST_UP {
            @Override
            public BlockPos getRelativePosition(BlockPos pos) {
                return pos.relative(Direction.WEST).relative(Direction.UP);
            }
        },
        WEST_DOWN {
            @Override
            public BlockPos getRelativePosition(BlockPos pos) {
                return pos.relative(Direction.WEST).relative(Direction.DOWN);
            }
        },
        EAST(Direction.EAST) {
            @Override
            public BlockPos getRelativePosition(BlockPos pos) {
                return pos.relative(Direction.EAST);
            }
        },
        EAST_UP {
            @Override
            public BlockPos getRelativePosition(BlockPos pos) {
                return pos.relative(Direction.EAST).relative(Direction.UP);
            }
        },
        EAST_DOWN {
            @Override
            public BlockPos getRelativePosition(BlockPos pos) {
                return pos.relative(Direction.EAST).relative(Direction.DOWN);
            }
        },
        UP {
            @Override
            public BlockPos getRelativePosition(BlockPos pos) {
                return pos.relative(Direction.UP);
            }
        },
        DOWN {
            @Override
            public BlockPos getRelativePosition(BlockPos pos) {
                return pos.relative(Direction.DOWN);
            }
        };

        private final Direction[] directions;
        RelativePosition(Direction... directions) {
            this.directions = directions;
        }

        public Direction[] getDirections() {
            return directions;
        }

        public BlockPos getRelativePosition(BlockPos pos) {
            throw new RuntimeException("Incorrect method call. This method should not be called without overriding the function.");
        }

        public static List<RelativePosition> getImmediateHorizontals() {
            return List.of(NORTH, SOUTH, EAST, WEST);
        }
    }
}
