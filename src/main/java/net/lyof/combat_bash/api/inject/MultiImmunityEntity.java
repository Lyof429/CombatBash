package net.lyof.combat_bash.api.inject;

import net.minecraft.world.entity.player.Player;

public interface MultiImmunityEntity {
    void cbash_setHitFrames(Player attacker, int frames);
    int cbash_getHitFrames(Player attacker);
}
