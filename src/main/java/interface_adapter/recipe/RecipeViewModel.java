package interface_adapter.recipe;


import entity.Recipe;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

public class RecipeViewModel {
    private List<Recipe> recipes;
    private String errorMessage;
    private boolean isLoading;

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    public static final String RECIPES_PROPERTY = "recipes";
    public static final String ERROR_PROPERTY = "error";
    public static final String LOADING_PROPERTY = "loading";

    public List<Recipe> getRecipes() { return recipes; }
    public void setRecipes(List<Recipe> recipes) {
        List<Recipe> old = this.recipes;
        this.recipes = recipes;
        support.firePropertyChange(RECIPES_PROPERTY, old, recipes);
    }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) {
        String old = this.errorMessage;
        this.errorMessage = errorMessage;
        support.firePropertyChange(ERROR_PROPERTY, old, errorMessage);
    }

    public boolean isLoading() { return isLoading; }
    public void setLoading(boolean loading) {
        boolean old = this.isLoading;
        this.isLoading = loading;
        support.firePropertyChange(LOADING_PROPERTY, old, loading);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
}
