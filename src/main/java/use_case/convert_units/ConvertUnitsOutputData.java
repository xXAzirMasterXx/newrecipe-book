package use_case.convert_units;

import entity.MeasurementSystem;

/**
 * Output data for the Convert Units use case.
 * Contains the converted measures and any metadata
 * useful for the presenter/view.
 */
public class ConvertUnitsOutputData {

    private final String recipeId;
    private final String recipeName;
    private final String[] ingredients;
    private final String[] convertedMeasures;
    private final MeasurementSystem targetSystem;

    public ConvertUnitsOutputData(String recipeId,
                                  String recipeName,
                                  String[] ingredients,
                                  String[] convertedMeasures,
                                  MeasurementSystem targetSystem) {
        this.recipeId = recipeId;
        this.recipeName = recipeName;
        this.ingredients = ingredients;
        this.convertedMeasures = convertedMeasures;
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

    public String[] getConvertedMeasures() {
        return convertedMeasures;
    }

    public MeasurementSystem getTargetSystem() {
        return targetSystem;
    }
}
