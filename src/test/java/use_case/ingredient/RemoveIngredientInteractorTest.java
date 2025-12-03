package use_case.ingredient;

import entity.UserFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.remove_ingredients.RemoveIngredientInputBoundary;
import use_case.remove_ingredients.RemoveIngredientInputData;
import use_case.remove_ingredients.RemoveIngredientInteractor;
import use_case.remove_ingredients.RemoveIngredientOutputData;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RemoveIngredientInteractorTest {

    private IngredientTestInMemoryDao dao;
    private UserFactory userFactory;

    @BeforeEach
    void setup() {
        dao = new IngredientTestInMemoryDao();
        userFactory = new UserFactory();
        dao.save(userFactory.create("Sam", "pw", Arrays.asList("Eggs", "Milk", "Salt")));
    }

    @Test
    void removeExistingIngredient() {
        RemoveIngredientInputBoundary interactor = new RemoveIngredientInteractor(dao, dao, userFactory);

        RemoveIngredientOutputData out = interactor.execute(new RemoveIngredientInputData("Sam", "Milk"));

        assertTrue(out.isRemoved());
        assertEquals(List.of("Eggs", "Salt"), out.getIngredients());
        assertEquals(List.of("Eggs", "Salt"), dao.getIngredients("Sam"));
    }

    @Test
    void removingNonExistentIngredientReturnsFalseAndNoChange() {
        RemoveIngredientInputBoundary interactor = new RemoveIngredientInteractor(dao, dao, userFactory);

        List<String> before = dao.getIngredients("Sam");
        RemoveIngredientOutputData out = interactor.execute(new RemoveIngredientInputData("Sam", "Flour"));

        assertFalse(out.isRemoved());
        assertEquals(before, out.getIngredients());
        assertEquals(before, dao.getIngredients("Sam"));
    }

    @Test
    void blankOrNullIngredientDoesNothing() {
        RemoveIngredientInputBoundary interactor = new RemoveIngredientInteractor(dao, dao, userFactory);

        List<String> before = dao.getIngredients("Sam");
        RemoveIngredientOutputData outBlank = interactor.execute(new RemoveIngredientInputData("Sam", "   "));
        RemoveIngredientOutputData outNull = interactor.execute(new RemoveIngredientInputData("Sam", null));

        assertFalse(outBlank.isRemoved());
        assertFalse(outNull.isRemoved());
        assertEquals(before, outBlank.getIngredients());
        assertEquals(before, outNull.getIngredients());
        assertEquals(before, dao.getIngredients("Sam"));
    }
}
