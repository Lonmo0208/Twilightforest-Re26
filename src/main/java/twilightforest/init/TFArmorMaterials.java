package twilightforest.init;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Util;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;
import twilightforest.TwilightForestMod;
import twilightforest.tags.TFItemTags;

import java.util.EnumMap;

public class TFArmorMaterials {

	private static ResourceKey<EquipmentAsset> asset(String name) {
		return ResourceKey.create(EquipmentAssets.ROOT_ID, TwilightForestMod.prefix(name));
	}

	public static final ArmorMaterial NAGA = new ArmorMaterial(21, Util.make(new EnumMap<>(ArmorType.class), map -> {
		map.put(ArmorType.BOOTS, 3);
		map.put(ArmorType.LEGGINGS, 6);
		map.put(ArmorType.CHESTPLATE, 7);
		map.put(ArmorType.HELMET, 2);
		map.put(ArmorType.BODY, 8);
	}), 15, SoundEvents.ARMOR_EQUIP_GENERIC, 0.5F, 0.0F, TFItemTags.REPAIRS_NAGA_ARMOR, asset("naga_scale"));

	public static final ArmorMaterial IRONWOOD = new ArmorMaterial(20, Util.make(new EnumMap<>(ArmorType.class), map -> {
		map.put(ArmorType.BOOTS, 2);
		map.put(ArmorType.LEGGINGS, 5);
		map.put(ArmorType.CHESTPLATE, 7);
		map.put(ArmorType.HELMET, 2);
		map.put(ArmorType.BODY, 5);
	}), 15, SoundEvents.ARMOR_EQUIP_GENERIC, 0.0F, 0.0F, TFItemTags.REPAIRS_IRONWOOD_ARMOR, asset("ironwood"));

	public static final ArmorMaterial FIERY = new ArmorMaterial(25, Util.make(new EnumMap<>(ArmorType.class), map -> {
		map.put(ArmorType.BOOTS, 4);
		map.put(ArmorType.LEGGINGS, 7);
		map.put(ArmorType.CHESTPLATE, 9);
		map.put(ArmorType.HELMET, 4);
		map.put(ArmorType.BODY, 13);
	}), 10, SoundEvents.ARMOR_EQUIP_GENERIC, 1.5F, 0.0F, TFItemTags.REPAIRS_FIERY_ARMOR, asset("fiery"));

	public static final ArmorMaterial STEELEAF = new ArmorMaterial(10, Util.make(new EnumMap<>(ArmorType.class), map -> {
		map.put(ArmorType.BOOTS, 3);
		map.put(ArmorType.LEGGINGS, 6);
		map.put(ArmorType.CHESTPLATE, 8);
		map.put(ArmorType.HELMET, 3);
		map.put(ArmorType.BODY, 11);
	}), 9, SoundEvents.ARMOR_EQUIP_GENERIC, 0.0F, 0.0F, TFItemTags.REPAIRS_STEELEAF_ARMOR, asset("steeleaf"));

	public static final ArmorMaterial KNIGHTMETAL = new ArmorMaterial(20, Util.make(new EnumMap<>(ArmorType.class), map -> {
		map.put(ArmorType.BOOTS, 3);
		map.put(ArmorType.LEGGINGS, 6);
		map.put(ArmorType.CHESTPLATE, 8);
		map.put(ArmorType.HELMET, 3);
		map.put(ArmorType.BODY, 11);
	}), 8, Holder.direct(TFSounds.KNIGHTMETAL_EQUIP), 1.0F, 0.0F, TFItemTags.REPAIRS_KNIGHTMETAL_ARMOR, asset("knightmetal"));

	public static final ArmorMaterial PHANTOM = new ArmorMaterial(30, Util.make(new EnumMap<>(ArmorType.class), map -> {
		map.put(ArmorType.BOOTS, 3);
		map.put(ArmorType.LEGGINGS, 6);
		map.put(ArmorType.CHESTPLATE, 8);
		map.put(ArmorType.HELMET, 3);
		map.put(ArmorType.BODY, 10);
	}), 8, SoundEvents.ARMOR_EQUIP_GENERIC, 2.5F, 0.0F, TFItemTags.REPAIRS_PHANTOM_ARMOR, asset("phantom"));

	public static final ArmorMaterial YETI = new ArmorMaterial(20, Util.make(new EnumMap<>(ArmorType.class), map -> {
		map.put(ArmorType.BOOTS, 3);
		map.put(ArmorType.LEGGINGS, 6);
		map.put(ArmorType.CHESTPLATE, 7);
		map.put(ArmorType.HELMET, 4);
		map.put(ArmorType.BODY, 11);
	}), 15, SoundEvents.ARMOR_EQUIP_GENERIC, 3.0F, 0.0F, TFItemTags.REPAIRS_YETI_ARMOR, asset("yeti"));

	public static final ArmorMaterial ARCTIC = new ArmorMaterial(10, Util.make(new EnumMap<>(ArmorType.class), map -> {
		map.put(ArmorType.BOOTS, 2);
		map.put(ArmorType.LEGGINGS, 5);
		map.put(ArmorType.CHESTPLATE, 7);
		map.put(ArmorType.HELMET, 2);
		map.put(ArmorType.BODY, 7);
	}), 8, SoundEvents.ARMOR_EQUIP_GENERIC, 2.0F, 0.0F, TFItemTags.REPAIRS_ARCTIC_ARMOR, asset("arctic"));

	public static final ArmorMaterial TRAVELLERS_GEAR = new ArmorMaterial(12, Util.make(new EnumMap<>(ArmorType.class), map -> {
		map.put(ArmorType.BOOTS, 2);
		map.put(ArmorType.LEGGINGS, 3);
		map.put(ArmorType.CHESTPLATE, 4);
		map.put(ArmorType.HELMET, 2);
		map.put(ArmorType.BODY, 4);
	}), 1, SoundEvents.ARMOR_EQUIP_GENERIC, 0.0F, 0.0F, TFItemTags.REPAIRS_TRAVELLERS_GEAR, asset("travellers_gear"));
}
