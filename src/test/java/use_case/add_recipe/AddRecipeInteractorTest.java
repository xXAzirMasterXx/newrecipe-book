package use_case.add_recipe;

import entity.Recipe;
import entity.RecipeFactory;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the AddRecipeInteractor use case.
 */
class AddRecipeInteractorTest {

    /**
     * Simple in-memory data access object for testing.
     */
    private static class InMemoryAddRecipeUserDataAccessObject
            implements AddRecipeUserDataAccessInterface {

        private final Map<String, Recipe> storage = new HashMap<>();

        @Override
        public boolean existsByName(String name) {
            return storage.containsKey(name);
        }

        @Override
        public void save(Recipe recipe) {
            storage.put(recipe.getName(), recipe);
        }

        public Recipe getByName(String name) {
            return storage.get(name);
        }
    }

    // ===== SUCCESS PATH =====
    @Test
    void successTest() {
        String[] ingredients = {"egg", "milk"};
        String[] measures = {"2", "200ml"};

        AddRecipeInputData inputData = new AddRecipeInputData(
                "Omelette",
                "Breakfast",
                "French",
                "Beat eggs and fry.",
                "imageUrl",
                "youtubeUrl",
                "sourceUrl",
                ingredients,
                measures,
                "10",
                false
        );

        InMemoryAddRecipeUserDataAccessObject repo =
                new InMemoryAddRecipeUserDataAccessObject();
        RecipeFactory factory = new RecipeFactory();

        AddRecipeOutputBoundary successPresenter = new AddRecipeOutputBoundary() {
            @Override
            public void prepareSuccessView(AddRecipeOutputData data) {
                assertEquals("Omelette", data.getName());
                assertEquals(10, data.getCookingTime());
                assertFalse(data.isOverwrite());
                assertNotNull(data.getId());
                assertNotNull(repo.getByName("Omelette"));
            }

            @Override
            public void prepareFailView(String error) {
                fail("Unexpected failure: " + error);
            }
        };

        AddRecipeInputBoundary interactor =
                new AddRecipeInteractor(repo, successPresenter, factory);
        interactor.execute(inputData);
    }

    // ===== REQUIRED FIELD FAILURES =====

    @Test
    void missingNameFailureTest() {
        String[] ingredients = {"egg"};
        String[] measures = {"1"};

        AddRecipeInputData inputData = new AddRecipeInputData(
                "",
                "Breakfast",
                "French",
                "Some instructions",
                "imageUrl",
                "youtubeUrl",
                "sourceUrl",
                ingredients,
                measures,
                "5",
                false
        );

        InMemoryAddRecipeUserDataAccessObject repo =
                new InMemoryAddRecipeUserDataAccessObject();
        RecipeFactory factory = new RecipeFactory();

        AddRecipeOutputBoundary failurePresenter = new AddRecipeOutputBoundary() {
            @Override
            public void prepareSuccessView(AddRecipeOutputData data) {
                fail("Unexpected success.");
            }

            @Override
            public void prepareFailView(String error) {
                assertEquals("Recipe name is required.", error);
            }
        };

        AddRecipeInputBoundary interactor =
                new AddRecipeInteractor(repo, failurePresenter, factory);
        interactor.execute(inputData);
    }

    @Test
    void missingIngredientsFailureTest() {
        String[] ingredients = new String[0];  // 长度为 0
        String[] measures = new String[0];

        AddRecipeInputData inputData = new AddRecipeInputData(
                "Omelette",
                "Breakfast",
                "French",
                "Some instructions",
                "imageUrl",
                "youtubeUrl",
                "sourceUrl",
                ingredients,
                measures,
                "5",
                false
        );

        InMemoryAddRecipeUserDataAccessObject repo =
                new InMemoryAddRecipeUserDataAccessObject();
        RecipeFactory factory = new RecipeFactory();

        AddRecipeOutputBoundary failurePresenter = new AddRecipeOutputBoundary() {
            @Override
            public void prepareSuccessView(AddRecipeOutputData data) {
                fail("Unexpected success.");
            }

            @Override
            public void prepareFailView(String error) {
                assertEquals("At least one ingredient is required.", error);
            }
        };

        AddRecipeInputBoundary interactor =
                new AddRecipeInteractor(repo, failurePresenter, factory);
        interactor.execute(inputData);
    }

    @Test
    void missingInstructionsFailureTest() {
        String[] ingredients = {"egg"};
        String[] measures = {"1"};

        AddRecipeInputData inputData = new AddRecipeInputData(
                "Omelette",
                "Breakfast",
                "French",
                "   ",   // 空白指令
                "imageUrl",
                "youtubeUrl",
                "sourceUrl",
                ingredients,
                measures,
                "5",
                false
        );

        InMemoryAddRecipeUserDataAccessObject repo =
                new InMemoryAddRecipeUserDataAccessObject();
        RecipeFactory factory = new RecipeFactory();

        AddRecipeOutputBoundary failurePresenter = new AddRecipeOutputBoundary() {
            @Override
            public void prepareSuccessView(AddRecipeOutputData data) {
                fail("Unexpected success.");
            }

            @Override
            public void prepareFailView(String error) {
                assertEquals("Instructions are required.", error);
            }
        };

        AddRecipeInputBoundary interactor =
                new AddRecipeInteractor(repo, failurePresenter, factory);
        interactor.execute(inputData);
    }

    // ===== COOKING TIME FAILURES =====

    @Test
    void invalidCookingTimeFailureTest() {
        String[] ingredients = {"egg"};
        String[] measures = {"1"};

        AddRecipeInputData inputData = new AddRecipeInputData(
                "Omelette",
                "Breakfast",
                "French",
                "Some instructions",
                "imageUrl",
                "youtubeUrl",
                "sourceUrl",
                ingredients,
                measures,
                "abc",     // 非数字，走 catch 分支
                false
        );

        InMemoryAddRecipeUserDataAccessObject repo =
                new InMemoryAddRecipeUserDataAccessObject();
        RecipeFactory factory = new RecipeFactory();

        AddRecipeOutputBoundary failurePresenter = new AddRecipeOutputBoundary() {
            @Override
            public void prepareSuccessView(AddRecipeOutputData data) {
                fail("Unexpected success.");
            }

            @Override
            public void prepareFailView(String error) {
                assertEquals("Cooking time must be a number.", error);
            }
        };

        AddRecipeInputBoundary interactor =
                new AddRecipeInteractor(repo, failurePresenter, factory);
        interactor.execute(inputData);
    }

    @Test
    void nonPositiveCookingTimeFailureTest() {
        String[] ingredients = {"egg"};
        String[] measures = {"1"};

        AddRecipeInputData inputData = new AddRecipeInputData(
                "Omelette",
                "Breakfast",
                "French",
                "Some instructions",
                "imageUrl",
                "youtubeUrl",
                "sourceUrl",
                ingredients,
                measures,
                "0",      // 解析成功，但 <= 0，走 if (cookingTime <= 0)
                false
        );

        InMemoryAddRecipeUserDataAccessObject repo =
                new InMemoryAddRecipeUserDataAccessObject();
        RecipeFactory factory = new RecipeFactory();

        AddRecipeOutputBoundary failurePresenter = new AddRecipeOutputBoundary() {
            @Override
            public void prepareSuccessView(AddRecipeOutputData data) {
                fail("Unexpected success.");
            }

            @Override
            public void prepareFailView(String error) {
                assertEquals("Cooking time must be positive.", error);
            }
        };

        AddRecipeInputBoundary interactor =
                new AddRecipeInteractor(repo, failurePresenter, factory);
        interactor.execute(inputData);
    }

    // ===== DUPLICATE NAME CASES =====

    @Test
    void duplicateNameWithoutOverwriteFailureTest() {
        String[] ingredients = {"egg"};
        String[] measures = {"1"};

        InMemoryAddRecipeUserDataAccessObject repo =
                new InMemoryAddRecipeUserDataAccessObject();
        RecipeFactory factory = new RecipeFactory();

        // Save an existing recipe
        Recipe existing = factory.create(
                "id-1",
                "Omelette",
                "Breakfast",
                "French",
                "Old instructions",
                "imageUrl",
                "youtubeUrl",
                "sourceUrl",
                ingredients,
                measures
        );
        repo.save(existing);

        // New input with same name and overwrite = false
        AddRecipeInputData inputData = new AddRecipeInputData(
                "Omelette",
                "Breakfast",
                "French",
                "New instructions",
                "newImageUrl",
                "newYoutubeUrl",
                "newSourceUrl",
                ingredients,
                measures,
                "10",
                false   // 不允许 overwrite
        );

        AddRecipeOutputBoundary failurePresenter = new AddRecipeOutputBoundary() {
            @Override
            public void prepareSuccessView(AddRecipeOutputData data) {
                fail("Unexpected success.");
            }

            @Override
            public void prepareFailView(String error) {
                assertEquals("Recipe already exists. Confirm overwrite.", error);
            }
        };

        AddRecipeInputBoundary interactor =
                new AddRecipeInteractor(repo, failurePresenter, factory);
        interactor.execute(inputData);
    }

    @Test
    void duplicateNameWithOverwriteSuccessTest() {
        String[] ingredients = {"egg"};
        String[] measures = {"1"};

        InMemoryAddRecipeUserDataAccessObject repo =
                new InMemoryAddRecipeUserDataAccessObject();
        RecipeFactory factory = new RecipeFactory();

        // 先存一个旧的
        Recipe existing = factory.create(
                "id-1",
                "Omelette",
                "Breakfast",
                "French",
                "Old instructions",
                "imageUrl",
                "youtubeUrl",
                "sourceUrl",
                ingredients,
                measures
        );
        repo.save(existing);

        // 新输入，同名，但 overwrite = true
        AddRecipeInputData inputData = new AddRecipeInputData(
                "Omelette",
                "Breakfast",
                "French",
                "New instructions",
                "newImageUrl",
                "newYoutubeUrl",
                "newSourceUrl",
                ingredients,
                measures,
                "15",
                true      // 允许 overwrite
        );

        AddRecipeOutputBoundary successPresenter = new AddRecipeOutputBoundary() {
            @Override
            public void prepareSuccessView(AddRecipeOutputData data) {
                assertEquals("Omelette", data.getName());
                assertEquals(15, data.getCookingTime());
                assertTrue(data.isOverwrite());
                // 确认 repo 里面被更新了
                Recipe saved = repo.getByName("Omelette");
                assertEquals("New instructions", saved.getInstructions());
            }

            @Override
            public void prepareFailView(String error) {
                fail("Unexpected failure: " + error);
            }
        };

        AddRecipeInputBoundary interactor =
                new AddRecipeInteractor(repo, successPresenter, factory);
        interactor.execute(inputData);
    }
}

