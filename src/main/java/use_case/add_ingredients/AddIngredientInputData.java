package use_case.add_ingredients;

public class AddIngredientInputData {
    private final String username;
    private final String ingredient;

    public AddIngredientInputData(String username, String ingredient) {
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
