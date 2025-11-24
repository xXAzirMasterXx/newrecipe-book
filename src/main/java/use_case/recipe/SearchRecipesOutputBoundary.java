package use_case.recipe;

public interface SearchRecipesOutputBoundary {
    void prepareSuccessView(SearchRecipesOutputData data);
    void prepareFailView(String error);
}
