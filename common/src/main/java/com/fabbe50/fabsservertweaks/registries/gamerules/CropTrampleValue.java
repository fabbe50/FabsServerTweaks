package com.fabbe50.fabsservertweaks.registries.gamerules;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.GameRules;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.IntFunction;
import java.util.stream.Collectors;

public class CropTrampleValue extends GameRules.Value<CropTrampleValue> {
    private CropTrampleMode value;

    private static GameRules.Type<CropTrampleValue> create(CropTrampleMode value, BiConsumer<MinecraftServer, CropTrampleValue> biConsumer) {
        return new GameRules.Type<>(CropTrampleArgumentType::value, type -> new CropTrampleValue(type, value), biConsumer, (visitor, key, type) -> {
            if (visitor instanceof CropTrampleVisitor) {
                ((CropTrampleVisitor) visitor).visitCropTrampleValue(key, type);
            }
        }, CropTrampleValue.class, FeatureFlagSet.of());
    }

    public static GameRules.Type<CropTrampleValue> create(CropTrampleMode value) {
        return create(value, (minecraftServer, cropTrampleValue) -> {
        });
    }

    public CropTrampleValue(GameRules.Type<CropTrampleValue> type, CropTrampleMode value) {
        super(type);
        this.value = value;
    }

    @Override
    protected void updateFromArgument(CommandContext<CommandSourceStack> commandContext, String string) {
        this.value = CropTrampleArgumentType.getValue(commandContext, string);
    }

    @Override
    protected void deserialize(String string) {
        this.value = CropTrampleMode.byName(string.toLowerCase());
    }

    @Override
    public @NotNull String serialize() {
        return this.value.getSerializedName();
    }

    @Override
    public int getCommandResult() {
        return this.value.id;
    }

    public CropTrampleMode getValue() {
        return value;
    }

    @Override
    protected @NotNull CropTrampleValue getSelf() {
        return this;
    }

    @Override
    protected @NotNull CropTrampleValue copy() {
        return new CropTrampleValue(this.type, this.value);
    }

    @Override
    public void setFrom(CropTrampleValue value, @Nullable MinecraftServer minecraftServer) {
        this.value = value.value;
        this.onChanged(minecraftServer);
    }

    public enum CropTrampleMode implements StringRepresentable {
        NORMAL(0, "normal"),
        FEATHER_FALLING(1, "feather_falling"),
        NO_TRAMPLE(2, "no_trample");

        public static final StringRepresentable.EnumCodec<CropTrampleMode> CODEC = StringRepresentable.fromEnum(CropTrampleMode::values);
        private static final IntFunction<CropTrampleMode> BY_ID = ByIdMap.continuous(CropTrampleMode::getId, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
        public static final StreamCodec<ByteBuf, CropTrampleMode> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, CropTrampleMode::getId);

        private final int id;
        private final String name;

        CropTrampleMode(int commandResult, String name) {
            this.id = commandResult;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public static CropTrampleMode byId(int i) {
            return BY_ID.apply(i);
        }

        public static CropTrampleMode byName(String name) {
            return byName(name, NORMAL);
        }

        public static CropTrampleMode byName(String name, CropTrampleMode cropTrampleMode) {
            CropTrampleMode newCropTrampleMode = CODEC.byName(name);
            return newCropTrampleMode != null ? newCropTrampleMode : cropTrampleMode;
        }

        public static Set<String> getNames() {
            return Arrays.stream(values()).map(CropTrampleMode::getSerializedName).collect(Collectors.toSet());
        }

        @Override
        public @NotNull String getSerializedName() {
            return name;
        }
    }

    public static class CropTrampleArgumentType implements ArgumentType<CropTrampleMode> {
        private static final CropTrampleMode[] VALUES;
        private static final DynamicCommandExceptionType ERROR_INVALID;

        private CropTrampleArgumentType() {
        }

        public static CropTrampleArgumentType value() {
            return new CropTrampleArgumentType();
        }

        public static CropTrampleMode getValue(CommandContext<?> context, String name) {
            return context.getArgument(name, CropTrampleMode.class);
        }

        @Override
        public CropTrampleMode parse(StringReader stringReader) throws CommandSyntaxException {
            String arg = stringReader.readUnquotedString();
            CropTrampleMode trampleMode = CropTrampleMode.byName(arg, null);
            if (trampleMode == null) {
                throw ERROR_INVALID.createWithContext(stringReader, arg);
            } else {
                return trampleMode;
            }
        }

        @Override
        public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
            return context.getSource() instanceof SharedSuggestionProvider ? SharedSuggestionProvider.suggest(Arrays.stream(VALUES).map(CropTrampleMode::getName), builder) : Suggestions.empty();
        }

        @Override
        public Collection<String> getExamples() {
            return CropTrampleMode.getNames();
        }

        static {
            VALUES = CropTrampleMode.values();
            ERROR_INVALID = new DynamicCommandExceptionType(object -> Component.translatableEscape("argument.fst_crop_trample_mode.invalid", object));
        }
    }

    interface CropTrampleVisitor extends GameRules.GameRuleTypeVisitor {
        default void visitCropTrampleValue(GameRules.Key<CropTrampleValue> key, GameRules.Type<CropTrampleValue> type) {
            visit(key, type);
        }
    }
}
