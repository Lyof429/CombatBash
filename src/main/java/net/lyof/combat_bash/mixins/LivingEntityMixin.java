package net.lyof.combat_bash.mixins;

import net.lyof.combat_bash.CombatBash;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void decrementFrames(CallbackInfo ci) {
        if (!this.getLevel().isClientSide())
            CombatBash.tickFrames(this.getStringUUID());
    }

    @Inject(method = "die", at = @At("HEAD"))
    public void clearFrames(DamageSource source, CallbackInfo ci) {
        CombatBash.FRAMES.remove(this.getStringUUID());
    }
}
