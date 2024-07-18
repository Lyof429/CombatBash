package net.lyof.combat_bash.enchant.custom;

import net.combatroll.enchantments.AmplifierEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;

public class InertiaEnchantment extends Enchantment {
    public InertiaEnchantment() {
        super(Rarity.UNCOMMON, EnchantmentTarget.ARMOR, new EquipmentSlot[] {EquipmentSlot.LEGS});
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }
}
