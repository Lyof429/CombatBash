package net.lyof.combat_bash.effect.custom;

import net.lyof.combat_bash.effect.ModEffects;
import net.lyof.combat_bash.event.ModEvents;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.player.Player;

public class RollingEffect extends MobEffect {
    private static Holder<MobEffect> holder = null;
    public static Holder<MobEffect> getHolder() {
        if (holder != null) return holder;
        holder = BuiltInRegistries.MOB_EFFECT.wrapAsHolder(ModEffects.ROLLING.get());
        return holder;
    }

    public RollingEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(net.minecraft.world.entity.LivingEntity entity, int level) {
        if (entity instanceof Player player && ModEvents.onPlayerRollingTick(player)) {
            entity.removeEffect(getHolder());
        }

        return super.applyEffectTick(entity, level);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}
