package net.lyof.combat_bash;

import net.lcc.sollib.api.common.logger.SolLogger;
import net.lcc.sollib.api.common.registry.SolModContainer;
import net.lyof.combat_bash.setup.ModConfig;
import net.lyof.combat_bash.effect.ModEffects;
import net.lyof.combat_bash.enchant.ModEnchants;
import net.lyof.combat_bash.event.ModEvents;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(CombatBash.MOD_ID)
public class CombatBash {
	public static final String MOD_ID = "combat_bash";
	public static final SolModContainer MOD = new SolModContainer("Combat Bash", MOD_ID);

	public CombatBash(IEventBus eventBus, ModContainer modContainer) {
		MOD.createConfig(MOD_ID, 2.0, ModConfig::build);
		ModEvents.register();

		ModEffects.register();
		ModEnchants.register();
	}

	public static SolLogger log() {
		return MOD.getLogger();
	}
}