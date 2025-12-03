package test;

import data_access.RecipeDataAccessObject;
import use_case.recipe.SearchRecipesUseCase;
import entity.Recipe;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class SearchRecipesIntegrationTest {

    @Test
    void testSearchRecipesByName_RealAPI() {
        // Given
        SearchRecipesUseCase searchUseCase = new SearchRecipesUseCase(new RecipeDataAccessObject());

        // When
        List<Recipe> results = searchUseCase.execute("pasta");

        // Then
        assertNotNull(results);
        assertFalse(results.isEmpty(), "Should find pasta recipes from real API");

        // Verify recipe structure
        Recipe firstRecipe = results.get(0);
        assertNotNull(firstRecipe.getId());
        assertNotNull(firstRecipe.getName());
        assertNotNull(firstRecipe.getCategory());
        assertNotNull(firstRecipe.getArea());

        System.out.println("Found " + results.size() + " pasta recipes");
        System.out.println("First recipe: " + firstRecipe.getName() + " (" + firstRecipe.getCategory() + ")");
    }

    @Test
    void testSearchRecipesByCategory_RealAPI() {
        // Given
        SearchRecipesUseCase searchUseCase = new SearchRecipesUseCase(new RecipeDataAccessObject());

        // When - search for Dessert category
        List<Recipe> results = searchUseCase.execute("Dessert");

        // Then
        assertNotNull(results);
        assertFalse(results.isEmpty(), "Should find dessert recipes from real API");

        // All results should be desserts
        boolean allDesserts = results.stream()
                .allMatch(recipe -> "Dessert".equalsIgnoreCase(recipe.getCategory()));
        assertTrue(allDesserts, "All results should be in Dessert category");

        System.out.println("Found " + results.size() + " dessert recipes");
    }

    @Test
    void testSearchRecipesByArea_RealAPI() {
        // Given
        SearchRecipesUseCase searchUseCase = new SearchRecipesUseCase(new RecipeDataAccessObject());

        // When - search for Italian area
        List<Recipe> results = searchUseCase.execute("Italian");

        // Then
        assertNotNull(results);
        assertFalse(results.isEmpty(), "Should find Italian recipes from real API");

        // All results should be Italian
        boolean allItalian = results.stream()
                .allMatch(recipe -> "Italian".equalsIgnoreCase(recipe.getArea()));
        assertTrue(allItalian, "All results should be from Italian area");

        System.out.println("Found " + results.size() + " Italian recipes");
    }
}