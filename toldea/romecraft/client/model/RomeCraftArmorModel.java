package toldea.romecraft.client.model;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public abstract class RomeCraftArmorModel extends ModelBiped {
	/**
	 * Returns true or false depending on if parsed Entity's itemStack contains an object with the specified itemId in the specified item slot.
	 * 
	 * @return
	 */
	protected boolean isItemIdEquippedInSlot(Entity entity, int slot, int itemId) {
		if (entity instanceof EntityLivingBase) {
			ItemStack itemStack = ((EntityLivingBase) entity).getCurrentItemOrArmor(slot);
			if (itemStack != null) {
				return (itemStack.itemID == itemId);
			}
		}
		return false;
	}
}
