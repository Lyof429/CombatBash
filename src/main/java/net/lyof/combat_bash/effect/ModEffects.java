package net.lyof.combat_bash.effect;

import net.lyof.combat_bash.CombatBash;
import net.lyof.combat_bash.effect.custom.RollingEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModEffects {
    public static void register() {
        CombatBash.log("Registering Effects for modid : combat_bash");
    }


    public static final StatusEffect ROLLING = Registry.register(Registries.STATUS_EFFECT, CombatBash.makeID("rolling"),
            new RollingEffect(StatusEffectCategory.NEUTRAL, 0xf0faf0));
}
