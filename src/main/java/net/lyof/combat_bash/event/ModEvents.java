package net.lyof.combat_bash.event;

import net.combat_roll.CombatRollMod;
import net.combat_roll.api.CombatRoll;
import net.combat_roll.api.event.ServerSideRollEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.lyof.combat_bash.CombatBash;
import net.lyof.combat_bash.config.ModConfig;
import net.lyof.combat_bash.effect.ModEffects;
import net.lyof.combat_bash.effect.custom.RollingEffect;
import net.lyof.combat_bash.enchant.ModEnchants;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
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
    public static Map<UUID, Vec3> VELOCITIES = new HashMap<>();

    public static void register() {
        ServerSideRollEvents.PLAYER_START_ROLLING.register(ModEvents::onPlayerStartedRolling);
        AttackEntityCallback.EVENT.register(ModEvents::beforeEntityHurt);
    }

    public static void onPlayerStartedRolling(ServerPlayer player, Vec3 velocity) {
        /*int swiftfooted = ModEnchants.getLevel(ModEnchants.SWIFTFOOTED, player);
        if (swiftfooted > 0)
            player.addEffect(new MobEffectInstance(MobEffects.SPEED, 20, swiftfooted));*/

        float enchantDamage = 0;//ModEnchants.getLevel(ModEnchants.INERTIA, player);
        if (enchantDamage <= 0 && ModConfig.needsEnchantment.get()) return;

        UUID uuid = player.getUUID();
        if (VELOCITIES.containsKey(uuid))   VELOCITIES.replace(uuid, velocity);
        else                                VELOCITIES.put(uuid, velocity);

        player.addEffect(new MobEffectInstance(RollingEffect.getHolder(), CombatRollMod.config.roll_duration,
                0, true, false));
        if (ModConfig.rollImmunity.get())
            player.invulnerableTime = CombatRollMod.config.roll_duration + 5;
    }

    public static boolean onPlayerRollingTick(Player player) {
        BlockPos pos = player.blockPosition();
        List<Entity> entities = player.level().getEntities(player, new AABB(pos).inflate(0.7));

        float damage = ModConfig.damage.get().floatValue() + 0/* ModEnchants.getLevel(ModEnchants.INERTIA, player) * 2*/;

        UUID uuid = player.getUUID();
        boolean result = false;

        for (Entity entity : entities) {
            if (!(entity instanceof LivingEntity target))
                continue;
            if (entity instanceof Player && ModConfig.ignorePlayers.get())
                continue;

            result = true;

            target.hurt(player.damageSources().playerAttack(player), damage);

            Vec3 velocity = VELOCITIES.getOrDefault(uuid, Vec3.ZERO);

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

        if (!CombatBash.FRAMES.containsKey(entity.getStringUUID()))
            CombatBash.FRAMES.put(entity.getStringUUID(), new HashMap<>());
        if (!CombatBash.FRAMES.get(entity.getStringUUID()).containsKey(player.getStringUUID()))
            CombatBash.FRAMES.get(entity.getStringUUID()).put(player.getStringUUID(), 0);

        entity.invulnerableTime = CombatBash.FRAMES.get(entity.getStringUUID()).get(player.getStringUUID());
        CombatBash.FRAMES.get(entity.getStringUUID()).replace(player.getStringUUID(), 20);
        return InteractionResult.PASS;
    }
}
