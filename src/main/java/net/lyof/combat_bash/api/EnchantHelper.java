package net.lyof.combat_bash.api;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.lyof.combat_bash.enchant.ModEnchants;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.item.enchantment.effects.EnchantmentValueEffect;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import java.util.Optional;

public class EnchantHelper {
    private static LivingEntity getTarget(LivingEntity enchanted, LivingEntity victim, EnchantmentTarget target) {
        return target == EnchantmentTarget.VICTIM ? victim : enchanted;
    }

    public static float getExtraBashDamage(Player player) {
        float value = 0;
        ItemStack stack;
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            stack = player.getItemBySlot(slot);
            if (stack.isEmpty())
                continue;

            for (Object2IntMap.Entry<Holder<Enchantment>> enchant : stack.getEnchantments().entrySet()) {
                if (!enchant.getKey().value().isSupportedItem(stack))
                    continue;

                LootContext context = new LootContext.Builder(new LootParams.Builder((ServerLevel) player.level())
                        .withParameter(LootContextParams.THIS_ENTITY, player)
                        .withParameter(LootContextParams.ENCHANTMENT_LEVEL, enchant.getIntValue())
                        .withParameter(LootContextParams.ORIGIN, player.position())
                        .create(LootContextParamSets.ENCHANTED_ENTITY)).create(Optional.empty());

                for (ConditionalEffect<EnchantmentValueEffect> effect : enchant.getKey().value().getEffects(ModEnchants.BASH_DAMAGE)) {
                    if (effect.matches(context))
                        effect.effect().process(enchant.getIntValue(), player.getRandom(), value);
                }
            }
        }
        return value;
    }

    public static void onStartRoll(ServerPlayer player) {
        ItemStack stack;
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            stack = player.getItemBySlot(slot);
            if (stack.isEmpty())
                continue;

            for (Object2IntMap.Entry<Holder<Enchantment>> enchant : stack.getEnchantments().entrySet()) {
                if (!enchant.getKey().value().isSupportedItem(stack))
                    continue;

                LootContext context = new LootContext.Builder(new LootParams.Builder((ServerLevel) player.level())
                        .withParameter(LootContextParams.THIS_ENTITY, player)
                        .withParameter(LootContextParams.ENCHANTMENT_LEVEL, enchant.getIntValue())
                        .withParameter(LootContextParams.ORIGIN, player.position())
                        .create(LootContextParamSets.ENCHANTED_ENTITY)).create(Optional.empty());

                for (ConditionalEffect<EnchantmentEntityEffect> effect : enchant.getKey().value().getEffects(ModEnchants.ON_ROLL)) {
                    if (effect.matches(context))
                        effect.effect().apply((ServerLevel) player.level(), enchant.getIntValue(),
                                new EnchantedItemInUse(stack, EquipmentSlot.BODY, player),
                                player, player.position());
                }
            }
        }
    }

    public static void onBash(ServerPlayer player, LivingEntity target, DamageSource source) {
        ItemStack stack;
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            stack = player.getItemBySlot(slot);
            if (stack.isEmpty())
                continue;

            for (Object2IntMap.Entry<Holder<Enchantment>> enchant : stack.getEnchantments().entrySet()) {
                if (!enchant.getKey().value().isSupportedItem(stack))
                    continue;

                LootContext context = new LootContext.Builder(new LootParams.Builder((ServerLevel) player.level())
                        .withParameter(LootContextParams.THIS_ENTITY, player)
                        .withParameter(LootContextParams.ENCHANTMENT_LEVEL, enchant.getIntValue())
                        .withParameter(LootContextParams.ORIGIN, player.position())
                        .withParameter(LootContextParams.DAMAGE_SOURCE, source)
                        .withParameter(LootContextParams.ATTACKING_ENTITY, player)
                        .withParameter(LootContextParams.DIRECT_ATTACKING_ENTITY, player)
                        .create(LootContextParamSets.ENCHANTED_DAMAGE)).create(Optional.empty());

                for (TargetedConditionalEffect<EnchantmentEntityEffect> effect : enchant.getKey().value().getEffects(ModEnchants.ON_BASH_HIT)) {
                    if (effect.matches(context))
                        effect.effect().apply((ServerLevel) player.level(), enchant.getIntValue(),
                                new EnchantedItemInUse(stack, EquipmentSlot.BODY, player),
                                getTarget(player, target, effect.affected()), getTarget(player, target, effect.affected()).position());
                }
            }
        }
    }
}
