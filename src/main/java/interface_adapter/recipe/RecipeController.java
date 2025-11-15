package interface_adapter.recipe;


import use_case.recipe.SearchRecipesUseCase;
import use_case.recipe.GetRandomRecipeUseCase;
import entity.Recipe;
import java.util.List;
import java.util.Optional;

public class RecipeController {
    private final SearchRecipesUseCase searchRecipesUseCase;
    private final GetRandomRecipeUseCase getRandomRecipeUseCase;

    public RecipeController(SearchRecipesUseCase searchRecipesUseCase,
                            GetRandomRecipeUseCase getRandomRecipeUseCase) {
        this.searchRecipesUseCase = searchRecipesUseCase;
        this.getRandomRecipeUseCase = getRandomRecipeUseCase;
    }

    public List<Recipe> searchRecipes(String query) {
        return searchRecipesUseCase.execute(query);
    }

    public Optional<Recipe> getRandomRecipe() {
        return getRandomRecipeUseCase.execute();
    }
}