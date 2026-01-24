//? if forge {
/*package com.fabbe50.fabsservertweaks.fabsservertweaks.platforms.forge;

import com.fabbe50.fabsservertweaks.fabsservertweaks.ConfigScreen;
import com.fabbe50.fabsservertweaks.fabsservertweaks.FabsServerTweaksInit;
import com.fabbe50.fabsservertweaks.fabsservertweaks.ModPlatform;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLPaths;

@Mod("fabsservertweaks")
public class FabsServerTweaksForge {
	public FabsServerTweaksForge() {
		FabsServerTweaksInit.entrypoint(new ForgePlatform());
        MinecraftForge.registerConfigScreen(ConfigScreen::getConfigScreen);
	}
	public static class ForgePlatform implements ModPlatform {
		@Override
		public String getModloader() {
			return "LexForge";
		}

		@Override
		public boolean isModLoaded(String modId) {
			return ModList.get().isLoaded(modId);
		}
		
		@Override
        public String getConfigPath() {
            return FMLPaths.CONFIGDIR.get().toString();
        }
	}

}
*///?}