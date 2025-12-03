package use_case.ingredient_inventory;
import java.util.List;

public class IngredientInventoryInputData {

    private final String username;

    public IngredientInventoryInputData(String username){
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }

}
