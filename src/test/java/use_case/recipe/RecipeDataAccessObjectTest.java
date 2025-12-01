package use_case.recipe;

// test/RecipeDataAccessObjectTest.java
import use_case.recipe.RecipeDataAccessInterface;
import data_access.RecipeDataAccessObject;
import entity.Recipe;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

public class RecipeDataAccessObjectTest {

    private final RecipeDataAccessInterface dataAccess = new RecipeDataAccessObject();

    @Test
    void testSearchRecipesByName_ValidName_ReturnsRecipes() {
        List<Recipe> recipes = dataAccess.searchRecipesByName("pasta");
        assertFalse(recipes.isEmpty(), "Should find recipes for 'pasta'");

        Recipe firstRecipe = recipes.get(0);
        assertNotNull(firstRecipe.getId());
        assertNotNull(firstRecipe.getName());
        assertNotNull(firstRecipe.getCategory());
    }

    @Test
    void testSearchRecipesByName_InvalidName_ReturnsEmpty() {
        List<Recipe> recipes = dataAccess.searchRecipesByName("xyz123nonexistent");
        assertTrue(recipes.isEmpty(), "Should return empty list for nonexistent recipe");
    }

    @Test
    void testGetRecipeById_ValidId_ReturnsRecipe() {
        Optional<Recipe> recipe = dataAccess.getRecipeById("52772"); // Known ID
        assertTrue(recipe.isPresent(), "Should find recipe with ID 52772");
        assertEquals("Teriyaki Chicken", recipe.get().getName());
    }

    @Test
    void testGetRecipeById_InvalidId_ReturnsEmpty() {
        Optional<Recipe> recipe = dataAccess.getRecipeById("999999");
        assertFalse(recipe.isPresent(), "Should return empty for invalid ID");
    }

    @Test
    void testGetRandomRecipe_ReturnsRecipe() {
        Optional<Recipe> recipe = dataAccess.getRandomRecipe();
        assertTrue(recipe.isPresent(), "Should always return a random recipe");
        assertNotNull(recipe.get().getName());
    }

    @Test
    void testGetAllCategories_ReturnsList() {
        List<String> categories = dataAccess.getAllCategories();
        assertFalse(categories.isEmpty(), "Should return categories");
        assertTrue(categories.contains("Beef") || categories.contains("Chicken"));
    }

    @Test
    void testGetAllAreas_ReturnsList() {
        List<String> areas = dataAccess.getAllAreas();
        assertFalse(areas.isEmpty(), "Should return areas");
        assertTrue(areas.contains("American") || areas.contains("British"));
    }
}