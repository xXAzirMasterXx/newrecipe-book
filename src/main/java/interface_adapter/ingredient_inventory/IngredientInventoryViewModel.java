package interface_adapter.ingredient_inventory;

import interface_adapter.ViewModel;
import use_case.ingredient_inventory.IngredientInventoryOutputData;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;

public class IngredientInventoryViewModel extends ViewModel<IngredientInventoryState> {

    public IngredientInventoryViewModel(){
        super("ingredient view");
        setState(new IngredientInventoryState());
    }
}
