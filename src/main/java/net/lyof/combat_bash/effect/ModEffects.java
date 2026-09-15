package net.lyof.combat_bash.effect;

import net.lcc.sollib.api.common.registry.holder.EffectHolder;
import net.lyof.combat_bash.CombatBash;
import net.lyof.combat_bash.effect.custom.RollingEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class ModEffects {
    public static void register() {}

    public static final EffectHolder ROLLING = CombatBash.MOD.register(EffectHolder.class, "rolling",
            () -> new RollingEffect(MobEffectCategory.NEUTRAL, 0xf0faf0));
}
