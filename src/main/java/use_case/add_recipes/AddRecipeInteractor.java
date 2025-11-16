package use_case.add_recipes;

import entity.Recipe;
import entity.RecipeFactory;

import java.util.UUID;

public class AddRecipeInteractor implements AddRecipeInputBoundary {

    private final AddRecipeUserDataAccessInterface recipeDataAccess;
    private final AddRecipeOutputBoundary presenter;
    private final RecipeFactory recipeFactory;

    public AddRecipeInteractor(AddRecipeUserDataAccessInterface recipeDataAccess,
                               AddRecipeOutputBoundary presenter,
                               RecipeFactory recipeFactory) {
        this.recipeDataAccess = recipeDataAccess;
        this.presenter = presenter;
        this.recipeFactory = recipeFactory;
    }

    @Override
    public void execute(AddRecipeInputData inputData) {

        // 1. Required fields
        if (inputData.getName() == null || inputData.getName().trim().isEmpty()) {
            presenter.prepareFailView("Recipe name is required.");
            return;
        }
        if (inputData.getIngredients() == null || inputData.getIngredients().length == 0) {
            presenter.prepareFailView("At least one ingredient is required.");
            return;
        }
        if (inputData.getInstructions() == null || inputData.getInstructions().trim().isEmpty()) {
            presenter.prepareFailView("Instructions are required.");
            return;
        }

        // 2. Cooking time must be numeric
        int cookingTime;
        try {
            cookingTime = Integer.parseInt(inputData.getCookingTime());
            if (cookingTime <= 0) {
                presenter.prepareFailView("Cooking time must be positive.");
                return;
            }
        } catch (NumberFormatException e) {
            presenter.prepareFailView("Cooking time must be a number.");
            return;
        }

        // 3. Duplicate name handling
        boolean overwrite = inputData.isOverwrite();
        if (recipeDataAccess.existsByName(inputData.getName())) {
            if (!overwrite) {
                presenter.prepareFailView("Recipe already exists. Confirm overwrite.");
                return;
            }
        }

        // 4. Create Recipe entity
        String id = UUID.randomUUID().toString();

        Recipe recipe = recipeFactory.create(
                id,
                inputData.getName(),
                inputData.getCategory(),
                inputData.getArea(),
                inputData.getInstructions(),
                inputData.getImageUrl(),
                inputData.getYoutubeUrl(),
                inputData.getSourceUrl(),
                inputData.getIngredients(),
                inputData.getMeasures()
        );

        // 5. Save
        recipeDataAccess.save(recipe);

        // 6. Success
        AddRecipeOutputData outputData =
                new AddRecipeOutputData(id, inputData.getName(), cookingTime, overwrite);

        presenter.prepareSuccessView(outputData);
    }
}
