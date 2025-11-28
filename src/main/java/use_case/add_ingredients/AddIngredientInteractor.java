package use_case.add_ingredients;

import entity.User;
import entity.UserFactory;
import use_case.ingredient_inventory.IngredientInventoryUserDataAccessInterface;

import java.util.ArrayList;
import java.util.List;

public class AddIngredientInteractor implements AddIngredientInputBoundary {

    private final IngredientInventoryUserDataAccessInterface inventoryDao;
    private final AddIngredientUserDataInterface addIngredientDao;
    private final UserFactory userFactory;

    public AddIngredientInteractor(IngredientInventoryUserDataAccessInterface inventoryDao,
                                   AddIngredientUserDataInterface addIngredientDao,
                                   UserFactory userFactory) {
        this.inventoryDao = inventoryDao;
        this.addIngredientDao = addIngredientDao;
        this.userFactory = userFactory;
    }

    @Override
    public List<String> execute(AddIngredientInputData data) {
        final String username = data.getUsername();
        final String ingredient = data.getIngredient();

        if (ingredient == null || ingredient.trim().isEmpty()) {
            return inventoryDao.getIngredients(username);
        }

        User current = inventoryDao.get(username);
        List<String> currentIngredients = new ArrayList<>();
        if (current.getIngredient_inventory() != null) {
            currentIngredients.addAll(current.getIngredient_inventory());
        }

        currentIngredients.add(ingredient.trim());

        User updated = userFactory.create(current.getName(), current.getPassword(), currentIngredients);
        addIngredientDao.addIngredient(updated);

        return inventoryDao.getIngredients(username);
    }
}
