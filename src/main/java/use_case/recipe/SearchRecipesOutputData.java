package use_case.recipe;

import entity.Recipe;
import java.util.List;

public class SearchRecipesOutputData {

    private final List<Recipe> recipes;

    public SearchRecipesOutputData(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }
}
