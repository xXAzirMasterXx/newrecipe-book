package use_case.ingredient_inventory;
import java.util.List;

public class IngredientInventoryOutputData {
    private final List<String> ingredients;

    IngredientInventoryOutputData(List<String> ingredients){
        this.ingredients = ingredients;
    }

    List<String> getIngredients(){
        return ingredients;
    }
}
