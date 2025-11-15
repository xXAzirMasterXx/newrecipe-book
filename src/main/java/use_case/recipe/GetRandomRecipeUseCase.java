package use_case.recipe;

import entity.Recipe;
import java.util.Optional;

public class GetRandomRecipeUseCase {
    private final RecipeDataAccessInterface recipeDataAccess;

    public GetRandomRecipeUseCase(RecipeDataAccessInterface recipeDataAccess) {
        this.recipeDataAccess = recipeDataAccess;
    }

    public Optional<Recipe> execute() {
        return recipeDataAccess.getRandomRecipe();
    }
}
