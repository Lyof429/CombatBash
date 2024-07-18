package net.lyof.combat_bash.effects.custom;

import net.lyof.combat_bash.events.ModEvents;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class RollingEffect extends MobEffect {
    public RollingEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int level) {
        if (entity instanceof Player player && ModEvents.onPlayerRollingTick(player)) {
            entity.removeEffect(this);
        }

        super.applyEffectTick(entity, level);
    }

    @Override
    public boolean isDurationEffectTick(int a, int b) {
        return true;
    }
}
