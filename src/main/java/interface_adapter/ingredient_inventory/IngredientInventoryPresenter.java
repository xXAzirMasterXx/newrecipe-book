package interface_adapter.ingredient_inventory;

import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInViewModel;
import use_case.ingredient_inventory.IngredientInventoryOutputBoundary;
import use_case.ingredient_inventory.IngredientInventoryOutputData;

public class IngredientInventoryPresenter implements IngredientInventoryOutputBoundary {

    private final IngredientInventoryViewModel ingredientInventoryViewModel;
    private final ViewManagerModel viewManagerModel;
    private final LoggedInViewModel loggedInViewModel;

    public IngredientInventoryPresenter(IngredientInventoryViewModel ingredientInventoryViewModel,
                                        ViewManagerModel viewManagerModel,
                                        LoggedInViewModel loggedInViewModel){
        this.ingredientInventoryViewModel = ingredientInventoryViewModel;
        this.viewManagerModel = viewManagerModel;
        this.loggedInViewModel = loggedInViewModel;
    }

    public void prepareSuccessView(IngredientInventoryOutputData response){

    }

    @Override
    public void switchToLoggedinView() {
        viewManagerModel.setState(loggedInViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }
}
