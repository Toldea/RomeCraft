package toldea.romecraft.item;

import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemSword;
import toldea.romecraft.managers.CreativeTabsManager;

public class RomeCraftItemMeleeWeapon extends ItemSword {
	public RomeCraftItemMeleeWeapon(int par1, EnumToolMaterial par2EnumToolMaterial) {
		super(par1, par2EnumToolMaterial);
		this.setCreativeTab(CreativeTabsManager.tabRomeCraft);
	}
}
