package net.ludocrypt.soundscold;

import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;

import eu.midnightdust.lib.config.MidnightConfig;
import eu.midnightdust.lib.config.MidnightConfig.Entry;

public class SoColdConfig implements ClientModInitializer {

	@Entry
	public static float vibratoStrength = 0.01F;

	@Entry
	public static float restingPitch = 0.5F;

	@Entry
	public static float pitchRange = 0.2F;

	@Override
	public void onInitializeClient(ModContainer mod) {
		MidnightConfig.init("soundscold", SoColdConfig.class);

	}

}
