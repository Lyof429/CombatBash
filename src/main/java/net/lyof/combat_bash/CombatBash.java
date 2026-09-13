package net.lyof.combat_bash;

import net.fabricmc.api.ModInitializer;
import net.lcc.sollib.api.common.logger.SolLogger;
import net.lcc.sollib.api.common.registry.SolModContainer;
import net.lyof.combat_bash.config.ModConfig;
import net.lyof.combat_bash.effect.ModEffects;
import net.lyof.combat_bash.enchant.ModEnchants;
import net.lyof.combat_bash.event.ModEvents;

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
}