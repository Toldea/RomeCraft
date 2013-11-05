package toldea.romecraft.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemScutum extends RomeCraftItem {

	public ItemScutum(int id) {
		super(id);
		this.setMaxStackSize(1);
	}

	@Override
	public EnumAction getItemUseAction(ItemStack par1ItemStack) {
		return EnumAction.block;
	}

	public int getMaxItemUseDuration(ItemStack par1ItemStack) {
		return 72000;
	}

	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
		par3EntityPlayer.setItemInUse(par1ItemStack, this.getMaxItemUseDuration(par1ItemStack));
		return par1ItemStack;
	}
}
