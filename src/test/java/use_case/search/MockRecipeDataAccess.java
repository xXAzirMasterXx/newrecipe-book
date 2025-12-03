package use_case.search;

import use_case.recipe.RecipeDataAccessInterface;
import entity.Recipe;
import java.util.List;
import java.util.Optional;
import java.util.Arrays;

public class MockRecipeDataAccess implements RecipeDataAccessInterface {

    private final List<String> categories = Arrays.asList("Dessert", "Main Course", "Appetizer", "Beef", "Chicken", "Vegetarian");
    private final List<String> areas = Arrays.asList("American", "British", "Italian", "Chinese", "Japanese", "Mexican");

    @Override
    public List<Recipe> searchRecipesByName(String name) {
        if ("pasta".equalsIgnoreCase(name)) {
            return Arrays.asList(
                    createRecipe("1", "Pasta Carbonara", "Main Course", "Italian"),
                    createRecipe("2", "Spaghetti Bolognese", "Main Course", "Italian")
            );
        }
        if ("cake".equalsIgnoreCase(name)) {
            return Arrays.asList(
                    createRecipe("3", "Chocolate Cake", "Dessert", "American")
            );
        }
        return Arrays.asList();
    }

    @Override
    public List<Recipe> getRecipesByCategory(String category) {
        if ("Dessert".equalsIgnoreCase(category)) {
            return Arrays.asList(
                    createRecipe("3", "Chocolate Cake", "Dessert", "American"),
                    createRecipe("4", "Cheesecake", "Dessert", "American"),
                    createRecipe("5", "Apple Pie", "Dessert", "American")
            );
        }
        if ("Main Course".equalsIgnoreCase(category)) {
            return Arrays.asList(
                    createRecipe("1", "Pasta Carbonara", "Main Course", "Italian"),
                    createRecipe("2", "Spaghetti Bolognese", "Main Course", "Italian"),
                    createRecipe("6", "Chicken Curry", "Main Course", "Indian")
            );
        }
        return Arrays.asList();
    }

    @Override
    public List<Recipe> getRecipesByArea(String area) {
        if ("Italian".equalsIgnoreCase(area)) {
            return Arrays.asList(
                    createRecipe("1", "Pasta Carbonara", "Main Course", "Italian"),
                    createRecipe("2", "Spaghetti Bolognese", "Main Course", "Italian")
            );
        }
        if ("American".equalsIgnoreCase(area)) {
            return Arrays.asList(
                    createRecipe("3", "Chocolate Cake", "Dessert", "American"),
                    createRecipe("4", "Cheesecake", "Dessert", "American"),
                    createRecipe("5", "Apple Pie", "Dessert", "American")
            );
        }
        return Arrays.asList();
    }

    @Override
    public List<String> getAllCategories() {
        return categories;
    }

    @Override
    public List<String> getAllAreas() {
        return areas;
    }

    @Override
    public Optional<Recipe> getRecipeById(String id) {
        return Optional.empty();
    }

    @Override
    public Optional<Recipe> getRandomRecipe() {
        return Optional.empty();
    }

    @Override
    public List<Recipe> getRecipesByMainIngredient(String ingredient) {
        return Arrays.asList();
    }

    @Override
    public List<Recipe> searchRecipesByFirstLetter(char letter) {
        return Arrays.asList();
    }

    private Recipe createRecipe(String id, String name, String category, String area) {
        return new Recipe(
                id, name, category, area,
                "Instructions for " + name,
                "https://example.com/" + name.toLowerCase().replace(" ", "_") + ".jpg",
                "https://youtube.com/watch?v=" + name.toLowerCase().replace(" ", ""),
                "https://example.com/recipe/" + id,
                new String[]{"Ingredient1", "Ingredient2"},
                new String[]{"100g", "200ml"}
        );
    }
}