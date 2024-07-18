package net.lyof.combat_bash.effect.custom;

import net.lyof.combat_bash.event.ModEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;

public class RollingEffect extends StatusEffect {
    public RollingEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int level) {
        if (entity instanceof PlayerEntity player && ModEvents.onPlayerRollingTick(player)) {
            entity.removeStatusEffect(this);
        }

        super.applyUpdateEffect(entity, level);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}
