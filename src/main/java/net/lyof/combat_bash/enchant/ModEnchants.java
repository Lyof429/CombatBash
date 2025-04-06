package net.lyof.combat_bash.enchant;

import net.lyof.combat_bash.CombatBash;
import net.lyof.combat_bash.enchant.custom.DashEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModEnchants {
    public static void register() {
        CombatBash.log("Registering Enchants for modid : combat_bash");
    }

    public static Enchantment INERTIA = Registry.register(Registries.ENCHANTMENT, CombatBash.makeID("inertia"),
            new DashEnchantment());

    public static Enchantment SWIFTFOOTED = Registry.register(Registries.ENCHANTMENT, CombatBash.makeID("swiftfooted"),
            new DashEnchantment());
}
