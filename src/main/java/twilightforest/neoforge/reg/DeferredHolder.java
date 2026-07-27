package twilightforest.neoforge.reg;

import java.util.function.Supplier;

public class DeferredHolder<R, T extends R> implements Supplier<T> {
    private final Supplier<T> valueSupplier;
    private T cachedValue;

    public DeferredHolder(Supplier<T> valueSupplier) {
        this.valueSupplier = valueSupplier;
    }

    @Override
    public T get() {
        if (cachedValue == null) {
            cachedValue = valueSupplier.get();
        }
        return cachedValue;
    }
}
