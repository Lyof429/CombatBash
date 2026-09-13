package net.lyof.combat_bash.mixin;

import net.lyof.combat_bash.api.inject.RollingPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Player.class)
public class PlayerMixin implements RollingPlayer {
    @Unique private Vec3 cbash_rollVelocity;

    @Override
    public void cbash_setRollVelocity(Vec3 velocity) {
        cbash_rollVelocity = velocity;
    }

    @Override
    public Vec3 cbash_getRollVelocity() {
        return cbash_rollVelocity == null ? Vec3.ZERO : cbash_rollVelocity;
    }
}
