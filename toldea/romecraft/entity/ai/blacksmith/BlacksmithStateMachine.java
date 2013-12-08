package toldea.romecraft.entity.ai.blacksmith;

import net.minecraft.block.Block;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.ChunkCoordinates;
import toldea.romecraft.entity.EntityPleb;
import toldea.romecraft.entity.ai.blacksmith.BlacksmithOrders.BlacksmithOrder;
import toldea.romecraft.entity.ai.fsm.State;
import toldea.romecraft.entity.ai.fsm.StateMachine;
import toldea.romecraft.entity.ai.fsm.StateMachineVariables;
import toldea.romecraft.managers.ItemManager;
import toldea.romecraft.managers.PacketManager;
import toldea.romecraft.tileentity.TileEntityBellows;
import toldea.romecraft.tileentity.TileEntityBloomery;
import toldea.romecraft.tileentity.TileEntityRomanAnvil;

public class BlacksmithStateMachine extends StateMachine {

	@Override
	public void initialize() {
		this.setVariable(StateMachineVariables.IN_RANGE_DIST, new Double(4.0d));

		Idle.instance.linkStateMachine(this);
		WithdrawFromChestAdjacentToBloomery.instance.linkStateMachine(this);
		PlaceInChestAdjacentToBloomery.instance.linkStateMachine(this);
		WithdrawFromISidedInventory.instance.linkStateMachine(this);
		PlaceInISidedInventory.instance.linkStateMachine(this);
		PushBellows.instance.linkStateMachine(this);
		HammerAnvil.instance.linkStateMachine(this);
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
		boolean entityHoldingSomethingElse = (!entityHoldingFuel && !entityHoldingIronOre && !entityHoldingIronBloom && equippedItem != null);
		
		if (entityHoldingSomethingElse) {
			return PlaceInChestAdjacentToBloomery.instance;
		}
		
		TileEntityRomanAnvil anvil = (TileEntityRomanAnvil)getVariable(StateMachineVariables.ROMAN_ANVIL);
		if (anvil != null && anvil instanceof TileEntityRomanAnvil) {
			if (anvil.hasFinishedItem()) {
				setVariable(StateMachineVariables.TARGET_TILE_ENTITY, anvil);
				setVariable(StateMachineVariables.TARGET_ISIDED_INVENTORY, anvil);
				setVariable(StateMachineVariables.SLOT, new Integer(1));
				setVariable(StateMachineVariables.QUANTITY, new Integer(1));
				return WithdrawFromISidedInventory.instance;
			}
		}
		
		if (hasBlacksmithOrders) {
			BlacksmithOrder nextOrder = orders.getNextOrder();
			if (anvil != null && anvil instanceof TileEntityRomanAnvil) {
				ItemStack ironBloomsInAnvilStack = anvil.getStackInSlot(0);
				int numIronBloomsInAnvil = 0;
				if (ironBloomsInAnvilStack != null) {
					numIronBloomsInAnvil = ironBloomsInAnvilStack.stackSize;
				}
				int recipeIronBloomQuantity = nextOrder.getAnvilRecipe().rawIngredientQuantity;
				if (numIronBloomsInAnvil == recipeIronBloomQuantity) {
					return HammerAnvil.instance;
				} else if (numIronBloomsInAnvil < recipeIronBloomQuantity) {
					if (entityHoldingIronBloom) {
						setVariable(StateMachineVariables.TARGET_TILE_ENTITY, anvil);
						setVariable(StateMachineVariables.TARGET_ISIDED_INVENTORY, anvil);
						setVariable(StateMachineVariables.SLOT, new Integer(0));
						setVariable(StateMachineVariables.QUANTITY, new Integer(1));
						return PlaceInISidedInventory.instance;
					} else {
						setVariable(StateMachineVariables.TARGET_ISIDED_INVENTORY, anvil);
						setVariable(StateMachineVariables.SLOT, new Integer(0));
						setVariable(StateMachineVariables.QUANTITY, new Integer(recipeIronBloomQuantity - numIronBloomsInAnvil));
						return WithdrawFromChestAdjacentToBloomery.instance;
					}
				} else if (numIronBloomsInAnvil > recipeIronBloomQuantity) {
					setVariable(StateMachineVariables.TARGET_TILE_ENTITY, anvil);
					setVariable(StateMachineVariables.TARGET_ISIDED_INVENTORY, anvil);
					setVariable(StateMachineVariables.SLOT, new Integer(1));
					setVariable(StateMachineVariables.QUANTITY, new Integer(1));
					return WithdrawFromISidedInventory.instance;
				}
			}
		}

		if (entityHoldingFuel) {
			if (!bloomeryHasFuel) {
				setVariable(StateMachineVariables.TARGET_TILE_ENTITY, bloomery);
				setVariable(StateMachineVariables.TARGET_ISIDED_INVENTORY, bloomery);
				setVariable(StateMachineVariables.SLOT, new Integer(1));
				setVariable(StateMachineVariables.QUANTITY, new Integer(1));
				return PlaceInISidedInventory.instance;
			} else {
				return PlaceInChestAdjacentToBloomery.instance;
			}
		} else if (entityHoldingIronOre) {
			if (!bloomeryHasIronOre) {
				setVariable(StateMachineVariables.TARGET_TILE_ENTITY, bloomery);
				setVariable(StateMachineVariables.TARGET_ISIDED_INVENTORY, bloomery);
				setVariable(StateMachineVariables.SLOT, new Integer(0));
				setVariable(StateMachineVariables.QUANTITY, new Integer(1));
				return PlaceInISidedInventory.instance;
			} else {
				return PlaceInChestAdjacentToBloomery.instance;
			}
		} else if (entityHoldingIronBloom) {
			return PlaceInChestAdjacentToBloomery.instance;
		} else if (bloomeryHasIronBloom) {
			setVariable(StateMachineVariables.TARGET_TILE_ENTITY, bloomery);
			setVariable(StateMachineVariables.TARGET_ISIDED_INVENTORY, bloomery);
			setVariable(StateMachineVariables.SLOT, new Integer(2));
			setVariable(StateMachineVariables.QUANTITY, new Integer(1));
			return WithdrawFromISidedInventory.instance;
		} else if (!bloomeryHasFuel) {
			setVariable(StateMachineVariables.SLOT, new Integer(1));
			setVariable(StateMachineVariables.QUANTITY, new Integer(1));
			setVariable(StateMachineVariables.TARGET_ISIDED_INVENTORY, bloomery);
			return WithdrawFromChestAdjacentToBloomery.instance;
		} else if (!bloomeryHasIronOre) {
			setVariable(StateMachineVariables.SLOT, new Integer(0));
			setVariable(StateMachineVariables.QUANTITY, new Integer(1));
			setVariable(StateMachineVariables.TARGET_ISIDED_INVENTORY, bloomery);
			return WithdrawFromChestAdjacentToBloomery.instance;
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
	
	private static class WithdrawFromChestAdjacentToBloomery extends State {
		public static final WithdrawFromChestAdjacentToBloomery instance = new WithdrawFromChestAdjacentToBloomery();

		@Override
		public boolean start() {
			TileEntityBloomery bloomery = (TileEntityBloomery) stateMachine.getVariable(StateMachineVariables.BLOOMERY);
			if (bloomery == null || !(bloomery instanceof TileEntityBloomery)) {
				return false;
			}
			ISidedInventory targetInventory = (ISidedInventory) stateMachine.getVariable(StateMachineVariables.TARGET_ISIDED_INVENTORY);
			if (targetInventory == null || !(targetInventory instanceof ISidedInventory)) {
				return false;
			}
			Integer slot = (Integer) stateMachine.getVariable(StateMachineVariables.SLOT);
			if (slot == null || !(slot instanceof Integer)) {
				return false;
			}
			TileEntityChest chest = bloomery.getAdjacentChestWithValidResourcesForISidedInventorySlot(targetInventory, slot);
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
				ISidedInventory targetInventory = (ISidedInventory) stateMachine.getVariable(StateMachineVariables.TARGET_ISIDED_INVENTORY);
				if (targetInventory == null || !(targetInventory instanceof ISidedInventory)) {
					return false;
				}
				Integer slot = (Integer) stateMachine.getVariable(StateMachineVariables.SLOT);
				if (slot == null || !(slot instanceof Integer)) {
					return false;
				}
				Integer quantity = (Integer) stateMachine.getVariable(StateMachineVariables.QUANTITY);
				if (slot == null || !(slot instanceof Integer)) {
					return false;
				}
				stateMachine.commonActions.withdrawItemWithQuantityFromChestValidForISidedInventorySlot(quantity, targetInventory, slot);
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

	private static class WithdrawFromISidedInventory extends State {
		public static final WithdrawFromISidedInventory instance = new WithdrawFromISidedInventory();

		@Override
		public boolean start() {
			TileEntity targetTileEntity = (TileEntity) stateMachine.getVariable(StateMachineVariables.TARGET_TILE_ENTITY);
			if (targetTileEntity == null || !(targetTileEntity instanceof TileEntity)) {
				return false;
			}
			stateMachine.setVariable(StateMachineVariables.TARGET_LOCATION, new ChunkCoordinates(targetTileEntity.xCoord, targetTileEntity.yCoord, targetTileEntity.zCoord));
			return true;
		}

		@Override
		public boolean update() {
			stateMachine.commonActions.lookAtTargetLocation();
			if (!stateMachine.commonActions.inRangeOfTargetLocation()) {
				stateMachine.commonActions.moveTowardsTargetLocation();
				return false;
			} else {
				ISidedInventory targetInventory = (ISidedInventory) stateMachine.getVariable(StateMachineVariables.TARGET_ISIDED_INVENTORY);
				if (targetInventory == null || !(targetInventory instanceof ISidedInventory)) {
					return false;
				}
				Integer slot = (Integer) stateMachine.getVariable(StateMachineVariables.SLOT);
				if (slot == null || !(slot instanceof Integer)) {
					return false;
				}
				Integer quantity = (Integer) stateMachine.getVariable(StateMachineVariables.QUANTITY);
				if (slot == null || !(slot instanceof Integer)) {
					return false;
				}
				return stateMachine.commonActions.withdrawItemQuantityFromISidedInventorySlot(quantity, targetInventory, slot);
			}
		}

		@Override
		public void finish() {
		}
	}

	private static class PlaceInISidedInventory extends State {
		public static final PlaceInISidedInventory instance = new PlaceInISidedInventory();

		@Override
		public boolean start() {
			TileEntity targetTileEntity = (TileEntity) stateMachine.getVariable(StateMachineVariables.TARGET_TILE_ENTITY);
			if (targetTileEntity == null || !(targetTileEntity instanceof TileEntity)) {
				return false;
			}
			stateMachine.setVariable(StateMachineVariables.TARGET_LOCATION, new ChunkCoordinates(targetTileEntity.xCoord, targetTileEntity.yCoord, targetTileEntity.zCoord));
			return true;

		}

		@Override
		public boolean update() {
			stateMachine.commonActions.lookAtTargetLocation();
			if (!stateMachine.commonActions.inRangeOfTargetLocation()) {
				stateMachine.commonActions.moveTowardsTargetLocation();
				return false;
			} else {
				ISidedInventory targetInventory = (ISidedInventory) stateMachine.getVariable(StateMachineVariables.TARGET_ISIDED_INVENTORY);
				if (targetInventory == null || !(targetInventory instanceof ISidedInventory)) {
					return false;
				}
				Integer slot = (Integer) stateMachine.getVariable(StateMachineVariables.SLOT);
				if (slot == null || !(slot instanceof Integer)) {
					return false;
				}
				Integer quantity = (Integer) stateMachine.getVariable(StateMachineVariables.QUANTITY);
				if (slot == null || !(slot instanceof Integer)) {
					return false;
				}
				stateMachine.commonActions.insertItemQuantityInISidedInventorySlot(quantity, targetInventory, slot);
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
	
	private static class HammerAnvil extends State {
		public static final HammerAnvil instance = new HammerAnvil();
		
		private static final int TASK_DURATION = 10;
		private static int taskTimer;

		@Override
		public boolean start() {
			TileEntityRomanAnvil anvil = (TileEntityRomanAnvil) stateMachine.getVariable(StateMachineVariables.ROMAN_ANVIL);
			if (anvil == null || !(anvil instanceof TileEntityRomanAnvil)) {
				return false;
			}
			stateMachine.setVariable(StateMachineVariables.TARGET_LOCATION, new ChunkCoordinates(anvil.xCoord, anvil.yCoord, anvil.zCoord));
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
					TileEntityRomanAnvil anvil = (TileEntityRomanAnvil) stateMachine.getVariable(StateMachineVariables.ROMAN_ANVIL);
					if (anvil == null || !(anvil instanceof TileEntityRomanAnvil)) {
						return false;
					}
					// Check if hammering the iron caused an item to be created.
					if (anvil.hammerIron() == true) {
						// If so, adjust the order quantity to reflect this.
						EntityPleb blacksmithPleb = (EntityPleb) stateMachine.getVariable(StateMachineVariables.OWNER_ENTITY);
						if (blacksmithPleb == null || !(blacksmithPleb instanceof EntityPleb)) {
							return false;
						}
						BlacksmithOrders orders = blacksmithPleb.getBlacksmithOrders();
						BlacksmithOrder finishedOrder = orders.getNextOrder();
						orders.adjustOrderQuantityForItemId(finishedOrder.getItemId(), -1);
						PacketManager.sendAdjustBlacksmithOrderQuantityPacketToSide(blacksmithPleb.entityId, finishedOrder.getItemId(), -1, false);
					}
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
