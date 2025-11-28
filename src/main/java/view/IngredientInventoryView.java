package view;

import interface_adapter.ingredient_inventory.IngredientInventoryController;
import interface_adapter.ingredient_inventory.IngredientInventoryState;
import interface_adapter.ingredient_inventory.IngredientInventoryViewModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * A panel containing a label and a text field.
 */
public class IngredientInventoryView extends JPanel implements PropertyChangeListener{
    private final String viewName = "ingredient view";
    private final IngredientInventoryViewModel ingredientInventoryViewModel;
    private IngredientInventoryController ingredientInventoryController;


    private final JButton recipe;

    public IngredientInventoryView(IngredientInventoryViewModel ingredientInventoryViewModel) {
        this.ingredientInventoryViewModel = ingredientInventoryViewModel;
        ingredientInventoryViewModel.addPropertyChangeListener(this);

        //editor pane
        JEditorPane ingredientsDisplay = new JEditorPane();
        ingredientsDisplay.setEditable(false);

        final JPanel buttons = new JPanel();
        recipe = new JButton("To Recipes");
        buttons.add(recipe);

        recipe.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        ingredientInventoryController.switchToLoggedInView();
                    }
                }
        );


    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final IngredientInventoryState state = (IngredientInventoryState) evt.getNewValue();
    }

    public String getViewName() {
        return viewName;
    }

    public void setIngredientInventoryController(IngredientInventoryController ingredientInventoryController) {
        this.ingredientInventoryController = ingredientInventoryController;
    }
}