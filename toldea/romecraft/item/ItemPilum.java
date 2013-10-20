package toldea.romecraft.item;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import toldea.romecraft.entity.EntityPilum;
import toldea.romecraft.managers.ItemManager;

import com.google.common.collect.Multimap;

public class ItemPilum extends RomeCraftItem {
	// Stone Sword like melee damage
	private float meleeDamage = 5f;
	private int sprintTime = 0;

	public ItemPilum(int id) {
		super(id);
		this.setFull3D();
		this.setMaxStackSize(1);
	}

	/**
	 * called when the player releases the use item button. Args: itemstack, world, entityplayer, itemInUseCount
	 */
	public void onPlayerStoppedUsing(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer, int par4) {
		int j = this.getMaxItemUseDuration(par1ItemStack) - par4;

		ArrowLooseEvent event = new ArrowLooseEvent(par3EntityPlayer, par1ItemStack, j);
		MinecraftForge.EVENT_BUS.post(event);
		if (event.isCanceled()) {
			return;
		}
		j = event.charge;

		boolean flag = par3EntityPlayer.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, par1ItemStack) > 0;

		if (flag || par3EntityPlayer.inventory.hasItem(ItemManager.itemPilum.itemID)) {
			float f = (float) j / 20.0F;
			f = (f * f + f * 2.0F) / 3.0F;

			if ((double) f < 0.1D) {
				return;
			}

			if (f > 1.0F) {
				f = 1.0F;
			}

			EntityPilum entityPilum = new EntityPilum(par2World, par3EntityPlayer, f * 2.0F);

			if (f == 1.0F) {
				entityPilum.setIsCritical(true);
			}

			int k = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, par1ItemStack);

			if (k > 0) {
				entityPilum.setDamage(entityPilum.getDamage() + (double) k * 0.5D + 0.5D);
			}

			int l = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, par1ItemStack);

			if (l > 0) {
				entityPilum.setKnockbackStrength(l);
			}

			if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, par1ItemStack) > 0) {
				entityPilum.setFire(100);
			}

			par1ItemStack.damageItem(1, par3EntityPlayer);
			par2World.playSoundAtEntity(par3EntityPlayer, "random.bow", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + f * 0.5F);

			if (flag) {
				entityPilum.canBePickedUp = 2;
			} else {
				par3EntityPlayer.inventory.consumeInventoryItem(ItemManager.itemPilum.itemID);
			}

			if (!par2World.isRemote) {
				par2World.spawnEntityInWorld(entityPilum);
			}
		}
	}

	public ItemStack onEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
		return par1ItemStack;
	}

	/**
	 * How long it takes to use or consume an item
	 */
	public int getMaxItemUseDuration(ItemStack par1ItemStack) {
		return 72000;
	}

	/**
	 * returns the action that specifies what animation to play when the items is being used
	 */
	public EnumAction getItemUseAction(ItemStack par1ItemStack) {
		return EnumAction.bow;
	}

	public void onUpdate(ItemStack itemstack, World world, Entity entity, int par4, boolean par5) {
		if (entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;
			if (player.isSprinting()) {
				sprintTime++;
			}
		}
	}

	/**
	 * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
	 */
	public ItemStack onItemRightClick(ItemStack itemStack, World par2World, EntityPlayer player) {
		ArrowNockEvent event = new ArrowNockEvent(player, itemStack);
		MinecraftForge.EVENT_BUS.post(event);

		if (event.isCanceled()) {
			return event.result;
		}

		if (player.isSprinting() && !par2World.isRemote) {
			onPlayerStoppedUsing(itemStack, par2World, player, this.getMaxItemUseDuration(itemStack) - (sprintTime / 2));
			sprintTime = 0;
		} else {
			if (player.capabilities.isCreativeMode || player.inventory.hasItem(ItemManager.itemPilum.itemID)) {
				player.setItemInUse(itemStack, this.getMaxItemUseDuration(itemStack));
			}
		}
		
		return itemStack;
	}

	/**
	 * Gets a map of item attribute modifiers, used by ItemSword to increase hit damage.
	 */
	public Multimap getItemAttributeModifiers() {
		Multimap multimap = super.getItemAttributeModifiers();
		multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(field_111210_e, "Weapon modifier",
				(double) this.meleeDamage, 0));
		return multimap;
	}
}
