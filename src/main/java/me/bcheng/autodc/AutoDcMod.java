package me.bcheng.autodc;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.networking.v1.S2CPlayChannelEvents;
import net.minecraft.client.MinecraftClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AutoDcMod implements ClientModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("autodc");

	@Override
	public void onInitializeClient() {
		LOGGER.info("Hello Fabric world!");
		AutoDcEventCallback.EVENT.register(reason -> {
			LOGGER.info("DcEvent reason {}", reason);
		});
	}
}
