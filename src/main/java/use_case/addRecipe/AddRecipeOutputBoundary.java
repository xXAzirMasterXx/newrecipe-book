
package use_case.addRecipe;

public interface AddRecipeOutputBoundary {
    void prepareSuccessView(AddRecipeOutputData outputData);
    void prepareFailView(String errorMessage);
}
