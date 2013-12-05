package toldea.romecraft.entity.ai.blacksmith;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.ChunkCoordinates;
import toldea.romecraft.entity.EntityPleb;
import toldea.romecraft.entity.ai.blacksmith.BlacksmithOrders.BlacksmithOrder;
import toldea.romecraft.entity.ai.fsm.State;
import toldea.romecraft.entity.ai.fsm.StateMachine;
import toldea.romecraft.entity.ai.fsm.StateMachineVariables;
import toldea.romecraft.managers.ItemManager;
import toldea.romecraft.tileentity.TileEntityBellows;
import toldea.romecraft.tileentity.TileEntityBloomery;
import toldea.romecraft.tileentity.TileEntityRomanAnvil;

public class BlacksmithStateMachine extends StateMachine {

	@Override
	public void initialize() {
		this.setVariable(StateMachineVariables.IN_RANGE_DIST, new Double(4.0d));

		Idle.instance.linkStateMachine(this);
		WithdrawFromChest.instance.linkStateMachine(this);
		PlaceInChestAdjacentToBloomery.instance.linkStateMachine(this);
		WithdrawFromBloomery.instance.linkStateMachine(this);
		PlaceInBloomery.instance.linkStateMachine(this);
		PushBellows.instance.linkStateMachine(this);
	}

	@Override
	public State selectNextState() {
		if (lastState != null && lastState != Idle.instance) {
			return Idle.instance;
		}
		
		TileEntityBloomery bloomery = (TileEntityBloomery) this.getVariable(StateMachineVariables.BLOOMERY);
		if (bloomery == null || !(bloomery instanceof TileEntityBloomery)) {
			return null;
		}
		EntityPleb pleb = (EntityPleb) getVariable(StateMachineVariables.OWNER_ENTITY);
		if (pleb == null || !(pleb instanceof EntityPleb)) {
			return null;
		}

		ItemStack equippedItem = pleb.getCurrentItemOrArmor(0);
		
		BlacksmithOrders orders = pleb.getBlacksmithOrders();
		
		boolean hasBlacksmithOrders = orders.hasOrder();

		boolean bloomeryHasFuel = bloomery.hasFuel();
		boolean bloomeryHasIronOre = bloomery.hasIronOre();
		boolean bloomeryHasIronBloom = bloomery.hasIronBloom();

		boolean entityHoldingFuel = (equippedItem != null && equippedItem.itemID == Item.coal.itemID);
		boolean entityHoldingIronOre = (equippedItem != null && equippedItem.itemID == Block.oreIron.blockID);
		boolean entityHoldingIronBloom = (equippedItem != null && equippedItem.itemID == ItemManager.itemIronBloom.itemID);
		boolean entityHoldingSomethingElse = (!entityHoldingFuel && !entityHoldingIronOre && !entityHoldingIronBloom);
		
		// if has order
		// check if enough iron blooms are in anvil
		// if not, check if enough iron blooms are available in a chest
		// if so, fetch from chest, then place in anvil
		// once enough in anvil, hammer until complete (enough items and not finished item)
		
		if (hasBlacksmithOrders) {
			BlacksmithOrder nextOrder = orders.getNextOrder();
			TileEntityRomanAnvil anvil = (TileEntityRomanAnvil)getVariable(StateMachineVariables.ROMAN_ANVIL);
			if (anvil != null && anvil instanceof TileEntityRomanAnvil) {
				ItemStack ironBloomsInAnvilStack = anvil.getStackInSlot(0);
				if (ironBloomsInAnvilStack != null) {
					int numIronBloomsInAnvil = ironBloomsInAnvilStack.stackSize;
					int recipeIronBloomQuantity = nextOrder.getAnvilRecipe().rawIngredientQuantity;
					if (numIronBloomsInAnvil == recipeIronBloomQuantity) {
						// craft item
					} else if (numIronBloomsInAnvil < recipeIronBloomQuantity) {
						// withdraw from chest
						setVariable(StateMachineVariables.TARGET_ISIDED_INVENTORY, anvil);
						setVariable(StateMachineVariables.SLOT, new Integer(0));
						setVariable(StateMachineVariables.QUANTITY, new Integer(recipeIronBloomQuantity - numIronBloomsInAnvil));
						return WithdrawFromChest.instance;
					} else if (numIronBloomsInAnvil > recipeIronBloomQuantity) {
						// withdraw from anvil -> put in chest
					}
				}
			}
		}

		if (entityHoldingFuel) {
			if (!bloomeryHasFuel) {
				setVariable(StateMachineVariables.SLOT, new Integer(1));
				return PlaceInBloomery.instance;
			} else {
				return PlaceInChestAdjacentToBloomery.instance;
			}
		} else if (entityHoldingIronOre) {
			if (!bloomeryHasIronOre) {
				setVariable(StateMachineVariables.SLOT, new Integer(0));
				return PlaceInBloomery.instance;
			} else {
				return PlaceInChestAdjacentToBloomery.instance;
			}
		} else if (entityHoldingIronBloom) {
			return PlaceInChestAdjacentToBloomery.instance;
		} else if (bloomeryHasIronBloom) {
			setVariable(StateMachineVariables.TARGET_ISIDED_INVENTORY, bloomery);
			setVariable(StateMachineVariables.SLOT, new Integer(2));
			setVariable(StateMachineVariables.QUANTITY, new Integer(1));
			return WithdrawFromBloomery.instance;
		} else if (!bloomeryHasFuel) {
			setVariable(StateMachineVariables.SLOT, new Integer(1));
			return WithdrawFromChest.instance;
		} else if (!bloomeryHasIronOre) {
			setVariable(StateMachineVariables.SLOT, new Integer(0));
			return WithdrawFromChest.instance;
		} else if (bloomeryHasIronOre && bloomeryHasFuel && !bloomeryHasIronBloom) {
			return PushBellows.instance;
		} else {
			return null;
		}
	}

	/**********
	 * STATES *
	 **********/

	private static class Idle extends State {
		public static final Idle instance = new Idle();
		
		private static final int IDLE_TIME = 10;
		private static int timer;
		
		
		@Override
		public boolean start() {
			timer = IDLE_TIME;
			return true;
		}

		@Override
		public boolean update() {
			// If we had any previous target location, keep looking at it while idling.
			// This prevents continiously looking up and down when switching between states.
			stateMachine.commonActions.lookAtTargetLocation();
			timer -= 1;
			return (timer <= 0);
		}

		@Override
		public void finish() {
		}
		
	}
	
	private static class WithdrawFromChest extends State {
		public static final WithdrawFromChest instance = new WithdrawFromChest();

		@Override
		public boolean start() {
			TileEntityBloomery bloomery = (TileEntityBloomery) stateMachine.getVariable(StateMachineVariables.BLOOMERY);
			if (bloomery == null || !(bloomery instanceof TileEntityBloomery)) {
				return false;
			}
			Integer slot = (Integer) stateMachine.getVariable(StateMachineVariables.SLOT);
			if (slot == null || !(slot instanceof Integer)) {
				return false;
			}
			TileEntityChest chest = bloomery.getAdjacentChestWithValidContentsForBloomerySlot(slot);
			if (chest != null) {
				stateMachine.setVariable(StateMachineVariables.CHEST, chest);
				stateMachine.setVariable(StateMachineVariables.TARGET_LOCATION, new ChunkCoordinates(chest.xCoord, chest.yCoord, chest.zCoord));
				return true;
			}
			return false;
		}

		@Override
		public boolean update() {
			stateMachine.commonActions.lookAtTargetLocation();
			if (!stateMachine.commonActions.inRangeOfTargetLocation()) {
				stateMachine.commonActions.moveTowardsTargetLocation();
				return false;
			} else {
				TileEntityBloomery bloomery = (TileEntityBloomery) stateMachine.getVariable(StateMachineVariables.BLOOMERY);
				if (bloomery == null || !(bloomery instanceof TileEntityBloomery)) {
					return false;
				}
				Integer slot = (Integer) stateMachine.getVariable(StateMachineVariables.SLOT);
				if (slot == null || !(slot instanceof Integer)) {
					return false;
				}
				stateMachine.commonActions.withdrawItemFromChestValidForISidedInventorySlot(bloomery, slot);
				return true;
			}
		}

		@Override
		public void finish() {
		}
	}

	private static class PlaceInChestAdjacentToBloomery extends State {
		public static final PlaceInChestAdjacentToBloomery instance = new PlaceInChestAdjacentToBloomery();

		@Override
		public boolean start() {
			TileEntityBloomery bloomery = (TileEntityBloomery) stateMachine.getVariable(StateMachineVariables.BLOOMERY);
			if (bloomery == null || !(bloomery instanceof TileEntityBloomery)) {
				return false;
			}
			TileEntityChest chest = bloomery.getAdjacentChest();
			if (chest != null) {
				stateMachine.setVariable(StateMachineVariables.CHEST, chest);
				stateMachine.setVariable(StateMachineVariables.TARGET_LOCATION, new ChunkCoordinates(chest.xCoord, chest.yCoord, chest.zCoord));
				return true;
			}
			return false;
		}

		@Override
		public boolean update() {
			stateMachine.commonActions.lookAtTargetLocation();
			if (!stateMachine.commonActions.inRangeOfTargetLocation()) {
				stateMachine.commonActions.moveTowardsTargetLocation();
				return false;
			} else {
				stateMachine.commonActions.insertEquippedItemInChest();
				return true;
			}
		}

		@Override
		public void finish() {
		}
	}

	private static class WithdrawFromBloomery extends State {
		public static final WithdrawFromBloomery instance = new WithdrawFromBloomery();

		@Override
		public boolean start() {
			TileEntityBloomery bloomery = (TileEntityBloomery) stateMachine.getVariable(StateMachineVariables.BLOOMERY);
			if (bloomery == null || !(bloomery instanceof TileEntityBloomery)) {
				return false;
			}
			stateMachine.setVariable(StateMachineVariables.TARGET_LOCATION, new ChunkCoordinates(bloomery.xCoord, bloomery.yCoord, bloomery.zCoord));
			return true;
		}

		@Override
		public boolean update() {
			stateMachine.commonActions.lookAtTargetLocation();
			if (!stateMachine.commonActions.inRangeOfTargetLocation()) {
				stateMachine.commonActions.moveTowardsTargetLocation();
				return false;
			} else {
				TileEntityBloomery bloomery = (TileEntityBloomery) stateMachine.getVariable(StateMachineVariables.BLOOMERY);
				if (bloomery == null || !(bloomery instanceof TileEntityBloomery)) {
					return false;
				}
				Integer slot = (Integer) stateMachine.getVariable(StateMachineVariables.SLOT);
				if (slot == null || !(slot instanceof Integer)) {
					return false;
				}
				stateMachine.commonActions.withdrawItemFromISidedInventorySlo(bloomery, slot);
				return true;
			}
		}

		@Override
		public void finish() {
		}
	}

	private static class PlaceInBloomery extends State {
		public static final PlaceInBloomery instance = new PlaceInBloomery();

		@Override
		public boolean start() {
			TileEntityBloomery bloomery = (TileEntityBloomery) stateMachine.getVariable(StateMachineVariables.BLOOMERY);
			if (bloomery == null || !(bloomery instanceof TileEntityBloomery)) {
				return false;
			}
			stateMachine.setVariable(StateMachineVariables.TARGET_LOCATION, new ChunkCoordinates(bloomery.xCoord, bloomery.yCoord, bloomery.zCoord));
			return true;

		}

		@Override
		public boolean update() {
			stateMachine.commonActions.lookAtTargetLocation();
			if (!stateMachine.commonActions.inRangeOfTargetLocation()) {
				stateMachine.commonActions.moveTowardsTargetLocation();
				return false;
			} else {
				TileEntityBloomery bloomery = (TileEntityBloomery) stateMachine.getVariable(StateMachineVariables.BLOOMERY);
				if (bloomery == null || !(bloomery instanceof TileEntityBloomery)) {
					return false;
				}
				Integer slot = (Integer) stateMachine.getVariable(StateMachineVariables.SLOT);
				if (slot == null || !(slot instanceof Integer)) {
					return false;
				}
				stateMachine.commonActions.insertItemInISidedInventorySlot(bloomery, slot);
				return true;
			}
		}

		@Override
		public void finish() {
		}
	}

	private static class PushBellows extends State {
		public static final PushBellows instance = new PushBellows();
		
		private static final int TASK_DURATION = TileEntityBellows.ROTATION_TIME + 10;
		private static int taskTimer;

		@Override
		public boolean start() {
			TileEntityBellows bellows = (TileEntityBellows) stateMachine.getVariable(StateMachineVariables.BELLOWS);
			if (bellows == null || !(bellows instanceof TileEntityBellows)) {
				return false;
			}
			stateMachine.setVariable(StateMachineVariables.TARGET_LOCATION, new ChunkCoordinates(bellows.xCoord, bellows.yCoord, bellows.zCoord));
			taskTimer = 0;
			return true;
		}

		@Override
		public boolean update() {
			stateMachine.commonActions.lookAtTargetLocation();
			if (!stateMachine.commonActions.inRangeOfTargetLocation()) {
				stateMachine.commonActions.moveTowardsTargetLocation();
				return false;
			} else {
				if (taskTimer == 0) {
					TileEntityBellows bellows = (TileEntityBellows) stateMachine.getVariable(StateMachineVariables.BELLOWS);
					if (bellows == null || !(bellows instanceof TileEntityBellows)) {
						return false;
					}
					bellows.pushBellows();
					taskTimer = 1;
					return false;
				} else {
					taskTimer++;
					return taskTimer >= TASK_DURATION;
				}
			}
		}

		@Override
		public void finish() {
		}
	}
}