package use_case.ingredient_inventory;

import java.util.List;

public interface IngredientInventoryInputBoundary {

    List<String> execute(IngredientInventoryInputData ingredientInventoryInputData);
}
