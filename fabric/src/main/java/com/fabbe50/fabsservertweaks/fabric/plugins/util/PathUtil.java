package com.fabbe50.fabsservertweaks.fabric.plugins.util;

public class PathUtil {
    public static String getPath(String modId, FolderType folder, String fileName, String extension) {
        return "assets/" + modId + "/" + folder.getFolderName() + "/" + fileName + "." + extension;
    }

    public static String getPath(String modId, FolderType folder, String subFolder, String fileName, String extension) {
        return "assets/" + modId + "/" + folder.getFolderName() + "/" + subFolder + "/" + fileName + "." + extension;
    }

    public enum FolderType {
        BLOCK_STATES("blockstates"),
        ITEMS("items"),
        BLOCK_MODELS("models/block"),
        ITEM_MODELS("models/item"),
        TEXTURES("textures"),
        BLOCK_TEXTURES("textures/block"),
        ENTITY_TEXTURES("textures/entity"),
        ;

        private final String folderName;
        FolderType(String folderName) {
            this.folderName = folderName;
        }

        public String getFolderName() {
            return folderName;
        }
    }
}
