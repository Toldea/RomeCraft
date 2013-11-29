package toldea.romecraft.romanvillage;

import java.util.HashMap;
import java.util.List;

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
}
