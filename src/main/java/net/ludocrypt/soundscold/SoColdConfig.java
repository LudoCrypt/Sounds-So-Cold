package net.ludocrypt.soundscold;

import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import org.quiltmc.qsl.networking.api.client.ClientPlayNetworking;

import eu.midnightdust.lib.config.MidnightConfig;
import eu.midnightdust.lib.config.MidnightConfig.Entry;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.random.RandomGenerator;

public class SoColdConfig implements ClientModInitializer {

	@Entry
	public static float vibratoStrength = 0.01F;

	@Entry
	public static float restingPitch = 0.5F;

	@Entry
	public static float pitchRange = 0.2F;

	@Entry
	public static boolean coldDiscs = true;

	@Override
	public void onInitializeClient(ModContainer mod) {
		MidnightConfig.init("soundscold", SoColdConfig.class);

		ClientPlayNetworking.registerGlobalReceiver(PlayColdCommand.PLAYCOLD_PACKET, (client, handler, buf, responseSender) -> {

			Identifier id = buf.readIdentifier();
			SoundCategory category = buf.readEnumConstant(SoundCategory.class);
			int fixedX = buf.readInt();
			int fixedY = buf.readInt();
			int fixedZ = buf.readInt();
			float volume = buf.readFloat();
			float pitch = buf.readFloat();
			long seed = buf.readLong();

			client.execute(() -> client.getSoundManager().play(new SoColdSoundInstance(id, category, volume, pitch, RandomGenerator.createLegacy(seed), false, 0, SoundInstance.AttenuationType.LINEAR, fixedX, fixedY, fixedZ, false)));
		});
	}

}
