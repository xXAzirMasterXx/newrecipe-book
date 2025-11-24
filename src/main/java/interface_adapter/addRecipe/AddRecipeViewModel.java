package interface_adapter.add_recipes;

import interface_adapter.ViewModel;

public class AddRecipeViewModel extends ViewModel<AddRecipeState> {

    public AddRecipeViewModel() {
        super("add recipe");
        setState(new AddRecipeState());
    }
}
