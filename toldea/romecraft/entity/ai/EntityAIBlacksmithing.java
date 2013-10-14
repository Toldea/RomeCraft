package toldea.romecraft.entity.ai;

import java.util.List;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import toldea.romecraft.entity.EntityPleb;
import toldea.romecraft.managers.TickManager;
import toldea.romecraft.romanvillage.RomanVillage;
import toldea.romecraft.romanvillage.RomanVillageBloomeryInfo;
import toldea.romecraft.tileentity.TileEntityBellows;
import toldea.romecraft.tileentity.TileEntityBloomery;

public class EntityAIBlacksmithing extends EntityAIBase {
	private enum BlacksmithTask {
		NONE, GET_FUEL, GET_IRON_ORE, STORE_IRON_BLOOM, PUSH_BELLOWS
	}

	private enum BlacksmithTargetLocation {
		NONE, BLOOMERY, CHEST, BELLOWS
	}

	private static final int IDLE_DELAY = 10;
	private static final int CHEST_CLOSE_DELAY = 10;

	private static final double IN_RANGE = 4.0d;

	private final EntityPleb entityPleb;

	private BlacksmithTask currentTask = BlacksmithTask.NONE;
	private BlacksmithTargetLocation currentTargetLocation = BlacksmithTargetLocation.NONE;
	private BlacksmithTargetLocation currentNavigationTarget = BlacksmithTargetLocation.NONE;

	private TileEntityBloomery bloomery = null;
	private TileEntityBellows bellows = null;
	private TileEntityChest chest = null;

	private int idleTimer = 0;
	private int chestOpenTimer = 0;

	public EntityAIBlacksmithing(EntityPleb entityPleb) {
		this.entityPleb = entityPleb;
		this.setMutexBits(1);
	}

	@Override
	public boolean shouldExecute() {
		// Make sure the pleb is a blacksmith, it is day and it is not raining. Return false if any of these fail.
		if (entityPleb.getProfession() != 1 || !entityPleb.worldObj.isDaytime() || entityPleb.worldObj.isRaining()) {
			return false;
		}

		// Find a nearby roman village. If we can't find one return false.
		RomanVillage village = TickManager.romanVillageCollection.findNearestVillage(MathHelper.floor_double(this.entityPleb.posX),
				MathHelper.floor_double(this.entityPleb.posY), MathHelper.floor_double(this.entityPleb.posZ), 14);
		if (village == null) {
			System.out.println("EntityAIBlacksmithing.shouldExecute - Couldn't find village!");
			return false;
		}

		// Make sure the village has at least one bloomery.
		int numBloomeries = village.getNumBloomeries();
		if (numBloomeries <= 0) {
			System.out.println("EntityAIBlacksmithing.shouldExecute - No bloomery in village!");
			return false;
		}

		// TODO: Maybe sort this by distance.

		// Get all the bloomeries in the village and loop over them.
		List bloomeryInfoList = village.getBloomeryInfoList();
		for (int i = 0; i < numBloomeries; i++) {
			RomanVillageBloomeryInfo bloomeryInfo = (RomanVillageBloomeryInfo) bloomeryInfoList.get(i);
			if (bloomeryInfo != null) {
				// Get the bloomery tile entity and check that it is valid.
				TileEntity tileEntity = entityPleb.worldObj.getBlockTileEntity(bloomeryInfo.posX, bloomeryInfo.posY, bloomeryInfo.posZ);
				if (tileEntity != null && tileEntity instanceof TileEntityBloomery) {
					TileEntityBloomery bloomery = (TileEntityBloomery) tileEntity;

					// Make sure it is valid and the master block and it has work to do.
					if (!bloomery.getIsValid() || !bloomery.getIsMaster() || !bloomery.hasWork()) {
						System.out.println("Invalid bloomery, not master, or no work to be done!");
						continue;
					}

					// TODO: Add logic so bloomeries get 'reserved' when a blacksmith starts to use it, as to prevent multiple plebs from using the same one.

					// Check if it has a valid bellows adjacent to it.
					boolean hasValidBellows = false;
					for (int d = 0; d < 4; d++) {
						TileEntityBellows bellows = bloomery.getAdjacentBellowsForDirection(d);
						if (bellows != null) {
							hasValidBellows = true;
							this.bellows = bellows;
							break;
						}
					}
					if (!hasValidBellows) {
						System.out.println("No valid bellows");
						continue;
					}

					this.bloomery = bloomery;
					System.out.println("Systems go! Should execute the blacksmithing behavior!");
					return true;
				}
			}
		}
		return false;
	}

	// public void startExecuting() {}

	public boolean continueExecuting() {
		if (chestOpenTimer > 0) {
			chestOpenTimer--;
			if (chestOpenTimer == 0) {
				if (chest != null) {
					interactWithChestForCurrentTask();
					chest.closeChest();
				}
			}
		}
		if (idleTimer > 0) {
			idleTimer--;
			return true;
		}
		// Make sure the pleb is still a blacksmith, it is still day and it is still not raining.
		if (entityPleb.getProfession() != 1 || !entityPleb.worldObj.isDaytime() || entityPleb.worldObj.isRaining()) {
			System.out.println("Stopping blacksmthing behavior, either lost profession or it is raining or it is not day!");
			return false;
		}
		// Make sure the bloomery still is valid and has work to do or the pleb is carrying some item, else stop this behavior.
		if (!bloomery.getIsValid() || (this.entityPleb.getCurrentItemOrArmor(0) == null && !bloomery.hasWork())) {
			System.out
					.println("Stopping blacksmithing behavior, either bloomery became invalid or bloomery has no more work to do and we aren't carrying some kind of item!");
			return false;
		}

		// Check if we still have a valid task to do. If not get a new one. If that one also is invalid, return false.
		if (currentTask == BlacksmithTask.NONE) {
			selectNextBlacksmithingTask();
			if (currentTask == BlacksmithTask.NONE) {
				System.out.println("Stopping blacksmithing behavior, selectNextBlacksmithTask selected BlacksmithTask.NONE!");
				return false;
			}
			startBlacksmithingTask();
		}

		updateBlacksmithingTask();

		return true;
	}

	public void resetTask() {
		System.out.println("Resetting Blacksmithing task!");
		this.bloomery = null;
		this.bellows = null;
		this.chest = null;

		currentTask = BlacksmithTask.NONE;
		currentTargetLocation = BlacksmithTargetLocation.NONE;
		currentNavigationTarget = BlacksmithTargetLocation.NONE;
	}

	private void selectNextBlacksmithingTask() {
		boolean hasFuel = bloomery.hasFuel();
		boolean hasIronOre = bloomery.hasIronOre();
		boolean hasIronBloom = bloomery.hasIronBloom();

		if (hasIronBloom) {
			currentTask = BlacksmithTask.STORE_IRON_BLOOM;
		} else if (!hasIronOre) {
			currentTask = BlacksmithTask.GET_IRON_ORE;
		} else if (!hasFuel) {
			currentTask = BlacksmithTask.GET_FUEL;
		} else if (hasFuel && hasIronOre) {
			currentTask = BlacksmithTask.PUSH_BELLOWS;
		} else {
			currentTask = BlacksmithTask.NONE;
		}

		System.out.println("Selecting next task: " + currentTask);
	}

	private void startBlacksmithingTask() {
		switch (currentTask) {
		case STORE_IRON_BLOOM:
			currentTargetLocation = BlacksmithTargetLocation.BLOOMERY;
			break;
		case GET_IRON_ORE:
		case GET_FUEL:
			chest = bloomery.getAdjacentChestWithValidContentsForBloomerySlot(currentTask == BlacksmithTask.GET_IRON_ORE ? 0 : 1);
			if (chest != null) {
				currentTargetLocation = BlacksmithTargetLocation.CHEST;
			} else {
				currentTask = BlacksmithTask.NONE;
			}
			break;
		case PUSH_BELLOWS:
			currentTargetLocation = BlacksmithTargetLocation.BELLOWS;
			break;
		default:
			break;
		}
	}

	private void updateBlacksmithingTask() {
		if (currentTask == BlacksmithTask.NONE) {
			return;
		}
		moveTowardsBlacksmithTargetLocation();
		PathEntity path = entityPleb.getNavigator().getPath();
		if (path != null && path.isFinished()) {
			currentNavigationTarget = BlacksmithTargetLocation.NONE;
		}
		if (inRangeOfTargetLocation()) {
			switch (currentTask) {
			case STORE_IRON_BLOOM:
				if (currentTargetLocation == BlacksmithTargetLocation.BLOOMERY) {
					if (interactWithBloomerySlot(2)) {
						chest = bloomery.getAdjacentChestWithValidContentsForBloomerySlot(2);
						if (chest != null) {
							currentTargetLocation = BlacksmithTargetLocation.CHEST;
							idleTimer = IDLE_DELAY;
							return;
						}
					}
				} else if (currentTargetLocation == BlacksmithTargetLocation.CHEST) {
					chest.openChest();
					chestOpenTimer = CHEST_CLOSE_DELAY;
					idleTimer = CHEST_CLOSE_DELAY + 1;
					return;
				}
				return;
			case GET_IRON_ORE:
			case GET_FUEL:
				if (currentTargetLocation == BlacksmithTargetLocation.CHEST) {
					chest.openChest();
					chestOpenTimer = CHEST_CLOSE_DELAY;
					idleTimer = CHEST_CLOSE_DELAY + 1;
					return;
				} else if (currentTargetLocation == BlacksmithTargetLocation.BLOOMERY) {
					if (interactWithBloomerySlot(currentTask == BlacksmithTask.GET_IRON_ORE ? 0 : 1)) {
						idleTimer = IDLE_DELAY;
						currentTargetLocation = BlacksmithTargetLocation.NONE;
						currentTask = BlacksmithTask.NONE;
						return;
					}
				}
				return;
			case PUSH_BELLOWS:
				if (!bloomery.hasIronOre()) {
					currentTask = BlacksmithTask.NONE;
					return;
				} else {
					bellows.pushBellows();
					idleTimer = TileEntityBellows.ROTATION_TIME + 10;
					return;
				}
			default:
				break;
			}
		}
	}

	private void moveTowardsBlacksmithTargetLocation() {
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
					// this.entityPleb.getNavigator().tryMoveToXYZ(vec3.xCoord, vec3.yCoord, vec3.zCoord, 1.0D);
					this.entityPleb.getNavigator().tryMoveToXYZ(vec3.xCoord, vec3.yCoord, vec3.zCoord, .6D); // TODO: Check if this causes the glitchy movement
																												// if set to 1.0?
					currentNavigationTarget = currentTargetLocation;
					return;
				}
			} else {
				// this.entityPleb.getNavigator().tryMoveToXYZ((double) targetEntity.xCoord + .5d, (double) targetEntity.yCoord + .5d, (double)
				// targetEntity.zCoord + .5d, 1.0D);
				this.entityPleb.getNavigator().tryMoveToXYZ((double) targetEntity.xCoord + .5d, (double) targetEntity.yCoord + .5d,
						(double) targetEntity.zCoord + .5d, .6D); // TODO: Check if this causes the glitchy movement if set to 1.0?
				currentNavigationTarget = currentTargetLocation;
				return;
			}
		}
		return;
	}

	private boolean inRangeOfTargetLocation() {
		TileEntity targetEntity = getTileEntityForTargetLocation();
		if (targetEntity == null) {
			return false;
		}
		double dist = entityPleb.getDistanceSq(targetEntity.xCoord, targetEntity.yCoord, targetEntity.zCoord);
		return (dist <= IN_RANGE);
	}

	private TileEntity getTileEntityForTargetLocation() {
		TileEntity targetEntity = null;
		switch (currentTargetLocation) {
		case BLOOMERY:
			targetEntity = bloomery;
			break;
		case CHEST:
			targetEntity = chest;
			break;
		case BELLOWS:
			targetEntity = bellows;
		default:
			break;
		}
		return targetEntity;
	}

	private boolean interactWithBloomerySlot(int slot) {
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
			}
			return false;
		default:
			return false;
		}

	}

	private boolean insertItemInBloomerySlot(int slot, ItemStack equippedItem) {
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
	}

	private void interactWithChestForCurrentTask() {
		switch (currentTask) {
		case STORE_IRON_BLOOM:
			insertEquippedItemInChest();
			currentTargetLocation = BlacksmithTargetLocation.NONE;
			currentTask = BlacksmithTask.NONE;
			break;
		case GET_IRON_ORE:
		case GET_FUEL:
			if (withdrawItemFromChestValidForBloomerySlot(currentTask == BlacksmithTask.GET_IRON_ORE ? 0 : 1)) {
				currentTargetLocation = BlacksmithTargetLocation.BLOOMERY;
				idleTimer = IDLE_DELAY;
			} else {
				currentTargetLocation = BlacksmithTargetLocation.NONE;
				currentTask = BlacksmithTask.NONE;
			}
			break;
		default:
			break;
		}
	}

	private void insertEquippedItemInChest() {
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
			System.out.println("Ikenai! " + firstEmptySlot);
			chestItemStack = equippedItem.copy();
			chestItemStack.stackSize = 1;
			insertOrMergeEquippedItemWithChestStackInSlot(equippedItem, chestItemStack, firstEmptySlot);
		}
	}

	private void insertOrMergeEquippedItemWithChestStackInSlot(ItemStack equippedItem, ItemStack chestItemStack, int slot) {
		chest.setInventorySlotContents(slot, chestItemStack);
		--equippedItem.stackSize;
		if (equippedItem.stackSize <= 0) {
			entityPleb.setCurrentItemOrArmor(0, null);
		} else {
			entityPleb.setCurrentItemOrArmor(0, equippedItem);
		}
	}

	private boolean withdrawItemFromChestValidForBloomerySlot(int bloomerySlot) {
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
	}
}
