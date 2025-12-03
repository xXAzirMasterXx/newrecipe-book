package test;

import interface_adapter.recipe.RecipePresenter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import use_case.recipe.*;
import use_case.search.MockRecipeDataAccess;
import entity.Recipe;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the 7 specified components
 */
class RecipeComponentsTest {

    private MockRecipeDataAccess mockDataAccess;

    @BeforeEach
    void setUp() {
        mockDataAccess = new MockRecipeDataAccess();
    }

    // ========== Test for RecipeDataAccessInterface ==========

    @Test
    @DisplayName("Test RecipeDataAccessInterface is implemented by MockRecipeDataAccess")
    void testRecipeDataAccessInterface() {
        // Verify MockRecipeDataAccess implements the interface
        assertTrue(mockDataAccess instanceof RecipeDataAccessInterface);

        RecipeDataAccessInterface dataAccess = mockDataAccess;

        // Test all methods return appropriate types (not null)
        List<Recipe> searchResults = dataAccess.searchRecipesByName("test");
        assertNotNull(searchResults);

        Optional<Recipe> recipeById = dataAccess.getRecipeById("1");
        assertNotNull(recipeById);

        List<Recipe> categoryResults = dataAccess.getRecipesByCategory("Dessert");
        assertNotNull(categoryResults);

        List<Recipe> areaResults = dataAccess.getRecipesByArea("Italian");
        assertNotNull(areaResults);

        List<String> categories = dataAccess.getAllCategories();
        assertNotNull(categories);

        List<String> areas = dataAccess.getAllAreas();
        assertNotNull(areas);

        Optional<Recipe> randomRecipe = dataAccess.getRandomRecipe();
        assertNotNull(randomRecipe);

        List<Recipe> ingredientResults = dataAccess.getRecipesByMainIngredient("chicken");
        assertNotNull(ingredientResults);

        List<Recipe> letterResults = dataAccess.searchRecipesByFirstLetter('A');
        assertNotNull(letterResults);
    }

    // ========== Test for SearchRecipesOutputBoundary ==========

    @Test
    @DisplayName("Test SearchRecipesOutputBoundary interface")
    void testSearchRecipesOutputBoundary() {
        // Create a test implementation
        TestOutputBoundary testBoundary = new TestOutputBoundary();

        // Verify it implements the interface
        assertTrue(testBoundary instanceof SearchRecipesOutputBoundary);

        // Test data
        Recipe recipe = new Recipe("1", "Test Recipe", "Category", "Area",
                "Instructions", "thumb.jpg", "youtube.com",
                "source.com", new String[]{}, new String[]{});
        List<Recipe> recipes = Arrays.asList(recipe);
        SearchRecipesOutputData outputData = new SearchRecipesOutputData(recipes);

        List<String> categories = Arrays.asList("Dessert", "Main Course");
        List<String> areas = Arrays.asList("Italian", "American");

        // Test all interface methods
        SearchRecipesOutputBoundary boundary = testBoundary;
        boundary.prepareSuccessView(outputData);
        boundary.prepareFailView("Test error");
        boundary.presentCategories(categories);
        boundary.presentAreas(areas);

        // Verify calls were made
        assertTrue(testBoundary.successCalled);
        assertTrue(testBoundary.failCalled);
        assertTrue(testBoundary.categoriesCalled);
        assertTrue(testBoundary.areasCalled);

        assertEquals(outputData, testBoundary.lastSuccessData);
        assertEquals("Test error", testBoundary.lastError);
        assertEquals(categories, testBoundary.lastCategories);
        assertEquals(areas, testBoundary.lastAreas);
    }

    // Test implementation for SearchRecipesOutputBoundary
    private static class TestOutputBoundary implements SearchRecipesOutputBoundary {
        boolean successCalled = false;
        boolean failCalled = false;
        boolean categoriesCalled = false;
        boolean areasCalled = false;
        SearchRecipesOutputData lastSuccessData = null;
        String lastError = null;
        List<String> lastCategories = null;
        List<String> lastAreas = null;

        @Override
        public void prepareSuccessView(SearchRecipesOutputData data) {
            successCalled = true;
            lastSuccessData = data;
        }

        @Override
        public void prepareFailView(String error) {
            failCalled = true;
            lastError = error;
        }

        @Override
        public void presentCategories(List<String> categories) {
            categoriesCalled = true;
            lastCategories = categories;
        }

        @Override
        public void presentAreas(List<String> areas) {
            areasCalled = true;
            lastAreas = areas;
        }
    }

    // ========== Test for SearchRecipesOutputData ==========

    @Test
    @DisplayName("Test SearchRecipesOutputData constructor and getter")
    void testSearchRecipesOutputData() {
        // Arrange
        Recipe recipe1 = new Recipe("1", "Recipe 1", "Category 1", "Area 1",
                "Instructions 1", "thumb1.jpg", "youtube1.com",
                "source1.com", new String[]{}, new String[]{});
        Recipe recipe2 = new Recipe("2", "Recipe 2", "Category 2", "Area 2",
                "Instructions 2", "thumb2.jpg", "youtube2.com",
                "source2.com", new String[]{}, new String[]{});
        List<Recipe> recipes = Arrays.asList(recipe1, recipe2);

        // Act
        SearchRecipesOutputData outputData = new SearchRecipesOutputData(recipes);

        // Assert
        assertEquals(2, outputData.getRecipes().size());
        assertEquals("Recipe 1", outputData.getRecipes().get(0).getName());
        assertEquals("Recipe 2", outputData.getRecipes().get(1).getName());
    }

    @Test
    @DisplayName("Test SearchRecipesOutputData with empty list")
    void testSearchRecipesOutputDataEmpty() {
        // Act
        SearchRecipesOutputData outputData = new SearchRecipesOutputData(Arrays.asList());

        // Assert
        assertNotNull(outputData.getRecipes());
        assertTrue(outputData.getRecipes().isEmpty());
    }

    // ========== Test for GetAreasUseCase ==========

    @Test
    @DisplayName("Test GetAreasUseCase")
    void testGetAreasUseCase() {
        // Arrange
        TestOutputBoundary testPresenter = new TestOutputBoundary();
        GetAreasUseCase useCase = new GetAreasUseCase(mockDataAccess, testPresenter);

        // Act
        useCase.execute();

        // Assert
        assertTrue(testPresenter.areasCalled);
        assertEquals(6, testPresenter.lastAreas.size());
        assertFalse(testPresenter.failCalled);

        List<String> expectedAreas = Arrays.asList(
                "American", "British", "Italian", "Chinese", "Japanese", "Mexican"
        );
        assertEquals(expectedAreas, testPresenter.lastAreas);
    }

    @Test
    @DisplayName("Test GetAreasUseCase exception handling")
    void testGetAreasUseCaseException() {
        // Arrange - Create a data access that throws exception
        RecipeDataAccessInterface failingDataAccess = new RecipeDataAccessInterface() {
            @Override public List<Recipe> searchRecipesByName(String name) { return Arrays.asList(); }
            @Override public Optional<Recipe> getRecipeById(String id) { return Optional.empty(); }
            @Override public List<Recipe> getRecipesByCategory(String category) { return Arrays.asList(); }
            @Override public List<Recipe> getRecipesByArea(String area) { return Arrays.asList(); }
            @Override public List<String> getAllCategories() { return Arrays.asList(); }
            @Override public List<String> getAllAreas() {
                throw new RuntimeException("Database connection failed");
            }
            @Override public Optional<Recipe> getRandomRecipe() { return Optional.empty(); }
            @Override public List<Recipe> getRecipesByMainIngredient(String ingredient) { return Arrays.asList(); }
            @Override public List<Recipe> searchRecipesByFirstLetter(char letter) { return Arrays.asList(); }
        };

        TestOutputBoundary testPresenter = new TestOutputBoundary();
        GetAreasUseCase useCase = new GetAreasUseCase(failingDataAccess, testPresenter);

        // Act
        useCase.execute();

        // Assert
        assertFalse(testPresenter.areasCalled);
        assertTrue(testPresenter.failCalled);
        assertTrue(testPresenter.lastError.contains("Failed to load areas"));
    }

    // ========== Test for GetCategoriesUseCase ==========

    @Test
    @DisplayName("Test GetCategoriesUseCase")
    void testGetCategoriesUseCase() {
        // Arrange
        TestOutputBoundary testPresenter = new TestOutputBoundary();
        GetCategoriesUseCase useCase = new GetCategoriesUseCase(mockDataAccess, testPresenter);

        // Act
        useCase.execute();

        // Assert
        assertTrue(testPresenter.categoriesCalled);
        assertEquals(6, testPresenter.lastCategories.size());
        assertFalse(testPresenter.failCalled);

        List<String> expectedCategories = Arrays.asList(
                "Dessert", "Main Course", "Appetizer", "Beef", "Chicken", "Vegetarian"
        );
        assertEquals(expectedCategories, testPresenter.lastCategories);
    }
    //Test other path
    @Test
    @DisplayName("Test GetCategoriesUseCase handles exception")
    void testGetCategoriesUseCaseException() {
        // Arrange - Create a dataAccess
        RecipeDataAccessInterface failingDataAccess = new RecipeDataAccessInterface() {
            @Override public List<Recipe> searchRecipesByName(String name) { return Arrays.asList(); }
            @Override public Optional<Recipe> getRecipeById(String id) { return Optional.empty(); }
            @Override public List<Recipe> getRecipesByCategory(String category) { return Arrays.asList(); }
            @Override public List<Recipe> getRecipesByArea(String area) { return Arrays.asList(); }
            @Override public List<String> getAllCategories() {
                throw new RuntimeException("Database connection failed");
            }
            @Override public List<String> getAllAreas() { return Arrays.asList(); }
            @Override public Optional<Recipe> getRandomRecipe() { return Optional.empty(); }
            @Override public List<Recipe> getRecipesByMainIngredient(String ingredient) { return Arrays.asList(); }
            @Override public List<Recipe> searchRecipesByFirstLetter(char letter) { return Arrays.asList(); }
        };

        TestOutputBoundary testPresenter = new TestOutputBoundary();
        GetCategoriesUseCase useCase = new GetCategoriesUseCase(failingDataAccess, testPresenter);

        // Act
        useCase.execute();

        // Assert
        assertFalse(testPresenter.categoriesCalled); // 不应该调用presentCategories
        assertTrue(testPresenter.failCalled);        // 应该调用prepareFailView
        assertTrue(testPresenter.lastError.contains("Failed to load categories"));
        assertTrue(testPresenter.lastError.contains("Database connection failed"));
    }
     // Test Construction
     @Test
     @DisplayName("Test GetCategoriesUseCase constructor")
     void testGetCategoriesUseCaseConstructor() {
         TestOutputBoundary presenter = new TestOutputBoundary();
         GetCategoriesUseCase useCase = new GetCategoriesUseCase(mockDataAccess, presenter);
         useCase.execute();
         assertTrue(presenter.categoriesCalled);
     }

    // ========== Test for GetRandomRecipeUseCase ==========

    @Test
    @DisplayName("Test GetRandomRecipeUseCase")
    void testGetRandomRecipeUseCase() {
        // Arrange
        // Note: GetRandomRecipeUseCase requires RecipePresenter in constructor, but only uses dataAccess
        // We need to create a mock presenter
        RecipePresenter mockPresenter = new RecipePresenter(null); // Can pass null since we don't test presenter

        GetRandomRecipeUseCase useCase = new GetRandomRecipeUseCase(mockDataAccess, mockPresenter);

        // Act
        Optional<Recipe> result = useCase.execute();

        // Assert - Mock returns empty, so should be empty
        assertFalse(result.isPresent());
    }

    // ========== Test for SearchRecipesUseCase ==========

    @Test
    @DisplayName("Test SearchRecipesUseCase name search")
    void testSearchRecipesUseCaseNameSearch() {
        // Arrange
        SearchRecipesUseCase useCase = new SearchRecipesUseCase(mockDataAccess);

        // Act
        List<Recipe> results = useCase.execute("pasta");

        // Assert
        assertEquals(2, results.size());
        assertEquals("Pasta Carbonara", results.get(0).getName());
    }

    @Test
    @DisplayName("Test SearchRecipesUseCase category search")
    void testSearchRecipesUseCaseCategorySearch() {
        // Arrange
        SearchRecipesUseCase useCase = new SearchRecipesUseCase(mockDataAccess);

        // Act
        List<Recipe> results = useCase.execute("Dessert");

        // Assert
        assertEquals(3, results.size());
        assertTrue(results.stream().allMatch(r -> "Dessert".equals(r.getCategory())));
    }

    @Test
    @DisplayName("Test SearchRecipesUseCase area search")
    void testSearchRecipesUseCaseAreaSearch() {
        // Arrange
        SearchRecipesUseCase useCase = new SearchRecipesUseCase(mockDataAccess);

        // Act
        List<Recipe> results = useCase.execute("Italian");

        // Assert
        assertEquals(2, results.size());
        assertTrue(results.stream().allMatch(r -> "Italian".equals(r.getArea())));
    }

    @Test
    @DisplayName("Test SearchRecipesUseCase with null query")
    void testSearchRecipesUseCaseNullQuery() {
        // Arrange
        SearchRecipesUseCase useCase = new SearchRecipesUseCase(mockDataAccess);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.execute(null)
        );
        assertEquals("Search query cannot be empty", exception.getMessage());
    }

    @Test
    @DisplayName("Test SearchRecipesUseCase with empty query")
    void testSearchRecipesUseCaseEmptyQuery() {
        // Arrange
        SearchRecipesUseCase useCase = new SearchRecipesUseCase(mockDataAccess);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.execute("   ")
        );
        assertEquals("Search query cannot be empty", exception.getMessage());
    }

    @Test
    @DisplayName("Test SearchRecipesUseCase with no results")
    void testSearchRecipesUseCaseNoResults() {
        // Arrange
        SearchRecipesUseCase useCase = new SearchRecipesUseCase(mockDataAccess);

        // Act
        List<Recipe> results = useCase.execute("nonexistent");

        // Assert
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    // ========== Integration tests ==========

    @Test
    @DisplayName("Test all use cases together")
    void testAllUseCasesIntegration() {
        // Test SearchRecipesUseCase
        SearchRecipesUseCase searchUseCase = new SearchRecipesUseCase(mockDataAccess);
        List<Recipe> searchResults = searchUseCase.execute("pasta");
        assertEquals(2, searchResults.size());

        // Test GetAreasUseCase
        TestOutputBoundary presenter1 = new TestOutputBoundary();
        GetAreasUseCase areasUseCase = new GetAreasUseCase(mockDataAccess, presenter1);
        areasUseCase.execute();
        assertTrue(presenter1.areasCalled);

        // Test GetCategoriesUseCase
        TestOutputBoundary presenter2 = new TestOutputBoundary();
        GetCategoriesUseCase categoriesUseCase = new GetCategoriesUseCase(mockDataAccess, presenter2);
        categoriesUseCase.execute();
        assertTrue(presenter2.categoriesCalled);

        // Test GetRandomRecipeUseCase
        RecipePresenter mockPresenter = new RecipePresenter(null);
        GetRandomRecipeUseCase randomUseCase = new GetRandomRecipeUseCase(mockDataAccess, mockPresenter);
        Optional<Recipe> randomRecipe = randomUseCase.execute();
        assertFalse(randomRecipe.isPresent());
    }
}