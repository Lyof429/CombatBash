package net.lyof.combat_bash.events;

import net.combatroll.CombatRoll;
import net.combatroll.api.event.ServerSideRollEvents;
import net.lyof.combat_bash.CombatBash;
import net.lyof.combat_bash.configs.ConfigEntry;
import net.lyof.combat_bash.configs.ModJsonConfigs;
import net.lyof.combat_bash.effects.ModEffects;
import net.lyof.combat_bash.enchant.ModEnchants;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber
public class ModEvents {
    public static Map<String, Vec3> VELOCITIES = new HashMap<>();

    public static void register() {
        ServerSideRollEvents.PLAYER_START_ROLLING.register(ModEvents::onPlayerStartedRolling);
    }

    public static final ConfigEntry<Boolean> NEEDS_ENCHANT = new ConfigEntry<>("combat_bash.needs_enchantment", false);
    public static final ConfigEntry<Float> DAMAGE = new ConfigEntry<>("combat_bash.damage", 4f);
    public static final ConfigEntry<Double> KB_PLAYER = new ConfigEntry<>("combat_bash.player_knockback", 1d);
    public static final ConfigEntry<Double> KB_TARGET = new ConfigEntry<>("combat_bash.target_knockback", 1d);
    public static final ConfigEntry<Float> EXHAUSTION = new ConfigEntry<>("combat_bash.extra_exhaustion", 0.03f);
    public static final ConfigEntry<Boolean> IMMUNITY = new ConfigEntry<>("combat_bash.roll_immunity", true);
    public static final ConfigEntry<Boolean> IGNORE_PLAYERS = new ConfigEntry<>("combat_bash.ignore_players", true);

    public static void onPlayerStartedRolling(ServerPlayer player, Vec3 velocity) {
        if (EnchantmentHelper.getEnchantmentLevel(ModEnchants.INERTIA.get(), player) <= 0 && NEEDS_ENCHANT.get()) return;

        String name = player.getDisplayName().getString();
        if (VELOCITIES.containsKey(name))   VELOCITIES.replace(name, velocity);
        else                                VELOCITIES.put(name, velocity);

        player.addEffect(new MobEffectInstance(ModEffects.ROLLING.get(), CombatRoll.config.roll_duration + 10,
                0, true, false));
        if (IMMUNITY.get())
            player.invulnerableTime = CombatRoll.config.roll_duration + 5;
    }

    public static boolean onPlayerRollingTick(Player player) {
        BlockPos pos = new BlockPos(player.getBlockX(), player.getBlockY(), player.getBlockZ());
        List<Entity> entities = player.getLevel().getEntities(null, new AABB(pos).inflate(0.7));
        entities.remove(player);

        float damage = DAMAGE.get() + EnchantmentHelper.getEnchantmentLevel(ModEnchants.INERTIA.get(), player) * 2;

        String name = player.getDisplayName().getString();
        boolean result = false;

        for (Entity entity : entities) {
            if (!(entity instanceof LivingEntity target))
                continue;
            if (entity instanceof Player && IGNORE_PLAYERS.get())
                continue;

            result = true;

            target.hurt(DamageSource.playerAttack(player), damage);

            Vec3 velocity = VELOCITIES.getOrDefault(name, Vec3.ZERO);

            player.setDeltaMovement(velocity.scale(-KB_PLAYER.get()).add(0, 0.3, 0));
            target.setDeltaMovement(velocity.scale(KB_TARGET.get()).add(0, 0.3, 0));

            player.causeFoodExhaustion(EXHAUSTION.get());
        }
        return result;
    }

    @SubscribeEvent
    public static void reloadConfigs(PlayerEvent.PlayerLoggedInEvent event) {
        ModJsonConfigs.register();
    }


    public static ConfigEntry<Boolean> ENABLE_MULTI_IMMUN = new ConfigEntry<>("enable_multiplayer_immunity_frames", true);

    @SubscribeEvent
    public static void beforeEntityHurt(LivingAttackEvent event) {
        if (!(event.getSource().getEntity() instanceof Player player)) return;
        LivingEntity entity = event.getEntity();

        if (player.isSpectator() || player.getLevel().isClientSide() || !ENABLE_MULTI_IMMUN.get())
            return;

        if (!CombatBash.FRAMES.containsKey(entity.getStringUUID()))
            CombatBash.FRAMES.put(entity.getStringUUID(), new HashMap<>());
        if (!CombatBash.FRAMES.get(entity.getStringUUID()).containsKey(player.getStringUUID()))
            CombatBash.FRAMES.get(entity.getStringUUID()).put(player.getStringUUID(), 0);

        entity.invulnerableTime = CombatBash.FRAMES.get(entity.getStringUUID()).get(player.getStringUUID());
        CombatBash.FRAMES.get(entity.getStringUUID()).replace(player.getStringUUID(), 20);
    }
}
