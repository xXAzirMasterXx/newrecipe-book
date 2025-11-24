package use_case.add_recipe;

public class AddRecipeInputData {

    private final String name;
    private final String category;
    private final String area;
    private final String instructions;
    private final String imageUrl;
    private final String youtubeUrl;
    private final String sourceUrl;
    private final String[] ingredients;

    // original measures as given (e.g. from TheMealDB)
    private final String[] measures;

    // NEW: normalized/converted measures
    // - metric: g / mL
    // - imperial: oz / lb / tbsp / tsp
    private final String[] measuresMetric;
    private final String[] measuresImperial;

    private final String cookingTime;
    private final boolean overwrite;   // 用户是否选择覆盖重名 recipe

    public AddRecipeInputData(String name,
                              String category,
                              String area,
                              String instructions,
                              String imageUrl,
                              String youtubeUrl,
                              String sourceUrl,
                              String[] ingredients,
                              String[] measures,
                              String cookingTime,
                              boolean overwrite) {
        this.name = name;
        this.category = category;
        this.area = area;
        this.instructions = instructions;
        this.imageUrl = imageUrl;
        this.youtubeUrl = youtubeUrl;
        this.sourceUrl = sourceUrl;
        this.ingredients = ingredients;
        this.measures = measures;
        this.cookingTime = cookingTime;
        this.overwrite = overwrite;

        // auto-generate converted measures
        this.measuresMetric = UnitConversionUtils.toMetric(measures);
        this.measuresImperial = UnitConversionUtils.toImperial(measures);
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

    /** Original measures from API / user input. */
    public String[] getMeasures() {
        return measures;
    }

    /** Measures converted to metric (g / mL) where possible. */
    public String[] getMeasuresMetric() {
        return measuresMetric;
    }

    /** Measures converted to imperial (oz / lb / tbsp / tsp) where possible. */
    public String[] getMeasuresImperial() {
        return measuresImperial;
    }

    public String getCookingTime() {
        return cookingTime;
    }

    public boolean isOverwrite() {
        return overwrite;
    }
}
