package use_case.ingredient_inventory;
import entity.User;

import java.util.*;

public class IngredientInventoryInteractor implements IngredientInventoryInputBoundary {
    private final IngredientInventoryUserDataAccessInterface ingredientInventoryUserDataAccessObject;
    private final IngredientInventoryOutputBoundary ingredientInventoryPresenter;

    public IngredientInventoryInteractor(IngredientInventoryUserDataAccessInterface ingredientInventoryUserDataAccessInterface, IngredientInventoryOutputBoundary ingredientInventoryOutputPresenter) {
        this.ingredientInventoryPresenter = ingredientInventoryOutputPresenter;
        this.ingredientInventoryUserDataAccessObject = ingredientInventoryUserDataAccessInterface;
    }

    public List<String> execute(IngredientInventoryInputData ingredientInventoryInputData) {
        List<String> ingredient = new ArrayList<>();
        ingredient = ingredientInventoryUserDataAccessObject.getIngredients(ingredientInventoryInputData.getUsername());

        final IngredientInventoryOutputData ingredientInventoryOutputData = new IngredientInventoryOutputData(ingredient);
        return ingredientInventoryOutputData.getIngredients();
    }
}
