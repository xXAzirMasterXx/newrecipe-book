package view;

import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.recipe.RecipeController;
import interface_adapter.recipe.RecipeViewModel;
import interface_adapter.my_recipes.MyRecipesController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Extend the original LoggedInView by adding an 'Add Recipe' and 'My Recipes' button at the bottom.
 */
public class LoggedInViewWithAddRecipe extends LoggedInView {

    private final ViewManagerModel viewManagerModel;
    private MyRecipesController myRecipesController;   // null
    private final JButton addRecipeButton = new JButton("Add Recipe");
    private final JButton myRecipesButton = new JButton("My Recipes");

    // setter 保留
    public void setMyRecipesController(MyRecipesController controller) {
        this.myRecipesController = controller;
    }

    public LoggedInViewWithAddRecipe(LoggedInViewModel loggedInViewModel,
                                     RecipeViewModel recipeViewModel,
                                     RecipeController recipeController,
                                     ViewManagerModel viewManagerModel) {

        super(loggedInViewModel, recipeViewModel, recipeController);
        this.viewManagerModel = viewManagerModel;

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(addRecipeButton);
        bottomPanel.add(myRecipesButton);
        this.add(bottomPanel);

        addRecipeButton.addActionListener(e -> {
            viewManagerModel.setState("add recipe");
            viewManagerModel.firePropertyChange();
        });

        myRecipesButton.addActionListener(e -> {
            if (myRecipesController != null) {
                myRecipesController.execute();
            } else {
                System.out.println("MyRecipesController is null!");
            }
        });
    }
}

