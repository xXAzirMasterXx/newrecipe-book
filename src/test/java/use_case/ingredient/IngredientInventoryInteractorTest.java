package use_case.ingredient;

import entity.UserFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.ingredient_inventory.IngredientInventoryInputBoundary;
import use_case.ingredient_inventory.IngredientInventoryInputData;
import use_case.ingredient_inventory.IngredientInventoryInteractor;
import use_case.ingredient_inventory.IngredientInventoryOutputBoundary;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IngredientInventoryInteractorTest {

    private IngredientTestInMemoryDao dao;
    private UserFactory userFactory;

    @BeforeEach
    void setup() {
        dao = new IngredientTestInMemoryDao();
        userFactory = new UserFactory();
        dao.save(userFactory.create("Alex", "pw", Arrays.asList("Apple", "Banana")));
        dao.save(userFactory.create("Empty", "pw"));
    }

    @Test
    void returnsCurrentInventoryForUser() {
        IngredientInventoryOutputBoundary noopPresenter = new IngredientInventoryOutputBoundary() {
            @Override
            public void prepareSuccessView(use_case.ingredient_inventory.IngredientInventoryOutputData outputData) { }

            @Override
            public void switchToLoggedinView() { }
        };

        IngredientInventoryInputBoundary interactor = new IngredientInventoryInteractor(dao, noopPresenter);

        List<String> result = interactor.execute(new IngredientInventoryInputData("Alex"));
        assertEquals(List.of("Apple", "Banana"), result);
    }

    @Test
    void returnsEmptyListWhenNoIngredients() {
        IngredientInventoryOutputBoundary noopPresenter = new IngredientInventoryOutputBoundary() {
            @Override
            public void prepareSuccessView(use_case.ingredient_inventory.IngredientInventoryOutputData outputData) { }

            @Override
            public void switchToLoggedinView() { }
        };

        IngredientInventoryInputBoundary interactor = new IngredientInventoryInteractor(dao, noopPresenter);
        List<String> result = interactor.execute(new IngredientInventoryInputData("Empty"));
        assertEquals(List.of(), result);
    }
}
