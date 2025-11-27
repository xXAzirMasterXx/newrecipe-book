package view;

import entity.Recipe;
import interface_adapter.my_recipes.MyRecipesViewModel;
import interface_adapter.my_recipes.MyRecipesState;
import interface_adapter.ViewManagerModel;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class MyRecipesView extends JPanel implements PropertyChangeListener {

    private final MyRecipesViewModel viewModel;
    private final ViewManagerModel viewManagerModel;
    private final JTextArea recipesArea = new JTextArea();

    public MyRecipesView(MyRecipesViewModel viewModel, ViewManagerModel viewManagerModel) {
        this.viewModel = viewModel;
        this.viewManagerModel = viewManagerModel;

        viewModel.addPropertyChangeListener(this);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(new JLabel("My Recipes"));
        recipesArea.setEditable(false);
        add(new JScrollPane(recipesArea));

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            viewManagerModel.setState("logged in");
            viewManagerModel.firePropertyChange();
        });
        add(backButton);
    }




    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        MyRecipesState state = (MyRecipesState) evt.getNewValue();

        if (state.getError() != null) {
            recipesArea.setText(state.getError());
            return;
        }

        StringBuilder sb = new StringBuilder();

        state.getRecipes().forEach(r -> {
            sb.append("Name: ").append(r.getName()).append("\n");

            sb.append("Ingredients:\n");
            String[] ingredients = r.getIngredients();
            String[] measures   = r.getMeasures();   // measures

            if (ingredients != null) {
                for (int i = 0; i < ingredients.length; i++) {
                    String ing = ingredients[i];
                    sb.append(" - ").append(ing == null ? "" : ing.trim());

                    if (measures != null && i < measures.length) {
                        String m = measures[i];
                        if (m != null && !m.trim().isEmpty()) {
                            sb.append(" (").append(m.trim()).append(")");
                        }
                    }
                    sb.append("\n");
                }
            }

            sb.append("Instructions:\n");
            sb.append(r.getInstructions() == null ? "" : r.getInstructions().trim())
                    .append("\n");

            sb.append("Cooking time: ")
                    .append(r.getCookingTime())
                    .append(" minutes\n");

            sb.append("------------------------------------------------\n");
        });

        recipesArea.setText(sb.toString());
    }



    public String getViewName() {
        return "my recipes";
    }
}
