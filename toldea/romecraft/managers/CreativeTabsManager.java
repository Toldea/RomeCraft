package toldea.romecraft.managers;

import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CreativeTabsManager {
	public static CreativeTabs tabRomeCraft = new CreativeTabs("tabRomeCraft") {
		public ItemStack getIconItemStack() {
			return new ItemStack(ItemManager.itemScutum, 1, 0);
		}
	};

	public static void registerCreativeTabs() {
		LanguageRegistry.instance().addStringLocalization("itemGroup.tabRomeCraft", "RomeCraft");
	}
}
