package interface_adapter.add_recipes;

import use_case.add_recipes.AddRecipeInputBoundary;
import use_case.add_recipes.AddRecipeInputData;

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
