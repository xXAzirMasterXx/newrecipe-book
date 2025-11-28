package use_case.recipe;

import java.util.List;

public interface SearchRecipesOutputBoundary {
    void prepareSuccessView(SearchRecipesOutputData data);
    void prepareFailView(String error);

    void presentCategories(List<String> categories);
    void presentAreas(List<String> areas);
}

