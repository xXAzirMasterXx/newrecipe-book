package use_case.remove_ingredients;

public class RemoveIngredientInputData {
    private final String username;
    private final String ingredient;

    public RemoveIngredientInputData(String username, String ingredient) {
        this.username = username;
        this.ingredient = ingredient;
    }

    public String getUsername() {
        return username;
    }

    public String getIngredient() {
        return ingredient;
    }
}
