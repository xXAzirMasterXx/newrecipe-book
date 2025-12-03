package interface_adapter.logged_in;

import interface_adapter.ViewManagerModel;
import interface_adapter.ingredient_inventory.IngredientInventoryViewModel;
import use_case.change_password.ChangePasswordOutputBoundary;
import use_case.change_password.ChangePasswordOutputData;

/**
 * The Presenter for the Change Password Use Case.
 */
public class ChangePasswordPresenter implements ChangePasswordOutputBoundary {

    private final LoggedInViewModel loggedInViewModel;
    private final ViewManagerModel viewManagerModel;
    private final IngredientInventoryViewModel ingredientInventoryViewModel;

    public ChangePasswordPresenter(ViewManagerModel viewManagerModel,
                             LoggedInViewModel loggedInViewModel,
                                   IngredientInventoryViewModel ingredientInventoryViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.loggedInViewModel = loggedInViewModel;
        this.ingredientInventoryViewModel = ingredientInventoryViewModel;
    }

    @Override
    public void prepareSuccessView(ChangePasswordOutputData outputData) {
        loggedInViewModel.getState().setPassword("");
        loggedInViewModel.getState().setPasswordError(null);
        loggedInViewModel.firePropertyChange("password");
    }

    @Override
    public void prepareFailView(String error) {
        loggedInViewModel.getState().setPasswordError(error);
        loggedInViewModel.firePropertyChange("password");
    }

    @Override
    public void switchToIngredientInventoryView() {
        viewManagerModel.setState(ingredientInventoryViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }
}
