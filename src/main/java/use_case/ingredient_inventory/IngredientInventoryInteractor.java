package use_case.ingredient_inventory;
import entity.Ingredient;
import entity.User;
import interface_adapter.ingredient_inventory.IngredientInventoryPresenter;

import java.util.*;

public class IngredientInventoryInteractor implements IngredientInventoryInputBoundary {
    private final IngredientInventoryUserDataAccessInterface ingredientInventoryUserDataAccessObject;
    private final IngredientInventoryOutputBoundary ingredientInventoryPresenter;

    public IngredientInventoryInteractor(IngredientInventoryUserDataAccessInterface ingredientInventoryUserDataAccessInterface, IngredientInventoryOutputBoundary ingredientInventoryOutputPresenter){
        this.ingredientInventoryPresenter = ingredientInventoryOutputPresenter;
        this.ingredientInventoryUserDataAccessObject = ingredientInventoryUserDataAccessInterface;
    }

    public void execute(IngredientInventoryInputData ingredientInventoryInputData){
        List<String> ingredient = new ArrayList<>();
        final User user = ingredientInventoryUserDataAccessObject.get(ingredientInventoryInputData.getUsername());
        ingredient = ingredientInventoryUserDataAccessObject.getIngredients(user.getName());

        final IngredientInventoryOutputData ingredientInventoryOutputData = new IngredientInventoryOutputData(ingredient);
        ingredientInventoryPresenter.prepareSuccessView(ingredientInventoryOutputData);
    }
}
