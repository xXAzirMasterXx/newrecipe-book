package interface_adapter.recipe;

import use_case.recipe.*;
import entity.Recipe;
import java.util.List;
import java.util.Optional;

public class RecipeController {

    private final SearchRecipesUseCase searchRecipesUseCase;
    private final GetRandomRecipeUseCase getRandomRecipeUseCase;
    private final GetAreasUseCase getAreasUseCase;
    private final GetCategoriesUseCase getCategoriesUseCase;

    public RecipeController(SearchRecipesUseCase searchRecipesUseCase,
                            GetRandomRecipeUseCase getRandomRecipeUseCase,
                            GetAreasUseCase getAreasUseCase,
                            GetCategoriesUseCase getCategoriesUseCase) {
        this.searchRecipesUseCase = searchRecipesUseCase;
        this.getRandomRecipeUseCase = getRandomRecipeUseCase;
        this.getAreasUseCase = getAreasUseCase;
        this.getCategoriesUseCase = getCategoriesUseCase;
    }

    public List<Recipe> searchRecipes(String query) {
        return searchRecipesUseCase.execute(query);
    }

    public Optional<Recipe> getRandomRecipe() {
        return getRandomRecipeUseCase.execute();
    }

    public void loadAreas() {
        getAreasUseCase.execute();
    }

    public void loadCategories() {
        getCategoriesUseCase.execute();
    }

}
