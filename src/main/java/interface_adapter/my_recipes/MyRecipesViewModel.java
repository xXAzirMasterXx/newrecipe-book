package interface_adapter.my_recipes;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class MyRecipesViewModel {

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    private MyRecipesState state = new MyRecipesState();

    public MyRecipesState getState() { return state; }

    public void setState(MyRecipesState state) {
        this.state = state;
        support.firePropertyChange("state", null, state);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
}
