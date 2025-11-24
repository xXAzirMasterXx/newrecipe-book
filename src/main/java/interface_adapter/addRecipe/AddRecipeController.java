package interface_adapter.addRecipe;

import use_case.addRecipe.AddRecipeInputBoundary;
import use_case.addRecipe.AddRecipeInputData;

public class AddRecipeController {

    private final AddRecipeInputBoundary interactor;

    public AddRecipeController(AddRecipeInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(String name, String category, String area,
                        String instructions, String imageUrl, String youtubeUrl,
                        String sourceUrl, String[] ingredients, String[] measures,
                        String cookingTime, boolean overwrite) {

        AddRecipeInputData data = new AddRecipeInputData(
                name, category, area, instructions, imageUrl,
                youtubeUrl, sourceUrl, ingredients, measures, cookingTime, overwrite
        );

        interactor.execute(data);
    }
}
