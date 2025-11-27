package use_case.my_recipes;

import entity.Recipe;
import java.util.List;

public class MyRecipesOutputData {
    private final List<Recipe> recipes;

    public MyRecipesOutputData(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }
}
