package interface_adapter.recipe;

import use_case.recipe.SearchRecipesOutputBoundary;
import use_case.recipe.SearchRecipesOutputData;

public class RecipePresenter implements SearchRecipesOutputBoundary {

    private final RecipeViewModel viewModel;

    public RecipePresenter(RecipeViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(SearchRecipesOutputData data) {
        viewModel.setLoading(false);
        viewModel.setRecipes(data.getRecipes());
    }

    @Override
    public void prepareFailView(String error) {
        viewModel.setLoading(false);
        viewModel.setErrorMessage(error);
    }
}
