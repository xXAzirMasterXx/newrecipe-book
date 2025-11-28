package interface_adapter.remove_ingredients;

import use_case.remove_ingredients.RemoveIngredientInputBoundary;
import use_case.remove_ingredients.RemoveIngredientInputData;
import use_case.remove_ingredients.RemoveIngredientOutputData;

public class RemoveIngredientController {

    private final RemoveIngredientInputBoundary interactor;

    public RemoveIngredientController(RemoveIngredientInputBoundary interactor) {
        this.interactor = interactor;
    }

    public RemoveIngredientOutputData removeIngredient(String username, String ingredient) {
        RemoveIngredientInputData data = new RemoveIngredientInputData(username, ingredient);
        return interactor.execute(data);
    }
}
