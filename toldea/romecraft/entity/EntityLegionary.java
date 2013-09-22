package toldea.romecraft.entity;

import toldea.romecraft.ItemManager;
import toldea.romecraft.ai.EntityAIFormation;
import toldea.romecraft.ai.EntityAIMeleeAttack;
import toldea.romecraft.ai.EntityAIThrowingAttack;
import toldea.romecraft.ai.SquadManager;
import toldea.romecraft.command.EntitySelectorLegionary;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityLivingData;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIBreakDoor;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveThroughVillage;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAIMoveTowardsTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.DamageSource;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.Event.Result;

public class EntityLegionary extends EntityMob implements IRangedAttackMob {
	public enum LEGIONARY_EQUIPMENT {
		LORICA_SEGMENTATA,
		SCUTUM,
		GALAE,
		PILUM,
		VERUTUM,
		GLADIUS,
		PUGIO,
		CALIGAE,
		SARCINA,
		SUDIS,
		SHOVEL,
		WATERSKIN
	}
	
	private static final EntitySelectorLegionary enemySelector = new EntitySelectorLegionary();
	private static final float accuracy = 5f;
	private static final float pilumRange = 20f;
	private static final double movementSpeed = .6d;
	
	private int contuberniumId = 1;
	private int pilaLeft;
	private boolean registered = false;

	public EntityLegionary(World par1World) {
		super(par1World);
		
		// this.experienceValue = 10;

		// this.getNavigator().setAvoidsWater(true);

		this.getNavigator().setCanSwim(true);

		// this.setSneaking(true);

		// this.tasks.addTask(1, new EntityAIAttackOnCollide(this, 1.0D, true));

		
		this.tasks.addTask(1, new EntityAIFormation(this, movementSpeed));
		this.tasks.addTask(2, new EntityAIMeleeAttack(this));
		this.tasks.addTask(3, new EntityAIThrowingAttack(this, 1.0D, 20, 60, pilumRange));

		// this.tasks.addTask(2, new EntityAIMoveTowardsTarget(this, 0.9D, 32.0F));
		// this.tasks.addTask(3, new EntityAIMoveThroughVillage(this, 0.6D, true));
		// this.tasks.addTask(4, new EntityAIMoveTowardsRestriction(this, 1.0D));
		// this.tasks.addTask(5, new EntityAILookAtVillager(this));
		// this.tasks.addTask(6, new EntityAIWander(this, 0.6D));
		// this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
		// this.tasks.addTask(8, new EntityAILookIdle(this));
		// this.targetTasks.addTask(1, new EntityAIDefendVillage(this));
		// this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
		
		this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityLiving.class, 0, false, true, enemySelector));

	}

	protected boolean isAIEnabled() {
		return true;
	}

	public void onDeath(DamageSource par1DamageSource) {
		super.onDeath(par1DamageSource);
		SquadManager.removeFromContubernium(this.contuberniumId, this);
	}

	public int getContuberniumId() {
		return contuberniumId;
	}

	public boolean isRegistered() {
		return registered;
	}

	public int getSquadIndex() {
		return SquadManager.getContubernium(contuberniumId).getSquadIndex(this);
	}

	public void onUpdate() {
		super.onUpdate();
		if (!this.worldObj.isRemote) {
			if (!registered) {
				SquadManager.registerToContubernium(this.contuberniumId, this);
				registered = true;
			}
		}
	}

	public EntityLivingData onSpawnWithEgg(EntityLivingData par1EntityLivingData) {
		par1EntityLivingData = super.onSpawnWithEgg(par1EntityLivingData);
		
		pilaLeft = 1;

		// Give the Legionary it's default equipment.
		equipItem(LEGIONARY_EQUIPMENT.LORICA_SEGMENTATA);
		equipItem(LEGIONARY_EQUIPMENT.GALAE);
		equipItem(LEGIONARY_EQUIPMENT.CALIGAE);
		
		//equipItem(LEGIONARY_EQUIPMENT.GLADIUS);
		equipItem(LEGIONARY_EQUIPMENT.PILUM);

		return par1EntityLivingData;
	}
	
	public boolean getPilumLeft() {
		return (pilaLeft >= 1);
	}
	
	public boolean isHoldingPilum() {
		ItemStack itemStack = getCurrentItemOrArmor(0);
		if (itemStack != null) {
			return (itemStack.itemID == ItemManager.itemPilum.itemID);
		} else {
			return false;
		}
	}
	
	public void equipItem(LEGIONARY_EQUIPMENT equipment) {
		switch(equipment) {
		case LORICA_SEGMENTATA:
			equipItemToSlot(Item.plateIron, 3);
			equipItemToSlot(Item.legsIron, 2);
			break;
		case SCUTUM:
			break;
		case GALAE:
			equipItemToSlot(Item.helmetIron, 4);
			break;
		case PILUM:
			equipItemToSlot(ItemManager.itemPilum, 0);
			break;
		case VERUTUM:
			break;
		case GLADIUS:
			equipItemToSlot(ItemManager.itemGladius, 0);
			break;
		case PUGIO:
			break;
		case CALIGAE:
			equipItemToSlot(Item.bootsLeather, 1);
			break;
		case SARCINA:
			break;
		case SUDIS:
			break;
		case SHOVEL:
			break;
		case WATERSKIN:
			break;
		default:
			break;
		}
	}
	private void equipItemToSlot(Item item, int itemSlot) {
		// Make sure the item isn't already equipped.
		ItemStack currentEquippedItem = this.getCurrentItemOrArmor(itemSlot);
		if (currentEquippedItem == null || currentEquippedItem.itemID != item.itemID) {
			this.setCurrentItemOrArmor(itemSlot, new ItemStack(item));
		}
	}

	public void attackEntityWithRangedAttack(EntityLivingBase par1EntityLivingBase, float par2) {
		EntityPilum entityPilum = new EntityPilum(this.worldObj, this, par1EntityLivingBase, 1.6F, accuracy);
		int i = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, this.getHeldItem());
		int j = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, this.getHeldItem());
		entityPilum.setDamage((double) (par2 * 2.0F) + this.rand.nextGaussian() * 0.25D + (double) ((float) this.worldObj.difficultySetting * 0.11F));

		if (i > 0) {
			entityPilum.setDamage(entityPilum.getDamage() + (double) i * 0.5D + 0.5D);
		}

		if (j > 0) {
			entityPilum.setKnockbackStrength(j);
		}
		
		if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, this.getHeldItem()) > 0) {
			entityPilum.setFire(100);
		}
		
		this.playSound("random.bow", 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
		this.worldObj.spawnEntityInWorld(entityPilum);
		pilaLeft--;
		if (pilaLeft <= 0) {
			this.setCurrentItemOrArmor(0, null);
		}
	}

	public boolean interact(EntityPlayer par1EntityPlayer) {
		ItemStack itemstack = par1EntityPlayer.inventory.getCurrentItem();
		boolean flag = itemstack != null && itemstack.itemID == Item.monsterPlacer.itemID;

		if (!flag && this.isEntityAlive()) {
			if (!this.worldObj.isRemote) {
				par1EntityPlayer.sendChatToPlayer(ChatMessageComponent.createFromText("Legionary! (Contubernium: " + contuberniumId + ", Size: "
						+ SquadManager.getContubernium(contuberniumId).getSquadSize() + ", Squad Index: " + getSquadIndex() + ")"));
			}
			return true;
		} else {
			return super.interact(par1EntityPlayer);
		}
	}

	protected boolean canDespawn() {
		return false;
	}
	
	@Override
	protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        // Set it's 'followRange' to twice the normal size. This also causes the legionary to be able to shoot targets twice as far away.
        this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.followRange).setAttribute(pilumRange + 1f);
    }

	public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeEntityToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setInteger("Contubernium", this.contuberniumId);
		par1NBTTagCompound.setInteger("pilaLeft", this.pilaLeft);
	}

	public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readEntityFromNBT(par1NBTTagCompound);
		this.contuberniumId = par1NBTTagCompound.getInteger("Contubernium");
		this.pilaLeft = par1NBTTagCompound.getInteger("pilaLeft");
	}
}
