package net.lyof.combat_bash.configs;

import com.google.gson.Gson;
import net.lyof.combat_bash.CombatBash;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ModJsonConfigs {
    public static final ConfigEntry<Double> VERSION = new ConfigEntry<>("TECHNICAL.VERSION_DO_NOT_EDIT", 0d);
    public static final ConfigEntry<Boolean> RELOAD = new ConfigEntry<>("TECHNICAL.FORCE_RELOAD", false);

    public static Map CONFIG = new TreeMap<>();


    public static void register() {
        register(false);
    }

    public static void register(boolean force) {
        String path = System.getProperty("user.dir") + File.separator +
                "config" + File.separator + CombatBash.MOD_ID + "-common.json";

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
        CONFIG = new Gson().fromJson(configContent, Map.class);

        if (!force && (RELOAD.get() || VERSION.get() < getVersion()))
            register(true);
    }

    public static double getVersion() {
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
