package view;

import interface_adapter.ingredient_inventory.IngredientInventoryController;
import interface_adapter.ingredient_inventory.IngredientInventoryState;
import interface_adapter.logged_in.ChangePasswordController;
import interface_adapter.logged_in.LoggedInState;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.logout.LogoutController;
import interface_adapter.add_ingredients.AddIngredientController;
import interface_adapter.remove_ingredients.RemoveIngredientController;

// Recipe-related imports
import interface_adapter.recipe.RecipeController;
import interface_adapter.recipe.RecipeViewModel;
import entity.Recipe;
import entity.FavoriteManager;

// For unit conversions
import interface_adapter.convert_units.ConvertUnitsController;
import interface_adapter.convert_units.ConvertUnitsViewModel;
import entity.MeasurementSystem;


import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.net.URL;
import java.awt.Image;


public class LoggedInView extends JPanel implements ActionListener, PropertyChangeListener {

    private final String viewName = "logged in";
    private final LoggedInViewModel loggedInViewModel;
    private final RecipeViewModel recipeViewModel;
    private final IngredientInventoryState ingredientInventoryState;
    private final JLabel passwordErrorField = new JLabel();
    private ChangePasswordController changePasswordController = null;
    private LogoutController logoutController;
    private RecipeController recipeController;
    private final JComboBox<String> categoryDropdown;
    private JComboBox<String> areaDropdown;
    private ConvertUnitsController convertUnitsController;
    private ConvertUnitsViewModel convertUnitsViewModel;




    private final IngredientInventoryController ingredientInventoryController;
    private AddIngredientController addIngredientController;
    private RemoveIngredientController removeIngredientController;

    private final JLabel username;

    // Recipe components
    private final JTextField searchField = new JTextField(20);
    private final JButton searchButton = new JButton("Search Recipes");
    private final JButton randomButton = new JButton("Random Recipe");
    private final JButton ingredientButton = new JButton("Ingredient View");
    private final JPanel resultsPanel = new JPanel();
    private final JLabel statusLabel = new JLabel("Welcome! Search for recipes or get a random one.");

    private final JTextField addIngredientField = new JTextField(15);
    private final JButton addIngredientButton = new JButton("Add Ingredient");

    private final JTextField removeIngredientField = new JTextField(15);
    private final JButton removeIngredientButton = new JButton("Remove Ingredient");

    private final JButton logOut;
    private final JTextField passwordInputField = new JTextField(15);
    private final JButton changePassword;

    public LoggedInView(LoggedInViewModel loggedInViewModel, RecipeViewModel recipeViewModel, RecipeController recipeController, IngredientInventoryState ingredientInventoryState, IngredientInventoryController ingredientInventoryController) {
        this.loggedInViewModel = loggedInViewModel;
        this.recipeViewModel = recipeViewModel;
        this.recipeController = recipeController;
        this.ingredientInventoryState = ingredientInventoryState;
        this.ingredientInventoryController = ingredientInventoryController;

        this.loggedInViewModel.addPropertyChangeListener(this);
        this.recipeViewModel.addPropertyChangeListener(this);
        this.ingredientInventoryState.addPropertyChangeListener(this);

        // Create dropdown BEFORE adding to panel
        categoryDropdown = new JComboBox<>();
        areaDropdown = new JComboBox<>();

        final JLabel title = new JLabel("Recipe Explorer");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Arial", Font.BOLD, 18));

        // Recipe Search Section
        final JPanel searchPanel = new JPanel();
        searchPanel.setBorder(BorderFactory.createTitledBorder("Recipe Search"));
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.Y_AXIS));

        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row1.add(new JLabel("Search:"));
        row1.add(searchField);
        row1.add(searchButton);
        row1.add(randomButton);
        row1.add(ingredientButton);
        searchPanel.add(row1);

        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row2.add(new JLabel("Category:"));
        row2.add(categoryDropdown);
        row2.add(new JLabel("Area:"));
        row2.add(areaDropdown);
        searchPanel.add(row2);

        JPanel row3 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        final JButton favoritesButton = new JButton("My Favorites");
        row3.add(favoritesButton);
        searchPanel.add(row3);

        // Results Panel
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
        resultsPanel.setBorder(BorderFactory.createTitledBorder("Recipes"));
        JScrollPane scrollPane = new JScrollPane(resultsPanel);
        scrollPane.setPreferredSize(new Dimension(600, 300));

        final JPanel addIngredientPanel = new JPanel();
        addIngredientPanel.setBorder(BorderFactory.createTitledBorder("Add Ingredient"));
        addIngredientPanel.add(new JLabel("Ingredient:"));
        addIngredientPanel.add(addIngredientField);
        addIngredientPanel.add(addIngredientButton);

        final JPanel removeIngredientPanel = new JPanel();
        removeIngredientPanel.setBorder(BorderFactory.createTitledBorder("Remove Ingredient"));
        removeIngredientPanel.add(new JLabel("Ingredient:"));
        removeIngredientPanel.add(removeIngredientField);
        removeIngredientPanel.add(removeIngredientButton);

        final JPanel manageIngredientsPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        manageIngredientsPanel.setBorder(BorderFactory.createTitledBorder("Manage Ingredients"));
        manageIngredientsPanel.add(addIngredientPanel);
        manageIngredientsPanel.add(removeIngredientPanel);

        final JPanel userPanel = new JPanel();
        userPanel.setBorder(BorderFactory.createTitledBorder("Account Management"));
        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));

        final LabelTextPanel passwordInfo = new LabelTextPanel(
                new JLabel("New Password"), passwordInputField);

        final JLabel usernameInfo = new JLabel("Currently logged in: ");
        username = new JLabel();

        final JPanel buttons = new JPanel();
        logOut = new JButton("Log Out");
        buttons.add(logOut);

        changePassword = new JButton("Change Password");
        buttons.add(changePassword);

        userPanel.add(usernameInfo);
        userPanel.add(username);
        userPanel.add(passwordInfo);
        userPanel.add(passwordErrorField);
        userPanel.add(buttons);

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        logOut.addActionListener(this);

        passwordInputField.getDocument().addDocumentListener(new DocumentListener() {
            private void documentListenerHelper() {
                final LoggedInState currentState = loggedInViewModel.getState();
                currentState.setPassword(passwordInputField.getText());
                loggedInViewModel.setState(currentState);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                documentListenerHelper();
            }
        });

        changePassword.addActionListener(
                evt -> {
                    if (evt.getSource().equals(changePassword)) {
                        final LoggedInState currentState = loggedInViewModel.getState();

                        if (this.changePasswordController != null) {
                            this.changePasswordController.execute(
                                    currentState.getUsername(),
                                    currentState.getPassword(),
                                    currentState.getIngredients()
                            );
                        }
                    }
                }
        );

        // Set up recipe search listeners
        searchButton.addActionListener(evt -> performSearch());
        randomButton.addActionListener(evt -> getRandomRecipe());
        ingredientButton.addActionListener(evt -> displayIngredients(getIngredients(username.getText())));
        addIngredientButton.addActionListener(evt -> {
            String user = username.getText();
            String ingredient = addIngredientField.getText();
            if (user != null && !user.isEmpty() && addIngredientController != null) {
                List<String> updated = addIngredientController.addIngredient(user, ingredient);
                ingredientInventoryState.setIngredients(updated);
                displayIngredients(updated);
                addIngredientField.setText("");
            }
        });
        removeIngredientButton.addActionListener(evt -> {
            String user = username.getText();
            String ingredient = removeIngredientField.getText();
            if (user != null && !user.isEmpty() && removeIngredientController != null) {
                var result = removeIngredientController.removeIngredient(user, ingredient);
                if (result.isRemoved()) {
                    ingredientInventoryState.setIngredients(result.getIngredients());
                    displayIngredients(result.getIngredients());
                    removeIngredientField.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "Ingredient not found", "Remove Ingredient", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Favorites button listener
        favoritesButton.addActionListener(evt -> showFavorites());

        // Add all components to main panel
        this.add(title);
        this.add(Box.createRigidArea(new Dimension(0, 10))); // Spacer
        this.add(searchPanel);
        this.add(Box.createRigidArea(new Dimension(0, 10))); // Spacer
        this.add(manageIngredientsPanel);
        this.add(scrollPane);
        this.add(Box.createRigidArea(new Dimension(0, 10))); // Spacer
        this.add(statusLabel);
        this.add(Box.createRigidArea(new Dimension(0, 10))); // Spacer
        this.add(userPanel);
        // auto-load categories and areas after UI loads
        new Thread(() -> {
            recipeController.loadCategories();
            recipeController.loadAreas();
        }).start();

        categoryDropdown.addActionListener(e -> {
            String selected = (String) categoryDropdown.getSelectedItem();
            if (selected != null && !selected.isBlank()) {
                searchField.setText(selected);
                performSearch();
            }
        });

        areaDropdown.addActionListener(e -> {
            String selected = (String) areaDropdown.getSelectedItem();
            if (selected != null && !selected.isBlank()) {
                searchField.setText(selected);
                performSearch();
            }
        });


    }

    private void performSearch() {
        String query = searchField.getText().trim();
        if (!query.isEmpty()) {
            recipeViewModel.setLoading(true);
            statusLabel.setText("Searching for recipes...");

            new Thread(() -> { // Run in background thread
                try {
                    List<Recipe> recipes = recipeController.searchRecipes(query);
                    SwingUtilities.invokeLater(() -> {
                        recipeViewModel.setRecipes(recipes);
                        recipeViewModel.setLoading(false);
                        statusLabel.setText("Found " + recipes.size() + " recipes");
                    });
                } catch (Exception e) {
                    SwingUtilities.invokeLater(() -> {
                        recipeViewModel.setErrorMessage("Search failed: " + e.getMessage());
                        recipeViewModel.setLoading(false);
                        statusLabel.setText("Search failed");
                    });
                }
            }).start();
        } else {
            JOptionPane.showMessageDialog(this, "Please enter a search term", "Search Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void getRandomRecipe() {
        recipeViewModel.setLoading(true);
        statusLabel.setText("Getting random recipe...");

        new Thread(() -> {
            try {
                var recipe = recipeController.getRandomRecipe();
                SwingUtilities.invokeLater(() -> {
                    if (recipe.isPresent()) {
                        recipeViewModel.setRecipes(List.of(recipe.get()));
                        statusLabel.setText("Found random recipe: " + recipe.get().getName());
                    } else {
                        recipeViewModel.setErrorMessage("No random recipe found");
                        statusLabel.setText("No random recipe found");
                    }
                    recipeViewModel.setLoading(false);
                });
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    recipeViewModel.setErrorMessage("Failed to get random recipe: " + e.getMessage());
                    recipeViewModel.setLoading(false);
                    statusLabel.setText("Failed to get random recipe");
                });
            }
        }).start();
    }

    private List<String> getIngredients(String username) {
        List<String> ingredients = ingredientInventoryController.getIngredients(username);
        ingredientInventoryState.setIngredients(ingredients);
        return ingredients;
    }

    private void displayIngredients(List<String> ingredients){
        resultsPanel.removeAll();
        if(ingredients.isEmpty()){
            resultsPanel.add(new JLabel("No ingredients stored"));
        }
        else {
            for(String ingredient: ingredients){
                JLabel ingredient_label = new JLabel(ingredient);
                resultsPanel.add(ingredient_label);
            }
        }
        resultsPanel.revalidate();
        resultsPanel.repaint();
    }

    private void displayRecipes(List<Recipe> recipes) {
        resultsPanel.removeAll();

        // Cache all displayed recipes
        for (Recipe recipe : recipes) {
            FavoriteManager.getInstance().cacheRecipe(recipe);
        }

        if (recipes.isEmpty()) {
            resultsPanel.add(new JLabel("No recipes found. Try a different search term."));
        } else {
            for (Recipe recipe : recipes) {
                resultsPanel.add(createRecipePanel(recipe));
                resultsPanel.add(Box.createRigidArea(new Dimension(0, 5))); // Spacer between recipes
            }
        }
        resultsPanel.revalidate();
        resultsPanel.repaint();
    }

    private JPanel createRecipePanel(Recipe recipe) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEtchedBorder());
        panel.setBackground(Color.WHITE);

        boolean isFav = FavoriteManager.getInstance().isFavorite(recipe.getId());
        String displayName = recipe.getName();
        if (isFav) {
            displayName = "FAV: " + displayName;
        }
        JLabel nameLabel = new JLabel(displayName);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        if (isFav) {
            nameLabel.setForeground(new Color(0, 100, 0));
        }

        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(200, 200));

        String imageUrl = recipe.getImageUrl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            new Thread(() -> {
                try {
                    URL url = new URL(imageUrl);
                    ImageIcon icon = new ImageIcon(url);
                    Image scaled = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                    ImageIcon scaledIcon = new ImageIcon(scaled);
                    SwingUtilities.invokeLater(() -> imageLabel.setIcon(scaledIcon));
                } catch (Exception ex) {
                    System.err.println("Failed to load image for recipe " + recipe.getName() + ": " + ex.getMessage());
                }
            }).start();
        }

        // Details text
        JTextArea detailsArea = new JTextArea(
                "Category: " + recipe.getCategory() + "\n" +
                        "Cuisine: " + recipe.getArea() + "\n" +
                        "Ingredients: " + recipe.getIngredients().length + "\n" +
                        "Instructions: " + (recipe.getInstructions().length() > 100 ?
                        recipe.getInstructions().substring(0, 100) + "..." :
                        recipe.getInstructions())
        );
        detailsArea.setEditable(false);
        detailsArea.setBackground(Color.WHITE);
        detailsArea.setLineWrap(true);
        detailsArea.setWrapStyleWord(true);

        // Put name + image at the top
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
        northPanel.setBackground(Color.WHITE);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        northPanel.add(nameLabel);
        northPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        northPanel.add(imageLabel);

        panel.add(northPanel, BorderLayout.NORTH);
        panel.add(detailsArea, BorderLayout.CENTER);

        // View details button
        JButton viewDetailsButton = new JButton("View Details");
        viewDetailsButton.addActionListener(e -> showRecipeDetails(recipe));
        panel.add(viewDetailsButton, BorderLayout.SOUTH);

        return panel;
    }

    private MeasurementSystem guessTargetSystemFromOriginal(String[] measures) {
        boolean sawMetric = false;
        boolean sawImperial = false;

        if (measures != null) {
            for (String raw : measures) {
                if (raw == null) continue;
                String lower = raw.toLowerCase();

                if (lower.contains(" g") || lower.endsWith("g") ||
                        lower.contains(" ml") || lower.endsWith("ml")) {
                    sawMetric = true;
                }
                if (lower.contains(" cup") || lower.contains("cups") ||
                        lower.contains("tbsp") || lower.contains("tablespoon") ||
                        lower.contains("tablespoons") ||
                        lower.contains("tsp") || lower.contains("teaspoon") ||
                        lower.contains("teaspoons") ||
                        lower.contains(" oz") || lower.endsWith("oz")) {
                    sawImperial = true;
                }
            }
        }

        // If original is metric → convert to imperial; if imperial → convert to metric.
        if (sawMetric && !sawImperial) {
            return MeasurementSystem.IMPERIAL;
        }
        if (sawImperial && !sawMetric) {
            return MeasurementSystem.METRIC;
        }

        // Mixed / unknown → default to IMPERIAL target
        return MeasurementSystem.IMPERIAL;
    }




    private void refreshRecipeDisplay() {
        System.out.println("DEBUG: refreshRecipeDisplay called");

        // Get the current scroll position BEFORE refreshing
        JScrollBar verticalBar = ((JScrollPane)resultsPanel.getParent().getParent()).getVerticalScrollBar();
        int scrollPosition = verticalBar.getValue();
        System.out.println("DEBUG: Current scroll position: " + scrollPosition);

        // Get the current recipes from the view model
        List<Recipe> currentRecipes = recipeViewModel.getRecipes();

        if (currentRecipes != null && !currentRecipes.isEmpty()) {
            System.out.println("DEBUG: Refreshing " + currentRecipes.size() + " recipes");

            // Completely rebuild the results panel
            resultsPanel.removeAll();
            resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));

            for (Recipe recipe : currentRecipes) {
                resultsPanel.add(createRecipePanel(recipe));
                resultsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            }

            // Force UI update
            resultsPanel.revalidate();
            resultsPanel.repaint();

            // Restore scroll position AFTER UI update
            SwingUtilities.invokeLater(() -> {
                verticalBar.setValue(scrollPosition);
                System.out.println("DEBUG: Restored scroll position to: " + scrollPosition);
            });

            System.out.println("DEBUG: UI refresh complete");
        } else {
            System.out.println("DEBUG: No recipes to refresh");
        }
    }

    private void showRecipeDetails(Recipe recipe) {
        // Cache this recipe when viewing details
        FavoriteManager.getInstance().cacheRecipe(recipe);

        String[] ingredients = recipe.getIngredients();
        String[] measures = recipe.getMeasures();

        final String[] ingredientsFinal = ingredients;
        final String instructions = recipe.getInstructions();

        // Original and current measures for toggling
        final String[] originalMeasures = measures != null ? measures.clone() : new String[0];
        final String[][] currentMeasuresHolder = { measures != null ? measures.clone() : new String[0] };
        final boolean[] showingConverted = { false };

        // Text area showing details; will be updated after conversions
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        // Helper to refresh the text area contents
        Runnable updateText = () -> {
            StringBuilder details = new StringBuilder();
            details.append("Name: ").append(recipe.getName()).append("\n\n");
            details.append("Category: ").append(recipe.getCategory()).append("\n");
            details.append("Cuisine: ").append(recipe.getArea()).append("\n\n");
            details.append("Ingredients:\n");

            String[] currentMeasures = currentMeasuresHolder[0];

            for (int i = 0; i < ingredientsFinal.length; i++) {
                String ing = ingredientsFinal[i];
                if (ing != null && !ing.isEmpty()) {
                    String measureText = "";
                    if (currentMeasures != null && i < currentMeasures.length && currentMeasures[i] != null) {
                        measureText = currentMeasures[i];
                    }
                    details.append("• ")
                            .append(measureText)
                            .append(measureText.isEmpty() ? "" : " ")
                            .append(ing)
                            .append("\n");
                }
            }

            details.append("\nInstructions:\n").append(instructions);

            textArea.setText(details.toString());
            textArea.setCaretPosition(0); // scroll to top
        };

        // Initial fill
        updateText.run();

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 300));

        // Image label
        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(300, 200));

        String imageUrl = recipe.getImageUrl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            // Load image in a background thread
            new Thread(() -> {
                try {
                    URL url = new URL(imageUrl);
                    ImageIcon icon = new ImageIcon(url);
                    Image scaled = icon.getImage().getScaledInstance(300, 200, Image.SCALE_SMOOTH);
                    ImageIcon scaledIcon = new ImageIcon(scaled);

                    SwingUtilities.invokeLater(() -> imageLabel.setIcon(scaledIcon));
                } catch (Exception ex) {
                    System.err.println("Failed to load details image for recipe "
                            + recipe.getName() + ": " + ex.getMessage());
                }
            }).start();
        }

        // Favorite button
        JButton favoriteButton = new JButton("☆ Favorite");
        favoriteButton.addActionListener(e -> {
            boolean isNowFavorite = FavoriteManager.getInstance().toggleFavorite(recipe.getId());
            if (isNowFavorite) {
                favoriteButton.setText("★ Favorited");
                JOptionPane.showMessageDialog(this, "Added to favorites!", "Favorite", JOptionPane.INFORMATION_MESSAGE);
            } else {
                favoriteButton.setText("☆ Favorite");
                JOptionPane.showMessageDialog(this, "Removed from favorites", "Favorite", JOptionPane.INFORMATION_MESSAGE);
            }

            // Refresh the recipe display to show/hide the star
            refreshRecipeDisplay();
        });

        // Convert Units button
        JButton convertUnitsButton = new JButton("Convert Units");
        convertUnitsButton.addActionListener(e -> {
            if (convertUnitsController == null || convertUnitsViewModel == null) {
                JOptionPane.showMessageDialog(this,
                        "Unit conversion is not available.",
                        "Conversion Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!showingConverted[0]) {
                // First click: convert FROM the original measures.
                // The interactor will detect unit system per-measure and flip it.
                convertUnitsController.execute(
                        recipe.getId(),
                        recipe.getName(),
                        ingredientsFinal,
                        originalMeasures,
                        entity.MeasurementSystem.METRIC  // ignored by interactor (it flips per-unit)
                );

                var state = convertUnitsViewModel.getState();
                String[] converted = state.getConvertedMeasures();

                if (converted != null && converted.length > 0) {
                    currentMeasuresHolder[0] = converted;
                    showingConverted[0] = true;
                    convertUnitsButton.setText("Show Original Units");
                    updateText.run();
                } else if (state.getErrorMessage() != null) {
                    JOptionPane.showMessageDialog(this,
                            state.getErrorMessage(),
                            "Conversion Error",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Failed to convert units.",
                            "Conversion Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else {
                // Second click: just show the original measures again.
                currentMeasuresHolder[0] = originalMeasures;
                showingConverted[0] = false;
                convertUnitsButton.setText("Convert Units");
                updateText.run();
            }
        });

        // Buttons panel: Favorite + Convert Units
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(favoriteButton);
        buttonPanel.add(convertUnitsButton);

        // Main panel (image + text + buttons)
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(imageLabel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        JOptionPane.showMessageDialog(this,
                mainPanel,
                "Recipe Details: " + recipe.getName(),
                JOptionPane.INFORMATION_MESSAGE);
    }


    private void showFavorites() {
        Set<String> favoriteIds = FavoriteManager.getInstance().getFavoriteRecipeIds();

        if (favoriteIds.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No favorite recipes yet!\n\nClick the ☆ Favorite button on recipe details to add some.",
                    "My Favorites",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Try to get favorites from cache
        List<Recipe> favoriteRecipes = FavoriteManager.getInstance().getFavoriteRecipesFromCache();

        if (favoriteRecipes.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No favorite recipes found in cache.\n\nFavorite some recipes from your current search first.",
                    "My Favorites",
                    JOptionPane.WARNING_MESSAGE);
        } else {
            // Display what we found
            displayRecipes(favoriteRecipes);

            // Show status message about cache
            int totalFavorites = favoriteIds.size();
            int cachedFavorites = favoriteRecipes.size();
            if (cachedFavorites < totalFavorites) {
                statusLabel.setText("Showing " + cachedFavorites + " of " + totalFavorites +
                        " favorites (" + (totalFavorites - cachedFavorites) + " not in cache)");
            } else {
                statusLabel.setText("Showing all " + totalFavorites + " favorite recipes");
            }
        }
    }

    private List<Recipe> getRecipesByIds(Set<String> recipeIds) {
        List<Recipe> favorites = new ArrayList<>();

        // For now, we can only show favorites from currently loaded recipes
        List<Recipe> currentRecipes = recipeViewModel.getRecipes();
        for (Recipe recipe : currentRecipes) {
            if (recipeIds.contains(recipe.getId())) {
                favorites.add(recipe);
            }
        }

        return favorites;
    }


    /**
     * React to a button click that results in evt.
     * @param evt the ActionEvent to react to
     */
    public void actionPerformed(ActionEvent evt) {
        System.out.println("Click " + evt.getActionCommand());
        if (evt.getSource().equals(logOut) && logoutController != null) {
            logoutController.execute();
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // Handle original LoggedInViewModel changes
        if (evt.getPropertyName().equals("state")) {
            final LoggedInState state = (LoggedInState) evt.getNewValue();
            username.setText(state.getUsername());
        }
        else if (evt.getPropertyName().equals("password")) {
            final LoggedInState state = (LoggedInState) evt.getNewValue();
            if (state.getPasswordError() == null) {
                JOptionPane.showMessageDialog(this, "Password updated for " + state.getUsername());
                passwordInputField.setText("");
            }
            else {
                JOptionPane.showMessageDialog(this, state.getPasswordError());
            }
        }

        // Handle RecipeViewModel changes
        else if (evt.getSource() == recipeViewModel) {
            switch (evt.getPropertyName()) {
                case RecipeViewModel.CATEGORY_PROPERTY:
                    categoryDropdown.removeAllItems();
                    categoryDropdown.addItem(""); // optional placeholder
                    for (String cat : recipeViewModel.getCategoryOptions()) {
                        categoryDropdown.addItem(cat);
                    }
                    statusLabel.setText("Categories loaded.");
                    break;

                case RecipeViewModel.AREA_PROPERTY:
                    areaDropdown.removeAllItems();
                    areaDropdown.addItem("");
                    for (String area : recipeViewModel.getAreaOptions()) {
                        areaDropdown.addItem(area);
                    }
                    statusLabel.setText("Areas loaded.");
                    break;

                case RecipeViewModel.RECIPES_PROPERTY:
                    displayRecipes(recipeViewModel.getRecipes());
                    break;
                case RecipeViewModel.ERROR_PROPERTY:
                    String error = recipeViewModel.getErrorMessage();
                    if (error != null && !error.isEmpty()) {
                        JOptionPane.showMessageDialog(this, error, "Recipe Error", JOptionPane.ERROR_MESSAGE);
                        recipeViewModel.setErrorMessage(null); // Clear error after displaying
                    }
                    break;
                case RecipeViewModel.LOADING_PROPERTY:
                    boolean loading = recipeViewModel.isLoading();
                    searchButton.setEnabled(!loading);
                    randomButton.setEnabled(!loading);
                    break;
            }
        }
        else if(evt.getSource() == ingredientInventoryState) {
            displayIngredients(ingredientInventoryState.getIngredients());
            ingredientButton.setEnabled(true);
        }
    }

    public String getViewName() {
        return viewName;
    }

    public void setChangePasswordController(ChangePasswordController changePasswordController) {
        this.changePasswordController = changePasswordController;
    }

    public void setLogoutController(LogoutController logoutController) {
        this.logoutController = logoutController;
    }

    public void setAddIngredientController(AddIngredientController addIngredientController) {
        this.addIngredientController = addIngredientController;
    }

    public void setRemoveIngredientController(RemoveIngredientController removeIngredientController) {
        this.removeIngredientController = removeIngredientController;
    }
    public void setConvertUnitsController(ConvertUnitsController convertUnitsController) {
        this.convertUnitsController = convertUnitsController;
        //System.out.println("DEBUG: ConvertUnitsController injected: " + convertUnitsController);
    }

    public void setConvertUnitsViewModel(ConvertUnitsViewModel convertUnitsViewModel) {
        this.convertUnitsViewModel = convertUnitsViewModel;
    }

}
