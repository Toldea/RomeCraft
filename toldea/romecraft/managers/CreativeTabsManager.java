package toldea.romecraft.managers;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class CreativeTabsManager {
	public static CreativeTabs tabRomeCraft = new CreativeTabs("tabRomeCraft") {
		public ItemStack getIconItemStack() {
			return new ItemStack(ItemManager.itemGalea, 1, 0);
		}
	};

	public static void registerCreativeTabs() {
		LanguageRegistry.instance().addStringLocalization("itemGroup.tabRomeCraft", "RomeCraft");
	}
}
