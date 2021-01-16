package com.github.NikBenson.RoleplayBot.modules.gamecycle;

import com.github.NikBenson.RoleplayBot.commands.Command;
import com.github.NikBenson.RoleplayBot.configurations.ConfigurationManager;
import com.github.NikBenson.RoleplayBot.modules.RoleplayBotModule;
import net.dv8tion.jda.api.entities.Guild;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GameCycleModule implements RoleplayBotModule {
	private static GameCycleModule instance;
	private final Map<Guild, GameManager> managers = new HashMap<>();

	public GameCycleModule() {
		instance = this;
		Command.register(new com.github.NikBenson.RoleplayBot.modules.gamecycle.commands.Ingame());
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

	public static GameManager getGameManager(Guild guild) {
		return instance.managers.get(guild);
	}
}
