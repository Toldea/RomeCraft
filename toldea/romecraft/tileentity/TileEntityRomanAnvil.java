package toldea.romecraft.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import toldea.romecraft.managers.ItemManager;

public class TileEntityRomanAnvil extends TileEntity implements ISidedInventory {
	private ItemStack[] anvilItems;
	
	private static final int[] slots_invald = new int[] {};

	public TileEntityRomanAnvil() {
		anvilItems = new ItemStack[2];
	}

	@Override
	public int getSizeInventory() {
		if (anvilItems == null) {
			return 0;
		} else {
			return anvilItems.length;
		}
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		if (anvilItems == null || anvilItems.length < slot) {
			return null;
		} else {
			return anvilItems[slot];
		}
	}

	@Override
	public ItemStack decrStackSize(int slot, int count) {
		ItemStack itemStack = getStackInSlot(slot);
		if (itemStack != null) {
			if (itemStack.stackSize <= count) {
				return itemStack;
			} else {
				itemStack = itemStack.splitStack(count);
				onInventoryChanged();
				// worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			}
		}
		return itemStack;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		ItemStack itemStack = getStackInSlot(slot);
		setInventorySlotContents(slot, null);
		return itemStack;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack itemStack) {
		if (anvilItems != null && anvilItems.length >= slot) {
			anvilItems[slot] = itemStack;

			if (itemStack != null && itemStack.stackSize > getInventoryStackLimit()) {
				itemStack.stackSize = getInventoryStackLimit();
			}

			onInventoryChanged();
			// worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}
	}

	@Override
	public String getInvName() {
		return "inventoryTileEntityRomanAnvil";
	}

	@Override
	public boolean isInvNameLocalized() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		return false;
	}

	@Override
	public void openChest() {
	}

	@Override
	public void closeChest() {
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
		switch (slot) {
		case 0:
			return false;
		case 1:
			return itemStack.itemID == ItemManager.itemIronBloom.itemID;
		default:
			return false;
		}
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int var1) {
		return slots_invald;
	}

	@Override
	public boolean canInsertItem(int i, ItemStack itemstack, int j) {
		return false;
	}

	@Override
	public boolean canExtractItem(int i, ItemStack itemstack, int j) {
		return false;
	}

}
