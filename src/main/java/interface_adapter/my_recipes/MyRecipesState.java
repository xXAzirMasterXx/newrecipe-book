package interface_adapter.my_recipes;

import entity.Recipe;

import java.util.List;

public class MyRecipesState {
    private List<Recipe> recipes;
    private String error;

    public List<Recipe> getRecipes() { return recipes; }
    public void setRecipes(List<Recipe> recipes) { this.recipes = recipes; }

    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
}
