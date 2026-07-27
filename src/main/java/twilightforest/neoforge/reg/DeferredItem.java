package twilightforest.neoforge.reg;

import net.minecraft.world.item.Item;

public class DeferredItem<T extends Item> extends DeferredHolder<Item, T> {
    public DeferredItem(java.util.function.Supplier<T> supplier) {
        super(supplier);
    }

    public Item asItem() {
        return get();
    }
}
