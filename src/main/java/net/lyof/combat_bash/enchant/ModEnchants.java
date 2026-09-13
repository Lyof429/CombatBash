package net.lyof.combat_bash.enchant;

import net.lyof.combat_bash.CombatBash;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.enchantment.ConditionalEffect;
import net.minecraft.world.item.enchantment.TargetedConditionalEffect;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.item.enchantment.effects.EnchantmentValueEffect;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.function.UnaryOperator;

public class ModEnchants {
    public static void register() {}

    private static <T> DataComponentType<T> register(String name, UnaryOperator<DataComponentType.Builder<T>> builder) {
        return Registry.register(BuiltInRegistries.ENCHANTMENT_EFFECT_COMPONENT_TYPE, CombatBash.MOD.makeID(name), builder.apply(DataComponentType.builder()).build());
    }

    public static final DataComponentType<List<ConditionalEffect<EnchantmentEntityEffect>>> ON_ROLL = register("on_roll",
            builder -> builder.persistent(ConditionalEffect.codec(EnchantmentEntityEffect.CODEC, LootContextParamSets.ENCHANTED_ENTITY).listOf()));
    public static final DataComponentType<List<TargetedConditionalEffect<EnchantmentEntityEffect>>> ON_BASH_HIT = register("on_bash_hit",
            builder -> builder.persistent(TargetedConditionalEffect.codec(EnchantmentEntityEffect.CODEC, LootContextParamSets.ENCHANTED_DAMAGE).listOf()));
    public static final DataComponentType<List<ConditionalEffect<EnchantmentValueEffect>>> BASH_DAMAGE = register("bash_damage",
            builder -> builder.persistent(ConditionalEffect.codec(EnchantmentValueEffect.CODEC, LootContextParamSets.ENCHANTED_ENTITY).listOf()));
}
