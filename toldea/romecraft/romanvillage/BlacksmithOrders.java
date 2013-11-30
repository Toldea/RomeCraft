package toldea.romecraft.romanvillage;

import java.util.HashMap;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import toldea.romecraft.item.crafting.RomanAnvilRecipes;
import toldea.romecraft.item.crafting.RomanAnvilRecipes.AnvilRecipe;

public class BlacksmithOrders {
	private final HashMap<Integer, Integer> itemOrderQuantityMap;

	public BlacksmithOrders() {
		itemOrderQuantityMap = new HashMap<Integer, Integer>();
		
		List<AnvilRecipe> anvilRecipes = RomanAnvilRecipes.instance().getRecipeList();
		for (int i = 0; i < anvilRecipes.size(); i++) {
			AnvilRecipe recipe = anvilRecipes.get(i);
			itemOrderQuantityMap.put(Integer.valueOf(recipe.craftedItem.itemID), 0);
		}
	}
	
	public int getOrderQuantityForItemId(int itemId) {
		if (itemOrderQuantityMap.containsKey(Integer.valueOf(itemId))) {
			return itemOrderQuantityMap.get(Integer.valueOf(itemId)).intValue();
		} else {
			return -1;
		}
	}
	
	public void adjustOrderQuantityForItemId(int itemId, int adjustment) {
		if (itemOrderQuantityMap.containsKey(Integer.valueOf(itemId))) {
			int currentQuantity = itemOrderQuantityMap.get(Integer.valueOf(itemId)).intValue();
			int newQuantity = currentQuantity + adjustment;
			if (newQuantity < 0) {
				System.out.println("Warning: RomeCraft.BlacksmithOrders.adjustOrderQuantityForItemId adjusted item with id" + itemId + " to below zero!");
			}
			itemOrderQuantityMap.put(Integer.valueOf(itemId), newQuantity);
		} else {
			System.out.println("Error: RomeCraft.BlacksmithOrders.adjustOrderQuantityForItemId called for unknown itemId:" + itemId);
		}
	}
	
	public void writeOrdersToNBT(NBTTagCompound compound) {
		NBTTagCompound ordersCompound = new NBTTagCompound();
		
		Object[] keys = itemOrderQuantityMap.keySet().toArray();
		Object[] values = itemOrderQuantityMap.values().toArray();
		int[] itemIdsArray = new int[itemOrderQuantityMap.size()];
		int[] quantitiesArray = new int[itemOrderQuantityMap.size()];
		
		for (int i = 0; i < keys.length; i++) {
			itemIdsArray[i] = ((Integer)keys[i]).intValue();
		}
		for (int i = 0; i < values.length; i++) {
			quantitiesArray[i] = ((Integer)values[i]).intValue();
			System.out.println("Writing blacksmith order combination: " + itemIdsArray[i]  + ", " + quantitiesArray[i]);
		}
	    
	    ordersCompound.setIntArray("itemIds", itemIdsArray);
		ordersCompound.setIntArray("quantities", quantitiesArray);
	    
	    compound.setCompoundTag("blacksmithOrders", ordersCompound);
	}
	public void readOrdersFromNBT(NBTTagCompound compound) {
		NBTTagCompound ordersCompound = compound.getCompoundTag("blacksmithOrders");
		if (ordersCompound != null) {
			int[] itemIdsArray = ordersCompound.getIntArray("itemIds");
			int[] quantitiesArray = ordersCompound.getIntArray("quantities");
			for (int i = 0; i < itemIdsArray.length; i++) {
				System.out.println("Reading blacksmith order combination: " + itemIdsArray[i]  + ", " + quantitiesArray[i]);
				itemOrderQuantityMap.put(Integer.valueOf(itemIdsArray[i]), Integer.valueOf(quantitiesArray[i]));
			}
		}
	}
}
