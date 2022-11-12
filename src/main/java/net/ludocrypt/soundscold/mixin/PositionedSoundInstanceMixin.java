package net.ludocrypt.soundscold.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.ludocrypt.soundscold.SoColdSoundInstance;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;

@Mixin(PositionedSoundInstance.class)
public class PositionedSoundInstanceMixin {

	@Inject(method = "Lnet/minecraft/client/sound/PositionedSoundInstance;music(Lnet/minecraft/sound/SoundEvent;)Lnet/minecraft/client/sound/PositionedSoundInstance;", at = @At("HEAD"), cancellable = true)
	private static void soundsSoCold$music(SoundEvent sound, CallbackInfoReturnable<PositionedSoundInstance> ci) {
		ci.setReturnValue(new SoColdSoundInstance(sound.getId(), SoundCategory.MUSIC, 1.0F, 1.0F, SoundInstance.method_43221(), false, 0, SoundInstance.AttenuationType.NONE, 0.0, 0.0, 0.0, true));
	}

}
