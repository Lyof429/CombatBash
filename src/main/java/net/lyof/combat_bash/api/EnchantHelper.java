package net.lyof.combat_bash.api;

import net.minecraft.world.entity.player.Player;

public class EnchantHelper {
    public static float getExtraBashDamage(Player player) {
        return 0;
    }

    public static void onStartRoll(Player player) {
        /*int swiftfooted = ModEnchants.getLevel(ModEnchants.SWIFTFOOTED, player);
        if (swiftfooted > 0)
            player.addEffect(new MobEffectInstance(MobEffects.SPEED, 20, swiftfooted));*/
    }
}
