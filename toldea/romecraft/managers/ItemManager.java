package toldea.romecraft.managers;

import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraftforge.client.MinecraftForgeClient;
import toldea.romecraft.client.renderer.RenderItemPilum;
import toldea.romecraft.item.ItemCustomArmor;
import toldea.romecraft.item.ItemMeleeWeapon;
import toldea.romecraft.item.ItemPilum;
import toldea.romecraft.item.ItemScepter;
import toldea.romecraft.item.RomeCraftItem;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class ItemManager {
	public static int nextItemID = 5000;

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
	public static Item itemSudis = null;

	public static Item itemScepter = null;

	public static Item itemIronBloom = null;

	public static void registerItems() {
		// Gladius
		itemGladius = new ItemMeleeWeapon(nextItemID++, EnumToolMaterial.IRON).setUnlocalizedName("gladius").setTextureName("romecraft:gladius");
		LanguageRegistry.addName(itemGladius, "Gladius");
		// Scutum
		itemScutum = new RomeCraftItem(nextItemID++).setUnlocalizedName("scutum").setTextureName("romecraft:scutum").setFull3D();
		LanguageRegistry.addName(itemScutum, "Scutum");
		// Lorica Segmentata
		itemLoricaSegmentata = (ItemArmor) (new ItemCustomArmor(nextItemID++, EnumArmorMaterial.IRON, 2, 1)).setUnlocalizedName("loricaSegmentata")
				.setTextureName("romecraft:loricasegmentata");
		LanguageRegistry.addName(itemLoricaSegmentata, "Lorica Segmentata");
		// Galea
		itemGalea = (ItemArmor) (new ItemCustomArmor(nextItemID++, EnumArmorMaterial.IRON, 2, 0)).setUnlocalizedName("galea").setTextureName("romecraft:galea");
		LanguageRegistry.addName(itemGalea, "Galea");
		// Cingulum
		itemCingulum = (ItemArmor) (new ItemCustomArmor(nextItemID++, EnumArmorMaterial.IRON, 2, 2)).setUnlocalizedName("cingulum").setTextureName(
				"romecraft:cingulum");
		LanguageRegistry.addName(itemCingulum, "Cingulum");
		// Caligae
		itemCaligae = (ItemArmor) (new ItemCustomArmor(nextItemID++, EnumArmorMaterial.IRON, 2, 3)).setUnlocalizedName("caligae").setTextureName(
				"romecraft:caligae");
		LanguageRegistry.addName(itemCaligae, "Caligae");
		// Pilum
		itemPilum = new ItemPilum(nextItemID++).setUnlocalizedName("pilum").setTextureName("romecraft:pilum");
		LanguageRegistry.addName(itemPilum, "Pilum");
		MinecraftForgeClient.registerItemRenderer(itemPilum.itemID, new RenderItemPilum());
		// Verutum
		itemVerutum = new RomeCraftItem(nextItemID++).setUnlocalizedName("verutum").setTextureName("romecraft:verutum");
		LanguageRegistry.addName(itemVerutum, "Verutum");
		// Pugio
		itemPugio = new RomeCraftItem(nextItemID++).setUnlocalizedName("pugio").setTextureName("romecraft:pugio");
		LanguageRegistry.addName(itemPugio, "Pugio");
		// Sarcina
		itemSarcina = new RomeCraftItem(nextItemID++).setUnlocalizedName("sarcina").setTextureName("romecraft:sarcina");
		LanguageRegistry.addName(itemSarcina, "Sarcina");
		// Sudis
		itemSudis = new RomeCraftItem(nextItemID++).setUnlocalizedName("sudis").setTextureName("romecraft:sudis");
		LanguageRegistry.addName(itemSudis, "Sudis");

		// Scepter
		itemScepter = new ItemScepter(nextItemID++).setUnlocalizedName("scepter").setTextureName("romecraft:scepter");

		// Iron Bloom
		itemIronBloom = new RomeCraftItem(nextItemID++).setUnlocalizedName("itemIronBloom").setTextureName("romecraft:ironbloom");
		LanguageRegistry.addName(itemIronBloom, "Iron Bloom");
	}
}
