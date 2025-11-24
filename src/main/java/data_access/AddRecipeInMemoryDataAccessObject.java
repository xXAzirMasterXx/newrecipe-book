package data_access;

import entity.Recipe;
import use_case.add_recipe.AddRecipeUserDataAccessInterface;

import java.util.HashMap;
import java.util.Map;

public class AddRecipeInMemoryDataAccessObject implements AddRecipeUserDataAccessInterface {

    private final Map<String, Recipe> customRecipes = new HashMap<>();

    @Override
    public boolean existsByName(String name) {
        return customRecipes.containsKey(name);
    }

    @Override
    public void save(Recipe recipe) {
        customRecipes.put(recipe.getName(), recipe);
    }

    public Map<String, Recipe> getAll() {
        return customRecipes;
    }
}
