package twilightforest.mixin;

import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(ModelBlockRenderer.class)
public interface ModelBlockRendererAccessor {

	@Accessor("parts")
	List<BlockStateModelPart> getParts();
}