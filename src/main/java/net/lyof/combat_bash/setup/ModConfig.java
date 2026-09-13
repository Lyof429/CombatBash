package net.lyof.combat_bash.setup;

import net.lcc.sollib.api.common.config.ConfigEntry;
import net.lcc.sollib.api.common.config.builder.IJsonBuilder;

public class ModConfig {
    public static void build(IJsonBuilder builder) {
        builder.addObject("combat_bash", main -> main
                .comment("If true, bashing will not be available unless you have a piece of armor with a combat_bash:roll_damage enchantment on it")
                .add("needs_enchantment", false)
                .bind(needsEnchantment)
                .comment("Base damage dealt by a bash (in half hearts)")
                .add("damage", 4)
                .bind(damage)
                .comment("How strongly will the rolling player be knocked backwards after a bash")
                .add("player_knockback", 1)
                .bind(playerKnockback)
                .comment("How strongly will the hit entity be knocked backwards after a bash")
                .add("target_knockback", 1)
                .bind(targetKnockback)
                .comment("Base food points to be deducted from the rolling player after a bash")
                .add("extra_exhaustion", 0.03)
                .bind(extraExhaustion)
                .comment("Should players be immune to damage during rolls")
                .add("roll_immunity", true)
                .bind(rollImmunity)
                .comment("If false, rolling into other players will hit them with a bash")
                .add("ignore_players", true)
                .bind(ignorePlayers)
        )
        .comment()
        .comment("Set these to false to disable the corresponding enchantment from appearing in game (disables at registry level)")
        .comment("  Only applies to Combat Bash enchantments")
        .addObject("enabled_enchants", enabled_enchants -> enabled_enchants
                .add("inertia", true)
                .add("swiftfooted", true)
        )
        .comment()
        .comment("If true, hit mobs will track their immunity frames to be per player and not global.")
        .comment("  Useful if you want to tackle bosses with friends")
        .comment("  Disable if you encounter issues with very fast hitting weapons not registering hits properly")
        .add("enable_multiplayer_immunity_frames", true)
        .bind(enableMultiImmun);
    }


    public static final ConfigEntry<Boolean> needsEnchantment = new ConfigEntry<>(false);
    public static final ConfigEntry<Double> damage = new ConfigEntry<>(4d);
    public static final ConfigEntry<Double> playerKnockback = new ConfigEntry<>(1d);
    public static final ConfigEntry<Double> targetKnockback = new ConfigEntry<>(1d);
    public static final ConfigEntry<Double> extraExhaustion = new ConfigEntry<>(0.03d);
    public static final ConfigEntry<Boolean> rollImmunity = new ConfigEntry<>(true);
    public static final ConfigEntry<Boolean> ignorePlayers = new ConfigEntry<>(true);

    public static final ConfigEntry<Boolean> enableMultiImmun = new ConfigEntry<>(true);
}
