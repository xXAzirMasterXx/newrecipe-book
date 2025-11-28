package app;

import data_access.FileUserDataAccessObject;
import data_access.RecipeDataAccessObject;
import entity.UserFactory;
import interface_adapter.ViewManagerModel;
import interface_adapter.ingredient_inventory.IngredientInventoryController;
import interface_adapter.ingredient_inventory.IngredientInventoryPresenter;
import interface_adapter.ingredient_inventory.IngredientInventoryViewModel;
import interface_adapter.logged_in.ChangePasswordController;
import interface_adapter.logged_in.ChangePasswordPresenter;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.add_ingredients.AddIngredientController;
import interface_adapter.remove_ingredients.RemoveIngredientController;
import interface_adapter.login.LoginController;
import interface_adapter.login.LoginPresenter;
import interface_adapter.login.LoginViewModel;
import interface_adapter.logout.LogoutController;
import interface_adapter.logout.LogoutPresenter;
import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupPresenter;
import interface_adapter.signup.SignupViewModel;
import interface_adapter.recipe.RecipeViewModel;
import interface_adapter.recipe.RecipeController;
import interface_adapter.ingredient_inventory.IngredientInventoryState;
import use_case.change_password.ChangePasswordInputBoundary;
import use_case.change_password.ChangePasswordInteractor;
import use_case.change_password.ChangePasswordOutputBoundary;
import use_case.ingredient_inventory.IngredientInventoryInputBoundary;
import use_case.ingredient_inventory.IngredientInventoryInteractor;
import use_case.ingredient_inventory.IngredientInventoryOutputBoundary;
import use_case.add_ingredients.AddIngredientInputBoundary;
import use_case.add_ingredients.AddIngredientInteractor;
import use_case.remove_ingredients.RemoveIngredientInputBoundary;
import use_case.remove_ingredients.RemoveIngredientInteractor;
import use_case.login.LoginInputBoundary;
import use_case.login.LoginInteractor;
import use_case.login.LoginOutputBoundary;
import use_case.logout.LogoutInputBoundary;
import use_case.logout.LogoutInteractor;
import use_case.recipe.GetRandomRecipeUseCase;
import use_case.recipe.RecipeDataAccessInterface;
import use_case.recipe.SearchRecipesUseCase;
import use_case.logout.LogoutOutputBoundary;
import use_case.signup.SignupInputBoundary;
import use_case.signup.SignupInteractor;
import use_case.signup.SignupOutputBoundary;
import view.*;

import javax.swing.*;
import java.awt.*;

public class AppBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    final UserFactory userFactory = new UserFactory();
    final ViewManagerModel viewManagerModel = new ViewManagerModel();
    ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

    // set which data access implementation to use, can be any
    // of the classes from the data_access package

    // DAO version using local file storage
    final FileUserDataAccessObject userDataAccessObject = new FileUserDataAccessObject("users.csv", userFactory);

    // DAO version using a shared external database
    // final DBUserDataAccessObject userDataAccessObject = new DBUserDataAccessObject(userFactory);
    final RecipeDataAccessInterface recipeDataAccess = new RecipeDataAccessObject();
    final RecipeViewModel recipeViewModel = new RecipeViewModel();

    private SignupView signupView;
    private SignupViewModel signupViewModel;
    private LoginViewModel loginViewModel;
    private LoggedInViewModel loggedInViewModel;
    private LoggedInView loggedInView;
    private LoginView loginView;
    private IngredientInventoryViewModel ingredientInventoryViewModel;
    private IngredientInventoryView ingredientInventoryView;

    private RecipeController recipeController;
    private IngredientInventoryController ingredientInventoryController;
    private AddIngredientController addIngredientController;
    private RemoveIngredientController removeIngredientController;

    private IngredientInventoryState ingredientInventoryState;

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
    }

    public AppBuilder addSignupView() {
        signupViewModel = new SignupViewModel();
        signupView = new SignupView(signupViewModel);
        cardPanel.add(signupView, signupView.getViewName());
        return this;
    }

    public AppBuilder addLoginView() {
        loginViewModel = new LoginViewModel();
        loginView = new LoginView(loginViewModel);
        cardPanel.add(loginView, loginView.getViewName());
        return this;
    }

    public AppBuilder addLoggedInView() {
        loggedInViewModel = new LoggedInViewModel();
        ingredientInventoryState = new IngredientInventoryState();

        // Ensure recipe use case exists first (your code already checks this)
        if (recipeController == null) {
            this.addRecipeUseCase();
        }

        // Ensure ingredient inventory use case is set up and controller is stored in the field
        this.addIngredientInventoryUseCase();

        // Ensure add ingredients use case is set up
        this.addAddIngredientsUseCase();

        // Ensure remove ingredients use case is set up
        this.addRemoveIngredientsUseCase();

        // Now use the initialized field
        loggedInView = new LoggedInView(
                loggedInViewModel,
                recipeViewModel,
                recipeController,
                ingredientInventoryState,
                ingredientInventoryController
        );
        // inject add ingredient controller
        loggedInView.setAddIngredientController(addIngredientController);
        // inject remove ingredient controller
        loggedInView.setRemoveIngredientController(removeIngredientController);
        cardPanel.add(loggedInView, loggedInView.getViewName());
        return this;
    }

    public AppBuilder addIngredientInventoryView(){
        ingredientInventoryViewModel = new IngredientInventoryViewModel();
        ingredientInventoryView = new IngredientInventoryView(ingredientInventoryViewModel);
        return this;
    }

    public AppBuilder addSignupUseCase() {
        final SignupOutputBoundary signupOutputBoundary = new SignupPresenter(viewManagerModel,
                signupViewModel, loginViewModel);
        final SignupInputBoundary userSignupInteractor = new SignupInteractor(
                userDataAccessObject, signupOutputBoundary, userFactory);

        SignupController controller = new SignupController(userSignupInteractor);
        signupView.setSignupController(controller);
        return this;
    }

    public AppBuilder addLoginUseCase() {
        final LoginOutputBoundary loginOutputBoundary = new LoginPresenter(viewManagerModel,
                loggedInViewModel, loginViewModel);
        final LoginInputBoundary loginInteractor = new LoginInteractor(
                userDataAccessObject, loginOutputBoundary);

        LoginController loginController = new LoginController(loginInteractor);
        loginView.setLoginController(loginController);
        return this;
    }

    public AppBuilder addChangePasswordUseCase() {
        final ChangePasswordOutputBoundary changePasswordOutputBoundary = new ChangePasswordPresenter(viewManagerModel,
                loggedInViewModel, new IngredientInventoryViewModel());

        final ChangePasswordInputBoundary changePasswordInteractor =
                new ChangePasswordInteractor(userDataAccessObject, changePasswordOutputBoundary, userFactory);

        ChangePasswordController changePasswordController = new ChangePasswordController(changePasswordInteractor);
        loggedInView.setChangePasswordController(changePasswordController);
        return this;
    }

    public AppBuilder addIngredientInventoryUseCase() {
        // Ensure the view model exists before constructing the presenter

        final IngredientInventoryOutputBoundary ingredientInventoryOutputBoundary =
                new IngredientInventoryPresenter(ingredientInventoryViewModel, viewManagerModel, loggedInViewModel);

        final IngredientInventoryInputBoundary ingredientInventoryInteractor =
                new IngredientInventoryInteractor(userDataAccessObject, ingredientInventoryOutputBoundary);

        // Store in the field so other methods can use it
        this.ingredientInventoryController = new IngredientInventoryController(ingredientInventoryInteractor);
        return this;
    }

    public AppBuilder addAddIngredientsUseCase() {
        final AddIngredientInputBoundary addIngredientInputBoundary =
                new AddIngredientInteractor(userDataAccessObject, userDataAccessObject, userFactory);

        this.addIngredientController = new AddIngredientController(addIngredientInputBoundary);
        return this;
    }

    public AppBuilder addRemoveIngredientsUseCase() {
        final RemoveIngredientInputBoundary removeIngredientInputBoundary =
                new RemoveIngredientInteractor(userDataAccessObject, userDataAccessObject, userFactory);

        this.removeIngredientController = new RemoveIngredientController(removeIngredientInputBoundary);
        return this;
    }

    /**
     * Adds the Logout Use Case to the application.
     * @return this builder
     */
    public AppBuilder addLogoutUseCase() {
        final LogoutOutputBoundary logoutOutputBoundary = new LogoutPresenter(viewManagerModel,
                loggedInViewModel, loginViewModel);

        final LogoutInputBoundary logoutInteractor =
                new LogoutInteractor(userDataAccessObject, logoutOutputBoundary);

        final LogoutController logoutController = new LogoutController(logoutInteractor);
        loggedInView.setLogoutController(logoutController);
        return this;
    }

    public AppBuilder addRecipeUseCase() {
        // Create use cases
        SearchRecipesUseCase searchRecipesUseCase = new SearchRecipesUseCase(recipeDataAccess);
        GetRandomRecipeUseCase getRandomRecipeUseCase = new GetRandomRecipeUseCase(recipeDataAccess);

        // Create controller
        recipeController = new RecipeController(searchRecipesUseCase, getRandomRecipeUseCase);
        return this;
    }

    public JFrame build() {
        final JFrame application = new JFrame("User Login Example");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        application.add(cardPanel);

        viewManagerModel.setState(signupView.getViewName());
        viewManagerModel.firePropertyChange();

        return application;
    }


}
