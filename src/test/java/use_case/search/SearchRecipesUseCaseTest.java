package use_case.search;

import entity.Recipe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.recipe.RecipeDataAccessInterface;
import use_case.recipe.SearchRecipesUseCase;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;


public class SearchRecipesUseCaseTest {

    private SearchRecipesUseCase searchRecipesUseCase;

    @BeforeEach
    void setUp() {
        RecipeDataAccessInterface mockDataAccess = new MockRecipeDataAccess();
        searchRecipesUseCase = new SearchRecipesUseCase(mockDataAccess);
    }

    @Test
    void testExecute_NameSearch_ReturnsRecipes() {
        List<Recipe> result = searchRecipesUseCase.execute("pasta");

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Pasta Carbonara", result.get(0).getName());
        assertEquals("Spaghetti Bolognese", result.get(1).getName());
    }

    @Test
    void testExecute_CategorySearch_ReturnsCategoryRecipes() {
        List<Recipe> result = searchRecipesUseCase.execute("Dessert");

        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.stream().allMatch(recipe ->
                "Dessert".equals(recipe.getCategory())));
    }

    @Test
    void testExecute_AreaSearch_ReturnsAreaRecipes() {
        List<Recipe> result = searchRecipesUseCase.execute("Italian");

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(recipe ->
                "Italian".equals(recipe.getArea())));
    }

    @Test
    void testExecute_EmptyQuery_ThrowsException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> searchRecipesUseCase.execute("")
        );
        assertEquals("Search query cannot be empty", exception.getMessage());
    }

    @Test
    void testExecute_NoResults_ReturnsEmptyList() {
        List<Recipe> result = searchRecipesUseCase.execute("nonexistent");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}