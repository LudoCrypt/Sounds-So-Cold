package net.ludocrypt.soundscold.mixin;

import java.util.Map;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.google.common.collect.Multimap;

import net.ludocrypt.soundscold.SoColdSoundInstance;
import net.minecraft.client.sound.Channel;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundSystem;
import net.minecraft.sound.SoundCategory;

@Mixin(SoundSystem.class)
public class SoundSystemMixin {

	@Shadow
	@Final
	private Map<SoundInstance, Channel.SourceManager> sources;

	@Shadow
	@Final
	private Multimap<SoundCategory, SoundInstance> sounds;

	@Inject(method = "Lnet/minecraft/client/sound/SoundSystem;tick()V", at = @At("HEAD"))
	private void soundsSoCold$tick(CallbackInfo ci) {
		sounds.forEach((category, instance) -> {
			if (instance instanceof SoColdSoundInstance soCold) {
				soCold.tick();
				Channel.SourceManager sourceManager = this.sources.get(soCold);
				if (sourceManager != null) {
					sourceManager.run(source -> source.setPitch(soCold.getPitch()));
				}
			}
		});
	}

}
