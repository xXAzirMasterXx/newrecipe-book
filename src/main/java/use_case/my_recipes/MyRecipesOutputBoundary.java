package use_case.my_recipes;

public interface MyRecipesOutputBoundary {
    void prepareSuccessView(MyRecipesOutputData data);
    void prepareFailView(String error);
}
