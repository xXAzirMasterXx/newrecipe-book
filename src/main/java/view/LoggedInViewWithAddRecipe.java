package view;

import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.recipe.RecipeController;
import interface_adapter.recipe.RecipeViewModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Extend the original LoggedInView by adding an 'Add Recipe' button at the bottom.
 */
public class LoggedInViewWithAddRecipe extends LoggedInView {

    private final ViewManagerModel viewManagerModel;
    private final JButton addRecipeButton = new JButton("Add Recipe");

    public LoggedInViewWithAddRecipe(LoggedInViewModel loggedInViewModel,
                                     RecipeViewModel recipeViewModel,
                                     RecipeController recipeController,
                                     ViewManagerModel viewManagerModel) {

        // Call the parent class constructor to create the original UI
        super(loggedInViewModel, recipeViewModel, recipeController);
        this.viewManagerModel = viewManagerModel;

        // Add a new button to the last row of the entire Panel
        // LoggedInView itself uses BoxLayout(Y_AXIS), here we directly add another row of buttons at the very bottom
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(addRecipeButton);
        this.add(bottomPanel);

        addRecipeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewManagerModel.setState("add recipe");
                viewManagerModel.firePropertyChange();
            }
        });
    }
}
