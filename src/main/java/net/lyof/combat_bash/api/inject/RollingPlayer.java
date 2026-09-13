package net.lyof.combat_bash.api.inject;

import net.minecraft.world.phys.Vec3;

public interface RollingPlayer {
    void cbash_setRollVelocity(Vec3 velocity);
    Vec3 cbash_getRollVelocity();
}
