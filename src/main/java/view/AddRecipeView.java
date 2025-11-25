package view;

import interface_adapter.ViewManagerModel;
import interface_adapter.addRecipe.AddRecipeController;
import interface_adapter.addRecipe.AddRecipeState;
import interface_adapter.addRecipe.AddRecipeViewModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class AddRecipeView extends JPanel implements PropertyChangeListener {

    private final AddRecipeViewModel viewModel;
    private final AddRecipeController controller;
    private final ViewManagerModel viewManagerModel;

    JTextField name = new JTextField(20);
    JTextArea ingredients = new JTextArea(5, 20);
    JTextArea measures = new JTextArea(5, 20);
    JTextArea instructions = new JTextArea(5, 20);
    JTextField cookingTime = new JTextField(10);

    JButton addBtn = new JButton("Add");
    JButton backBtn = new JButton("Back");

    public AddRecipeView(AddRecipeViewModel vm, ViewManagerModel vmm, AddRecipeController controller) {
        this.viewModel = vm;
        this.viewManagerModel = vmm;
        this.controller = controller;

        vm.addPropertyChangeListener(this);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(new JLabel("Add Recipe"));
        add(name);
        add(new JLabel("Ingredients (one per line)"));
        add(new JScrollPane(ingredients));
        add(new JLabel("Measures (one per line)"));
        add(new JScrollPane(measures));
        add(new JLabel("Instructions"));
        add(new JScrollPane(instructions));
        add(new JLabel("Cooking time"));
        add(cookingTime);

        JPanel btns = new JPanel();
        btns.add(addBtn);
        btns.add(backBtn);
        add(btns);

        addBtn.addActionListener(e -> submit());
        backBtn.addActionListener(e -> {
            viewManagerModel.setState("logged in");
            viewManagerModel.firePropertyChange();
        });
    }

    private void submit() {
        controller.execute(
                name.getText(), "", "", instructions.getText(), "", "",
                "", ingredients.getText().split("\\R"),
                measures.getText().split("\\R"),
                cookingTime.getText(), false
        );
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        AddRecipeState s = (AddRecipeState) evt.getNewValue();

        if (s.getError() != null)
            JOptionPane.showMessageDialog(this, s.getError());

        if (s.getSuccess() != null)
            JOptionPane.showMessageDialog(this, s.getSuccess());
    }

    public String getViewName() {
        return "add recipe";
    }
}
