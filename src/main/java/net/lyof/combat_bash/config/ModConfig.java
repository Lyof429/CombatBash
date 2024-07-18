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
    "FORCE_RELOAD": false
  },

  "combat_bash": {
    "needs_enchantment": false,
    "damage": 4,
    "player_knockback": 1,
    "target_knockback": 1,
    "extra_exhaustion": 0.03,
    "roll_immunity": true,
    "ignore_players": true
  },
  
  "enable_multiplayer_immunity_frames": true
}""";
}
