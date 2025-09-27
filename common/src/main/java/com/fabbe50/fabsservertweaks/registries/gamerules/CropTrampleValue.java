package com.fabbe50.fabsservertweaks.registries.gamerules;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.GameRules;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
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
        this.value = CropTrampleMode.getFromName(string);
    }

    @Override
    public @NotNull String serialize() {
        return this.value.name;
    }

    @Override
    public int getCommandResult() {
        return this.value.commandResult;
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

    public enum CropTrampleMode {
        NORMAL(0, "normal"),
        FEATHER_FALLING(1, "feather_falling"),
        NO_TRAMPLE(2, "no_trample");

        private final int commandResult;
        private final String name;

        CropTrampleMode(int commandResult, String name) {
            this.commandResult = commandResult;
            this.name = name;
        }

        public static CropTrampleMode getFromName(String name) {
            for (CropTrampleMode mode : values()) {
                if (mode.name.equals(name)) {
                    return mode;
                }
            }
            return NORMAL;
        }

        public static Set<String> getNames() {
            return Arrays.stream(values()).map(cropTrampleMode -> cropTrampleMode.name).collect(Collectors.toSet());
        }
    }

    public static class CropTrampleArgumentType implements ArgumentType<CropTrampleMode> {
        private CropTrampleArgumentType() {
        }

        public static CropTrampleArgumentType value() {
            return new CropTrampleArgumentType();
        }

        public static CropTrampleMode getValue(CommandContext<?> context, String name) {
            return context.getArgument(name.toUpperCase(), CropTrampleMode.class);
        }

        @Override
        public CropTrampleMode parse(StringReader stringReader) throws CommandSyntaxException {
            return CropTrampleMode.getFromName(stringReader.readString().toLowerCase());
        }

        @Override
        public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
            for (String name : CropTrampleMode.getNames()) {
                if (name.startsWith(builder.getRemainingLowerCase())) {
                    builder.suggest(name);
                }
            }
            return builder.buildFuture();
        }

        @Override
        public Collection<String> getExamples() {
            return CropTrampleMode.getNames();
        }
    }

    interface CropTrampleVisitor extends GameRules.GameRuleTypeVisitor {
        default void visitCropTrampleValue(GameRules.Key<CropTrampleValue> key, GameRules.Type<CropTrampleValue> type) {
            visit(key, type);
        }
    }
}
