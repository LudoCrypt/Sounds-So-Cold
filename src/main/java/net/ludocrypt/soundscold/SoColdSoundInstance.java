package net.ludocrypt.soundscold;

import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.random.RandomGenerator;

public class SoColdSoundInstance extends PositionedSoundInstance {

	public int ticks = 0;
	public int alarm = 100;
	public float basePitch = SoColdConfig.restingPitch;

	public SoColdSoundInstance(Identifier identifier, SoundCategory soundCategory, float f, float g, RandomGenerator random, boolean bl, int i, AttenuationType attenuationType, double d, double e, double h, boolean bl2) {
		super(identifier, soundCategory, f, g, random, bl, i, attenuationType, d, e, h, bl2);
	}

	@Override
	public float getPitch() {
		return this.basePitch + (float) (Math.sin((float) (this.ticks * 1.6F) % (2 * Math.PI)) * SoColdConfig.vibratoStrength);
	}

	public void tick() {
		this.ticks++;
		this.alarm--;
		if (this.alarm <= 0) {
			this.alarm = 2 + this.random.nextInt(60);
			this.basePitch = MathHelper.nextBetween(this.random, SoColdConfig.restingPitch - SoColdConfig.pitchRange, SoColdConfig.restingPitch + SoColdConfig.pitchRange);
		}
	}

}
