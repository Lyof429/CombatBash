package net.lyof.combat_bash.enchant;

import net.lyof.combat_bash.CombatBash;
import net.lyof.combat_bash.enchant.custom.InertiaEnchantment;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEnchants {
    public static final DeferredRegister<Enchantment> ENCHANTS =
            DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, CombatBash.MOD_ID);

    public static RegistryObject<Enchantment> INERTIA = ENCHANTS.register("inertia",
            () -> new InertiaEnchantment());

    public static void register(IEventBus eventbus) {
        ENCHANTS.register(eventbus);
    }
}
