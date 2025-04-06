package net.lyof.combat_bash.enchant.custom;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class DashEnchantment extends Enchantment {
    public DashEnchantment() {
        super(Rarity.UNCOMMON, EnchantmentCategory.ARMOR_LEGS, new EquipmentSlot[] {EquipmentSlot.LEGS});
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }
}
