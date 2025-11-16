
package use_case.add_recipes;

public interface AddRecipeOutputBoundary {
    void prepareSuccessView(AddRecipeOutputData outputData);
    void prepareFailView(String errorMessage);
}
