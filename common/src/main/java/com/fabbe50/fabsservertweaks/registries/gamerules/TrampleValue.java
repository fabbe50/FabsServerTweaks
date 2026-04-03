package com.fabbe50.fabsservertweaks.registries.gamerules;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleTypeVisitor;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.IntFunction;
import java.util.stream.Collectors;

public class TrampleValue {
    public enum TrampleMode implements StringRepresentable {
        NORMAL(0, "normal"),
        FEATHER_FALLING(1, "feather_falling"),
        NO_TRAMPLE(2, "no_trample");

        public static final StringRepresentable.EnumCodec<TrampleMode> CODEC = StringRepresentable.fromEnum(TrampleMode::values);
        private static final IntFunction<TrampleMode> BY_ID = ByIdMap.continuous(TrampleMode::getId, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
        public static final StreamCodec<ByteBuf, TrampleMode> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, TrampleMode::getId);

        private final int id;
        private final String name;

        TrampleMode(int id, String name) {
            this.id = id;
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

    public interface TrampleVisitor extends GameRuleTypeVisitor {
        default void visitTrampleValue(GameRule<TrampleMode> gameRule) {
            visit(gameRule);
        }
    }
}
