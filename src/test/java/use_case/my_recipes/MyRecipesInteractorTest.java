package use_case.my_recipes;

import data_access.AddRecipeInMemoryDataAccessObject;
import entity.Recipe;
import entity.RecipeFactory;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MyRecipesInteractorTest {

    // Fake presenter record data form interactor.
    private static class TestPresenter implements MyRecipesOutputBoundary {
        String failMessage = null;
        MyRecipesOutputData successData = null;

        @Override
        public void prepareFailView(String error) {
            this.failMessage = error;
        }

        @Override
        public void prepareSuccessView(MyRecipesOutputData outputData) {
            this.successData = outputData;
        }
    }

    @Test
    void testShowMyRecipes_NoRecipes_FailView() {
        AddRecipeInMemoryDataAccessObject dao = new AddRecipeInMemoryDataAccessObject();
        TestPresenter presenter = new TestPresenter();
        MyRecipesInteractor interactor = new MyRecipesInteractor(dao, presenter);

        interactor.showMyRecipes();

        assertNotNull(presenter.failMessage);
        assertEquals("You have not added any recipes yet.", presenter.failMessage);
        assertNull(presenter.successData);
    }

    @Test
    void testShowMyRecipes_WithRecipes_SuccessView() {
        AddRecipeInMemoryDataAccessObject dao = new AddRecipeInMemoryDataAccessObject();
        TestPresenter presenter = new TestPresenter();
        MyRecipesInteractor interactor = new MyRecipesInteractor(dao, presenter);

        // create new Recipe
        RecipeFactory factory = new RecipeFactory();
        Recipe recipe = factory.create(
                "id1", "Pasta", "Main", "Italian",
                "Boil pasta", "", "", "",
                new String[]{"pasta"}, new String[]{"100g"}
        );
        dao.save(recipe);

        interactor.showMyRecipes();

        assertNull(presenter.failMessage);
        assertNotNull(presenter.successData);

        List<Recipe> recipes = presenter.successData.getRecipes();
        assertEquals(1, recipes.size());
        assertEquals("Pasta", recipes.get(0).getName());
    }
}


