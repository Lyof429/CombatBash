package net.lyof.combat_bash.enchant;

import net.lcc.sollib.api.common.registry.SHolder;
import net.lcc.sollib.api.common.registry.SolModContainer;
import net.lyof.combat_bash.CombatBash;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.enchantment.ConditionalEffect;
import net.minecraft.world.item.enchantment.TargetedConditionalEffect;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.item.enchantment.effects.EnchantmentValueEffect;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

@SuppressWarnings("unchecked")
public class ModEnchants {
    public static class EnchantComponentHolder<T> extends SHolder<DataComponentType<T>> {
        public EnchantComponentHolder(SolModContainer mod, String name, Supplier<DataComponentType<T>> entrySupplier) {
            super(mod, name, entrySupplier);
        }

        @Override
        public Registry<DataComponentType<T>> getRegistry() {
            return (Registry<DataComponentType<T>>) (Object) BuiltInRegistries.ENCHANTMENT_EFFECT_COMPONENT_TYPE;
        }
    }

    public static void register() {}

    private static <T> EnchantComponentHolder<T> register(String name, UnaryOperator<DataComponentType.Builder<T>> builder) {
        return CombatBash.MOD.register(EnchantComponentHolder.class, name, builder.apply(DataComponentType.builder())::build);
    }

    public static final EnchantComponentHolder<List<ConditionalEffect<EnchantmentEntityEffect>>> ON_ROLL = register("on_roll",
            builder -> builder.persistent(ConditionalEffect.codec(EnchantmentEntityEffect.CODEC, LootContextParamSets.ENCHANTED_ENTITY).listOf()));
    public static final EnchantComponentHolder<List<TargetedConditionalEffect<EnchantmentEntityEffect>>> ON_BASH_HIT = register("on_bash_hit",
            builder -> builder.persistent(TargetedConditionalEffect.codec(EnchantmentEntityEffect.CODEC, LootContextParamSets.ENCHANTED_DAMAGE).listOf()));
    public static final EnchantComponentHolder<List<ConditionalEffect<EnchantmentValueEffect>>> BASH_DAMAGE = register("bash_damage",
            builder -> builder.persistent(ConditionalEffect.codec(EnchantmentValueEffect.CODEC, LootContextParamSets.ENCHANTED_ENTITY).listOf()));
}
