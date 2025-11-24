package use_case.recipe;

import entity.Recipe;
import interface_adapter.recipe.RecipePresenter;

import java.util.List;

public class SearchRecipesUseCase {
    private final RecipeDataAccessInterface recipeDataAccess;

    public SearchRecipesUseCase(RecipeDataAccessInterface recipeDataAccess, RecipePresenter recipePresenter) {
        this.recipeDataAccess = recipeDataAccess;
    }

    public List<Recipe> execute(String searchQuery) {
        if (searchQuery == null || searchQuery.trim().isEmpty()) {
            throw new IllegalArgumentException("Search query cannot be empty");
        }
        return recipeDataAccess.searchRecipesByName(searchQuery.trim());
    }
}