package use_case.convert_units;

import entity.MeasurementSystem;

/**
 * Input data for the Convert Units use case.
 * Represents the information the user provides when they
 * request a unit conversion (e.g., toggle to METRIC).
 */
public class ConvertUnitsInputData {

    private final String recipeId;          // can be null if not using IDs
    private final String recipeName;        // optional, nice for UI
    private final String[] ingredients;     // ingredient names
    private final String[] measures;        // original measures as strings
    private final MeasurementSystem targetSystem;

    public ConvertUnitsInputData(String recipeId,
                                 String recipeName,
                                 String[] ingredients,
                                 String[] measures,
                                 MeasurementSystem targetSystem) {
        this.recipeId = recipeId;
        this.recipeName = recipeName;
        this.ingredients = ingredients;
        this.measures = measures;
        this.targetSystem = targetSystem;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public String[] getIngredients() {
        return ingredients;
    }

    public String[] getMeasures() {
        return measures;
    }

    public MeasurementSystem getTargetSystem() {
        return targetSystem;
    }
}
