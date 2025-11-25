package interface_adapter.recipe;

import use_case.recipe.SearchRecipesOutputBoundary;
import use_case.recipe.SearchRecipesOutputData;

import java.util.List;

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

    @Override
    public void presentCategories(List<String> categories) {
        System.out.println("DEBUG: Presenter received categories: " + categories);

        viewModel.setCategoryOptions(categories);
        viewModel.firePropertyChanged(RecipeViewModel.CATEGORY_PROPERTY);
    }


    @Override
    public void presentAreas(List<String> areas) {
        System.out.println("DEBUG: Presenter received areas: " + areas);

        viewModel.setAreaOptions(areas);
        viewModel.firePropertyChanged(RecipeViewModel.AREA_PROPERTY);
    }

}
