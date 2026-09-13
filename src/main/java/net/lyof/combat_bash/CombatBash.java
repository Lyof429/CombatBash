package net.lyof.combat_bash;

import net.fabricmc.api.ModInitializer;
import net.lcc.sollib.api.common.logger.SolLogger;
import net.lcc.sollib.api.common.registry.SolModContainer;
import net.lyof.combat_bash.config.ModConfig;
import net.lyof.combat_bash.effect.ModEffects;
import net.lyof.combat_bash.enchant.ModEnchants;
import net.lyof.combat_bash.event.ModEvents;

import java.util.HashMap;
import java.util.Map;

public class CombatBash implements ModInitializer {
	public static final String MOD_ID = "combat_bash";
	public static final SolModContainer MOD = new SolModContainer("Combat Bash", MOD_ID);

	@Override
	public void onInitialize() {
		MOD.createConfig("combat_bash", 2.0, ModConfig::build);
		ModEvents.register();

		ModEffects.register();
		ModEnchants.register();
	}

	public static SolLogger log() {
		return MOD.getLogger();
	}


	// Entity: Player: Frames
	public static Map<String, Map<String, Integer>> FRAMES = new HashMap<>();

	public static void tickFrames(String entity) {
		if (!FRAMES.containsKey(entity)) return;
		Map<String, Integer> frames = FRAMES.get(entity);

		for (String player : frames.keySet()) {
			if (frames.get(player) > 0) frames.replace(player, frames.get(player) - 1);
			//if (frames.get(player) <= 0) frames.remove(player);
		}
	}
}