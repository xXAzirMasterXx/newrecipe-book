package view;

import interface_adapter.logged_in.ChangePasswordController;
import interface_adapter.logged_in.LoggedInState;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.logout.LogoutController;

// Recipe-related imports
import interface_adapter.recipe.RecipeController;
import interface_adapter.recipe.RecipeViewModel;
import entity.Recipe;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.net.URL;
import java.awt.Image;



public class LoggedInView extends JPanel implements ActionListener, PropertyChangeListener {

    private final String viewName = "logged in";
    private final LoggedInViewModel loggedInViewModel;
    private final RecipeViewModel recipeViewModel;
    private final JLabel passwordErrorField = new JLabel();
    private ChangePasswordController changePasswordController = null;
    private LogoutController logoutController;
    private RecipeController recipeController;

    private final JLabel username;

    // Recipe components
    private final JTextField searchField = new JTextField(20);
    private final JButton searchButton = new JButton("Search Recipes");
    private final JButton randomButton = new JButton("Random Recipe");
    private final JPanel resultsPanel = new JPanel();
    private final JLabel statusLabel = new JLabel("Welcome! Search for recipes or get a random one.");

    // Original components
    private final JButton logOut;
    private final JTextField passwordInputField = new JTextField(15);
    private final JButton changePassword;

    public LoggedInView(LoggedInViewModel loggedInViewModel, RecipeViewModel recipeViewModel, RecipeController recipeController) {
        this.loggedInViewModel = loggedInViewModel;
        this.recipeViewModel = recipeViewModel;
        this.recipeController = recipeController;

        this.loggedInViewModel.addPropertyChangeListener(this);
        this.recipeViewModel.addPropertyChangeListener(this);

        final JLabel title = new JLabel("Recipe Explorer");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Arial", Font.BOLD, 18));

        // Recipe Search Section
        final JPanel searchPanel = new JPanel();
        searchPanel.setBorder(BorderFactory.createTitledBorder("Recipe Search"));
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(randomButton);

        // Results Panel
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
        resultsPanel.setBorder(BorderFactory.createTitledBorder("Recipes"));
        JScrollPane scrollPane = new JScrollPane(resultsPanel);
        scrollPane.setPreferredSize(new Dimension(600, 300));

        // User Management Section (original functionality)
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

        // Add components to user panel
        userPanel.add(usernameInfo);
        userPanel.add(username);
        userPanel.add(passwordInfo);
        userPanel.add(passwordErrorField);
        userPanel.add(buttons);

        // Set up layout
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Set up listeners for original functionality
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
                                    currentState.getPassword()
                            );
                        }
                    }
                }
        );

        // Set up recipe search listeners
        searchButton.addActionListener(evt -> performSearch());
        randomButton.addActionListener(evt -> getRandomRecipe());

        // Add all components to main panel
        this.add(title);
        this.add(Box.createRigidArea(new Dimension(0, 10))); // Spacer
        this.add(searchPanel);
        this.add(Box.createRigidArea(new Dimension(0, 10))); // Spacer
        this.add(scrollPane);
        this.add(Box.createRigidArea(new Dimension(0, 10))); // Spacer
        this.add(statusLabel);
        this.add(Box.createRigidArea(new Dimension(0, 10))); // Spacer
        this.add(userPanel);
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

    private void displayRecipes(List<Recipe> recipes) {
        resultsPanel.removeAll();
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

        // Name label
        JLabel nameLabel = new JLabel(recipe.getName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));

        // ==== NEW: Image label ====
        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(200, 200)); // adjust as you like

        String imageUrl = recipe.getImageUrl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            // Load image in a background thread so UI doesn't freeze
            new Thread(() -> {
                try {
                    URL url = new URL(imageUrl);
                    ImageIcon icon = new ImageIcon(url);
                    // Optionally scale the image to fit nicely
                    Image scaled = icon.getImage().getScaledInstance(
                            200, 200, Image.SCALE_SMOOTH);
                    ImageIcon scaledIcon = new ImageIcon(scaled);

                    SwingUtilities.invokeLater(() -> imageLabel.setIcon(scaledIcon));
                } catch (Exception ex) {
                    // If loading fails, you can set a placeholder or leave it blank
                    System.err.println("Failed to load image for recipe "
                            + recipe.getName() + ": " + ex.getMessage());
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


    private void showRecipeDetails(Recipe recipe) {
        // ==== Build the text details ====
        StringBuilder details = new StringBuilder();
        details.append("Name: ").append(recipe.getName()).append("\n\n");
        details.append("Category: ").append(recipe.getCategory()).append("\n");
        details.append("Cuisine: ").append(recipe.getArea()).append("\n\n");
        details.append("Ingredients:\n");

        String[] ingredients = recipe.getIngredients();
        String[] measures = recipe.getMeasures();
        for (int i = 0; i < ingredients.length; i++) {
            if (ingredients[i] != null && !ingredients[i].isEmpty()) {
                details.append("• ")
                        .append(measures[i] != null ? measures[i] : "")
                        .append(" ")
                        .append(ingredients[i])
                        .append("\n");
            }
        }

        details.append("\nInstructions:\n").append(recipe.getInstructions());

        JTextArea textArea = new JTextArea(details.toString());
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 300));

        // ==== Image label ====
        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(300, 200)); // adjust as you like

        String imageUrl = recipe.getImageUrl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            // Load image in a background thread
            new Thread(() -> {
                try {
                    URL url = new URL(imageUrl);
                    ImageIcon icon = new ImageIcon(url);
                    Image scaled = icon.getImage().getScaledInstance(
                            300, 200, Image.SCALE_SMOOTH);
                    ImageIcon scaledIcon = new ImageIcon(scaled);

                    SwingUtilities.invokeLater(() -> imageLabel.setIcon(scaledIcon));
                } catch (Exception ex) {
                    System.err.println("Failed to load details image for recipe "
                            + recipe.getName() + ": " + ex.getMessage());
                }
            }).start();
        }

        // ==== Combine image + text in one panel ====
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(imageLabel, BorderLayout.NORTH);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        // Show dialog
        JOptionPane.showMessageDialog(
                this,
                contentPanel,
                "Recipe Details: " + recipe.getName(),
                JOptionPane.INFORMATION_MESSAGE
        );
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
}