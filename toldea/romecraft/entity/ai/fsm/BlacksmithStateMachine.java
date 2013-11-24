package toldea.romecraft.entity.ai.fsm;

import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.ChunkCoordinates;
import toldea.romecraft.tileentity.TileEntityBloomery;

public class BlacksmithStateMachine extends StateMachine {
	public static class GetFuel extends State {
		public static final GetFuel instance = new GetFuel();

		@Override
		public boolean start() {
			return false;

		}

		@Override
		public boolean update() {
			return false;
		}

		@Override
		public void finish() {
		}
	}

	public static class GetIronOre extends State {
		public static final GetIronOre instance = new GetIronOre();

		@Override
		public boolean start() {
			TileEntityBloomery bloomery = (TileEntityBloomery) this.stateMachine.getVariable(StateMachineVariables.BLOOMERY);
			if (bloomery == null || !(bloomery instanceof TileEntityBloomery)) {
				return false;
			}
			TileEntityChest chest = bloomery.getAdjacentChestWithValidContentsForBloomerySlot(0);
			if (chest != null) {
				stateMachine.setVariable(StateMachineVariables.CHEST, chest);
				stateMachine.setVariable(StateMachineVariables.TARGET_LOCATION, new ChunkCoordinates(chest.xCoord, chest.yCoord, chest.zCoord));
				return true;
			} else {
				return false;
			}
		}

		@Override
		public boolean update() {
			return false;
		}

		@Override
		public void finish() {
		}
	}

	public static class PushBellows extends State {
		public static final PushBellows instance = new PushBellows();

		@Override
		public boolean start() {
			return false;
		}

		@Override
		public boolean update() {
			return false;
		}

		@Override
		public void finish() {

		}
	}

	public static class StoreIronBloom extends State {
		public static final StoreIronBloom instance = new StoreIronBloom();

		@Override
		public boolean start() {
			return false;
		}

		@Override
		public boolean update() {
			return false;
		}

		@Override
		public void finish() {

		}
	}

	@Override
	public void initialize() {
		this.setVariable(StateMachineVariables.IN_RANGE_DIST, new Double(4.0d));
		
		GetFuel.instance.linkStateMachine(this);
		GetIronOre.instance.linkStateMachine(this);
		PushBellows.instance.linkStateMachine(this);
		StoreIronBloom.instance.linkStateMachine(this);
	}

	@Override
	public State selectNextState() {
		// If we are currently holding an iron bloom, store that away first before we begin the correct smelting logic.
		// This can occur when the server shuts down just as we picked up an iron bloom.
		/*
		ItemStack equippedItem = entityPleb.getCurrentItemOrArmor(0);
		if (equippedItem != null && equippedItem.itemID == ItemManager.itemIronBloom.itemID) {
			currentTask = BlacksmithTask.STORE_IRON_BLOOM;
			System.out.println("Selecting next task: " + currentTask);
			return;
		}*/
		
		TileEntityBloomery bloomery = (TileEntityBloomery) this.getVariable("bloomery");
		if (bloomery == null || !(bloomery instanceof TileEntityBloomery)) {
			return null;
		}

		boolean hasFuel = bloomery.hasFuel();
		boolean hasIronOre = bloomery.hasIronOre();
		boolean hasIronBloom = bloomery.hasIronBloom();

		if (hasIronBloom) {
			return StoreIronBloom.instance;
		} else if (!hasIronOre) {
			return GetIronOre.instance;
		} else if (!hasFuel) {
			return GetFuel.instance;
		} else if (hasFuel && hasIronOre) {
			return PushBellows.instance;
		} else {
			return null;
		}
	}
}
