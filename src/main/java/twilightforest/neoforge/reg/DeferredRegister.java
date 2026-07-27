package twilightforest.neoforge.reg;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import twilightforest.TwilightForestMod;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Compatibility shim: lazy-register via Fabric's Registry API at init time.
 * Does NOT create intrusive holders.
 */
public class DeferredRegister<T> {
    private final ResourceKey<? extends Registry<T>> registryKey;
    private final String modId;
    private final List<Registration<T>> registrations = new ArrayList<>();

    private record Registration<T>(String name, Supplier<? extends T> supplier, DeferredHolder<T, ?> holder) {}

    private DeferredRegister(ResourceKey<? extends Registry<T>> registryKey, String modId) {
        this.registryKey = registryKey;
        this.modId = modId;
    }

    public static <T> DeferredRegister<T> create(ResourceKey<? extends Registry<T>> registryKey, String modId) {
        return new DeferredRegister<>(registryKey, modId);
    }

    @SuppressWarnings("unchecked")
    public <I extends T> DeferredHolder<T, I> register(String name, Supplier<? extends I> supplier) {
        var id = TwilightForestMod.prefix(name);
        DeferredHolder<T, I> holder = new DeferredHolder<>(() -> {
            var registry = RegistryUtil.getRegistry(registryKey);
            if (registry != null) {
                var value = supplier.get();
                return (I) Registry.register((Registry<? super I>) registry, id, value);
            }
            return (I) supplier.get();
        });
        registrations.add(new Registration<>(name, supplier, holder));
        return holder;
    }

    public void register() {
        for (Registration<T> reg : registrations) {
            reg.holder().get(); // trigger lazy registration
        }
    }

    public List<DeferredHolder<T, ? extends T>> getEntries() {
        List<DeferredHolder<T, ? extends T>> result = new ArrayList<>();
        for (Registration<T> reg : registrations) {
            result.add((DeferredHolder<T, ? extends T>) reg.holder());
        }
        return result;
    }

    public ResourceKey<? extends Registry<T>> getRegistryKey() {
        return registryKey;
    }
}
