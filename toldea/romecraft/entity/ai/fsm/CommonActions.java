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
				Vec3 vec3 = RandomPositionGenerator.findRandomTargetBlockTowards(
						ownerEntity,
						14,
						3,
						ownerEntity.worldObj.getWorldVec3Pool().getVecFromPool((double) targetLocation.posX + .5d, (double) targetLocation.posY + .5d,
								(double) targetLocation.posZ + .5d));
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

	public void lookAtTargetLocation() {
		ChunkCoordinates targetLocation = (ChunkCoordinates) stateMachine.getVariable(StateMachineVariables.TARGET_LOCATION);
		if (targetLocation == null || !(targetLocation instanceof ChunkCoordinates)) {
			return;
		}
		EntityCreature ownerEntity = (EntityCreature) stateMachine.getVariable(StateMachineVariables.OWNER_ENTITY);
		if (ownerEntity == null || !(ownerEntity instanceof Entity)) {
			return;
		}
		ownerEntity.getLookHelper().setLookPosition(targetLocation.posX, targetLocation.posY + .5d, targetLocation.posZ, 10.0F,
				(float) ownerEntity.getVerticalFaceSpeed());
	}

	public boolean insertItemQuantityInISidedInventorySlot(int quantity, ISidedInventory inventory, int inventorySlot) {
		EntityCreature entityCreature = (EntityCreature) stateMachine.getVariable(StateMachineVariables.OWNER_ENTITY);
		if (entityCreature == null || !(entityCreature instanceof EntityCreature)) {
			return false;
		}

		ItemStack equippedItem = entityCreature.getCurrentItemOrArmor(0);
		if (equippedItem.stackSize < quantity) {
			return false;
		}

		if (inventory.isItemValidForSlot(inventorySlot, equippedItem)) {
			ItemStack inventoryItemStack = inventory.getStackInSlot(inventorySlot);
			if (inventoryItemStack == null) {
				inventoryItemStack = equippedItem.copy();
				inventoryItemStack.stackSize = quantity;
				equippedItem.stackSize -= quantity;
			} else {
				if (inventoryItemStack.stackSize + quantity <= inventory.getInventoryStackLimit()) {
					inventoryItemStack.stackSize += quantity;
					equippedItem.stackSize -= quantity;
				} else {
					return false;
				}
			}
			inventory.setInventorySlotContents(inventorySlot, inventoryItemStack);
			if (equippedItem.stackSize <= 0) {
				entityCreature.setCurrentItemOrArmor(0, null);
			} else {
				entityCreature.setCurrentItemOrArmor(0, equippedItem);
			}
			return true;
		}
		return false;
	}

	public boolean withdrawItemQuantityFromISidedInventorySlot(int quantity, ISidedInventory inventory, int inventorySlot) {
		EntityCreature entityCreature = (EntityCreature) stateMachine.getVariable(StateMachineVariables.OWNER_ENTITY);
		if (entityCreature == null || !(entityCreature instanceof EntityCreature)) {
			return false;
		}

		ItemStack equippedItem = entityCreature.getCurrentItemOrArmor(0);

		if (equippedItem == null) {
			ItemStack inventoryStack = inventory.getStackInSlot(inventorySlot);
			if (inventoryStack.stackSize >= quantity) {
				equippedItem = inventoryStack.copy();
				equippedItem.stackSize = quantity;
				inventoryStack.stackSize -= quantity;
				if (inventoryStack.stackSize <= 0) {
					inventory.setInventorySlotContents(inventorySlot, null);
				} else {
					inventory.setInventorySlotContents(inventorySlot, inventoryStack);
				}
				entityCreature.setCurrentItemOrArmor(0, equippedItem);
				return true;
			}
		}

		return false;
	}

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

	private void insertOrMergeEquippedItemWithChestStackInSlot(EntityCreature creature, TileEntityChest chest, ItemStack equippedItem,
			ItemStack chestItemStack, int slot) {
		chest.setInventorySlotContents(slot, chestItemStack);
		--equippedItem.stackSize;
		if (equippedItem.stackSize <= 0) {
			creature.setCurrentItemOrArmor(0, null);
		} else {
			creature.setCurrentItemOrArmor(0, equippedItem);
		}
	}

	public boolean withdrawItemWithQuantityFromChestValidForISidedInventorySlot(int quantity, ISidedInventory inventory, int inventorySlot) {
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
				if (chestItemStack.stackSize >= quantity) {
					equippedItem = chestItemStack.copy();
					equippedItem.stackSize = quantity;
					chestItemStack.stackSize -= quantity;
					if (chestItemStack.stackSize <= 0) {
						chest.setInventorySlotContents(slot, null);
					} else {
						chest.setInventorySlotContents(slot, chestItemStack);
					}
					entityCreature.setCurrentItemOrArmor(0, equippedItem);
					return true;
				}
			}
		}
		return false;
	}
}
