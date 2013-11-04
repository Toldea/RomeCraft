package toldea.romecraft.item;

import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemStack;

public class ItemGladiusScutum extends RomeCraftItemMeleeWeapon {

	public ItemGladiusScutum(int par1, EnumToolMaterial par2EnumToolMaterial) {
		super(par1, par2EnumToolMaterial);
	}

	// Override the normal sword block animation action, as we will use the shield to block instead.
	@Override
	public EnumAction getItemUseAction(ItemStack par1ItemStack) {
        return EnumAction.none;
    }
}
