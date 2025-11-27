package use_case.my_recipes;

import data_access.AddRecipeInMemoryDataAccessObject;
import entity.Recipe;

import java.util.ArrayList;
import java.util.List;

public class MyRecipesInteractor implements MyRecipesInputBoundary {

    private final AddRecipeInMemoryDataAccessObject addRecipeDAO;
    private final MyRecipesOutputBoundary presenter;

    public MyRecipesInteractor(AddRecipeInMemoryDataAccessObject addRecipeDAO,
                               MyRecipesOutputBoundary presenter) {
        this.addRecipeDAO = addRecipeDAO;
        this.presenter = presenter;
    }

    @Override
    public void showMyRecipes() {
        List<Recipe> result = new ArrayList<>(addRecipeDAO.getAll().values());

        if (result.isEmpty()) {
            presenter.prepareFailView("You have not added any recipes yet.");
        } else {
            presenter.prepareSuccessView(new MyRecipesOutputData(result));
        }
    }
}
