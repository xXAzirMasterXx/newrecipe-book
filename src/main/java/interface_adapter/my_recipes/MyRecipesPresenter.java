package interface_adapter.my_recipes;

import use_case.my_recipes.MyRecipesOutputBoundary;
import use_case.my_recipes.MyRecipesOutputData;
import interface_adapter.ViewManagerModel;

public class MyRecipesPresenter implements MyRecipesOutputBoundary {

    private final MyRecipesViewModel viewModel;
    private final ViewManagerModel viewManagerModel;

    public MyRecipesPresenter(MyRecipesViewModel viewModel,
                              ViewManagerModel viewManagerModel) {
        this.viewModel = viewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void prepareSuccessView(MyRecipesOutputData data) {
        MyRecipesState state = new MyRecipesState();
        state.setRecipes(data.getRecipes());
        viewModel.setState(state);

        // 跳转到 "my recipes" 这个 view
        viewManagerModel.setState("my recipes");
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String error) {
        MyRecipesState state = new MyRecipesState();
        state.setError(error);
        viewModel.setState(state);

        viewManagerModel.setState("my recipes");
        viewManagerModel.firePropertyChange();
    }
}
