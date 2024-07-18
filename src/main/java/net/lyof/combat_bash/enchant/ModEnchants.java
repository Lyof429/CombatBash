package net.lyof.combat_bash.enchant;

import net.lyof.combat_bash.CombatBash;
import net.lyof.combat_bash.enchant.custom.InertiaEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModEnchants {
    public static void register() {
        CombatBash.log("Registering Enchants for modid : combat_bash");
    }

    public static Enchantment INERTIA = Registry.register(Registries.ENCHANTMENT, CombatBash.makeID("inertia"),
            new InertiaEnchantment());
}
