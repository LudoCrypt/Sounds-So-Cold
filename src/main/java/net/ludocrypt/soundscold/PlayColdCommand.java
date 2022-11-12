package net.ludocrypt.soundscold;

import java.util.Collection;
import java.util.Iterator;

import org.quiltmc.qsl.networking.api.PacketByteBufs;
import org.quiltmc.qsl.networking.api.ServerPlayNetworking;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.command.argument.Vec3ArgumentType;
import net.minecraft.command.suggestion.SuggestionProviders;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

public class PlayColdCommand {
	public static final Identifier PLAYCOLD_PACKET = new Identifier("soundscold", "playcold");
	private static final SimpleCommandExceptionType FAILED_EXCEPTION = new SimpleCommandExceptionType(Text.translatable("commands.playsound.failed"));

	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		RequiredArgumentBuilder<ServerCommandSource, Identifier> requiredArgumentBuilder = CommandManager.argument("sound", IdentifierArgumentType.identifier()).suggests(SuggestionProviders.AVAILABLE_SOUNDS);

		for (SoundCategory soundCategory : SoundCategory.values()) {
			requiredArgumentBuilder.then(makeArgumentsForCategory(soundCategory));
		}

		dispatcher.register(CommandManager.literal("playcold").requires(source -> source.hasPermissionLevel(2)).then(requiredArgumentBuilder));
	}

	private static LiteralArgumentBuilder<ServerCommandSource> makeArgumentsForCategory(SoundCategory category) {
		return CommandManager.literal(category.getName()).then(CommandManager.argument("targets", EntityArgumentType.players()).executes(context -> execute(context.getSource(), EntityArgumentType.getPlayers(context, "targets"), IdentifierArgumentType.getIdentifier(context, "sound"), category, context.getSource().getPosition(), 1.0F, 1.0F, 0.0F)).then(CommandManager.argument("pos", Vec3ArgumentType.vec3()).executes(context -> execute(context.getSource(), EntityArgumentType.getPlayers(context, "targets"), IdentifierArgumentType.getIdentifier(context, "sound"), category, Vec3ArgumentType.getVec3(context, "pos"), 1.0F, 1.0F, 0.0F)).then(CommandManager.argument("volume", FloatArgumentType.floatArg(0.0F)).executes(context -> execute(context.getSource(), EntityArgumentType.getPlayers(context, "targets"), IdentifierArgumentType.getIdentifier(context, "sound"), category, Vec3ArgumentType.getVec3(context, "pos"), context.getArgument("volume", Float.class), 1.0F, 0.0F)).then(CommandManager.argument("pitch", FloatArgumentType.floatArg(0.0F, 2.0F)).executes(context -> execute(context.getSource(), EntityArgumentType.getPlayers(context, "targets"), IdentifierArgumentType.getIdentifier(context, "sound"), category, Vec3ArgumentType.getVec3(context, "pos"), context.getArgument("volume", Float.class), context.getArgument("pitch", Float.class), 0.0F)).then(CommandManager.argument("minVolume", FloatArgumentType.floatArg(0.0F, 1.0F)).executes(context -> execute(context.getSource(), EntityArgumentType.getPlayers(context, "targets"), IdentifierArgumentType.getIdentifier(context, "sound"), category, Vec3ArgumentType.getVec3(context, "pos"), context.getArgument("volume", Float.class), context.getArgument("pitch", Float.class), context.getArgument("minVolume", Float.class))))))));
	}

	private static int execute(ServerCommandSource source, Collection<ServerPlayerEntity> targets, Identifier sound, SoundCategory category, Vec3d pos, float volume, float pitch, float minVolume) throws CommandSyntaxException {
		double d = Math.pow(volume > 1.0F ? (double) (volume * 16.0F) : 16.0, 2.0);
		int i = 0;
		long l = source.getWorld().getRandom().nextLong();
		Iterator<ServerPlayerEntity> var13 = targets.iterator();

		while (true) {
			ServerPlayerEntity serverPlayerEntity;
			Vec3d vec3d;
			float j;
			while (true) {
				if (!var13.hasNext()) {
					if (i == 0) {
						throw FAILED_EXCEPTION.create();
					}

					if (targets.size() == 1) {
						source.sendFeedback(Text.translatable("commands.playsound.success.single", sound, ((ServerPlayerEntity) targets.iterator().next()).getDisplayName()), true);
					} else {
						source.sendFeedback(Text.translatable("commands.playsound.success.multiple", sound, targets.size()), true);
					}

					return i;
				}

				serverPlayerEntity = (ServerPlayerEntity) var13.next();
				double e = pos.x - serverPlayerEntity.getX();
				double f = pos.y - serverPlayerEntity.getY();
				double g = pos.z - serverPlayerEntity.getZ();
				double h = e * e + f * f + g * g;
				vec3d = pos;
				j = volume;
				if (!(h > d)) {
					break;
				}

				if (!(minVolume <= 0.0F)) {
					double k = Math.sqrt(h);
					vec3d = new Vec3d(serverPlayerEntity.getX() + e / k * 2.0, serverPlayerEntity.getY() + f / k * 2.0, serverPlayerEntity.getZ() + g / k * 2.0);
					j = minVolume;
					break;
				}
			}

			PacketByteBuf buf = PacketByteBufs.create();

			buf.writeIdentifier(sound);
			buf.writeEnumConstant(category);
			buf.writeInt((int) (vec3d.x * 8.0));
			buf.writeInt((int) (vec3d.y * 8.0));
			buf.writeInt((int) (vec3d.z * 8.0));
			buf.writeFloat(j);
			buf.writeFloat(pitch);
			buf.writeLong(l);

			ServerPlayNetworking.send(serverPlayerEntity, PLAYCOLD_PACKET, buf);
			++i;
		}
	}
}
