package use_case.recipe;


import entity.Recipe;
import java.util.List;
import java.util.Optional;

public interface RecipeDataAccessInterface {

    // Search recipes by name
    List<Recipe> searchRecipesByName(String name);

    // Get recipe by ID
    Optional<Recipe> getRecipeById(String id);

    // Get recipes by category
    List<Recipe> getRecipesByCategory(String category);

    // Get recipes by area (cuisine)
    List<Recipe> getRecipesByArea(String area);

    // Get all available categories
    List<String> getAllCategories();

    // Get all available areas (cuisines)
    List<String> getAllAreas();

    // Get random recipe
    Optional<Recipe> getRandomRecipe();

    // Get recipes by main ingredient
    List<Recipe> getRecipesByMainIngredient(String ingredient);

    // Search recipes by first letter
    List<Recipe> searchRecipesByFirstLetter(char letter);
}
