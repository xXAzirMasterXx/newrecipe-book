package use_case.convert_units;

import entity.MeasurementSystem;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConvertUnitsInputDataTest {

    @Test
    void constructorAndGetters_workForNormalValues() {
        // Arrange
        String recipeId = "123";
        String recipeName = "Pancakes";
        String[] ingredients = {"Flour", "Milk", "Eggs"};
        String[] measures = {"200 g", "300 ml", "2 pcs"};
        MeasurementSystem targetSystem = MeasurementSystem.METRIC;

        // Act
        ConvertUnitsInputData inputData = new ConvertUnitsInputData(
                recipeId, recipeName, ingredients, measures, targetSystem
        );

        // Assert
        assertEquals(recipeId, inputData.getRecipeId());
        assertEquals(recipeName, inputData.getRecipeName());
        assertArrayEquals(ingredients, inputData.getIngredients());
        assertArrayEquals(measures, inputData.getMeasures());
        assertEquals(targetSystem, inputData.getTargetSystem());
    }

    @Test
    void constructorAndGetters_supportNullsAndEmptyArrays() {
        // Arrange
        String recipeId = null; // allowed according to comment
        String recipeName = null; // optional
        String[] ingredients = new String[0];
        String[] measures = new String[0];
        MeasurementSystem targetSystem = MeasurementSystem.IMPERIAL;

        // Act
        ConvertUnitsInputData inputData = new ConvertUnitsInputData(
                recipeId, recipeName, ingredients, measures, targetSystem
        );

        // Assert
        assertNull(inputData.getRecipeId());
        assertNull(inputData.getRecipeName());
        assertArrayEquals(ingredients, inputData.getIngredients());
        assertArrayEquals(measures, inputData.getMeasures());
        assertEquals(targetSystem, inputData.getTargetSystem());
    }
}
