package net.lyof.combat_bash.enchant;

import net.lyof.combat_bash.CombatBash;
import net.lyof.combat_bash.enchant.custom.DashEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModEnchants {
    public static void register() {
        CombatBash.log("Registering Enchants for modid : combat_bash");
    }

    public static int getLevel(Enchantment enchant, PlayerEntity player) {
        return enchant == null ? 0 : EnchantmentHelper.getEquipmentLevel(enchant, player);
    }

    public static Enchantment INERTIA = Registry.register(Registries.ENCHANTMENT, CombatBash.makeID("inertia"),
            DashEnchantment.of("enchantments.inertia"));

    public static Enchantment SWIFTFOOTED = Registry.register(Registries.ENCHANTMENT, CombatBash.makeID("swiftfooted"),
            DashEnchantment.of("enchantments.swiftfooted"));
}
