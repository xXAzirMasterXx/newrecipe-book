package use_case.ingredient_inventory;
import entity.User;
import java.util.*;

public interface IngredientInventoryUserDataAccessInterface {

    List<String> getIngredients(String user);

    User get(String username);

    void save(User user);
}
