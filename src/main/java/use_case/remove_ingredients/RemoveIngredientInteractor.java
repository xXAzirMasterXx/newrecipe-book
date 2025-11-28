package use_case.remove_ingredients;

import entity.User;
import entity.UserFactory;
import use_case.ingredient_inventory.IngredientInventoryUserDataAccessInterface;

import java.util.ArrayList;
import java.util.List;

public class RemoveIngredientInteractor implements RemoveIngredientInputBoundary {

    private final IngredientInventoryUserDataAccessInterface inventoryDao;
    private final RemoveIngredientUserDataInterface removeIngredientDao;
    private final UserFactory userFactory;

    public RemoveIngredientInteractor(IngredientInventoryUserDataAccessInterface inventoryDao,
                                      RemoveIngredientUserDataInterface removeIngredientDao,
                                      UserFactory userFactory) {
        this.inventoryDao = inventoryDao;
        this.removeIngredientDao = removeIngredientDao;
        this.userFactory = userFactory;
    }

    @Override
    public RemoveIngredientOutputData execute(RemoveIngredientInputData data) {
        final String username = data.getUsername();
        final String ingredientRaw = data.getIngredient();

        List<String> currentList = inventoryDao.getIngredients(username);
        if (ingredientRaw == null || ingredientRaw.trim().isEmpty()) {
            return new RemoveIngredientOutputData(currentList, false);
        }

        final String ingredient = ingredientRaw.trim();
        boolean removed = false;

        User current = inventoryDao.get(username);
        List<String> updated = new ArrayList<>();
        if (current.getIngredient_inventory() != null) {
            updated.addAll(current.getIngredient_inventory());
        }

        // Try to remove one occurrence (case-sensitive, consistent with add)
        removed = updated.remove(ingredient);

        if (removed) {
            User saved = userFactory.create(current.getName(), current.getPassword(), updated);
            removeIngredientDao.removeIngredient(saved);
            // refresh from DAO to be consistent with storage
            currentList = inventoryDao.getIngredients(username);
        }

        return new RemoveIngredientOutputData(currentList, removed);
    }
}
