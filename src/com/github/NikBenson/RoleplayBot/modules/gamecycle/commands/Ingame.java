package com.github.nikbenson.roleplaybot.modules.gamecycle.commands;

import com.github.nikbenson.roleplaybot.commands.Command;
import com.github.nikbenson.roleplaybot.commands.context.GuildContext;
import com.github.nikbenson.roleplaybot.modules.gamecycle.GameManager;
import net.dv8tion.jda.api.entities.Guild;

import static com.github.nikbenson.roleplaybot.modules.gamecycle.GameCycleModule.getGameManager;

public class Ingame extends Command<GuildContext> {
	@Override
	public Class<GuildContext> getContext() {
		return GuildContext.class;
	}

	@Override
	public String getRegex() {
		return "ingame .*";
	}

	@Override
	public String execute(String command, GuildContext context) {
		GameManager gameManager = getGameManager(((Guild) context.getParams().get("guild")));
		if(gameManager != null) {
			String args = command.substring(7);

			String special = executeSpecial(args, gameManager);
			if (special != null) {
				return special;
			}

			String result = gameManager.getSeason().get(args);

			if (result == null) return String.format("%s not found!", args);
			else return result;
		}

		return "Module not activated!";
	}

	private String executeSpecial(String args, GameManager gameManager) {
		if(args.equals("day")) {
			return String.valueOf(gameManager.getDay());
		} else if(args.matches("time( \"[. :-GyMwWDdFEuaHkKhmsSzZX]*\")?")) {
			String pattern = "HH:mm";

			if(!args.equals("time")) {
				pattern = args.substring(7, args.length() - 2);
			}

			return gameManager.getTime(pattern);
		} else {
			return null;
		}
	}
}
