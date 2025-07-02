package net.lyof.combat_bash.config;

import com.google.gson.Gson;
import net.fabricmc.loader.api.FabricLoader;
import net.lyof.combat_bash.CombatBash;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ModConfig {
    static final ConfigEntry<Double> VERSION = new ConfigEntry<>("TECHNICAL.VERSION_DO_NOT_EDIT", 0d);
    static final ConfigEntry<Boolean> RELOAD = new ConfigEntry<>("TECHNICAL.FORCE_RESET", false);

    static Map CONFIG = new TreeMap<>();


    public static void register() {
        register(false);
    }

    public static void register(boolean force) {
        String path = FabricLoader.getInstance().getConfigDir().resolve(CombatBash.MOD_ID + ".json").toString();

        CombatBash.log("Loading Configs for Combat Bash");

        // Create config file if it doesn't exist already
        File config = new File(path);
        boolean create = !config.isFile();

        if (create || force) {
            try {
                config.delete();
                config.createNewFile();

                FileWriter writer = new FileWriter(path);
                writer.write(DEFAULT_CONFIG);
                writer.close();

                CombatBash.log("Combat Bash Config file created");
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }


        String configContent = DEFAULT_CONFIG;
        try {
            configContent = FileUtils.readFileToString(config, StandardCharsets.UTF_8);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        CONFIG = new Gson().fromJson(parseJson(configContent), Map.class);
        ConfigEntries.reload();

        if (!force && (RELOAD.get() || VERSION.get() < getVersion())) {
            register(true);
        }
    }

    static String parseJson(String text) {
        StringBuilder result = new StringBuilder();

        for (String line : text.split("\n")) {
            if (!line.strip().startsWith("//"))
                result.append("\n").append(line);
        }

        return result.toString();
    }

    static double getVersion() {
        String text = DEFAULT_CONFIG;
        int start = 0;

        while (!List.of('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.').contains(text.charAt(start))) {
            start++;
        }
        int end = start + 1;
        while (List.of('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.').contains(text.charAt(end))) {
            end++;
        }

        return Double.parseDouble(text.substring(start, end));
    }


    public static final String DEFAULT_CONFIG = """
{
  "TECHNICAL": {
    "VERSION_DO_NOT_EDIT": 1.1,
    "FORCE_RESET": false
  },

  "combat_bash": {
    // If true, bashing will not be available unless you have a piece of armor with Inertia on it
    "needs_enchantment": false,
    // Base damage dealt by a bash (in half hearts)
    "damage": 4,
    // How strongly will the rolling player be knocked backwards after a bash
    "player_knockback": 1,
    // How strongly will the hit entity be knocked backwards after a bash
    "target_knockback": 1,
    // Food points to be deducted from the rolling player after a bash
    "extra_exhaustion": 0.03,
    // Should players be immune to damage during rolls
    "roll_immunity": true,
    // If false, rolling into other players will hit them with a bash
    "ignore_players": true
  },
  
  "enchantments": {
    "inertia": {
      // Set to 0 to disable
      "max_level": 3,
      // Which armor piece can get this enchantment. Must be one of "HELMET", "CHESTPLATE", "LEGGINGS" or "BOOTS"
      "target": "LEGGINGS"
    },
    "swiftfooted": {
      // Set to 0 to disable
      "max_level": 3,
      // Which armor piece can get this enchantment. Must be one of "HELMET", "CHESTPLATE", "LEGGINGS" or "BOOTS"
      "target": "LEGGINGS"
    }
  },
  
  // If true, hit mobs will track their immunity frames to be per player and not global.
  //   Useful if you want to tackle bosses with friends
  //   Disable if you encounter issues with very fast hitting weapons not registering hits properly
  "enable_multiplayer_immunity_frames": true
}""";
}
