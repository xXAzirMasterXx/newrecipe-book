package use_case.ingredient;

import entity.UserFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.add_ingredients.AddIngredientInputBoundary;
import use_case.add_ingredients.AddIngredientInputData;
import use_case.add_ingredients.AddIngredientInteractor;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AddIngredientInteractorTest {

    private IngredientTestInMemoryDao dao;
    private UserFactory userFactory;

    @BeforeEach
    void setup() {
        dao = new IngredientTestInMemoryDao();
        userFactory = new UserFactory();
        dao.save(userFactory.create("Paul", "password"));
    }

    @Test
    void addToEmptyInventory() {
        AddIngredientInputBoundary interactor = new AddIngredientInteractor(dao, dao, userFactory);

        List<String> result = interactor.execute(new AddIngredientInputData("Paul", "Tomato"));

        assertEquals(List.of("Tomato"), result);
        assertEquals(List.of("Tomato"), dao.getIngredients("Paul"));
    }

    @Test
    void addToNonEmptyInventory_trimsInput() {
        dao.save(userFactory.create("Paul", "password", Arrays.asList("Eggs", "Milk")));

        AddIngredientInputBoundary interactor = new AddIngredientInteractor(dao, dao, userFactory);
        List<String> result = interactor.execute(new AddIngredientInputData("Paul", "  Flour  "));

        assertEquals(List.of("Eggs", "Milk", "Flour"), result);
    }

    @Test
    void ignoreBlankOrNullIngredient() {
        AddIngredientInputBoundary interactor = new AddIngredientInteractor(dao, dao, userFactory);

        dao.save(userFactory.create("Paul", "password", Arrays.asList("Salt", "Pepper")));

        List<String> before = dao.getIngredients("Paul");
        List<String> resultBlank = interactor.execute(new AddIngredientInputData("Paul", "   "));
        List<String> resultNull = interactor.execute(new AddIngredientInputData("Paul", null));

        assertEquals(before, resultBlank);
        assertEquals(before, resultNull);
        assertEquals(before, dao.getIngredients("Paul"));
    }
}
