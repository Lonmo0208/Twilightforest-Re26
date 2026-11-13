package twilightforest.mixin.client.fcap;

import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.components.AbstractCycleButton;
import net.minecraft.client.gui.components.CycleButton;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.Collection;
import java.util.List;
import java.util.function.BooleanSupplier;

/**
 * Forge Config API Port v26.3.1 was compiled against Minecraft 26.3, where
 * {@code CycleButton.Builder} declared its {@code withValues} overloads directly. Snapshot-8
 * moved them all to the generic {@link AbstractCycleButton.Builder} superclass, so FCAP's
 * compiled call sites (chaining {@code .withValues(...)} with a {@code CycleButton$Builder}
 * return type) resolve to non-existent descriptors and throw {@code NoSuchMethodError}.
 * <p>
 * This mixin adds forwarding overloads with exactly the descriptors FCAP's bytecode expects;
 * vanilla callers are unaffected since these methods never existed on this class before.
 */
@Mixin(CycleButton.Builder.class)
public abstract class CycleButtonBuilderMixin<Value> {

	@Unique
	@SuppressWarnings("unchecked")
	private AbstractCycleButton.Builder<Value, ?> tf$self() {
		return (AbstractCycleButton.Builder<Value, ?>) (Object) this;
	}

	@Unique
	public CycleButton.Builder<Value> withValues(Collection<Value> values) {
		return (CycleButton.Builder<Value>) (Object) this.tf$self().withValues(values);
	}

	@Unique
	public CycleButton.Builder<Value> withValues(Value... values) {
		return (CycleButton.Builder<Value>) (Object) this.tf$self().withValues(values);
	}

	@Unique
	public CycleButton.Builder<Value> withValues(List<Value> values, List<Value> altValues) {
		return (CycleButton.Builder<Value>) (Object) this.tf$self().withValues(values, altValues);
	}

	@Unique
	public CycleButton.Builder<Value> withValues(BooleanSupplier altCondition, List<Value> values, List<Value> altValues) {
		return (CycleButton.Builder<Value>) (Object) this.tf$self().withValues(altCondition, values, altValues);
	}

	@Unique
	public CycleButton.Builder<Value> withValues(AbstractCycleButton.ValueListSupplier<Value> valueListSupplier) {
		return (CycleButton.Builder<Value>) (Object) this.tf$self().withValues(valueListSupplier);
	}

	@Unique
	public CycleButton.Builder<Value> withTooltip(OptionInstance.TooltipSupplier<Value> tooltipSupplier) {
		return (CycleButton.Builder<Value>) (Object) this.tf$self().withTooltip(tooltipSupplier);
	}
}
