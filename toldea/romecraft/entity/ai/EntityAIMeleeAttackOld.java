package toldea.romecraft.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.world.World;
import toldea.romecraft.entity.EntityLegionary;
import toldea.romecraft.entity.EntityLegionary.LEGIONARY_EQUIPMENT;

public class EntityAIMeleeAttackOld extends EntityAIBase {
	World worldObj;
	EntityLegionary legionaryEntityHost;

	/**
	 * An amount of decrementing ticks that allows the entity to attack once the tick reaches 0.
	 */
	int attackTick;

	Class classTarget;

	public EntityAIMeleeAttackOld(EntityLegionary par1EntityLegionary, Class par2Class) {
		this(par1EntityLegionary);
		this.classTarget = par2Class;
	}

	public EntityAIMeleeAttackOld(EntityLegionary par1EntityLegionary) {
		this.legionaryEntityHost = par1EntityLegionary;
		this.worldObj = par1EntityLegionary.worldObj;
		this.setMutexBits(3);
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute() {
		EntityLivingBase entitylivingbase = this.legionaryEntityHost.getAttackTarget();

		if (entitylivingbase == null) {
			return false;
		} else if (!entitylivingbase.isEntityAlive()) {
			return false;
		} else if (this.classTarget != null && !this.classTarget.isAssignableFrom(entitylivingbase.getClass())) {
			return false;
		} else {
			// Check if the attack target is withing melee swing range.
			double d0 = (double) (this.legionaryEntityHost.width * 2.0F * this.legionaryEntityHost.width * 2.0F + entitylivingbase.width);
			if (this.legionaryEntityHost.getDistanceSq(entitylivingbase.posX, entitylivingbase.boundingBox.minY, entitylivingbase.posZ) <= d0) {
				// If within melee range, equip 'Gladius' sword and return true.
				legionaryEntityHost.equipItem(LEGIONARY_EQUIPMENT.GLADIUS);
				return true;
			} else {
				return false;
			}
		}
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	public boolean continueExecuting() {
		return shouldExecute();
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting() {}

	/**
	 * Resets the task
	 */
	public void resetTask() {}

	/**
	 * Updates the task
	 */
	public void updateTask() {
		//this.legionaryEntityHost.swingItem();
		
		EntityLivingBase entitylivingbase = this.legionaryEntityHost.getAttackTarget();
		
		this.legionaryEntityHost.attackEntityAsMob(entitylivingbase);
		/*
		this.legionaryEntityHost.getLookHelper().setLookPositionWithEntity(entitylivingbase, 30.0F, 30.0F);

		this.attackTick = Math.max(this.attackTick - 1, 0);
		double d0 = (double) (this.legionaryEntityHost.width * 2.0F * this.legionaryEntityHost.width * 2.0F + entitylivingbase.width);
		
		if (this.legionaryEntityHost.getDistanceSq(entitylivingbase.posX, entitylivingbase.boundingBox.minY, entitylivingbase.posZ) <= d0) {
			if (this.attackTick <= 0) {
				this.attackTick = 20;

				if (this.legionaryEntityHost.getHeldItem() != null) {
					System.out.println("Tabete tabete!");
					this.legionaryEntityHost.swingItem();
				}

				this.legionaryEntityHost.attackEntityAsMob(entitylivingbase);
			}
		}
		*/
	}
}
