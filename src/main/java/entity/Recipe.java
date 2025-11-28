package entity;

/**
 * A simple entity representing a Recipe.
 * Recipes have an id, name, category, area (cuisine), instructions, image link,
 * and key ingredients with their measures.
 */
public class Recipe {

    private final String id;
    private final String name;
    private final String category;
    private final String area;
    private final String instructions;
    private final String imageUrl;
    private final String youtubeUrl;
    private final String sourceUrl;

    private final String[] ingredients;
    private final String[] measures;
    private int cookingTime;

    /**
     * Creates a new recipe entity with all the required fields.
     * @param id the recipe ID
     * @param name the recipe name
     * @param category the recipe category (e.g., "Vegetarian")
     * @param area the region/cuisine (e.g., "Greek")
     * @param instructions the preparation instructions
     * @param imageUrl the URL of the meal image
     * @param youtubeUrl a related YouTube link
     * @param sourceUrl the source link of the recipe
     * @param ingredients array of ingredient names
     * @param measures array of corresponding measurements
     * @throws IllegalArgumentException if id or name are empty
     */
    public Recipe(String id, String name, String category, String area,
                  String instructions, String imageUrl, String youtubeUrl,
                  String sourceUrl, String[] ingredients, String[] measures) {

        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Recipe ID cannot be empty");
        }
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Recipe name cannot be empty");
        }

        this.id = id;
        this.name = name;
        this.category = category;
        this.area = area;
        this.instructions = instructions;
        this.imageUrl = imageUrl;
        this.youtubeUrl = youtubeUrl;
        this.sourceUrl = sourceUrl;
        this.ingredients = ingredients;
        this.measures = measures;
        this.cookingTime = 0;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getArea() {
        return area;
    }

    public String getInstructions() {
        return instructions;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getYoutubeUrl() {
        return youtubeUrl;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public String[] getIngredients() {
        return ingredients;
    }

    public String[] getMeasures() {
        return measures;
    }

    public int getCookingTime() {
        return cookingTime;
    }

    public void setCookingTime(int cookingTime) {
        this.cookingTime = cookingTime;
    }

}

