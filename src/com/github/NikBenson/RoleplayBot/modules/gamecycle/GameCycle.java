package com.github.NikBenson.RoleplayBot.modules.gamecycle;

import com.github.NikBenson.RoleplayBot.commands.Command;
import com.github.NikBenson.RoleplayBot.modules.RoleplayBotModule;
import net.dv8tion.jda.api.entities.Guild;

import java.util.HashMap;
import java.util.Map;

public class GameCycle implements RoleplayBotModule {
	private static GameCycle instance;
	private final Map<Guild, GameManager> managers = new HashMap<>();

	public GameCycle() {
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
			managers.put(guild, new GameManager(guild));
		}
	}

	@Override
	public void unload(Guild guild) {
		if(managers.containsKey(guild)) {
			managers.remove(guild);
		}
	}

	public static GameManager getGameManager(Guild guild) {
		return instance.managers.get(guild);
	}
}
