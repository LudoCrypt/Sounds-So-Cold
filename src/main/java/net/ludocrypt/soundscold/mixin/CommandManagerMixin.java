package net.ludocrypt.soundscold.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.brigadier.CommandDispatcher;

import net.ludocrypt.soundscold.PlayColdCommand;
import net.minecraft.command.CommandBuildContext;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

@Mixin(CommandManager.class)
public class CommandManagerMixin {

	@Shadow
	@Final
	private CommandDispatcher<ServerCommandSource> dispatcher;

	@Inject(method = "Lnet/minecraft/server/command/CommandManager;<init>(Lnet/minecraft/server/command/CommandManager$RegistrationEnvironment;Lnet/minecraft/command/CommandBuildContext;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/command/PlaySoundCommand;register(Lcom/mojang/brigadier/CommandDispatcher;)V", shift = Shift.AFTER))
	private void soundsSoCold$init(CommandManager.RegistrationEnvironment environment, CommandBuildContext context, CallbackInfo ci) {
		PlayColdCommand.register(this.dispatcher);
	}

}
