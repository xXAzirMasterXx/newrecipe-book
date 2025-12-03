package interface_adapter.ingredient_inventory;

import use_case.ingredient_inventory.IngredientInventoryInputBoundary;
import use_case.ingredient_inventory.IngredientInventoryInputData;

import java.util.List;

public class IngredientInventoryController {

    private final IngredientInventoryInputBoundary ingredientInventoryUsecaseInteractor;
    public IngredientInventoryController(IngredientInventoryInputBoundary ingredientInventoryUsecaseInteractor){
        this.ingredientInventoryUsecaseInteractor = ingredientInventoryUsecaseInteractor;
    }


    public List<String> getIngredients(String username) {
        final IngredientInventoryInputData ingredientInventoryInputData = new IngredientInventoryInputData(username);
        return ingredientInventoryUsecaseInteractor.execute(ingredientInventoryInputData);
    }
}
