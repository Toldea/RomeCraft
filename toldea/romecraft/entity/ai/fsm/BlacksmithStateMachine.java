package toldea.romecraft.entity.ai.fsm;

import net.minecraft.item.ItemStack;
import toldea.romecraft.managers.ItemManager;
import toldea.romecraft.tileentity.TileEntityBloomery;

public class BlacksmithStateMachine extends StateMachine {
	public static class GetFuel extends State {
		public static final GetFuel instance = new GetFuel();

		@Override
		public void start() {

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
		public void start() {
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
		public void start() {
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
		public void start() {
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
		GetFuel.instance.linkStateMachine(this);
		GetIronOre.instance.linkStateMachine(this);
		PushBellows.instance.linkStateMachine(this);
		StoreIronBloom.instance.linkStateMachine(this);
	}

	@Override
	public State selectNextState() {
		// If we are currently holding an iron bloom, store that away first before we begin the correct smelting logic.
		// This can occur when the server shuts down just as we picked up an iron bloom.
		ItemStack equippedItem = entityPleb.getCurrentItemOrArmor(0);
		if (equippedItem != null && equippedItem.itemID == ItemManager.itemIronBloom.itemID) {
			currentTask = BlacksmithTask.STORE_IRON_BLOOM;
			System.out.println("Selecting next task: " + currentTask);
			return;
		}
		
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
