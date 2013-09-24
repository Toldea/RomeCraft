package toldea.romecraft.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import toldea.romecraft.entity.EntityLegionary;
import toldea.romecraft.entity.EntityLegionary.LEGIONARY_EQUIPMENT;

public class EntityAIChargeThrow extends EntityAIBase {
	/**
	 * The entity (as a RangedAttackMob) the AI instance has been applied to.
	 */
	private final EntityLegionary entityLegionary;
	private EntityLivingBase attackTarget;

	/**
	 * A decrementing tick that spawns a ranged attack once this value reaches 0. It is then set back to the maxRangedAttackTime.
	 */
	private int rangedAttackTime;
	private int field_96561_g;

	/**
	 * The maximum time the AI has to wait before peforming another ranged attack.
	 */
	private int maxRangedAttackTime;
	private float maxDistanceToTarget;
	private float maxDistanceToTargetSquared;

	public EntityAIChargeThrow(EntityLegionary par1IRangedAttackMob, int par4, float par5) {
		this(par1IRangedAttackMob, par4, par4, par5);
	}

	public EntityAIChargeThrow(EntityLegionary par1IRangedAttackMob, int par4, int par5maxRangedAttackTime,
			float par6maxDistanceToTarget) {
		this.rangedAttackTime = -1;

		if (!(par1IRangedAttackMob instanceof EntityLivingBase)) {
			throw new IllegalArgumentException("ArrowAttackGoal requires Mob implements RangedAttackMob");
		} else {
			this.entityLegionary = par1IRangedAttackMob;
			this.field_96561_g = par4;
			this.maxRangedAttackTime = par5maxRangedAttackTime;
			this.maxDistanceToTarget = par6maxDistanceToTarget;
			this.maxDistanceToTargetSquared = par6maxDistanceToTarget * par6maxDistanceToTarget;
			//this.setMutexBits(3);
			this.setMutexBits(0);
		}
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute() {
		// We obviously can't throw a Pilum if we don't have one left, so check that we do.
		if (!entityLegionary.getPilumLeft()) {
			return false;
		}
		if (!entityLegionary.isRegistered()) {
			return false;
		}
		
		int contuberniumId = entityLegionary.getContuberniumId();
		Contubernium contubernium = SquadManager.getContubernium(contuberniumId);
		if (contubernium == null || contubernium.getTargetEntity() == null) {
			return false;
		}

		EntityLivingBase entitylivingbase = contubernium.getTargetEntity();

		if (entitylivingbase == null || !entitylivingbase.isEntityAlive()) {
			return false;
		} else {
			this.attackTarget = entitylivingbase;
			return true;
		}
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	public boolean continueExecuting() {
		return this.shouldExecute();
	}

	/**
	 * Resets the task
	 */
	public void resetTask() {
		this.attackTarget = null;
		this.rangedAttackTime = -1;
	}

	/**
	 * Updates the task
	 */
	public void updateTask() {
		int contuberniumId = entityLegionary.getContuberniumId();
		Contubernium contubernium = SquadManager.getContubernium(contuberniumId);
		// If we have an active attack target, prioritize that over this behavior. 
		if (contubernium == null) {
			return;
		}
		//System.out.println("center: " + contubernium.getContuberniumCenter() + ", legionary position: " + entityLegionary.getPosition(1.0f));
		//double derpsz = this.entityLegionary.getDistanceSq(this.attackTarget.posX, this.attackTarget.boundingBox.minY, this.attackTarget.posZ);
		double d0 = contubernium.getCenter().distanceTo(Vec3.createVectorHelper(this.attackTarget.posX, this.attackTarget.boundingBox.minY, this.attackTarget.posZ));
		d0 *= d0;
		//System.out.println("derpz: " + derpsz + ", d0: " + d0);
		boolean flag = this.entityLegionary.getEntitySenses().canSee(this.attackTarget);

		this.entityLegionary.getLookHelper().setLookPositionWithEntity(this.attackTarget, 30.0F, 30.0F);
		float f;

		if (d0 > (double) this.maxDistanceToTargetSquared || !flag) {
			return;
		}
		
		entityLegionary.equipItem(LEGIONARY_EQUIPMENT.PILUM);
		
		if (--this.rangedAttackTime == 0) {
			f = MathHelper.sqrt_double(d0) / this.maxDistanceToTarget;
			float f1 = f;

			if (f < 0.1F) {
				f1 = 0.1F;
			}

			if (f1 > 1.0F) {
				f1 = 1.0F;
			}

			this.entityLegionary.attackEntityWithRangedAttack(this.attackTarget, f1);
			this.rangedAttackTime = MathHelper.floor_float(f * (float) (this.maxRangedAttackTime) + (float) this.field_96561_g);
		} else if (this.rangedAttackTime < 0) {
			f = MathHelper.sqrt_double(d0) / this.maxDistanceToTarget;
			this.rangedAttackTime = MathHelper.floor_float(f * (float) (this.maxRangedAttackTime) + (float) this.field_96561_g);
		}
	}
}