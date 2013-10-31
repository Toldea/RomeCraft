package toldea.romecraft.item.crafting;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.ItemStack;
import toldea.romecraft.managers.ItemManager;

public class RomanAnvilRecipes {
	private static final RomanAnvilRecipes anvilRecipes = new RomanAnvilRecipes();
	
	private class AnvilRecipe {
		public final int rawIngredientId;
		public final int rawIngredientQuantity;
		public final ItemStack craftedItem;
		public final float experience;
		public AnvilRecipe(int rawIngredientId, int rawIngredientQuantity, ItemStack craftedItem, float experience) {
			this.rawIngredientId = rawIngredientId;
			this.rawIngredientQuantity = rawIngredientQuantity;
			this.craftedItem = craftedItem;
			this.experience = experience;
		}
	}

	private HashMap<Integer, HashMap<Integer, AnvilRecipe>> anvilRecipesList = new HashMap<Integer, HashMap<Integer, AnvilRecipe>>();
	
	public static final RomanAnvilRecipes instance() {
		return anvilRecipes;
	}

	private RomanAnvilRecipes() {
		this.addRecipe(ItemManager.itemIronBloom.itemID, 1, new ItemStack(ItemManager.itemPilum), .7f);
		//this.addRecipe(ItemManager.itemIronBloom.itemID, 2, new ItemStack(ItemManager.itemPugio), .7f);
		this.addRecipe(ItemManager.itemIronBloom.itemID, 2, new ItemStack(ItemManager.itemScutum), .7f);
		this.addRecipe(ItemManager.itemIronBloom.itemID, 3, new ItemStack(ItemManager.itemGladius), .7f);
		this.addRecipe(ItemManager.itemIronBloom.itemID, 4, new ItemStack(ItemManager.itemCingulum), .7f);
		this.addRecipe(ItemManager.itemIronBloom.itemID, 5, new ItemStack(ItemManager.itemGalea), .7f);
		this.addRecipe(ItemManager.itemIronBloom.itemID, 6, new ItemStack(ItemManager.itemLoricaSegmentata), .7f);
	}

	public void addRecipe(int rawMaterialId, int rawMaterialQuantity, ItemStack finishedItem, float experience) {
		HashMap<Integer, AnvilRecipe> map = anvilRecipesList.get(Integer.valueOf(rawMaterialId));
		if (map == null) {
			map = new HashMap<Integer, AnvilRecipe>();
		}
		map.put(Integer.valueOf(rawMaterialQuantity), new AnvilRecipe(rawMaterialId, rawMaterialQuantity, finishedItem, experience));
		anvilRecipesList.put(Integer.valueOf(rawMaterialId), map);
	}

	public Map getRecipeList() {
		return this.anvilRecipesList;
	}

	public ItemStack getRecipeResult(ItemStack rawItem) {
		if (rawItem == null) {
			return null;
		}
		HashMap<Integer, AnvilRecipe> recipesForRawItemMap = anvilRecipesList.get(Integer.valueOf(rawItem.itemID));
		if (recipesForRawItemMap == null || recipesForRawItemMap.size() <= 0) {
			return null;
		}
		int rawItemQuantity = rawItem.stackSize;
		
		AnvilRecipe recipe = recipesForRawItemMap.get(Integer.valueOf(rawItemQuantity));
		if (recipe == null) {
			return null;
		} else {
			return recipe.craftedItem;
		}
	}
}
