package toldea.romecraft.item;

import toldea.romecraft.CreativeTabsManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class GenericItem extends Item {

	public GenericItem(int id) {
		super(id);
		this.setCreativeTab(CreativeTabsManager.tabRomeCraft);
	}
}
