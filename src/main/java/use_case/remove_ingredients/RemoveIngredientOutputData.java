package use_case.remove_ingredients;

import java.util.List;

public class RemoveIngredientOutputData {
    private final List<String> ingredients;
    private final boolean removed;

    public RemoveIngredientOutputData(List<String> ingredients, boolean removed) {
        this.ingredients = ingredients;
        this.removed = removed;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public boolean isRemoved() {
        return removed;
    }
}
