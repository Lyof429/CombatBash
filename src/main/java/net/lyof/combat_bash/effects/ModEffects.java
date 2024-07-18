package net.lyof.combat_bash.effects;

import net.lyof.combat_bash.CombatBash;
import net.lyof.combat_bash.effects.custom.RollingEffect;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEffects {
    public static final DeferredRegister<MobEffect> EFFECTS =
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, CombatBash.MOD_ID);

    public static final RegistryObject<MobEffect> ROLLING = EFFECTS.register("rolling",
            () -> new RollingEffect(MobEffectCategory.NEUTRAL, Mth.color(0, 255, 0)));

    public static void register(IEventBus eventbus) {
        EFFECTS.register(eventbus);
    }
}
