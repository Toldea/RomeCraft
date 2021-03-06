package toldea.romecraft.tileentity;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.world.World;
import toldea.romecraft.block.BlockHelper;
import toldea.romecraft.item.crafting.BloomeryRecipes;
import toldea.romecraft.managers.BlockManager;
import toldea.romecraft.managers.PacketManager;
import toldea.romecraft.utility.AdjacentTileEntityCache;

public class TileEntityBloomery extends TileEntity implements ISidedInventory {
	private static final int SMELTING_TIME = 180;
	private static final int MAX_BELLOWS_IDLE_TIME = 100;

	private static final int[] slots_invald = new int[] {};
	private static final int[] slots_bottom = new int[] { 2, 1 };
	private static final int[] slots_sides = new int[] { 1, 0 };

	private ItemStack[] bloomeryItems;

	private boolean isValidBloomeryMultiblock = false;
	private boolean isMaster = false;
	private boolean isActive = false;

	private final AdjacentTileEntityCache<TileEntityBellows> adjacentBellows = new AdjacentTileEntityCache<TileEntityBellows>();
	private final AdjacentTileEntityCache<TileEntityChest> adjacentChests = new AdjacentTileEntityCache<TileEntityChest>();

	public int furnaceBurnTime = 0;
	public int currentItemBurnTime = 0;
	public int furnaceCookTime = 0;

	private int bellowsActiveTime = 0;
	private int furnaceNotBellowedTime = 0;

	public boolean getIsValid() {
		return isValidBloomeryMultiblock;
	}

	public boolean getIsMaster() {
		return isMaster;
	}

	public boolean getIsActive() {
		return isActive;
	}

	public void invalidateMultiblock() {
		updateValidityOther(false);
		
		isValidBloomeryMultiblock = false;
		isActive = false;
		isMaster = false;
		furnaceBurnTime = currentItemBurnTime = furnaceCookTime = bellowsActiveTime = furnaceNotBellowedTime = 0;

		bloomeryItems = null;

		int metadata = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		metadata = metadata & BlockHelper.MASK_DIR;
		worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, metadata, 2);
	}

	public boolean checkIfProperlyFormed() {
		boolean isSlave = false;

		for (int height = -2; height < 3; height++) {
			if (height == 0) {
				continue;
			}
			int x = xCoord;
			int y = yCoord + height;
			int z = zCoord;

			int blockId = worldObj.getBlockId(x, y, z);
			boolean blockIsAir = (blockId == 0);

			if (height == -2) {
				// Make sure there isn't a bloomery block 2 blocks under us, to prevent 'bloomery looping'.
				if (!blockIsAir && blockId == BlockManager.blockBloomery.blockID) {
					return false;
				}
			} else if (height == -1) {
				// Check if there is another Bloomery block underneath us. If so we act like the 'slave' and will check if there is an air space above us.
				if (!blockIsAir && blockId == BlockManager.blockBloomery.blockID) {
					isSlave = true;
				}
			} else if (height == 1) {
				if (!isSlave) {
					// If we are not a slave, check if we have another Bloomery block above us.
					if (blockIsAir || blockId != BlockManager.blockBloomery.blockID) {
						return false;
					}
				} else {
					// Else if we are a slave, make sure there is air above us.
					if (!blockIsAir) {
						return false;
					}
				}
			} else if (height == 2 && !isSlave) {
				// Finally on the top level, if we are not the slave make sure there is an air block above us.
				if (!blockIsAir) {
					return false;
				}
			}
		}

		// If we survived all these checks, we are properly formed.
		setIsProperlyFormed(true, !isSlave);
		updateValidityOther(true);
		return true;
	}

	public void setIsProperlyFormed(boolean isProperlyFormed, boolean isMaster) {
		this.isValidBloomeryMultiblock = isProperlyFormed;
		this.isMaster = isMaster;
		if (isMaster && bloomeryItems == null) {
			bloomeryItems = new ItemStack[3];
		}
		// Notify this block so it knows if it needs to start or stop rendering.
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}

	public void updateValidityOther(boolean isValid) {
		TileEntityBloomery otherTileEntity = getOtherTileEntity();
		if (otherTileEntity != null) {
			otherTileEntity.setIsProperlyFormed(isValid, !isMaster);
		}
	}

	private TileEntityBloomery getOtherTileEntity() {
		return (TileEntityBloomery) worldObj.getBlockTileEntity(xCoord, yCoord + (isMaster ? 1 : -1), zCoord);
	}

	private TileEntityBloomery getMasterTileEntity() {
		if (!this.isValidBloomeryMultiblock) {
			return null;
		} else
			return (this.isMaster ? this : (TileEntityBloomery) worldObj.getBlockTileEntity(xCoord, yCoord - 1, zCoord));
	}

	public Packet getDescriptionPacket() {
		NBTTagCompound nbtTag = new NBTTagCompound();
		this.writeToNBT(nbtTag);
		return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 1, nbtTag);
	}

	public void onDataPacket(INetworkManager net, Packet132TileEntityData packet) {
		readFromNBT(packet.data);
	}

	public TileEntityBellows getAdjacentBellowsForDirection(int direction) {
		return this.adjacentBellows.getAdjacentTileEntityOfTypeForDirection(TileEntityBellows.class, direction, worldObj, xCoord, yCoord, zCoord);
	}

	public TileEntityChest getAdjacentChestForDirection(int direction) {
		return this.adjacentChests.getAdjacentTileEntityOfTypeForDirection(TileEntityChest.class, direction, worldObj, xCoord, yCoord, zCoord);
	}
	
	/**
	 * Checks if there is either a smelted iron bloom in the bloomery waiting for retrieval or if there is both iron ore and fuel in either the bloomery or any
	 * adjacent chests.
	 */
	public boolean hasWork() {
		boolean hasFuel = hasFuel();
		boolean hasIronOre = hasIronOre();
		boolean hasSmeltedItem = hasIronBloom();

		ItemStack itemstack;

		// If there is a smelted iron bloom left in the furnace and we have at least one adjacent chest with room left, return true.
		if (hasSmeltedItem) {
			return (getAdjacentChestWithStoreRoomForItemStack(getStackInSlot(2)) != null);
		}

		// Loop through all adjacent chests to find if there is any fuel and iron available.
		if (!hasIronOre) {
			hasIronOre = (getAdjacentChestWithValidResourcesForISidedInventorySlot(this, 0) != null);
		}
		if (!hasFuel) {
			hasFuel = (getAdjacentChestWithValidResourcesForISidedInventorySlot(this, 1) != null);
		}

		// Return true if we have both fuel and iron somewhere in the system, else return false.
		return (hasFuel && hasIronOre);
	}

	public boolean hasFuel() {
		if (this.isBurning()) {
			return true;
		} else {
			ItemStack itemstack = getStackInSlot(1);
			return (itemstack != null && itemstack.itemID == Item.coal.itemID);
		}
	}

	public boolean hasIronOre() {
		return isItemValidForSlot(0, getStackInSlot(0));
	}

	public boolean hasIronBloom() {
		ItemStack itemstack = getStackInSlot(2);
		return (itemstack != null && itemstack.stackSize > 0);
	}

	public float getSmeltingProgress() {
		return (float) this.furnaceCookTime / (float) SMELTING_TIME;
	}

	public float getBellowsIdleFailureProgress() {
		return (float) this.furnaceNotBellowedTime / (float) MAX_BELLOWS_IDLE_TIME;
	}

	/**
	 * Gets the first adjacent chest which has valid resources able to put into the specified slot of the specified ISidedInventory.
	 */
	public TileEntityChest getAdjacentChestWithValidResourcesForISidedInventorySlot(ISidedInventory inventory, int slot) {
		ItemStack itemstack;
		for (int i = 0; i < 4; i++) {
			TileEntityChest chest = getAdjacentChestForDirection(i);
			if (chest != null) {
				for (int s = 0; s < chest.getSizeInventory(); s++) {
					itemstack = chest.getStackInSlot(s);
					if (itemstack != null && inventory.isItemValidForSlot(slot, itemstack)) {
						return chest;
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * Gets the first adjacent chest which has valid storage room to store the passed itemStack.
	 */
	public TileEntityChest getAdjacentChestWithStoreRoomForItemStack(ItemStack itemStackToStore) {
		ItemStack itemstack;
		for (int i = 0; i < 4; i++) {
			TileEntityChest chest = getAdjacentChestForDirection(i);
			if (chest != null) {
				for (int s = 0; s < chest.getSizeInventory(); s++) {
					itemstack = chest.getStackInSlot(s);
					if (itemstack == null || (itemstack.itemID == itemStackToStore.itemID && itemstack.stackSize + itemStackToStore.stackSize < chest.getInventoryStackLimit())) {
						return chest;
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * Returns the first found adjacent chest, or null if none are to be found.
	 */
	public TileEntityChest getAdjacentChest() {
		ItemStack itemstack;
		for (int i = 0; i < 4; i++) {
			TileEntityChest chest = getAdjacentChestForDirection(i);
			if (chest != null && chest instanceof TileEntityChest) {
				return chest;
			}
		}
		return null;
	}

	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setBoolean("isValid", isValidBloomeryMultiblock);
		compound.setBoolean("isMaster", isMaster);
		compound.setBoolean("isActive", isActive);

		if (isValidBloomeryMultiblock && isMaster) {
			compound.setShort("BurnTime", (short) furnaceBurnTime);
			compound.setShort("CookTime", (short) furnaceCookTime);
			compound.setShort("bellowsActiveTime", (short) bellowsActiveTime);
			compound.setShort("furnaceNotBellowedTime", (short) furnaceNotBellowedTime);
			NBTTagList itemsList = new NBTTagList();

			if (bloomeryItems != null) {
				for (int i = 0; i < bloomeryItems.length; i++) {
					if (bloomeryItems[i] != null) {
						NBTTagCompound slotTag = new NBTTagCompound();
						slotTag.setByte("Slot", (byte) i);
						bloomeryItems[i].writeToNBT(slotTag);
						itemsList.appendTag(slotTag);
					}
					compound.setTag("Items", itemsList);
				}
			}
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		isValidBloomeryMultiblock = compound.getBoolean("isValid");
		isMaster = compound.getBoolean("isMaster");
		isActive = compound.getBoolean("isActive");

		if (isValidBloomeryMultiblock && isMaster) {
			NBTTagList itemsTag = compound.getTagList("Items");

			bloomeryItems = new ItemStack[3];

			for (int i = 0; i < itemsTag.tagCount(); i++) {
				NBTTagCompound slotTag = (NBTTagCompound) itemsTag.tagAt(i);
				byte slot = slotTag.getByte("Slot");

				if (slot >= 0 && slot < bloomeryItems.length)
					bloomeryItems[slot] = ItemStack.loadItemStackFromNBT(slotTag);
			}

			furnaceBurnTime = compound.getShort("BurnTime");
			furnaceCookTime = compound.getShort("CookTime");
			currentItemBurnTime = TileEntityFurnace.getItemBurnTime(bloomeryItems[1]);

			bellowsActiveTime = compound.getShort("bellowsActiveTime");
			furnaceNotBellowedTime = compound.getShort("furnaceNotBellowedTime");
		}
	}

	public void applyBellowsBoost(World world) {
		// Applying a bellows boost without a burning fire does nothing, so return immediatly.
		// The same goes for when an iron bloom is inside it.
		if (!isBurning() || hasIronBloom()) {
			return;
		}
		if (world.isRemote) {
			PacketManager.sendApplyBellowsBoostPacketToServer(this);
		}
		bellowsActiveTime = TileEntityBellows.ROTATION_TIME;
		furnaceNotBellowedTime = 0;
	}

	public boolean isBurning() {
		return furnaceBurnTime > 0;
	}

	private boolean canSmelt() {
		if (bloomeryItems == null || bloomeryItems[0] == null)
			return false;
		else {
			ItemStack itemStack = BloomeryRecipes.smelting().getSmeltingResult(bloomeryItems[0]);
			if (itemStack == null)
				return false;
			if (bloomeryItems[2] == null)
				return true;
			if (!bloomeryItems[2].isItemEqual(itemStack))
				return false;

			int resultingStackSize = bloomeryItems[2].stackSize + itemStack.stackSize;
			return (resultingStackSize <= getInventoryStackLimit() && resultingStackSize <= itemStack.getMaxStackSize());
		}
	}

	public boolean isApplyingBellows() {
		return bellowsActiveTime > 0;
	}

	public void smeltItem() {
		if (canSmelt()) {
			ItemStack itemStack = BloomeryRecipes.smelting().getSmeltingResult(bloomeryItems[0]);

			if (bloomeryItems[2] == null) {
				bloomeryItems[2] = itemStack.copy();
			} else if (bloomeryItems[2].isItemEqual(itemStack)) {
				bloomeryItems[2].stackSize += itemStack.stackSize;
			}

			bloomeryItems[0].stackSize--;
			if (bloomeryItems[0].stackSize <= 0) {
				bloomeryItems[0] = null;
			}
			
			if (!this.worldObj.isRemote)  {
				this.worldObj.playSoundEffect(xCoord + .5, yCoord + .5, zCoord + .5, "liquid.lavapop", 2.0F, 0.0F);
	        }
		}
	}

	@Override
	public void updateEntity() {
		if (!this.isValidBloomeryMultiblock || !this.isMaster) {
			return;
		}

		boolean flag = furnaceBurnTime > 0;
		boolean flag1 = false;
		int metadata = getBlockMetadata();

		if (flag) {
			furnaceBurnTime--;
		}

		if (bellowsActiveTime > 0) {
			bellowsActiveTime--;
		}

		if (!this.worldObj.isRemote) {
			if (furnaceBurnTime == 0 && canSmelt()) {
				currentItemBurnTime = furnaceBurnTime = TileEntityFurnace.getItemBurnTime(bloomeryItems[1]);

				if (furnaceBurnTime > 0) {
					flag1 = true;

					if (bloomeryItems[1] != null) {
						bloomeryItems[1].stackSize--;

						if (bloomeryItems[1].stackSize == 0)
							bloomeryItems[1] = bloomeryItems[1].getItem().getContainerItemStack(bloomeryItems[1]);
					}
				}
			}

			if (isBurning() && canSmelt() && isApplyingBellows()) {
				furnaceCookTime++;

				if (furnaceCookTime == SMELTING_TIME) {
					furnaceCookTime = 0;
					smeltItem();
					flag1 = true;
				}
			} else {
				if (furnaceNotBellowedTime < MAX_BELLOWS_IDLE_TIME) {
					furnaceNotBellowedTime++;
				} else {
					furnaceCookTime = 0;
				}
			}

			if (flag != this.furnaceBurnTime > 0) {
				flag1 = true;
				this.isActive = true;
			} else if (this.isActive && this.furnaceBurnTime <= 0) {
				this.isActive = false;
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			}
		}

		if (this.isActive) {
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}
		if (flag1) {
			onInventoryChanged();
		}
	}

	@Override
	public int getSizeInventory() {
		if (!this.isValidBloomeryMultiblock) {
			return 0;
		} else if (this.isMaster) {
			if (bloomeryItems == null) {
				return 0;
			} else {
				return bloomeryItems.length;
			}
		} else {
			return getMasterTileEntity().getSizeInventory();
		}
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		if (!this.isValidBloomeryMultiblock) {
			return null;
		} else if (this.isMaster) {
			if (bloomeryItems == null || bloomeryItems.length < i) {
				return null;
			} else {
				return bloomeryItems[i];
			}
		} else {
			return getMasterTileEntity().getStackInSlot(i);
		}
	}

	@Override
	public ItemStack decrStackSize(int i, int count) {
		if (!this.isValidBloomeryMultiblock) {
			return null;
		} else if (this.isMaster) {
			ItemStack itemstack = getStackInSlot(i);
			if (itemstack != null) {
				if (itemstack.stackSize <= count) {
					setInventorySlotContents(i, null);
				} else {
					itemstack = itemstack.splitStack(count);
					onInventoryChanged();
					worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				}
			}
			return itemstack;
		} else {
			return getMasterTileEntity().decrStackSize(i, count);
		}
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		if (!this.isValidBloomeryMultiblock) {
			return null;
		} else if (this.isMaster) {
			ItemStack item = getStackInSlot(i);
			setInventorySlotContents(i, null);
			return item;
		} else {
			return getMasterTileEntity().getStackInSlotOnClosing(i);
		}
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		if (!this.isValidBloomeryMultiblock) {
			return;
		} else if (this.isMaster) {
			if (bloomeryItems != null && i < bloomeryItems.length) {
				bloomeryItems[i] = itemstack;

				if (itemstack != null && itemstack.stackSize > getInventoryStackLimit()) {
					itemstack.stackSize = getInventoryStackLimit();
				}
				onInventoryChanged();
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			}
		} else {
			getMasterTileEntity().setInventorySlotContents(i, itemstack);
		}
	}

	@Override
	public String getInvName() {
		return "inventoryTileEntityBloomery";
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
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		if (!this.isValidBloomeryMultiblock || itemstack == null) {
			return false;
		} else if (this.isMaster) {
			switch (i) {
			case 0:
				return (!hasIronBloom() && itemstack.itemID == Block.oreIron.blockID);
			case 1:
				return (!isBurning() && itemstack.itemID == Item.coal.itemID);
			case 2:
			default:
				return false;
			}
		} else {
			return getMasterTileEntity().isItemValidForSlot(i, itemstack);
		}
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		if (!this.isValidBloomeryMultiblock) {
			return slots_invald;
		} else if (this.isMaster) {
			return side == 0 ? slots_bottom : (side == 1 ? slots_invald : slots_sides);
		} else {
			return slots_invald;
		}
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack itemstack, int side) {
		if (!this.isValidBloomeryMultiblock) {
			return false;
		} else if (this.isMaster) {
			return this.isItemValidForSlot(slot, itemstack);
		} else {
			return false;
		}
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack itemstack, int side) {
		if (!this.isValidBloomeryMultiblock) {
			return false;
		} else if (this.isMaster) {
			return side != 0 || slot != 1;
		} else {
			return false;
		}
	}
}
