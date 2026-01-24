package com.fabbe50.fabsservertweaks.fabsservertweaks.registries.gamerules;

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

public class TrampleValue extends GameRules.Value<TrampleValue> {
    private TrampleMode value;

    private static GameRules.Type<TrampleValue> create(TrampleMode value, BiConsumer<MinecraftServer, TrampleValue> biConsumer) {
        return new GameRules.Type<>(TrampleArgumentType::value, type -> new TrampleValue(type, value), biConsumer, (visitor, key, type) -> {
            if (visitor instanceof TrampleVisitor) {
                ((TrampleVisitor) visitor).visitTrampleValue(key, type);
            }
        }, TrampleValue.class, FeatureFlagSet.of());
    }

    public static GameRules.Type<TrampleValue> create(TrampleMode value) {
        return create(value, (minecraftServer, trampleValue) -> {
        });
    }

    public TrampleValue(GameRules.Type<TrampleValue> type, TrampleMode value) {
        super(type);
        this.value = value;
    }

    @Override
    protected void updateFromArgument(CommandContext<CommandSourceStack> commandContext, String string) {
        this.value = TrampleArgumentType.getValue(commandContext, string);
    }

    @Override
    protected void deserialize(String string) {
        this.value = TrampleMode.byName(string.toLowerCase());
    }

    @Override
    public @NotNull String serialize() {
        return this.value.getSerializedName();
    }

    @Override
    public int getCommandResult() {
        return this.value.id;
    }

    public TrampleMode getValue() {
        return value;
    }

    @Override
    protected @NotNull TrampleValue getSelf() {
        return this;
    }

    @Override
    protected @NotNull TrampleValue copy() {
        return new TrampleValue(this.type, this.value);
    }

    @Override
    public void setFrom(TrampleValue value, @Nullable MinecraftServer minecraftServer) {
        this.value = value.value;
        this.onChanged(minecraftServer);
    }

    public enum TrampleMode implements StringRepresentable {
        NORMAL(0, "normal"),
        FEATHER_FALLING(1, "feather_falling"),
        NO_TRAMPLE(2, "no_trample");

        public static final EnumCodec<TrampleMode> CODEC = StringRepresentable.fromEnum(TrampleMode::values);
        private static final IntFunction<TrampleMode> BY_ID = ByIdMap.continuous(TrampleMode::getId, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
        public static final StreamCodec<ByteBuf, TrampleMode> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, TrampleMode::getId);

        private final int id;
        private final String name;

        TrampleMode(int commandResult, String name) {
            this.id = commandResult;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public static TrampleMode byId(int i) {
            return BY_ID.apply(i);
        }

        public static TrampleMode byName(String name) {
            return byName(name, NORMAL);
        }

        public static TrampleMode byName(String name, TrampleMode trampleMode) {
            TrampleMode newTrampleMode = CODEC.byName(name);
            return newTrampleMode != null ? newTrampleMode : trampleMode;
        }

        public static Set<String> getNames() {
            return Arrays.stream(values()).map(TrampleMode::getSerializedName).collect(Collectors.toSet());
        }

        @Override
        public @NotNull String getSerializedName() {
            return name;
        }
    }

    public static class TrampleArgumentType implements ArgumentType<TrampleMode> {
        private static final TrampleMode[] VALUES;
        private static final DynamicCommandExceptionType ERROR_INVALID;

        private TrampleArgumentType() {
        }

        public static TrampleArgumentType value() {
            return new TrampleArgumentType();
        }

        public static TrampleMode getValue(CommandContext<?> context, String name) {
            return context.getArgument(name, TrampleMode.class);
        }

        @Override
        public TrampleMode parse(StringReader stringReader) throws CommandSyntaxException {
            String arg = stringReader.readUnquotedString();
            TrampleMode trampleMode = TrampleMode.byName(arg, null);
            if (trampleMode == null) {
                throw ERROR_INVALID.createWithContext(stringReader, arg);
            } else {
                return trampleMode;
            }
        }

        @Override
        public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
            return context.getSource() instanceof SharedSuggestionProvider ? SharedSuggestionProvider.suggest(Arrays.stream(VALUES).map(TrampleMode::getName), builder) : Suggestions.empty();
        }

        @Override
        public Collection<String> getExamples() {
            return TrampleMode.getNames();
        }

        static {
            VALUES = TrampleMode.values();
            ERROR_INVALID = new DynamicCommandExceptionType(object -> Component.translatableEscape("argument.fst_crop_trample_mode.invalid", object));
        }
    }

    interface TrampleVisitor extends GameRules.GameRuleTypeVisitor {
        default void visitTrampleValue(GameRules.Key<TrampleValue> key, GameRules.Type<TrampleValue> type) {
            visit(key, type);
        }
    }
}
