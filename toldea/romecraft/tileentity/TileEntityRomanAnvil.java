package toldea.romecraft.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import toldea.romecraft.item.crafting.RomanAnvilRecipes;
import toldea.romecraft.managers.ItemManager;

public class TileEntityRomanAnvil extends TileEntity implements ISidedInventory {
	private ItemStack[] anvilItems;

	private static final int[] slots_invald = new int[] {};

	private static final int MAX_ANVIL_NOT_HAMMERED_TIME = 100;
	public static final int NEW_IRON_BLOOM_TIME = 15;

	private int anvilHammeredCount = 0;
	private int anvilNotHammeredTime = 0;

	private int anvilNewIronBloomTimer = 0;

	public TileEntityRomanAnvil() {
		anvilItems = new ItemStack[2];
	}

	public void hammerIron(World world) {
		if (hasIronBloom()) {
			anvilHammeredCount++;
			if (anvilHammeredCount >= 10) {
				createItem();
			} else if (world.isRemote) {
				// Play a 'hammer on the iron bloom' sound.
				world.playSound(xCoord + .5, yCoord + .5, zCoord + .5, "romecraft:hammer_use", 1f, 1f, false);
			}
		} else if (world.isRemote) {
			// Play a 'there is nothing on the anvil so hit the anvil' sound.
			world.playSound(xCoord + .5, yCoord + .5, zCoord + .5, "romecraft:hammer_use", .4f, .7f, false);
		}
	}

	public void updateEntity() {
		if (anvilNewIronBloomTimer > 0) {
			anvilNewIronBloomTimer--;
		}
		if (anvilHammeredCount > 0) {
			anvilNotHammeredTime++;
			if (anvilNotHammeredTime > MAX_ANVIL_NOT_HAMMERED_TIME) {
				anvilNotHammeredTime = anvilHammeredCount = 0;
			}
		}
	}

	private boolean canCreateItem() {
		if (anvilItems == null || anvilItems[0] == null)
			return false;
		else {
			ItemStack itemStack = RomanAnvilRecipes.instance().getRecipeResult(anvilItems[0]);
			if (itemStack != null && anvilItems[1] == null) {
				return true;
			} else {
				return false;
			}
		}
	}

	public void createItem() {
		if (canCreateItem()) {
			ItemStack itemStack = RomanAnvilRecipes.instance().getRecipeResult(anvilItems[0]);
			anvilItems[1] = itemStack.copy();

			anvilHammeredCount = anvilNotHammeredTime = 0;
			anvilItems[0] = null;

			if (!this.worldObj.isRemote) {
				this.worldObj.playSoundEffect(xCoord + .5, yCoord + .5, zCoord + .5, "random.anvil_land", 1.0F, 1.0F);
			}
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);

		compound.setShort("anvilHammeredCount", (short) anvilHammeredCount);
		compound.setShort("anvilNotHammeredTime", (short) anvilNotHammeredTime);

		NBTTagList itemsList = new NBTTagList();

		if (anvilItems != null) {
			for (int i = 0; i < anvilItems.length; i++) {
				if (anvilItems[i] != null) {
					NBTTagCompound slotTag = new NBTTagCompound();
					slotTag.setByte("Slot", (byte) i);
					anvilItems[i].writeToNBT(slotTag);
					itemsList.appendTag(slotTag);
				}
				compound.setTag("Items", itemsList);
			}
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);

		anvilHammeredCount = compound.getShort("anvilHammeredCount");
		anvilNotHammeredTime = compound.getShort("anvilNotHammeredTime");

		NBTTagList itemsTag = compound.getTagList("Items");

		anvilItems = new ItemStack[2];
		for (int i = 0; i < itemsTag.tagCount(); i++) {
			NBTTagCompound slotTag = (NBTTagCompound) itemsTag.tagAt(i);
			byte slot = slotTag.getByte("Slot");

			if (slot >= 0 && slot < anvilItems.length) {
				anvilItems[slot] = ItemStack.loadItemStackFromNBT(slotTag);
			}
		}
	}

	public Packet getDescriptionPacket() {
		NBTTagCompound nbtTag = new NBTTagCompound();
		this.writeToNBT(nbtTag);
		return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 1, nbtTag);
	}

	public void onDataPacket(INetworkManager net, Packet132TileEntityData packet) {
		readFromNBT(packet.data);
	}

	public boolean hasIronBloom() {
		ItemStack itemstack = getStackInSlot(0);
		return (itemstack != null && itemstack.stackSize > 0);
	}

	public int getIronBloomCount() {
		ItemStack itemstack = getStackInSlot(0);
		if (itemstack == null) {
			return 0;
		} else {
			return itemstack.stackSize;
		}
	}
	
	public boolean didRecentlyChangeIronBloomCount() {
		return (anvilNewIronBloomTimer > 0);
	}
	
	public final int getNewIronBloomTimer() {
		return anvilNewIronBloomTimer;
	}

	public boolean hasFinishedItem() {
		ItemStack itemstack = getStackInSlot(1);
		return (itemstack != null && itemstack.stackSize > 0);
	}

	public ItemStack getFinishedItemStack() {
		return getStackInSlot(1);
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

			// If we are adding another iron bloom, set a 'new' timer that in rendering will display a phantom item of the final crafting result.
			if (slot == 0 && itemStack != null && itemStack.stackSize > 0) {
				anvilNewIronBloomTimer = NEW_IRON_BLOOM_TIME;
			}

			onInventoryChanged();
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
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
		return 6;
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
			return (!hasFinishedItem() && itemStack.itemID == ItemManager.itemIronBloom.itemID);
		case 1:
			return false;
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
