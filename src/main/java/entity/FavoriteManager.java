package entity;

import java.util.*;

public class FavoriteManager {
    private static FavoriteManager instance;
    private Set<String> favoriteRecipeIds;
    private Map<String, Recipe> recipeCache;

    private FavoriteManager() {
        favoriteRecipeIds = new HashSet<>();
        recipeCache = new HashMap<>();
    }

    public static FavoriteManager getInstance() {
        if (instance == null) {
            instance = new FavoriteManager();
        }
        return instance;
    }

    public boolean toggleFavorite(String recipeId) {
        if (favoriteRecipeIds.contains(recipeId)) {
            favoriteRecipeIds.remove(recipeId);
            return false;
        } else {
            favoriteRecipeIds.add(recipeId);
            return true;
        }
    }

    public boolean isFavorite(String recipeId) {
        return favoriteRecipeIds.contains(recipeId);
    }

    public Set<String> getFavoriteRecipeIds() {
        return new HashSet<>(favoriteRecipeIds);
    }

    /**
     * Cache a recipe so we can display it later even if not in current search
     */
    public void cacheRecipe(Recipe recipe) {
        if (recipe != null && recipe.getId() != null) {
            recipeCache.put(recipe.getId(), recipe);
        }
    }

    /**
     * Get all favorite recipes from cache
     */
    public List<Recipe> getFavoriteRecipesFromCache() {
        List<Recipe> favorites = new ArrayList<>();
        for (String favoriteId : favoriteRecipeIds) {
            if (recipeCache.containsKey(favoriteId)) {
                favorites.add(recipeCache.get(favoriteId));
            }
        }
        return favorites;
    }

    /**
     * Get the number of favorites that are cached vs total
     */
    public int getCachedFavoritesCount() {
        int cached = 0;
        for (String favoriteId : favoriteRecipeIds) {
            if (recipeCache.containsKey(favoriteId)) {
                cached++;
            }
        }
        return cached;
    }

    /**
     * Get cache size for debugging
     */
    public int getCacheSize() {
        return recipeCache.size();
    }
}