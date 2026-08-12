package twilightforest.init;

import net.minecraft.core.registries.BuiltInRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.advancements.*;
import net.minecraft.core.Registry;

public class TFAdvancements {

	public static final SimpleAdvancementTrigger MADE_TF_PORTAL = new SimpleAdvancementTrigger();
	public static final SimpleAdvancementTrigger CONSUME_HYDRA_CHOP = new SimpleAdvancementTrigger();
	public static final SimpleAdvancementTrigger QUEST_RAM_COMPLETED = new SimpleAdvancementTrigger();
	public static final SimpleAdvancementTrigger PLACED_TROPHY_ON_PEDESTAL = new SimpleAdvancementTrigger();
	public static final SimpleAdvancementTrigger ACTIVATED_GHAST_TRAP = new SimpleAdvancementTrigger();
	public static final StructureClearedTrigger STRUCTURE_CLEARED = new StructureClearedTrigger();
	public static final DrinkFromFlaskTrigger DRINK_FROM_FLASK = new DrinkFromFlaskTrigger();
	public static final KillBugTrigger KILL_BUG = new KillBugTrigger();
	public static final HurtBossTrigger HURT_BOSS = new HurtBossTrigger();
	public static final SimpleAdvancementTrigger KILL_ALL_PHANTOMS = new SimpleAdvancementTrigger();
	public static final UncraftItemTrigger UNCRAFT_ITEM = new UncraftItemTrigger();
	public static final SimpleAdvancementTrigger BROKE_GLASS_SWORD = new SimpleAdvancementTrigger();
	public static final AddModifierTrigger ADD_MODIFIER = new AddModifierTrigger();

	public static void init() {
		Registry.register(BuiltInRegistries.TRIGGER_TYPES, TwilightForestMod.prefix("make_tf_portal"), MADE_TF_PORTAL);
		Registry.register(BuiltInRegistries.TRIGGER_TYPES, TwilightForestMod.prefix("consume_hydra_chop_on_low_hunger"), CONSUME_HYDRA_CHOP);
		Registry.register(BuiltInRegistries.TRIGGER_TYPES, TwilightForestMod.prefix("complete_quest_ram"), QUEST_RAM_COMPLETED);
		Registry.register(BuiltInRegistries.TRIGGER_TYPES, TwilightForestMod.prefix("placed_on_trophy_pedestal"), PLACED_TROPHY_ON_PEDESTAL);
		Registry.register(BuiltInRegistries.TRIGGER_TYPES, TwilightForestMod.prefix("activate_ghast_trap"), ACTIVATED_GHAST_TRAP);
		Registry.register(BuiltInRegistries.TRIGGER_TYPES, TwilightForestMod.prefix("structure_cleared"), STRUCTURE_CLEARED);
		Registry.register(BuiltInRegistries.TRIGGER_TYPES, TwilightForestMod.prefix("drink_from_flask"), DRINK_FROM_FLASK);
		Registry.register(BuiltInRegistries.TRIGGER_TYPES, TwilightForestMod.prefix("kill_bug"), KILL_BUG);
		Registry.register(BuiltInRegistries.TRIGGER_TYPES, TwilightForestMod.prefix("hurt_boss"), HURT_BOSS);
		Registry.register(BuiltInRegistries.TRIGGER_TYPES, TwilightForestMod.prefix("kill_all_phantoms"), KILL_ALL_PHANTOMS);
		Registry.register(BuiltInRegistries.TRIGGER_TYPES, TwilightForestMod.prefix("uncraft_item"), UNCRAFT_ITEM);
		Registry.register(BuiltInRegistries.TRIGGER_TYPES, TwilightForestMod.prefix("broke_glass_sword"), BROKE_GLASS_SWORD);
		Registry.register(BuiltInRegistries.TRIGGER_TYPES, TwilightForestMod.prefix("add_modifier"), ADD_MODIFIER);
	}
}