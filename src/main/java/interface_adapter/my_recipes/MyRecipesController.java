package interface_adapter.my_recipes;

import use_case.my_recipes.MyRecipesInputBoundary;

public class MyRecipesController {

    private final MyRecipesInputBoundary interactor;

    public MyRecipesController(MyRecipesInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute() {
        interactor.showMyRecipes();
    }
}
