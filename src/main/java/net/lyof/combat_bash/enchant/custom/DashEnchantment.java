package net.lyof.combat_bash.enchant.custom;

import net.lyof.combat_bash.config.ConfigEntry;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;

public class DashEnchantment extends Enchantment {
    public static DashEnchantment of(String configPath) {
        int level = new ConfigEntry<>(configPath + ".max_level", 3).get();
        String targetName = new ConfigEntry<>(configPath + ".target", "LEGGINGS").get();

        if (level == 0) return null;

        EnchantmentTarget target;
        EquipmentSlot[] slot;

        switch (targetName) {
            case "HELMET" -> {
                target = EnchantmentTarget.ARMOR_HEAD;
                slot = new EquipmentSlot[]{EquipmentSlot.HEAD};
            }
            case "CHESTPLATE" -> {
                target = EnchantmentTarget.ARMOR_CHEST;
                slot = new EquipmentSlot[]{EquipmentSlot.CHEST};
            }
            case "LEGGINGS" -> {
                target = EnchantmentTarget.ARMOR_LEGS;
                slot = new EquipmentSlot[]{EquipmentSlot.LEGS};
            }
            case "BOOTS" -> {
                target = EnchantmentTarget.ARMOR_FEET;
                slot = new EquipmentSlot[]{EquipmentSlot.FEET};
            }
            default -> {
                return null;
            }
        }

        return new DashEnchantment(Rarity.UNCOMMON, target, slot, level);
    }

    private final int level;

    protected DashEnchantment(Rarity weight, EnchantmentTarget target, EquipmentSlot[] slotTypes, int level) {
        super(weight, target, slotTypes);
        this.level = level;
    }

    @Override
    public int getMaxLevel() {
        return this.level;
    }
}
