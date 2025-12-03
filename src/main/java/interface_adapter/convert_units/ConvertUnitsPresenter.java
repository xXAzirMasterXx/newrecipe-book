package interface_adapter.convert_units;

import entity.MeasurementSystem;
import use_case.convert_units.ConvertUnitsOutputBoundary;
import use_case.convert_units.ConvertUnitsOutputData;

/**
 * Presenter for the Convert Units use case.
 * Updates the ConvertUnitsViewModel based on the interactor's output.
 */
public class ConvertUnitsPresenter implements ConvertUnitsOutputBoundary {

    private final ConvertUnitsViewModel viewModel;

    public ConvertUnitsPresenter(ConvertUnitsViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(ConvertUnitsOutputData outputData) {
        ConvertUnitsState state = viewModel.getState();

        state.setRecipeId(outputData.getRecipeId());
        state.setRecipeName(outputData.getRecipeName());
        state.setIngredients(outputData.getIngredients());
        state.setConvertedMeasures(outputData.getConvertedMeasures());
        state.setTargetSystem(outputData.getTargetSystem());

        // We can also set originalMeasures to be the same as converted,
        // or keep it null if your View still has the original.
        // Here we'll assume the original measures are still known by the View,
        // so we don't overwrite them.
        state.setErrorMessage(null);
        state.setSuccess(true);

        // If you have PropertyChangeSupport in your ViewModel,
        // you'd call viewModel.firePropertyChanged() here.
    }

    @Override
    public void prepareFailView(String errorMessage) {
        ConvertUnitsState state = viewModel.getState();

        state.setErrorMessage(errorMessage);
        state.setSuccess(false);

        // Do not change other fields; just reflect the failure.
        // Again, if using PropertyChangeSupport, you'd notify the listeners here.
    }
}
