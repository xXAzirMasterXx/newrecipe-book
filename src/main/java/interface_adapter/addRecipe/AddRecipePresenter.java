package interface_adapter.add_recipes;

import interface_adapter.ViewManagerModel;
import use_case.add_recipes.AddRecipeOutputBoundary;
import use_case.add_recipes.AddRecipeOutputData;

public class AddRecipePresenter implements AddRecipeOutputBoundary {

    private final AddRecipeViewModel viewModel;
    private final ViewManagerModel viewManagerModel;

    public AddRecipePresenter(AddRecipeViewModel vm, ViewManagerModel vmm) {
        this.viewModel = vm;
        this.viewManagerModel = vmm;
    }

    @Override
    public void prepareSuccessView(AddRecipeOutputData data) {
        AddRecipeState s = new AddRecipeState(viewModel.getState());
        s.setSuccess("Added: " + data.getName());
        s.setError(null);
        viewModel.setState(s);
        viewModel.firePropertyChange("state");

        viewManagerModel.setState("logged in");
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String error) {
        AddRecipeState s = new AddRecipeState(viewModel.getState());
        s.setError(error);
        s.setSuccess(null);
        viewModel.setState(s);
        viewModel.firePropertyChange("state");
    }
}
