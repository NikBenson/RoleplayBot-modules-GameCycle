package com.github.nikbenson.roleplaybot.modules.gamecycle;

import com.github.nikbenson.roleplaybot.commands.Command;
import com.github.nikbenson.roleplaybot.configurations.ConfigurationManager;
import com.github.nikbenson.roleplaybot.modules.RoleplayBotModule;
import net.dv8tion.jda.api.entities.Guild;

import java.util.HashMap;
import java.util.Map;

public class GameCycleModule implements RoleplayBotModule {
	private static GameCycleModule instance;
	private final Map<Guild, GameManager> managers = new HashMap<>();

	public GameCycleModule() {
		instance = this;
		Command.register(new com.github.nikbenson.roleplaybot.modules.gamecycle.commands.Ingame());
	}

	@Override
	public boolean isActive(Guild guild) {
		return managers.containsKey(guild);
	}

	@Override
	public void load(Guild guild) {
		if(!managers.containsKey(guild)) {
			GameManager gameManager = new GameManager(guild);
			ConfigurationManager configurationManager = ConfigurationManager.getInstance();

			configurationManager.registerConfiguration(gameManager);
			try {
				configurationManager.load(gameManager);
			} catch (Exception ignored) {}

			managers.put(guild, gameManager);
		}
	}

	@Override
	public void unload(Guild guild) {
		managers.remove(guild);
	}

	@Override
	public Guild[] getLoaded() {
		return managers.keySet().toArray(new Guild[0]);
	}

	public static GameManager getGameManager(Guild guild) {
		return instance.managers.get(guild);
	}
}
