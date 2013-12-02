package toldea.romecraft.entity.ai.blacksmith;

import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.ChunkCoordinates;
import toldea.romecraft.entity.ai.fsm.State;
import toldea.romecraft.entity.ai.fsm.StateMachine;
import toldea.romecraft.entity.ai.fsm.StateMachineVariables;
import toldea.romecraft.tileentity.TileEntityBellows;
import toldea.romecraft.tileentity.TileEntityBloomery;
import toldea.romecraft.tileentity.TileEntityRomanAnvil;

public class BlacksmithStateMachine extends StateMachine {

	@Override
	public void initialize() {
	}

	@Override
	public State selectNextState() {
		return null;
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

	private static class PlaceInChest extends State { // TODO decide where to place the item, in an adjacent chest of the anvil or some other chest
		public static final PlaceInChest instance = new PlaceInChest();

		@Override
		public boolean start() {
			/*
			TileEntityBloomery bloomery = (TileEntityBloomery) stateMachine.getVariable(StateMachineVariables.BLOOMERY);
			if (bloomery == null || !(bloomery instanceof TileEntityBloomery)) {
				return false;
			}
			TileEntityChest chest = bloomery.getAdjacentChest();
			if (chest != null) {
				stateMachine.setVariable(StateMachineVariables.CHEST, chest);
				stateMachine.setVariable(StateMachineVariables.TARGET_LOCATION, new ChunkCoordinates(chest.xCoord, chest.yCoord, chest.zCoord));
				return true;
			}*/
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
			TileEntityRomanAnvil anvil = (TileEntityRomanAnvil) stateMachine.getVariable(StateMachineVariables.ROMAN_ANVIL);
			if (anvil == null || !(anvil instanceof TileEntityRomanAnvil)) {
				return false;
			}
			stateMachine.setVariable(StateMachineVariables.TARGET_LOCATION, new ChunkCoordinates(anvil.xCoord, anvil.yCoord, anvil.zCoord));
			return true;
		}

		@Override
		public boolean update() {
			stateMachine.commonActions.lookAtTargetLocation();
			if (!stateMachine.commonActions.inRangeOfTargetLocation()) {
				stateMachine.commonActions.moveTowardsTargetLocation();
				return false;
			} else {
				TileEntityRomanAnvil anvil = (TileEntityRomanAnvil) stateMachine.getVariable(StateMachineVariables.ROMAN_ANVIL);
				if (anvil == null || !(anvil instanceof TileEntityRomanAnvil)) {
					return false;
				}
				/* TODO
				Integer slot = (Integer) stateMachine.getVariable(StateMachineVariables.SLOT);
				if (slot == null || !(slot instanceof Integer)) {
					return false;
				}
				stateMachine.commonActions.withdrawItemFromISidedInventorySlo(bloomery, slot);
				*/
				return true;
			}
		}

		@Override
		public void finish() {
		}
	}

	private static class PlaceInAnvil extends State {
		public static final PlaceInAnvil instance = new PlaceInAnvil();

		@Override
		public boolean start() {
			TileEntityRomanAnvil anvil = (TileEntityRomanAnvil) stateMachine.getVariable(StateMachineVariables.ROMAN_ANVIL);
			if (anvil == null || !(anvil instanceof TileEntityRomanAnvil)) {
				return false;
			}
			stateMachine.setVariable(StateMachineVariables.TARGET_LOCATION, new ChunkCoordinates(anvil.xCoord, anvil.yCoord, anvil.zCoord));
			return true;

		}

		@Override
		public boolean update() {
			stateMachine.commonActions.lookAtTargetLocation();
			if (!stateMachine.commonActions.inRangeOfTargetLocation()) {
				stateMachine.commonActions.moveTowardsTargetLocation();
				return false;
			} else {
				TileEntityRomanAnvil anvil = (TileEntityRomanAnvil) stateMachine.getVariable(StateMachineVariables.ROMAN_ANVIL);
				if (anvil == null || !(anvil instanceof TileEntityRomanAnvil)) {
					return false;
				}
				// TODO
				/*
				Integer slot = (Integer) stateMachine.getVariable(StateMachineVariables.SLOT);
				if (slot == null || !(slot instanceof Integer)) {
					return false;
				}
				stateMachine.commonActions.insertItemInISidedInventorySlot(bloomery, slot);
				*/
				return true;
			}
		}

		@Override
		public void finish() {
		}
	}

	private static class HammerAnvil extends State {
		public static final HammerAnvil instance = new HammerAnvil();
		
		private static final int TASK_DURATION = TileEntityBellows.ROTATION_TIME + 10; // TODO
		private static int taskTimer;

		@Override
		public boolean start() {
			// TODO
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
					//bellows.pushBellows(); TODO
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
