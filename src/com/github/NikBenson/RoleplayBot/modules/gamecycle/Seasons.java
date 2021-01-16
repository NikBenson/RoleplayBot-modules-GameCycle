package com.github.NikBenson.RoleplayBot.modules.gamecycle;

import com.github.NikBenson.RoleplayBot.modules.gamecycle.ChooseRandom;
import com.github.NikBenson.RoleplayBot.modules.gamecycle.Cycled;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Seasons {
	private Season[] all;
	private int current;

	public Season getCurrent() {
		return all[current];
	}

	public void update() {
		getCurrent().next();
	}

	public Seasons(JSONArray json, long passedUpdates) {
		all = new Season[json.size()];

		for(int i = 0; i < all.length; i++) {
			all[i] = new Season((JSONObject) json.get(i));
		}

		current = 0;

		getCurrent().next(passedUpdates);
	}

	public class Season {
		private final long length;
		private long time = 1;

		private final Map<String, String> constants = new HashMap<>();
		private final Map<String, Cycled> cyclical = new HashMap<>();
		private final Map<String, ChooseRandom> randoms = new HashMap<>();

		private Season(JSONObject json) {
			length = (long) json.get("length");

			JSONArray values = (JSONArray) json.get("values");

			for (Object o : values) {
				JSONObject value = (JSONObject) o;
				String type = (String) value.getOrDefault("type", "constant");
				switch (type) {
					case "constant" -> addConstant(value);
					case "cyclic" -> addCyclic(value);
					case "probability" -> addProbability(value);
				}
			}
		}

		private void addConstant(JSONObject json) {
			String name = (String) json.get("name");
			String value = (String) json.get("value");
			constants.put(name, value);
		}

		private void addCyclic(JSONObject json) {
			String name = (String) json.get("name");
			JSONArray values = (JSONArray) json.get("values");
			cyclical.put(name, new Cycled(values));
		}

		private void addProbability(JSONObject json) {
			String name = (String) json.get("name");
			JSONArray values = (JSONArray) json.get("values");
			randoms.put(name, new ChooseRandom(values));
		}

		private void next() {
			next(1L);
		}

		private void next(long delta) {
			if (delta > 0) {
				time++;
				cycle();

				if (time > length) {
					time = 1;

					if (++current >= all.length) {
						current = 0;
					}
				}

				getCurrent().next(delta - 1);
			}
		}

		private void cycle() {
			for (Cycled cycled : cyclical.values()) {
				cycled.next();
			}
			generateValues();
		}

		private void generateValues() {
			for (ChooseRandom random : randoms.values()) {
				random.next();
			}
		}

		public String get(String name) {
			if (constants.containsKey(name)) {
				return constants.get(name);
			} else if (cyclical.containsKey(name)) {
				return cyclical.get(name).get();
			} else if (randoms.containsKey(name)) {
				return randoms.get(name).get();
			} else {
				return null;
			}
		}
	}
}
