package toldea.romecraft.item;

import toldea.romecraft.managers.CreativeTabsManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class RomeCraftItem extends Item {

	public RomeCraftItem(int id) {
		super(id);
		this.setCreativeTab(CreativeTabsManager.tabRomeCraft);
	}
}
