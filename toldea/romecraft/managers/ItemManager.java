package toldea.romecraft.managers;

import toldea.romecraft.client.renderer.CustomItemRenderer;
import toldea.romecraft.client.renderer.RenderItemPilum;
import toldea.romecraft.item.GenericItem;
import toldea.romecraft.item.Gureebu;
import toldea.romecraft.item.ItemMeleeWeapon;
import toldea.romecraft.item.ItemPilum;
import toldea.romecraft.item.ItemScepter;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class ItemManager {
	private static int nextItemID = 5000;

	public static Item itemGladius = null;
	public static Item itemScutum = null;
	public static Item itemLoricaSegmentata = null;
	public static Item itemGalea = null;
	public static Item itemPilum = null;
	public static Item itemVerutum = null;
	public static Item itemPugio = null;
	public static Item itemCaligae = null;
	public static Item itemSarcina = null;
	public static Item itemSudis = null;

	public static Item itemTestScutum = null;

	public static Item itemScepter = null;
	public static Item itemGureebu = null;

	public static void registerItems() {
		// Gladius
		itemGladius = new ItemMeleeWeapon(nextItemID++, EnumToolMaterial.IRON).setUnlocalizedName("gladius").setTextureName("romecraft:gladius");
		LanguageRegistry.addName(itemGladius, "Gladius");
		// Scutum
		itemScutum = new GenericItem(nextItemID++).setUnlocalizedName("scutum").setTextureName("romecraft:scutum");
		LanguageRegistry.addName(itemScutum, "Scutum");
		MinecraftForgeClient.registerItemRenderer(itemScutum.itemID, new CustomItemRenderer());
		// Lorica Segmentata
		itemLoricaSegmentata = new GenericItem(nextItemID++).setUnlocalizedName("loricaSegmentata").setTextureName("romecraft:loricaSegmentata");
		LanguageRegistry.addName(itemLoricaSegmentata, "Lorica Segmentata");
		// Galea
		itemGalea = new GenericItem(nextItemID++).setUnlocalizedName("galea").setTextureName("romecraft:galea");
		LanguageRegistry.addName(itemGalea, "Galea");
		// Pilum
		itemPilum = new ItemPilum(nextItemID++).setUnlocalizedName("pilum").setTextureName("romecraft:pilum");
		LanguageRegistry.addName(itemPilum, "Pilum");
		MinecraftForgeClient.registerItemRenderer(itemPilum.itemID, new RenderItemPilum());
		// Verutum
		itemVerutum = new GenericItem(nextItemID++).setUnlocalizedName("verutum").setTextureName("romecraft:verutum");
		LanguageRegistry.addName(itemVerutum, "Verutum");
		// Pugio
		itemPugio = new GenericItem(nextItemID++).setUnlocalizedName("pugio").setTextureName("romecraft:pugio");
		LanguageRegistry.addName(itemPugio, "Pugio");
		// Caligae
		itemCaligae = new GenericItem(nextItemID++).setUnlocalizedName("galigae").setTextureName("romecraft:caligae");
		LanguageRegistry.addName(itemCaligae, "Caligae");
		// Sarcina
		itemSarcina = new GenericItem(nextItemID++).setUnlocalizedName("sarcina").setTextureName("romecraft:sarcina");
		LanguageRegistry.addName(itemSarcina, "Sarcina");
		// Sudis
		itemSudis = new GenericItem(nextItemID++).setUnlocalizedName("sudis").setTextureName("romecraft:sudis");
		LanguageRegistry.addName(itemSudis, "Sudis");

		// TestScutum
		itemTestScutum = new GenericItem(nextItemID++).setUnlocalizedName("testScutum").setTextureName("romecraft:scutum");
		LanguageRegistry.addName(itemTestScutum, "Test Scutum");

		// Scepter
		itemScepter = new ItemScepter(nextItemID++).setUnlocalizedName("scepter").setTextureName("romecraft:scepter");
		// Gureebu
		itemGureebu = new Gureebu(nextItemID++).setUnlocalizedName("gureebu").setTextureName("romecraft:gureebu");
	}
}