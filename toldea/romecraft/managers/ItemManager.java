package toldea.romecraft.managers;

import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import toldea.romecraft.item.ItemPilum;
import toldea.romecraft.item.ItemScepter;
import toldea.romecraft.item.ItemScutum;
import toldea.romecraft.item.RomeCraftArmor;
import toldea.romecraft.item.RomeCraftItem;
import toldea.romecraft.item.RomeCraftItemMeleeWeapon;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class ItemManager {
	public static Item itemGladius = null;
	public static Item itemScutum = null;
	public static ItemArmor itemGalea = null;
	public static ItemArmor itemLoricaSegmentata = null;
	public static ItemArmor itemCingulum = null;
	public static ItemArmor itemCaligae = null;
	public static Item itemPilum = null;
	public static Item itemVerutum = null;
	public static Item itemPugio = null;
	public static Item itemSarcina = null;
	public static Item itemLegionaryEquipment = null;
	public static Item itemBlacksmithEquipment = null;
	public static Item itemScepter = null;
	public static Item itemIronBloom = null;
	public static Item itemHammer = null;

	public static void registerItems() {
		// Gladius
		itemGladius = new RomeCraftItemMeleeWeapon(ConfigManager.itemGladiusId, EnumToolMaterial.IRON).setUnlocalizedName("gladius").setTextureName(
				"romecraft:gladius");
		LanguageRegistry.addName(itemGladius, "Gladius");
		// Scutum
		itemScutum = new ItemScutum(ConfigManager.itemScutumId).setUnlocalizedName("scutum").setTextureName("romecraft:scutum").setFull3D();
		LanguageRegistry.addName(itemScutum, "Scutum");
		// Lorica Segmentata
		itemLoricaSegmentata = (ItemArmor) (new RomeCraftArmor(ConfigManager.itemLoricaSegmentataId, EnumArmorMaterial.IRON, 2, 1)).setUnlocalizedName(
				"loricaSegmentata").setTextureName("romecraft:loricasegmentata");
		LanguageRegistry.addName(itemLoricaSegmentata, "Lorica Segmentata");
		// Galea
		itemGalea = (ItemArmor) (new RomeCraftArmor(ConfigManager.itemGaleaId, EnumArmorMaterial.IRON, 2, 0)).setUnlocalizedName("galea").setTextureName(
				"romecraft:galea");
		LanguageRegistry.addName(itemGalea, "Galea");
		// Cingulum
		itemCingulum = (ItemArmor) (new RomeCraftArmor(ConfigManager.itemCingulumId, EnumArmorMaterial.IRON, 2, 2)).setUnlocalizedName("cingulum")
				.setTextureName("romecraft:cingulum");
		LanguageRegistry.addName(itemCingulum, "Cingulum");
		// Caligae
		itemCaligae = (ItemArmor) (new RomeCraftArmor(ConfigManager.itemCaligaeId, EnumArmorMaterial.IRON, 2, 3)).setUnlocalizedName("caligae").setTextureName(
				"romecraft:caligae");
		LanguageRegistry.addName(itemCaligae, "Caligae");
		// Pilum
		itemPilum = new ItemPilum(ConfigManager.itemPilumId).setUnlocalizedName("pilum").setTextureName("romecraft:pilum");
		LanguageRegistry.addName(itemPilum, "Pilum");
		// Verutum
		itemVerutum = new RomeCraftItem(ConfigManager.itemVerutumId).setUnlocalizedName("verutum").setTextureName("romecraft:verutum");
		LanguageRegistry.addName(itemVerutum, "Verutum");
		// Pugio
		itemPugio = new RomeCraftItem(ConfigManager.itemPugioId).setUnlocalizedName("pugio").setTextureName("romecraft:pugio");
		LanguageRegistry.addName(itemPugio, "Pugio");
		// Sarcina
		itemSarcina = new RomeCraftItem(ConfigManager.itemSarcinaId).setUnlocalizedName("sarcina").setTextureName("romecraft:sarcina");
		LanguageRegistry.addName(itemSarcina, "Sarcina");

		// Legionary Equipment
		itemLegionaryEquipment = new RomeCraftItem(ConfigManager.itemLegionaryEquipmentId).setUnlocalizedName("itemLegionaryEquipment").setTextureName(
				"romecraft:legionaryequipment");
		LanguageRegistry.addName(itemLegionaryEquipment, "Legionary Equipment");

		// Blacksmith Equipment
		itemBlacksmithEquipment = new RomeCraftItem(ConfigManager.itemBlacksmithEquipmentId).setUnlocalizedName("itemBlacksmithEquipment").setTextureName(
				"romecraft:blacksmithequipment");
		LanguageRegistry.addName(itemBlacksmithEquipment, "Blacksmith Equipment");

		// Scepter
		itemScepter = new ItemScepter(ConfigManager.itemScepterId).setUnlocalizedName("scepter").setTextureName("romecraft:scepter");
		LanguageRegistry.addName(itemScepter, "Scepter");

		// Iron Bloom
		itemIronBloom = new RomeCraftItem(ConfigManager.itemIronBloomId).setUnlocalizedName("itemIronBloom").setTextureName("romecraft:ironbloom");
		LanguageRegistry.addName(itemIronBloom, "Iron Bloom");

		// Hammer
		itemHammer = new RomeCraftItem(ConfigManager.itemHammerId).setUnlocalizedName("itemHammer").setTextureName("romecraft:hammer").setFull3D();
		LanguageRegistry.addName(itemHammer, "Hammer");
	}
}
