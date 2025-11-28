package interface_adapter.convert_units;

import entity.MeasurementSystem;

/**
 * State for the Convert Units view.
 * Holds the data to be displayed after conversion, plus errors.
 */
public class ConvertUnitsState {

    private String recipeId;
    private String recipeName;
    private String[] ingredients;
    private String[] originalMeasures;
    private String[] convertedMeasures;
    private MeasurementSystem targetSystem;

    private String errorMessage;
    private boolean success;

    public ConvertUnitsState() {
    }

    // Copy constructor (useful if you ever want to clone states)
    public ConvertUnitsState(ConvertUnitsState copy) {
        this.recipeId = copy.recipeId;
        this.recipeName = copy.recipeName;
        this.ingredients = copy.ingredients;
        this.originalMeasures = copy.originalMeasures;
        this.convertedMeasures = copy.convertedMeasures;
        this.targetSystem = copy.targetSystem;
        this.errorMessage = copy.errorMessage;
        this.success = copy.success;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String[] getIngredients() {
        return ingredients;
    }

    public void setIngredients(String[] ingredients) {
        this.ingredients = ingredients;
    }

    public String[] getOriginalMeasures() {
        return originalMeasures;
    }

    public void setOriginalMeasures(String[] originalMeasures) {
        this.originalMeasures = originalMeasures;
    }

    public String[] getConvertedMeasures() {
        return convertedMeasures;
    }

    public void setConvertedMeasures(String[] convertedMeasures) {
        this.convertedMeasures = convertedMeasures;
    }

    public MeasurementSystem getTargetSystem() {
        return targetSystem;
    }

    public void setTargetSystem(MeasurementSystem targetSystem) {
        this.targetSystem = targetSystem;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
