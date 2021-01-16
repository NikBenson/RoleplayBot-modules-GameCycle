package com.github.NikBenson.RoleplayBot.modules.gamecycle.commands;

import com.github.NikBenson.RoleplayBot.commands.Command;
import com.github.NikBenson.RoleplayBot.commands.context.Context;
import com.github.NikBenson.RoleplayBot.modules.gamecycle.GameManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import static com.github.NikBenson.RoleplayBot.modules.gamecycle.GameCycle.getGameManager;

public class Ingame extends Command<Context> {

	@Override
	public Class<Context> getContext() {
		return Context.class;
	}

	@Override
	public String getRegex() {
		return "ingame .*";
	}

	@Override
	public String execute(String command, Context context) {
		GameManager gameManager = getGameManager(((MessageReceivedEvent) context.getParams().get("event")).getGuild());
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
