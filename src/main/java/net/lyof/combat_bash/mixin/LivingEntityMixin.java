package net.lyof.combat_bash.mixin;

import net.lyof.combat_bash.CombatBash;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void decrementFrames(CallbackInfo ci) {
        if (!this.getWorld().isClient())
            CombatBash.tickFrames(this.getUuidAsString());
    }

    @Inject(method = "onDeath", at = @At("HEAD"))
    public void clearFrames(DamageSource source, CallbackInfo ci) {
        CombatBash.FRAMES.remove(this.getUuidAsString());
    }
}
