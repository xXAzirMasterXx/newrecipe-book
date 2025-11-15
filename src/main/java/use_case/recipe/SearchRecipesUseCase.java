package use_case.recipe;

import entity.Recipe;
import java.util.List;

public class SearchRecipesUseCase {
    private final RecipeDataAccessInterface recipeDataAccess;

    public SearchRecipesUseCase(RecipeDataAccessInterface recipeDataAccess) {
        this.recipeDataAccess = recipeDataAccess;
    }

    public List<Recipe> execute(String searchQuery) {
        if (searchQuery == null || searchQuery.trim().isEmpty()) {
            throw new IllegalArgumentException("Search query cannot be empty");
        }
        return recipeDataAccess.searchRecipesByName(searchQuery.trim());
    }
}