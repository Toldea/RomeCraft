package toldea.romecraft.entity.ai;

import java.util.List;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.MathHelper;
import toldea.romecraft.entity.EntityPleb;
import toldea.romecraft.entity.EntityPleb.PLEB_PROFESSION;
import toldea.romecraft.entity.ai.fsm.BlacksmithStateMachine;
import toldea.romecraft.entity.ai.fsm.StateMachineVariables;
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

	private TileEntityChest chest = null;

	private int idleTimer = 0;
	private int chestOpenTimer = 0;
	
	//
	private final BlacksmithStateMachine blacksmithStateMachine;
	//

	public EntityAIBlacksmithing(EntityPleb entityPleb) {
		this.entityPleb = entityPleb;
		this.setMutexBits(1);
		blacksmithStateMachine = new BlacksmithStateMachine();
	}

	@Override
	public boolean shouldExecute() {
		// Make sure the pleb is a blacksmith, it is day and it is not raining. Return false if any of these fail.
		if (entityPleb.getProfession() != PLEB_PROFESSION.BLACKSMITH || !entityPleb.worldObj.isDaytime() || entityPleb.worldObj.isRaining()) {
			return false;
		}

		// Find a nearby roman village. If we can't find one return false.
		RomanVillage village = TickManager.romanVillageCollection.findNearestVillage(MathHelper.floor_double(this.entityPleb.posX),
				MathHelper.floor_double(this.entityPleb.posY), MathHelper.floor_double(this.entityPleb.posZ), 14);
		if (village == null) {
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
					TileEntityBellows bellows = null;
					for (int d = 0; d < 4; d++) {
						bellows = bloomery.getAdjacentBellowsForDirection(d);
						if (bellows != null) {
							hasValidBellows = true;
							break;
						}
					}
					if (!hasValidBellows) {
						continue;
					}
					
					blacksmithStateMachine.initialize();
					
					blacksmithStateMachine.setVariable(StateMachineVariables.OWNER_ENTITY, entityPleb);
					blacksmithStateMachine.setVariable(StateMachineVariables.BLOOMERY, bloomery);
					blacksmithStateMachine.setVariable(StateMachineVariables.BELLOWS, bellows);
					
					return true;
				}
			}
		}
		return false;
	}

	// public void startExecuting() {}

	public boolean continueExecuting() {
		return blacksmithStateMachine.update();
		/*
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
		if (entityPleb.getProfession() != PLEB_PROFESSION.BLACKSMITH || !entityPleb.worldObj.isDaytime() || entityPleb.worldObj.isRaining()) {
			return false;
		}
		// Make sure the bloomery still is valid and has work to do or the pleb is carrying some item, else stop this behavior.
		if (!bloomery.getIsValid() || (this.entityPleb.getCurrentItemOrArmor(0) == null && !bloomery.hasWork())) {
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
		*/
	}
	/*
	public void updateTask() {
		if (this.currentTargetLocation != BlacksmithTargetLocation.NONE) {
			TileEntity tileEntity = getTileEntityForTargetLocation();
			if (tileEntity != null) {
				this.entityPleb.getLookHelper().setLookPosition(tileEntity.xCoord, tileEntity.yCoord + .5d, tileEntity.zCoord, 10.0F,
						(float) this.entityPleb.getVerticalFaceSpeed());
			}
		}
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
				if (!bloomery.hasIronOre() || !bloomery.hasFuel()) {
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
	*/
}
