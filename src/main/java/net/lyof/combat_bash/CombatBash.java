package net.lyof.combat_bash;

import com.mojang.logging.LogUtils;
import net.lyof.combat_bash.configs.ModJsonConfigs;
import net.lyof.combat_bash.effects.ModEffects;
import net.lyof.combat_bash.enchant.ModEnchants;
import net.lyof.combat_bash.events.ModEvents;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

@Mod(CombatBash.MOD_ID)
public class CombatBash {
    public static final String MOD_ID = "combat_bash";
    public static final Logger LOGGER = LogUtils.getLogger();

    public CombatBash() {
        ModJsonConfigs.register();
        ModEvents.register();

        IEventBus eventbus = FMLJavaModLoadingContext.get().getModEventBus();
        ModEffects.register(eventbus);
        ModEnchants.register(eventbus);

        // Register the commonSetup method for modloading
        eventbus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
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
