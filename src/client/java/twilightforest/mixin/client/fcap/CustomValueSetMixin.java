package twilightforest.mixin.client.fcap;

import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.core.Holder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;

/**
 * Forge Config API Port v26.3.1 was built against Minecraft 26.3, where
 * {@link OptionInstance.ValueSet} exposed {@code createButton(...)} directly. Snapshot-8 replaced
 * that with a {@link OptionInstance.ButtonFactory buttonFactory()} factory method, so FCAP's
 * {@code Custom} value set (which backs dropdown-style config entries) is missing the abstract
 * method and crashes {@code ConfigurationScreen} with an {@code AbstractMethodError}.
 * <p>
 * This mixin restores compatibility by implementing {@code buttonFactory()} on FCAP's class,
 * adapting the legacy {@code createButton} shape (which returns a
 * {@code Function<OptionInstance, AbstractWidget>}) to the new factory contract.
 */
@Mixin(targets = "net.neoforged.neoforge.client.gui.ConfigurationScreen$ConfigurationSectionScreen$Custom")
public abstract class CustomValueSetMixin<T> implements OptionInstance.ValueSet<T> {

	@Shadow
	public abstract java.util.function.Function<OptionInstance<T>, AbstractWidget> createButton(
		OptionInstance.TooltipSupplier<T> tooltip,
		Options options,
		int x,
		int y,
		int width,
		OptionInstance.ValueUpdateListener<? super T> onValueChanged
	);

	@Unique
	@Override
	public OptionInstance.ButtonFactory<T> buttonFactory() {
		return (instance, tooltip, options, x, y, width, onValueChanged) ->
			this.createButton(tooltip, options, x, y, width, onValueChanged).apply(instance);
	}
}
