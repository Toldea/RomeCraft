package toldea.romecraft.entity.ai.fsm;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;


public class CommonActions {
	public final StateMachine stateMachine;
	
	public CommonActions(StateMachine stateMachine) {
		this.stateMachine = stateMachine;
	}
	
	private void moveTowardsTargetLocation() {
		/*
		if (currentNavigationTarget == currentTargetLocation && !entityPleb.getNavigator().noPath()) {
			return;
		}
		TileEntity targetEntity = getTileEntityForTargetLocation();
		if (targetEntity == null) {
			return;
		}
		double dist = entityPleb.getDistanceSq(targetEntity.xCoord, targetEntity.yCoord, targetEntity.zCoord);
		if (dist > IN_RANGE) {
			if (dist > 256.0d) {
				Vec3 vec3 = RandomPositionGenerator.findRandomTargetBlockTowards(this.entityPleb, 14, 3, this.entityPleb.worldObj.getWorldVec3Pool()
						.getVecFromPool((double) targetEntity.xCoord + .5d, (double) targetEntity.yCoord + .5d, (double) targetEntity.zCoord + .5d));
				if (vec3 != null) {
					this.entityPleb.getNavigator().tryMoveToXYZ(vec3.xCoord, vec3.yCoord, vec3.zCoord, .6D);
					currentNavigationTarget = currentTargetLocation;
					return;
				}
			} else {
				this.entityPleb.getNavigator().tryMoveToXYZ((double) targetEntity.xCoord + .5d, (double) targetEntity.yCoord + .5d,
						(double) targetEntity.zCoord + .5d, .6D);
				currentNavigationTarget = currentTargetLocation;
				return;
			}
		}
		return;
		*/
	}

	private boolean inRangeOfTargetLocation() {
		ChunkCoordinates targetLocation = (ChunkCoordinates) stateMachine.getVariable(StateMachineVariables.TARGET_LOCATION);
		if (targetLocation == null || !(targetLocation instanceof ChunkCoordinates)) {
			return false;
		}
		Entity ownerEntity = (Entity) stateMachine.getVariable(StateMachineVariables.OWNER_ENTITY);
		if (ownerEntity == null || !(ownerEntity instanceof Entity)) {
			return false;
		}
		Double inRangeDist = (Double) stateMachine.getVariable(StateMachineVariables.IN_RANGE_DIST);
		if (inRangeDist == null) {
			return false;
		}
		double dist = ownerEntity.getDistanceSq(targetLocation.posX, targetLocation.posY, targetLocation.posZ);
		return (dist <= inRangeDist.doubleValue());
	}

	private boolean interactWithBloomerySlot(int slot) {
		/*
		ItemStack equippedItem = entityPleb.getCurrentItemOrArmor(0);

		switch (slot) {
		case 0:
		case 1:
			return insertItemInBloomerySlot(slot, equippedItem);
		case 2:
			if (equippedItem == null) {
				equippedItem = bloomery.getStackInSlot(2);
				if (equippedItem != null && equippedItem.stackSize > 0) {
					entityPleb.setCurrentItemOrArmor(0, equippedItem);
					bloomery.setInventorySlotContents(2, null);
					return true;
				}
			} else if (equippedItem.itemID == ItemManager.itemIronBloom.itemID) {
				return true;
			}
			return false;
		default:
			return false;
		}
		 */
		return false;
	}

	private boolean insertItemInBloomerySlot(int slot, ItemStack equippedItem) {
		/*
		if (bloomery.isItemValidForSlot(slot, equippedItem)) {
			ItemStack bloomerySlotStack = bloomery.getStackInSlot(slot);
			if (bloomerySlotStack == null) {
				bloomerySlotStack = equippedItem.copy();
				bloomerySlotStack.stackSize = 1;
			} else {
				if (bloomerySlotStack.stackSize < bloomery.getInventoryStackLimit()) {
					bloomerySlotStack.stackSize++;
				} else {
					return false;
				}
			}
			bloomery.setInventorySlotContents(slot, bloomerySlotStack);
			--equippedItem.stackSize;
			if (equippedItem.stackSize == 0) {
				entityPleb.setCurrentItemOrArmor(0, null);
			} else {
				entityPleb.setCurrentItemOrArmor(0, equippedItem);
			}
			return true;
		}
		return false;
		*/
		return false;
	}

	private void interactWithChestForCurrentTask() {
		/*
		switch (currentTask) {
		case STORE_IRON_BLOOM:
			insertEquippedItemInChest();
			currentTargetLocation = BlacksmithTargetLocation.NONE;
			currentTask = BlacksmithTask.NONE;
			break;
		case GET_IRON_ORE:
		case GET_FUEL:
			int slot = currentTask == BlacksmithTask.GET_IRON_ORE ? 0 : 1;
			if (withdrawItemFromChestValidForBloomerySlot(slot)) {
				currentTargetLocation = BlacksmithTargetLocation.BLOOMERY;
				idleTimer = IDLE_DELAY;
			} else {
				// Check if we somehow were already holding the correct item (i.e. when the server was shut down mid task).
				ItemStack equippedItem = entityPleb.getCurrentItemOrArmor(0);
				if (equippedItem != null && bloomery.isItemValidForSlot(slot, equippedItem)) {
					currentTargetLocation = BlacksmithTargetLocation.BLOOMERY;
					idleTimer = IDLE_DELAY;
				} else {
					currentTargetLocation = BlacksmithTargetLocation.NONE;
					currentTask = BlacksmithTask.NONE;
				}
			}
			break;
		default:
			break;
		}
		*/
	}
	
	private void insertEquippedItemInChest() {
		/*
		ItemStack equippedItem = entityPleb.getCurrentItemOrArmor(0);
		ItemStack chestItemStack;

		int firstEmptySlot = -1;

		for (int slot = 0; slot < chest.getSizeInventory(); slot++) {
			chestItemStack = chest.getStackInSlot(slot);
			if (chestItemStack == null) {
				if (firstEmptySlot == -1) {
					firstEmptySlot = slot;
				}
				continue;
			} else if (chestItemStack.itemID == equippedItem.itemID && chestItemStack.stackSize < chest.getInventoryStackLimit()) {
				chestItemStack.stackSize++;
				insertOrMergeEquippedItemWithChestStackInSlot(equippedItem, chestItemStack, slot);
				return;
			}
		}

		if (firstEmptySlot != -1) {
			chestItemStack = equippedItem.copy();
			chestItemStack.stackSize = 1;
			insertOrMergeEquippedItemWithChestStackInSlot(equippedItem, chestItemStack, firstEmptySlot);
		}
		*/
	}

	private void insertOrMergeEquippedItemWithChestStackInSlot(ItemStack equippedItem, ItemStack chestItemStack, int slot) {
		/*
		chest.setInventorySlotContents(slot, chestItemStack);
		--equippedItem.stackSize;
		if (equippedItem.stackSize <= 0) {
			entityPleb.setCurrentItemOrArmor(0, null);
		} else {
			entityPleb.setCurrentItemOrArmor(0, equippedItem);
		}
		*/
	}

	private boolean withdrawItemFromChestValidForBloomerySlot(int bloomerySlot) {
		/*
		ItemStack equippedItem = entityPleb.getCurrentItemOrArmor(0);
		if (equippedItem != null) {
			return false;
		}
		ItemStack chestItemStack;

		for (int slot = 0; slot < chest.getSizeInventory(); slot++) {
			chestItemStack = chest.getStackInSlot(slot);
			if (chestItemStack != null && bloomery.isItemValidForSlot(bloomerySlot, chestItemStack)) {
				equippedItem = chestItemStack.copy();
				equippedItem.stackSize = 1;
				chestItemStack.stackSize--;
				if (chestItemStack.stackSize <= 0) {
					chest.setInventorySlotContents(slot, null);
				} else {
					chest.setInventorySlotContents(slot, chestItemStack);
				}
				entityPleb.setCurrentItemOrArmor(0, equippedItem);
				return true;
			}
		}
		return false;
		*/
		return false;
	}
}
