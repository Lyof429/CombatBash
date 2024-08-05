package net.lyof.combat_bash.event;

import com.google.common.eventbus.Subscribe;
import net.combatroll.CombatRoll;
import net.combatroll.api.event.ServerSideRollEvents;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.lyof.combat_bash.CombatBash;
import net.lyof.combat_bash.config.ConfigEntries;
import net.lyof.combat_bash.effect.ModEffects;
import net.lyof.combat_bash.enchant.ModEnchants;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ModEvents {
    public static Map<UUID, Vec3d> VELOCITIES = new HashMap<>();

    public static void register() {
        ServerSideRollEvents.PLAYER_START_ROLLING.register(ModEvents::onPlayerStartedRolling);
        AttackEntityCallback.EVENT.register(ModEvents::beforeEntityHurt);
    }

    public static void onPlayerStartedRolling(ServerPlayerEntity player, Vec3d velocity) {
        if (EnchantmentHelper.getEquipmentLevel(ModEnchants.INERTIA, player) <= 0 && ConfigEntries.needsEnchantment) return;

        UUID uuid = player.getUuid();
        if (VELOCITIES.containsKey(uuid))   VELOCITIES.replace(uuid, velocity);
        else                                VELOCITIES.put(uuid, velocity);

        player.addStatusEffect(new StatusEffectInstance(ModEffects.ROLLING, CombatRoll.config.roll_duration,
                0, true, false));
        if (ConfigEntries.immunity)
            player.timeUntilRegen = CombatRoll.config.roll_duration + 5;
    }

    public static boolean onPlayerRollingTick(PlayerEntity player) {
        BlockPos pos = player.getBlockPos();
        List<Entity> entities = player.getWorld().getOtherEntities(player, new Box(pos).expand(0.7));

        float damage = (float) ConfigEntries.damage + EnchantmentHelper.getEquipmentLevel(ModEnchants.INERTIA, player) * 2;

        UUID uuid = player.getUuid();
        boolean result = false;

        for (Entity entity : entities) {
            if (!(entity instanceof LivingEntity target))
                continue;
            if (entity instanceof PlayerEntity && ConfigEntries.ignorePlayers)
                continue;

            result = true;

            target.damage(player.getDamageSources().playerAttack(player), damage);

            Vec3d velocity = VELOCITIES.getOrDefault(uuid, Vec3d.ZERO);

            player.setVelocity(velocity.normalize().multiply(-ConfigEntries.playerKnockback).add(0, 0.3, 0));
            target.setVelocity(velocity.normalize().multiply(ConfigEntries.targetKnockback).add(0, 0.3, 0));
            player.velocityModified = true;

            player.addExhaustion((float) ConfigEntries.extraExhaustion);
        }
        return result;
    }

    public static ActionResult beforeEntityHurt(PlayerEntity player, World world, Hand hand, Entity entity,
                                                @Nullable EntityHitResult entityHitResult) {
        if (player.isSpectator() || world.isClient() || !(entity instanceof LivingEntity) || !ConfigEntries.enableMultiImmun)
            return ActionResult.PASS;

        if (!CombatBash.FRAMES.containsKey(entity.getUuidAsString()))
            CombatBash.FRAMES.put(entity.getUuidAsString(), new HashMap<>());
        if (!CombatBash.FRAMES.get(entity.getUuidAsString()).containsKey(player.getUuidAsString()))
            CombatBash.FRAMES.get(entity.getUuidAsString()).put(player.getUuidAsString(), 0);

        entity.timeUntilRegen = CombatBash.FRAMES.get(entity.getUuidAsString()).get(player.getUuidAsString());
        CombatBash.FRAMES.get(entity.getUuidAsString()).replace(player.getUuidAsString(), 20);
        return ActionResult.PASS;
    }
}
