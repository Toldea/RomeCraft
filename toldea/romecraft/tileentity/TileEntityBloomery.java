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
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.world.World;
import toldea.romecraft.block.BlockBloomery;
import toldea.romecraft.item.crafting.BloomeryRecipes;
import toldea.romecraft.managers.BlockManager;
import toldea.romecraft.managers.PacketManager;

public class TileEntityBloomery extends TileEntity implements ISidedInventory {
	private boolean isValidBloomeryMultiblock = false;
	private boolean isMaster = false;
	private boolean isActive = false;

	private static final int[] slots_invald = new int[] {};
	private static final int[] slots_bottom = new int[] { 2, 1 };
	private static final int[] slots_sides = new int[] { 1, 0 };
	private ItemStack[] bloomeryItems;

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
		isValidBloomeryMultiblock = false;

		bloomeryItems = null;

		furnaceBurnTime = 0;
		currentItemBurnTime = 0;
		furnaceCookTime = 0;

		int metadata = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		metadata = metadata & BlockBloomery.MASK_DIR;
		worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, metadata, 2);

		updateValidityOther(false);
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

	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setBoolean("isValid", isValidBloomeryMultiblock);
		compound.setBoolean("isMaster", isMaster);
		compound.setBoolean("isActive", isActive);

		if (isValidBloomeryMultiblock && isMaster) {
			compound.setShort("BurnTime", (short) furnaceBurnTime);
			compound.setShort("CookTime", (short) furnaceCookTime);
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
		}
	}
	
	public void applyBellowsBoost(World world) {
		if (world.isRemote) {
			System.out.println("Applying bellows boost - Sending packet to server!");
			PacketManager.sendApplyBellowsBoostPacket(this);
		} else {
			System.out.println("Receiving bellow boost packet! Server now synced with client.");
		}
		
		bellowsActiveTime = TileEntityBellows.ROTATION_TIME;
		furnaceNotBellowedTime = 0;
		
		System.out.println("Applying a bellows boost for " + bellowsActiveTime + " ticks!");
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

		if (furnaceBurnTime > 0) {
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

				if (furnaceCookTime == 100) {
					furnaceCookTime = 0;
					smeltItem();
					flag1 = true;
				}
			} else {
				if (furnaceNotBellowedTime < 100) {
					furnaceNotBellowedTime++;
				} else {
					furnaceCookTime = 0;
				}
			}

			if (furnaceBurnTime > 0) {
				flag1 = true;
				this.isActive = true;
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			} else if (this.isActive) {
				this.isActive = false;
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			}
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
		return 64;
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
		if (!this.isValidBloomeryMultiblock) {
			return false;
		} else if (this.isMaster) {
			switch (i) {
			case 0:
				return itemstack.itemID == Block.oreIron.blockID;
			case 1:
				return itemstack.itemID == Item.coal.itemID;
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
