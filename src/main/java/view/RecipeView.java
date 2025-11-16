package view;

import interface_adapter.recipe.RecipeController;
import interface_adapter.recipe.RecipeViewModel;
import entity.Recipe;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class RecipeView extends JPanel {

    private final JTextField searchInput = new JTextField(15);
    private final JButton searchButton = new JButton("Search");
    private final JButton randomButton = new JButton("Random Recipe");

    private final RecipeController controller;
    private final RecipeViewModel viewModel;
    private final DefaultListModel<String> resultsModel = new DefaultListModel<>();
    private final JList<String> resultsList = new JList<>(resultsModel);

    public RecipeView(RecipeController controller, RecipeViewModel viewModel) {
        this.controller = controller;
        this.viewModel = viewModel;

        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Search:"));
        topPanel.add(searchInput);
        topPanel.add(searchButton);
        topPanel.add(randomButton);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(resultsList), BorderLayout.CENTER);

        // listen to viewmodel updates
        viewModel.addPropertyChangeListener(evt -> {
            switch (evt.getPropertyName()) {
                case RecipeViewModel.RECIPES_PROPERTY:
                    updateResults(viewModel.getRecipes());
                    break;
                case RecipeViewModel.ERROR_PROPERTY:
                    JOptionPane.showMessageDialog(this, viewModel.getErrorMessage());
                    break;
            }
        });

        searchButton.addActionListener(e -> searchRecipes());
        randomButton.addActionListener(e -> loadRandomRecipe());
    }

    private void searchRecipes() {
        String term = searchInput.getText();
        List<Recipe> recipes = controller.searchRecipes(term);

        if (recipes.isEmpty()) {
            viewModel.setErrorMessage("No results found.");
        } else {
            viewModel.setRecipes(recipes);
        }
    }

    private void loadRandomRecipe() {
        controller.getRandomRecipe().ifPresentOrElse(
                recipe -> viewModel.setRecipes(List.of(recipe)),
                () -> viewModel.setErrorMessage("Failed to load random recipe.")
        );
    }

    private void updateResults(List<Recipe> recipes) {
        resultsModel.clear();
        for (Recipe r : recipes) {
            resultsModel.addElement(r.getName());
        }
    }
}
