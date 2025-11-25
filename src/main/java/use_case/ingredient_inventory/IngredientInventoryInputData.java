package use_case.ingredient_inventory;
import java.util.List;

public class IngredientInventoryInputData {

    private final List<String> ingredients;
    private final String username;

    public IngredientInventoryInputData(List<String> ingredients, String username){
        this.ingredients = ingredients;
        this.username = username;
    }

    List<String> getIngredients(){
        return ingredients;
    }

    String getUsername(){
        return username;
    }
}
