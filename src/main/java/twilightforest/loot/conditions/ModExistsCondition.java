package twilightforest.loot.conditions;

import net.minecraft.world.level.storage.loot.ValidationContext;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

// Loot condition for checking if a mod exists.
public class ModExistsCondition implements LootItemCondition {

	public static final MapCodec<ModExistsCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(Codec.STRING.fieldOf("mod_id").forGetter(o -> o.modID)).apply(instance, ModExistsCondition::new));

	private final boolean exists;
	private final String modID;

	public ModExistsCondition(String modID) {
		this.exists = FabricLoader.getInstance().isModLoaded(modID);
		this.modID = modID;
	}

	@Override
	public MapCodec<? extends LootItemCondition> codec() {
		return CODEC;
	}

	@Override
	public boolean test(LootContext context) {
		return this.exists;
	}

	public static LootItemCondition.Builder builder(String modid) {
		return () -> new ModExistsCondition(modid);
	}
	// 26.4-snapshot-8: LootItemCondition gained the abstract constantValue hook; these conditions are runtime-dependent, never constant
	@org.jetbrains.annotations.Nullable
	@Override
	public Boolean constantValue(LootItemCondition.ConstantConditionEvaluationContext context) {
		return null;
	}
	// 26.4-snapshot-8: Validatable gained the abstract validate hook
	@Override
	public void validate(ValidationContext context) {
	}
}
