package toldea.romecraft.entity.ai;

import java.util.List;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.tileentity.TileEntity;
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
	private final EntityPleb entityPleb;
	
	private final BlacksmithStateMachine blacksmithStateMachine;

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

	public boolean continueExecuting() {
		return blacksmithStateMachine.update();
	}
}
