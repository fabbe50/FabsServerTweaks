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

public class DifficultyValue extends GameRules.Value<DifficultyValue> {
    private Difficulty value;

    private static GameRules.Type<DifficultyValue> create(Difficulty value, BiConsumer<MinecraftServer, DifficultyValue> biConsumer) {
        return new GameRules.Type<>(DifficultyArgumentType::value, type -> new DifficultyValue(type, value), biConsumer, (visitor, key, type) -> {
            if (visitor instanceof DifficultyVisitor) {
                ((DifficultyVisitor) visitor).visitDifficulty(key, type);
            }
        }, DifficultyValue.class, FeatureFlagSet.of());
    }

    public static GameRules.Type<DifficultyValue> create(Difficulty value) {
        return create(value, (minecraftServer, trampleValue) -> {
        });
    }

    public DifficultyValue(GameRules.Type<DifficultyValue> type, Difficulty value) {
        super(type);
        this.value = value;
    }

    @Override
    protected void updateFromArgument(CommandContext<CommandSourceStack> commandContext, String string) {
        this.value = DifficultyArgumentType.getValue(commandContext, string);
    }

    @Override
    protected void deserialize(String string) {
        this.value = Difficulty.byName(string.toLowerCase());
    }

    @Override
    public @NotNull String serialize() {
        return this.value.getSerializedName();
    }

    @Override
    public int getCommandResult() {
        return this.value.id;
    }

    public Difficulty getValue() {
        return value;
    }

    @Override
    protected @NotNull DifficultyValue getSelf() {
        return this;
    }

    @Override
    protected @NotNull DifficultyValue copy() {
        return new DifficultyValue(this.type, this.value);
    }

    @Override
    public void setFrom(DifficultyValue value, @Nullable MinecraftServer minecraftServer) {
        this.value = value.value;
        this.onChanged(minecraftServer);
    }

    public enum Difficulty implements StringRepresentable {
        SAME_ON_ALL_DIFFICULTIES(0, "same_on_all"),
        SCALE_BY_DIFFICULTY(1, "scale_difficulty");

        public static final EnumCodec<Difficulty> CODEC = StringRepresentable.fromEnum(Difficulty::values);
        private static final IntFunction<Difficulty> BY_ID = ByIdMap.continuous(Difficulty::getId, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
        public static final StreamCodec<ByteBuf, Difficulty> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Difficulty::getId);

        private final int id;
        private final String name;

        Difficulty(int commandResult, String name) {
            this.id = commandResult;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public static Difficulty byId(int i) {
            return BY_ID.apply(i);
        }

        public static Difficulty byName(String name) {
            return byName(name, SCALE_BY_DIFFICULTY);
        }

        public static Difficulty byName(String name, Difficulty difficulty) {
            Difficulty newDifficulty = CODEC.byName(name);
            return newDifficulty != null ? newDifficulty : difficulty;
        }

        public static Set<String> getNames() {
            return Arrays.stream(values()).map(Difficulty::getSerializedName).collect(Collectors.toSet());
        }

        @Override
        public @NotNull String getSerializedName() {
            return name;
        }
    }

    public static class DifficultyArgumentType implements ArgumentType<Difficulty> {
        private static final Difficulty[] VALUES;
        private static final DynamicCommandExceptionType ERROR_INVALID;

        private DifficultyArgumentType() {
        }

        public static DifficultyArgumentType value() {
            return new DifficultyArgumentType();
        }

        public static Difficulty getValue(CommandContext<?> context, String name) {
            return context.getArgument(name, Difficulty.class);
        }

        @Override
        public Difficulty parse(StringReader stringReader) throws CommandSyntaxException {
            String arg = stringReader.readUnquotedString();
            Difficulty difficulty = Difficulty.byName(arg, null);
            if (difficulty == null) {
                throw ERROR_INVALID.createWithContext(stringReader, arg);
            } else {
                return difficulty;
            }
        }

        @Override
        public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
            return context.getSource() instanceof SharedSuggestionProvider ? SharedSuggestionProvider.suggest(Arrays.stream(VALUES).map(Difficulty::getName), builder) : Suggestions.empty();
        }

        @Override
        public Collection<String> getExamples() {
            return Difficulty.getNames();
        }

        static {
            VALUES = Difficulty.values();
            ERROR_INVALID = new DynamicCommandExceptionType(object -> Component.translatableEscape("argument.fst_difficulty.invalid", object));
        }
    }

    interface DifficultyVisitor extends GameRules.GameRuleTypeVisitor {
        default void visitDifficulty(GameRules.Key<DifficultyValue> key, GameRules.Type<DifficultyValue> type) {
            visit(key, type);
        }
    }
}
