package toldea.romecraft.entity.ai;

import java.util.List;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.item.ItemStack;
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

	private final EntityPleb entityPleb;

	private BlacksmithTask currentTask = BlacksmithTask.NONE;
	private BlacksmithTargetLocation currentTargetLocation = BlacksmithTargetLocation.NONE;
	private BlacksmithTargetLocation currentNavigationTarget = BlacksmithTargetLocation.NONE;

	private TileEntityBloomery bloomery = null;
	private TileEntityBellows bellows = null;
	private TileEntityChest chest = null;

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
		// Make sure the pleb is still a blacksmith, it is still day and it is still not raining.
		if (entityPleb.getProfession() != 1 || !entityPleb.worldObj.isDaytime() || entityPleb.worldObj.isRaining()) {
			return false;
		}
		// Make sure the bloomery still is valid and has work to do, else stop this behavior.
		if (!bloomery.getIsValid() || !bloomery.hasWork()) {
			return false;
		}

		// Check if we still have a valid task to do. If not get a new one. If that one also is invalid, return false.
		if (currentTask == BlacksmithTask.NONE) {
			selectNextBlacksmithingTask();
			if (currentTask == BlacksmithTask.NONE) {
				return false;
			}
			startBlacksmithingTask();
		}

		updateBlacksmithingTask();

		return true;
	}

	public void resetTask() {
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
			// Move to bloomery
			// Get iron bloom from bloomery
			// Move to chest
			// Put iron bloom in chest
			currentTask = BlacksmithTask.STORE_IRON_BLOOM;
		} else if (!hasIronOre) {
			// Move to chest
			// Get iron from chest
			// Move to bloomery
			// Put iron in bloomery
			currentTask = BlacksmithTask.GET_IRON_ORE;
		} else if (!hasFuel) {
			// Move to chest
			// Get fuel from chest
			// Move to bloomery
			// Put fuel in bloomery
			currentTask = BlacksmithTask.GET_FUEL;
		} else if (hasFuel && hasIronOre) {
			// While smelting:
			// (Move to bellows)
			// Push bellows
			currentTask = BlacksmithTask.PUSH_BELLOWS;
		} else {
			currentTask = BlacksmithTask.NONE;
		}
	}

	private boolean startBlacksmithingTask() {
		switch (currentTask) {
		case STORE_IRON_BLOOM:
			// Move to bloomery
			// Get iron bloom from bloomery
			// Move to chest
			// Put iron bloom in chest

			currentTargetLocation = BlacksmithTargetLocation.BLOOMERY;

			break;
		case GET_IRON_ORE:
			break;
		case GET_FUEL:
			break;
		case PUSH_BELLOWS:
			break;
		default:
			break;
		}

		return false; // TODO: This probably gets removed when all logic is done.
	}

	private void updateBlacksmithingTask() {
		switch (currentTask) {
		case STORE_IRON_BLOOM:
			// Move to bloomery
			// Get iron bloom from bloomery
			// Move to chest
			// Put iron bloom in chest

			moveTowardsBlacksmithTargetLocation();
			if (entityPleb.getNavigator().getPath().isFinished()) {
				currentNavigationTarget = BlacksmithTargetLocation.NONE;

				if (inRangeOfTargetLocation()) {
					if (currentTargetLocation == BlacksmithTargetLocation.BLOOMERY) {
						if (interactWithBloomerySlot(2)) {
							chest = bloomery.getAdjacentChestWithValidContentsForBloomerySlot(2);
							if (chest != null) {
								if (interactWithBloomerySlot(2)) {
									currentTargetLocation = BlacksmithTargetLocation.CHEST;
								}
							}
						}
					} else if (currentTargetLocation == BlacksmithTargetLocation.CHEST) {
						insertEquippedItemInChest();
						currentTargetLocation = BlacksmithTargetLocation.NONE;
						currentTask = BlacksmithTask.NONE;
						return;
					}
				}
			}

			break;
		case GET_IRON_ORE:
			break;
		case GET_FUEL:
			break;
		case PUSH_BELLOWS:
			break;
		default:
			break;
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
		if (dist > 4.0d) {
			if (dist > 256.0d) {
				Vec3 vec3 = RandomPositionGenerator.findRandomTargetBlockTowards(this.entityPleb, 14, 3, this.entityPleb.worldObj.getWorldVec3Pool()
						.getVecFromPool((double) targetEntity.xCoord + .5d, (double) targetEntity.yCoord + .5d, (double) targetEntity.zCoord + .5d));
				if (vec3 != null) {
					this.entityPleb.getNavigator().tryMoveToXYZ(vec3.xCoord, vec3.yCoord, vec3.zCoord, 1.0D);
					currentNavigationTarget = currentTargetLocation;
					return;
				}
			} else {
				this.entityPleb.getNavigator().tryMoveToXYZ((double) targetEntity.xCoord + .5d, (double) targetEntity.yCoord + .5d,
						(double) targetEntity.zCoord + .5d, 1.0D);
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
		return (dist <= 4.0d);
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
			entityPleb.setCurrentItemOrArmor(slot, equippedItem);
			return true;
		}
		return false;
	}

	private boolean insertEquippedItemInChest() {
		ItemStack equippedItem = entityPleb.getCurrentItemOrArmor(0);
		ItemStack chestItemStack;

		for (int slot = 0; slot < chest.getSizeInventory(); slot++) {
			chestItemStack = chest.getStackInSlot(slot);
			if (chestItemStack == null) {
				chestItemStack = equippedItem.copy();
				chestItemStack.stackSize = 1;
			} else if (chestItemStack.itemID == equippedItem.itemID && chestItemStack.stackSize < chest.getInventoryStackLimit()) {
				chestItemStack.stackSize++;
			} else {
				continue;
			}
			chest.setInventorySlotContents(slot, chestItemStack);
			--equippedItem.stackSize;
			entityPleb.setCurrentItemOrArmor(slot, equippedItem);
			return true;
		}
		return false;
	}
}
