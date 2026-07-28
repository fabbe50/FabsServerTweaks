package com.fabbe50.fabsservertweaks.util;

import com.google.gson.JsonParseException;
import dev.architectury.platform.Platform;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.repository.PackRepository;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public final class BuiltinDatapackUtil {
    private static boolean NEEDS_RELOAD = false;

    private BuiltinDatapackUtil() {
    }

    public static CompletableFuture<Void> enable(MinecraftServer server, BuiltinDatapack pack) {
        return setEnabled(server, pack, true);
    }

    public static CompletableFuture<Void> disable(MinecraftServer server, BuiltinDatapack pack) {
        return setEnabled(server, pack, false);
    }

    public static CompletableFuture<Void> setEnabled(MinecraftServer server, BuiltinDatapack pack, boolean enabled) {
        Objects.requireNonNull(server, "server");
        Objects.requireNonNull(pack, "pack");

        PackRepository repository = server.getPackRepository();
        reloadIfUnavailable(repository, pack);

        String packId = resolvePackId(repository, pack);
        if (packId == null) {
            throw new IllegalStateException("Builtin datapack not found in repository: " + pack.fabricPackId() + " or " + pack.neoForgePackId());
        }

        LinkedHashSet<String> selectedIds = new LinkedHashSet<>(repository.getSelectedIds());
        if (enabled) {
            selectedIds.add(packId);
        } else {
            selectedIds.remove(packId);
        }

        if (pack.requiresRestart()) {
            NEEDS_RELOAD = true;
        }

        repository.setSelected(selectedIds);
        return server.reloadResources(repository.getSelectedIds());
    }

    public static boolean isEnabled(MinecraftServer server, BuiltinDatapack pack) {
        if (server == null || pack == null) {
            return false;
        }
        Objects.requireNonNull(server, "server");
        Objects.requireNonNull(pack, "pack");

        if (pack.requiresRestart() && NEEDS_RELOAD) {
            return false;
        }

        PackRepository repository = server.getPackRepository();
        reloadIfUnavailable(repository, pack);

        String packId = resolvePackId(repository, pack);
        return packId != null && repository.getSelectedIds().contains(packId);
    }

    public static String resolvePackId(PackRepository repository, BuiltinDatapack pack) {
        Objects.requireNonNull(repository, "repository");
        Objects.requireNonNull(pack, "pack");

        if (repository.isAvailable(pack.fabricPackId())) {
            return pack.fabricPackId();
        }
        if (repository.isAvailable(pack.neoForgePackId())) {
            return pack.neoForgePackId();
        }
        return null;
    }

    private static void reloadIfUnavailable(PackRepository repository, BuiltinDatapack pack) {
        if (!(repository.isAvailable(pack.fabricPackId()) || repository.isAvailable(pack.neoForgePackId()))) {
            try {
                repository.reload();
            } catch (JsonParseException ignored) {
            }
        }
    }
}
