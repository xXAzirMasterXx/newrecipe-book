package interface_adapter.add_ingredients;

import use_case.add_ingredients.AddIngredientInputBoundary;
import use_case.add_ingredients.AddIngredientInputData;

import java.util.List;

public class AddIngredientController {

    private final AddIngredientInputBoundary addIngredientInteractor;

    public AddIngredientController(AddIngredientInputBoundary addIngredientInteractor) {
        this.addIngredientInteractor = addIngredientInteractor;
    }

    public List<String> addIngredient(String username, String ingredient) {
        AddIngredientInputData data = new AddIngredientInputData(username, ingredient);
        return addIngredientInteractor.execute(data);
    }
}
