package use_case.recipe;

import entity.Recipe;

import java.util.List;

public class SearchRecipesUseCase {
    private final RecipeDataAccessInterface recipeDataAccess;

    public SearchRecipesUseCase(RecipeDataAccessInterface recipeDataAccess) {
        this.recipeDataAccess = recipeDataAccess;
    }

    public List<Recipe> execute(String query) {

        if (query == null || query.trim().isEmpty()) {
            throw new IllegalArgumentException("Search query cannot be empty");
        }

        query = query.trim();

        System.out.println("DEBUG: Search request received for: " + query);

        // Detect category search
        if (recipeDataAccess.getAllCategories().contains(query)) {
            System.out.println("DEBUG: Detected CATEGORY search -> " + query);
            return recipeDataAccess.getRecipesByCategory(query);
        }

        // Detect area search
        if (recipeDataAccess.getAllAreas().contains(query)) {
            System.out.println("DEBUG: Detected AREA search -> " + query);
            return recipeDataAccess.getRecipesByArea(query);
        }

        // Name search (default)
        System.out.println("DEBUG: Detected NAME search -> " + query);
        return recipeDataAccess.searchRecipesByName(query);
    }
}
