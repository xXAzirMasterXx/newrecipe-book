package interface_adapter.recipe;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import entity.Recipe;

public class RecipeViewModel {

    public static final String RECIPES_PROPERTY = "recipes";
    public static final String ERROR_PROPERTY = "error";
    public static final String LOADING_PROPERTY = "loading";
    public static final String DROPDOWN_PROPERTY = "dropdown";
    public static final String CATEGORY_PROPERTY = "categories";
    public static final String AREA_PROPERTY = "areas";

    private List<Recipe> recipes = new ArrayList<>();
    private String errorMessage = null;
    private boolean loading = false;
    private List<String> dropdownOptions = new ArrayList<>();
    private List<String> categoryOptions = new ArrayList<>();
    private List<String> areaOptions = new ArrayList<>();


    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
        support.firePropertyChange(RECIPES_PROPERTY, null, recipes);
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        support.firePropertyChange(ERROR_PROPERTY, null, errorMessage);
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
        support.firePropertyChange(LOADING_PROPERTY, null, loading);
    }

    public boolean isLoading() {
        return loading;
    }

    public void setDropdownOptions(List<String> options) {
        this.dropdownOptions = options;
        support.firePropertyChange(DROPDOWN_PROPERTY, null, options);
    }

    public List<String> getDropdownOptions() {
        return dropdownOptions;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
    public void setCategoryOptions(List<String> options) {
        this.categoryOptions = options;
        support.firePropertyChange(CATEGORY_PROPERTY, null, options);
    }

    public void setAreaOptions(List<String> options) {
        this.areaOptions = options;
        support.firePropertyChange(AREA_PROPERTY, null, options);
    }

    public List<String> getCategoryOptions() {
        return categoryOptions;
    }

    public List<String> getAreaOptions() {
        return areaOptions;
    }

    public void firePropertyChanged(String propertyName) {
        support.firePropertyChange(propertyName, null, null);
    }

}
