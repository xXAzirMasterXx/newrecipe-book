package use_case.ingredient;

import entity.User;
import use_case.add_ingredients.AddIngredientUserDataInterface;
import use_case.ingredient_inventory.IngredientInventoryUserDataAccessInterface;
import use_case.remove_ingredients.RemoveIngredientUserDataInterface;

import java.util.*;

class IngredientTestInMemoryDao implements IngredientInventoryUserDataAccessInterface,
        AddIngredientUserDataInterface, RemoveIngredientUserDataInterface {

    private final Map<String, User> users = new HashMap<>();

    @Override
    public List<String> getIngredients(String username) {
        User u = users.get(username);
        if (u == null || u.getIngredient_inventory() == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(u.getIngredient_inventory());
    }

    @Override
    public User get(String username) {
        return users.get(username);
    }

    @Override
    public void save(User user) {
        users.put(user.getName(), user);
    }

    @Override
    public void addIngredient(User user) {
        save(user);
    }

    @Override
    public void removeIngredient(User user) {
        save(user);
    }
}
