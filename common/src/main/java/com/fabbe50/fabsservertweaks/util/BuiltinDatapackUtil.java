package com.fabbe50.fabsservertweaks.util;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.repository.PackRepository;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public final class BuiltinDatapackUtil {
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
        repository.reload();

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

        repository.setSelected(selectedIds);
        return server.reloadResources(repository.getSelectedIds());
    }

    public static boolean isEnabled(MinecraftServer server, BuiltinDatapack pack) {
        Objects.requireNonNull(server, "server");
        Objects.requireNonNull(pack, "pack");

        PackRepository repository = server.getPackRepository();
        repository.reload();

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
}
