package interface_adapter.ingredient_inventory;

import entity.Recipe;

import java.util.ArrayList;
import java.util.List;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class IngredientInventoryState {
    private String username = "";
    private List<String> ingredients = new ArrayList<>();
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    public static final String INGREDIENTS_PROPERTY = "ingredients";


    public String getUsername(){
        return username;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        List<String> old = this.ingredients;
        this.ingredients = ingredients;
        support.firePropertyChange(INGREDIENTS_PROPERTY, old, ingredients);
    }

    public void setUsername(String username){
        this.username = username;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
}
