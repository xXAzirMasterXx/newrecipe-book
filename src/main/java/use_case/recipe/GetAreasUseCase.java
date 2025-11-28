package use_case.recipe;

import use_case.recipe.RecipeDataAccessInterface;
import use_case.recipe.SearchRecipesOutputBoundary;

import java.util.List;

public class GetAreasUseCase {

    private final RecipeDataAccessInterface dataAccess;
    private final SearchRecipesOutputBoundary presenter;

    public GetAreasUseCase(RecipeDataAccessInterface dataAccess,
                           SearchRecipesOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    public void execute() {
        try {
            List<String> areas = dataAccess.getAllAreas();
            presenter.presentAreas(areas);
        } catch (Exception e) {
            presenter.prepareFailView("Failed to load areas: " + e.getMessage());
        }
    }


}
