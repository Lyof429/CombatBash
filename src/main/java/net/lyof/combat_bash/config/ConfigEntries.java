package net.lyof.combat_bash.config;

public class ConfigEntries {
    public static void reload() {
        needsEnchantment = new ConfigEntry<>("combat_bash.needs_enchantment", false).get();
        damage = new ConfigEntry<>("combat_bash.damage", 4).get();
        playerKnockback = new ConfigEntry<>("combat_bash.player_knockback", 1).get();
        targetKnockback = new ConfigEntry<>("combat_bash.target_knockback", 1).get();
        extraExhaustion = new ConfigEntry<>("combat_bash.extra_exhaustion", 0.03).get();
        immunity = new ConfigEntry<>("combat_bash.roll_immunity", true).get();
        ignorePlayers = new ConfigEntry<>("combat_bash.ignore_players", true).get();

        enableMultiImmun = new ConfigEntry<>("enable_multiplayer_immunity_frames", true).get();
    }

    public static boolean needsEnchantment;
    public static double damage;
    public static double playerKnockback;
    public static double targetKnockback;
    public static double extraExhaustion;
    public static boolean immunity;
    public static boolean ignorePlayers;

    public static boolean enableMultiImmun;
}
