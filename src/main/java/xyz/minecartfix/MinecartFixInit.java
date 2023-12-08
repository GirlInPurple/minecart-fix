package xyz.minecartfix;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class MinecartFixInit implements ModInitializer {
	public static final String MODID = "minecartfix";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

	public static List<PlayerEntity> blacklistedPlayers = new ArrayList<>();

	@Override
	public void onInitialize() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
				dispatcher.register(CommandManager.literal("toggleMinecartFix")
						.then(CommandManager.argument("value", BoolArgumentType.bool())
								.executes(context -> toggleMinecartFix(context, BoolArgumentType.getBool(context, "value"))))));
	}

	private int toggleMinecartFix(CommandContext<ServerCommandSource> context, boolean value) {
		ServerCommandSource source = context.getSource();
		PlayerEntity player = source.getPlayer();

		if (value) {
			blacklistedPlayers.remove(player);
			source.sendFeedback(() -> Text.translatable("text.minecartfix.feedback.enabled"), false);
		} else {
			blacklistedPlayers.add(player);
			source.sendFeedback(() -> Text.translatable("text.minecartfix.feedback.disabled"), false);
		}

		return 1;
	}
}