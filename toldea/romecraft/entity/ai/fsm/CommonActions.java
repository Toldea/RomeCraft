package toldea.romecraft.entity.ai.fsm;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Vec3;


public class CommonActions {
	private final StateMachine stateMachine;
	
	public CommonActions(StateMachine stateMachine) {
		this.stateMachine = stateMachine;
	}
	
	public void moveTowardsTargetLocation() {
		ChunkCoordinates targetLocation = (ChunkCoordinates) stateMachine.getVariable(StateMachineVariables.TARGET_LOCATION);
		if (targetLocation == null || !(targetLocation instanceof ChunkCoordinates)) {
			return;
		}
		EntityCreature ownerEntity = (EntityCreature) stateMachine.getVariable(StateMachineVariables.OWNER_ENTITY);
		if (ownerEntity == null || !(ownerEntity instanceof Entity)) {
			return;
		}
		Double inRangeDist = (Double) stateMachine.getVariable(StateMachineVariables.IN_RANGE_DIST);
		if (inRangeDist == null) {
			return;
		}
		double dist = ownerEntity.getDistanceSq(targetLocation.posX, targetLocation.posY, targetLocation.posZ);
		
		if (dist > inRangeDist) {
			if (dist > 256.0d) {
				Vec3 vec3 = RandomPositionGenerator.findRandomTargetBlockTowards(ownerEntity, 14, 3, ownerEntity.worldObj.getWorldVec3Pool()
						.getVecFromPool((double) targetLocation.posX + .5d, (double) targetLocation.posY + .5d, (double) targetLocation.posZ + .5d));
				if (vec3 != null) {
					ownerEntity.getNavigator().tryMoveToXYZ(vec3.xCoord, vec3.yCoord, vec3.zCoord, .6D);
					return;
				}
			} else {
				ownerEntity.getNavigator().tryMoveToXYZ((double) targetLocation.posX + .5d, (double) targetLocation.posY + .5d,
						(double) targetLocation.posZ + .5d, .6D);
				return;
			}
		}
		return;
	}

	public boolean inRangeOfTargetLocation() {
		ChunkCoordinates targetLocation = (ChunkCoordinates) stateMachine.getVariable(StateMachineVariables.TARGET_LOCATION);
		if (targetLocation == null || !(targetLocation instanceof ChunkCoordinates)) {
			return false;
		}
		EntityCreature ownerEntity = (EntityCreature) stateMachine.getVariable(StateMachineVariables.OWNER_ENTITY);
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
	
	public boolean insertItemInISidedInventorySlot(ISidedInventory inventory, int inventorySlot) {
		EntityCreature entityCreature = (EntityCreature) stateMachine.getVariable(StateMachineVariables.OWNER_ENTITY);
		if (entityCreature == null || !(entityCreature instanceof EntityCreature)) {
			return false;
		}
		
		ItemStack equippedItem = entityCreature.getCurrentItemOrArmor(0);
		
		if (inventory.isItemValidForSlot(inventorySlot, equippedItem)) {
			ItemStack bloomerySlotStack = inventory.getStackInSlot(inventorySlot);
			if (bloomerySlotStack == null) {
				bloomerySlotStack = equippedItem.copy();
				bloomerySlotStack.stackSize = 1;
			} else {
				if (bloomerySlotStack.stackSize < inventory.getInventoryStackLimit()) {
					bloomerySlotStack.stackSize++;
				} else {
					return false;
				}
			}
			inventory.setInventorySlotContents(inventorySlot, bloomerySlotStack);
			--equippedItem.stackSize;
			if (equippedItem.stackSize == 0) {
				entityCreature.setCurrentItemOrArmor(0, null);
			} else {
				entityCreature.setCurrentItemOrArmor(0, equippedItem);
			}
			return true;
		}
		return false;
	}

	public boolean withdrawItemFromISidedInventorySlo(ISidedInventory inventory, int inventorySlot) {
		EntityCreature entityCreature = (EntityCreature) stateMachine.getVariable(StateMachineVariables.OWNER_ENTITY);
		if (entityCreature == null || !(entityCreature instanceof EntityCreature)) {
			return false;
		}
		
		ItemStack equippedItem = entityCreature.getCurrentItemOrArmor(0);

		if (equippedItem == null) {
			equippedItem = inventory.getStackInSlot(2);
			if (equippedItem != null && equippedItem.stackSize > 0) {
				entityCreature.setCurrentItemOrArmor(0, equippedItem);
				inventory.setInventorySlotContents(2, null);
				return true;
			}
		}
		
		return false;
	}

	/*
	private void interactWithChestForCurrentTask() {
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
	}*/
	
	public void insertEquippedItemInChest() {
		TileEntityChest chest = (TileEntityChest) stateMachine.getVariable(StateMachineVariables.CHEST);
		if (chest == null || !(chest instanceof TileEntityChest)) {
			return;
		}
		EntityCreature entityCreature = (EntityCreature) stateMachine.getVariable(StateMachineVariables.OWNER_ENTITY);
		if (entityCreature == null || !(entityCreature instanceof EntityCreature)) {
			return;
		}
		
		ItemStack equippedItem = entityCreature.getCurrentItemOrArmor(0);
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
				insertOrMergeEquippedItemWithChestStackInSlot(entityCreature, chest, equippedItem, chestItemStack, slot);
				return;
			}
		}

		if (firstEmptySlot != -1) {
			chestItemStack = equippedItem.copy();
			chestItemStack.stackSize = 1;
			insertOrMergeEquippedItemWithChestStackInSlot(entityCreature, chest, equippedItem, chestItemStack, firstEmptySlot);
		}
	}

	private void insertOrMergeEquippedItemWithChestStackInSlot(EntityCreature creature, TileEntityChest chest, ItemStack equippedItem, ItemStack chestItemStack, int slot) {
		chest.setInventorySlotContents(slot, chestItemStack);
		--equippedItem.stackSize;
		if (equippedItem.stackSize <= 0) {
			creature.setCurrentItemOrArmor(0, null);
		} else {
			creature.setCurrentItemOrArmor(0, equippedItem);
		}
	}

	public boolean withdrawItemFromChestValidForISidedInventorySlot(ISidedInventory inventory, int inventorySlot) {
		TileEntityChest chest = (TileEntityChest) stateMachine.getVariable(StateMachineVariables.CHEST);
		if (chest == null || !(chest instanceof TileEntityChest)) {
			return false;
		}
		EntityCreature entityCreature = (EntityCreature) stateMachine.getVariable(StateMachineVariables.OWNER_ENTITY);
		if (entityCreature == null || !(entityCreature instanceof EntityCreature)) {
			return false;
		}
		
		ItemStack equippedItem = entityCreature.getCurrentItemOrArmor(0);
		if (equippedItem != null) {
			return false;
		}
		ItemStack chestItemStack;

		for (int slot = 0; slot < chest.getSizeInventory(); slot++) {
			chestItemStack = chest.getStackInSlot(slot);
			if (chestItemStack != null && inventory.isItemValidForSlot(inventorySlot, chestItemStack)) {
				equippedItem = chestItemStack.copy();
				equippedItem.stackSize = 1;
				chestItemStack.stackSize--;
				if (chestItemStack.stackSize <= 0) {
					chest.setInventorySlotContents(slot, null);
				} else {
					chest.setInventorySlotContents(slot, chestItemStack);
				}
				entityCreature.setCurrentItemOrArmor(0, equippedItem);
				return true;
			}
		}
		return false;
	}
}
