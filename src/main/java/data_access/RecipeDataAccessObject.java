package data_access;

import entity.Recipe;
import use_case.recipe.RecipeDataAccessInterface;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class RecipeDataAccessObject implements RecipeDataAccessInterface {

    private static final String API_BASE_URL = "https://www.themealdb.com/api/json/v1/1/";

    @Override
    public List<Recipe> searchRecipesByName(String name) {
        try {
            String encodedName = URLEncoder.encode(name, StandardCharsets.UTF_8);
            String urlString = API_BASE_URL + "search.php?s=" + encodedName;
            JSONObject response = makeApiCall(urlString);
            return parseRecipesFromResponse(response);
        } catch (Exception e) {
            System.err.println("Error searching recipes by name: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public Optional<Recipe> getRecipeById(String id) {
        try {
            String urlString = API_BASE_URL + "lookup.php?i=" + id;
            JSONObject response = makeApiCall(urlString);
            List<Recipe> recipes = parseRecipesFromResponse(response);
            return recipes.isEmpty() ? Optional.empty() : Optional.of(recipes.get(0));
        } catch (Exception e) {
            System.err.println("Error getting recipe by ID: " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public List<Recipe> getRecipesByCategory(String category) {
        try {
            String encodedCategory = URLEncoder.encode(category, StandardCharsets.UTF_8);
            String urlString = API_BASE_URL + "filter.php?c=" + encodedCategory;
            JSONObject response = makeApiCall(urlString);
            return parseRecipesFromResponse(response);
        } catch (Exception e) {
            System.err.println("Error getting recipes by category: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<Recipe> getRecipesByArea(String area) {
        try {
            String encodedArea = URLEncoder.encode(area, StandardCharsets.UTF_8);
            String urlString = API_BASE_URL + "filter.php?a=" + encodedArea;
            JSONObject response = makeApiCall(urlString);
            return parseRecipesFromResponse(response);
        } catch (Exception e) {
            System.err.println("Error getting recipes by area: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<String> getAllCategories() {
        List<String> result = new ArrayList<>();

        try {
            URL url = new URL("https://www.themealdb.com/api/json/v1/1/list.php?c=list");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            reader.close();
            connection.disconnect();

            // Parse JSON
            JSONObject json = new JSONObject(response.toString());
            JSONArray mealsArray = json.getJSONArray("meals");

            for (int i = 0; i < mealsArray.length(); i++) {
                result.add(mealsArray.getJSONObject(i).getString("strCategory"));
            }

        } catch (Exception e) {
            System.err.println("ERROR loading categories: " + e.getMessage());
        }

        return result;
    }


    @Override
    public List<String> getAllAreas() {
        List<String> result = new ArrayList<>();

        try {
            URL url = new URL("https://www.themealdb.com/api/json/v1/1/list.php?a=list");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            reader.close();
            connection.disconnect();

            // Parse JSON
            JSONObject json = new JSONObject(response.toString());
            JSONArray mealsArray = json.getJSONArray("meals");

            for (int i = 0; i < mealsArray.length(); i++) {
                result.add(mealsArray.getJSONObject(i).getString("strArea"));
            }

        } catch (Exception e) {
            System.err.println("ERROR loading areas: " + e.getMessage());
        }

        return result;
    }



    @Override
    public Optional<Recipe> getRandomRecipe() {
        try {
            String urlString = API_BASE_URL + "random.php";
            JSONObject response = makeApiCall(urlString);
            List<Recipe> recipes = parseRecipesFromResponse(response);
            return recipes.isEmpty() ? Optional.empty() : Optional.of(recipes.get(0));
        } catch (Exception e) {
            System.err.println("Error getting random recipe: " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public List<Recipe> getRecipesByMainIngredient(String ingredient) {
        try {
            String encodedIngredient = URLEncoder.encode(ingredient, StandardCharsets.UTF_8);
            String urlString = API_BASE_URL + "filter.php?i=" + encodedIngredient;
            JSONObject response = makeApiCall(urlString);
            return parseRecipesFromResponse(response);
        } catch (Exception e) {
            System.err.println("Error getting recipes by ingredient: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<Recipe> searchRecipesByFirstLetter(char letter) {
        try {
            String urlString = API_BASE_URL + "search.php?f=" + letter;
            JSONObject response = makeApiCall(urlString);
            return parseRecipesFromResponse(response);
        } catch (Exception e) {
            System.err.println("Error searching recipes by first letter: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // Helper method to make API calls
    private JSONObject makeApiCall(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(10000);
        connection.setReadTimeout(10000);

        int responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            throw new RuntimeException("HTTP error code: " + responseCode);
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(connection.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            return new JSONObject(response.toString());
        } finally {
            connection.disconnect();
        }
    }

    // Helper method to parse recipes from API response
    private List<Recipe> parseRecipesFromResponse(JSONObject response) {
        List<Recipe> recipes = new ArrayList<>();

        if (!response.has("meals") || response.isNull("meals")) {
            return recipes;
        }

        JSONArray meals = response.getJSONArray("meals");

        for (int i = 0; i < meals.length(); i++) {
            JSONObject meal = meals.getJSONObject(i);
            Recipe recipe = parseRecipe(meal);
            if (recipe != null) {
                recipes.add(recipe);
            }
        }

        return recipes;
    }

    // Helper method to parse a single recipe
    private Recipe parseRecipe(JSONObject meal) {
        try {
            String id = meal.getString("idMeal");
            String name = meal.getString("strMeal");
            String category = meal.optString("strCategory", "");
            String area = meal.optString("strArea", "");
            String instructions = meal.optString("strInstructions", "");
            String imageUrl = meal.optString("strMealThumb", "");
            String youtubeUrl = meal.optString("strYoutube", "");
            String sourceUrl = meal.optString("strSource", "");

            // Parse ingredients and measures
            List<String> ingredients = new ArrayList<>();
            List<String> measures = new ArrayList<>();

            for (int j = 1; j <= 20; j++) {
                String ingredient = meal.optString("strIngredient" + j, "").trim();
                String measure = meal.optString("strMeasure" + j, "").trim();

                if (!ingredient.isEmpty() && !ingredient.equals("null")) {
                    ingredients.add(ingredient);
                    measures.add(measure);
                }
            }

            return new Recipe(
                    id, name, category, area, instructions, imageUrl,
                    youtubeUrl, sourceUrl,
                    ingredients.toArray(new String[0]),
                    measures.toArray(new String[0])
            );

        } catch (Exception e) {
            System.err.println("Error parsing recipe: " + e.getMessage());
            return null;
        }
    }

    // Helper method to parse string lists (categories, areas, etc.)
    private List<String> parseStringListFromResponse(JSONObject response, String key) {
        List<String> result = new ArrayList<>();

        if (!response.has("meals") || response.isNull("meals")) {
            return result;
        }

        JSONArray items = response.getJSONArray("meals");
        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);
            String value = item.optString(key, "");
            if (!value.isEmpty()) {
                result.add(value);
            }
        }

        return result;
    }
}
