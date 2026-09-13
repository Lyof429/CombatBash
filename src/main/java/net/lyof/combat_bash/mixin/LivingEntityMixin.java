package net.lyof.combat_bash.mixin;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.lyof.combat_bash.api.inject.MultiImmunityEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements MultiImmunityEntity {
    public LivingEntityMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Unique
    private final Object2IntMap<Player> cbash_immunityFrames = new Object2IntOpenHashMap<>();

    @Inject(method = "tick", at = @At("HEAD"))
    public void decrementFrames(CallbackInfo ci) {
        if (!this.level().isClientSide()) {
            for (Object2IntMap.Entry<Player> entry : cbash_immunityFrames.object2IntEntrySet()) {
                if (entry.getIntValue() > 0)
                    entry.setValue(entry.getIntValue() - 1);
                else
                    cbash_immunityFrames.object2IntEntrySet().remove(entry);
            }
        }
    }

    @Override
    public void cbash_setHitFrames(Player attacker, int frames) {
        cbash_immunityFrames.merge(attacker, frames, Math::max);
    }

    @Override
    public int cbash_getHitFrames(Player attacker) {
        return cbash_immunityFrames.getOrDefault(attacker, 0);
    }
}
