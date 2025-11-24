
package use_case.add_recipe;

public interface AddRecipeOutputBoundary {
    void prepareSuccessView(AddRecipeOutputData outputData);
    void prepareFailView(String errorMessage);
}
