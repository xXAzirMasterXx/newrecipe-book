package use_case.recipe;

import java.util.List;

public class GetCategoriesUseCase {

    private final RecipeDataAccessInterface dataAccess;
    private final SearchRecipesOutputBoundary presenter;

    public GetCategoriesUseCase(RecipeDataAccessInterface dataAccess,
                                SearchRecipesOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    public void execute() {
        try {
            List<String> categories = dataAccess.getAllCategories();
            presenter.presentCategories(categories);
        } catch (Exception e) {
            presenter.prepareFailView("Failed to load categories: " + e.getMessage());
        }
    }

}
