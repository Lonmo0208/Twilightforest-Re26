package twilightforest.mixin.client;

import com.google.common.collect.ImmutableList;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.resources.model.sprite.AtlasManager;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.client.MagicPaintingAtlasInfo;

import java.util.List;

/**
 * Registers the Twilight Forest magic-painting custom texture atlas by injecting it into the
 * vanilla {@link AtlasManager}'s known-atlas list. This mirrors the approach used by the Fabric
 * API's {@code AtlasRegistry} (which is not present in this Fabric API version), and avoids any
 * NeoForge-specific atlas-gathering hooks.
 */
@Mixin(AtlasManager.class)
public abstract class AtlasManagerMixin {

	@ModifyExpressionValue(
		method = "<init>",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/client/resources/model/sprite/AtlasManager;KNOWN_ATLASES:Ljava/util/List;",
			opcode = Opcodes.GETSTATIC
		)
	)
	private static List<AtlasManager.AtlasConfig> tf$registerMagicPaintingAtlas(List<AtlasManager.AtlasConfig> original) {
		ImmutableList.Builder<AtlasManager.AtlasConfig> builder = ImmutableList.builder();
		builder.addAll(original);
		builder.add(new AtlasManager.AtlasConfig(
			MagicPaintingAtlasInfo.ATLAS_LOCATION,
			MagicPaintingAtlasInfo.ATLAS_INFO_LOCATION,
			false
		));
		return builder.build();
	}
}