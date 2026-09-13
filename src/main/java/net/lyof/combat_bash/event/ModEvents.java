package net.lyof.combat_bash.event;

import net.combat_roll.CombatRollMod;
import net.combat_roll.api.event.ServerSideRollEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.lyof.combat_bash.api.EnchantHelper;
import net.lyof.combat_bash.api.inject.MultiImmunityEntity;
import net.lyof.combat_bash.api.inject.RollingPlayer;
import net.lyof.combat_bash.setup.ModConfig;
import net.lyof.combat_bash.effect.custom.RollingEffect;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ModEvents {
    public static void register() {
        ServerSideRollEvents.PLAYER_START_ROLLING.register(ModEvents::onPlayerStartedRolling);
        AttackEntityCallback.EVENT.register(ModEvents::beforeEntityHurt);
    }

    public static void onPlayerStartedRolling(ServerPlayer player, Vec3 velocity) {
        EnchantHelper.onStartRoll(player);

        if (EnchantHelper.getExtraBashDamage(player) <= 0 && ModConfig.needsEnchantment.get()) return;

        ((RollingPlayer) player).cbash_setRollVelocity(velocity);
        player.addEffect(new MobEffectInstance(RollingEffect.getHolder(), CombatRollMod.config.roll_duration,
                0, true, false));

        if (ModConfig.rollImmunity.get())
            player.invulnerableTime = CombatRollMod.config.roll_duration + 5;
    }

    public static boolean onPlayerRollingTick(Player player) {
        BlockPos pos = player.blockPosition();
        List<Entity> entities = player.level().getEntities(player, new AABB(pos).inflate(0.7));
        boolean result = false;

        float damage = ModConfig.damage.get().floatValue() + EnchantHelper.getExtraBashDamage(player);

        for (Entity entity : entities) {
            if (!(entity instanceof LivingEntity target)) continue;
            if (entity instanceof Player && ModConfig.ignorePlayers.get()) continue;

            result = true;

            if (player instanceof ServerPlayer p)
                EnchantHelper.onBash(p, target, player.damageSources().playerAttack(player));
            target.hurt(player.damageSources().playerAttack(player), damage);

            Vec3 velocity = ((RollingPlayer) player).cbash_getRollVelocity();
            player.setDeltaMovement(velocity.normalize().multiply(
                    -ModConfig.playerKnockback.get(),
                    -ModConfig.playerKnockback.get(),
                    -ModConfig.playerKnockback.get())
                    .add(0, 0.3, 0));
            target.setDeltaMovement(velocity.normalize().multiply(
                    ModConfig.targetKnockback.get(),
                    ModConfig.targetKnockback.get(),
                    ModConfig.targetKnockback.get())
                    .add(0, 0.3, 0));
            player.hasImpulse = true;

            player.causeFoodExhaustion(ModConfig.extraExhaustion.get().floatValue());
        }
        return result;
    }

    public static InteractionResult beforeEntityHurt(Player player, Level world, InteractionHand hand, Entity entity,
                                                     @Nullable EntityHitResult entityHitResult) {
        if (player.isSpectator() || world.isClientSide() || !(entity instanceof LivingEntity) || !ModConfig.enableMultiImmun.get())
            return InteractionResult.PASS;

        MultiImmunityEntity multi = (MultiImmunityEntity) entity;
        entity.invulnerableTime = multi.cbash_getHitFrames(player);
        multi.cbash_setHitFrames(player, 20);
        return InteractionResult.PASS;
    }
}
