
package use_case.addRecipe;

import entity.Recipe;

/**
 * Data access interface for the Add Recipe use case.
 * The concrete implementation will live in the data_access package.
 */
public interface AddRecipeUserDataAccessInterface {

    /**
     * Check whether a recipe with the given name already exists.
     */
    boolean existsByName(String name);

    /**
     * Save a recipe (insert or overwrite).
     */
    void save(Recipe recipe);
}