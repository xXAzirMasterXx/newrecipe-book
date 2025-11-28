package interface_adapter.convert_units;

import entity.MeasurementSystem;
import use_case.convert_units.ConvertUnitsInputBoundary;
import use_case.convert_units.ConvertUnitsInputData;

public class ConvertUnitsController {

    private final ConvertUnitsInputBoundary interactor;

    public ConvertUnitsController(ConvertUnitsInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(String recipeId,
                        String recipeName,
                        String[] ingredients,
                        String[] measures,
                        MeasurementSystem targetSystem) {

        ConvertUnitsInputData inputData = new ConvertUnitsInputData(
                recipeId,
                recipeName,
                ingredients,
                measures,
                targetSystem
        );

        interactor.execute(inputData);
    }
}
