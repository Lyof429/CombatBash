package net.lyof.combat_bash.config;

import net.lyof.combat_bash.CombatBash;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ConfigEntry<T> {
    private final List<String> path;
    private final T fallback;

    public ConfigEntry(String path) {
        this(path, null);
    }

    public ConfigEntry(String path, @Nullable T fallback) {
        this.path = List.of(path.split("\\."));
        this.fallback = fallback;
    }

    public T get() {
        return this.get(this.fallback);
    }

    @SuppressWarnings("unchecked")
    public T get(T fallback) {
        Map next = ModConfig.CONFIG;
        Object result = null;

        for (String step : this.path) {
            try {
                next = (Map) next.get(step);
            }
            catch (Exception e) {
                if (Objects.equals(step, this.path.get(this.path.size() - 1)))
                    result = next.get(step);
                else {
                    CombatBash.log("Got past end of path : \"" + this.path + "\", defaulting to " + fallback);
                    return fallback;
                }
            }
            if (next == null) {
                CombatBash.log("Couldn't get to end of path : \"" + this.path + "\", defaulting to " + fallback);
                return fallback;
            }
        }

        if (fallback instanceof Integer)
            return (T) (Integer) Long.valueOf(Math.round(Double.parseDouble(String.valueOf(result)))).intValue();
        if (fallback instanceof Double)
            return (T) Double.valueOf(String.valueOf(result));
        if (fallback instanceof String)
            return (T) String.valueOf(result);
        if (fallback instanceof Boolean)
            return (T) Boolean.valueOf(String.valueOf(result));
        if (fallback instanceof Map)
            return (T) next;
        if (fallback instanceof List)
            return (T) result;

        CombatBash.log("Couldn't find config value for path : \"" + this.path + "\", defaulting to " + fallback);
        return fallback;
    }
}