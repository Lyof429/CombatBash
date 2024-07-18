package net.lyof.combat_bash;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.lyof.combat_bash.config.ModConfig;
import net.lyof.combat_bash.effect.ModEffects;
import net.lyof.combat_bash.enchant.ModEnchants;
import net.lyof.combat_bash.event.ModEvents;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class CombatBash implements ModInitializer {
    private static final Logger LOGGER = LoggerFactory.getLogger("Combat Bash");
	public static final String MOD_ID = "combat_bash";

	@Override
	public void onInitialize() {
		ModConfig.register();
		ModEvents.register();

		ModEffects.register();
		ModEnchants.register();

		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
			@Override
			public Identifier getFabricId() {
				return makeID("reload_listener");
			}

			@Override
			public void reload(ResourceManager manager) {
				ModConfig.register();
			}
		});
	}

	public static Identifier makeID(String name) {
		return Identifier.of(MOD_ID, name);
	}

	public static <T> T log(T message) {
		LOGGER.info(String.valueOf(message));
		return message;
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