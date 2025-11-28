package interface_adapter.logged_in;

import use_case.change_password.ChangePasswordInputBoundary;
import use_case.change_password.ChangePasswordInputData;

import java.util.List;

/**
 * Controller for the Change Password Use Case.
 */
public class ChangePasswordController {
    private final ChangePasswordInputBoundary userChangePasswordUseCaseInteractor;

    public ChangePasswordController(ChangePasswordInputBoundary userChangePasswordUseCaseInteractor) {
        this.userChangePasswordUseCaseInteractor = userChangePasswordUseCaseInteractor;
    }

    /**
     * Executes the Change Password Use Case.
     * @param password the new password
     * @param username the user whose password to change
     */
    public void execute(String password, String username, List<String> ingredients) {
        final ChangePasswordInputData changePasswordInputData = new ChangePasswordInputData(username, password, ingredients);

        userChangePasswordUseCaseInteractor.execute(changePasswordInputData);
    }

    public void switchToIngredientInventoryView() {
        userChangePasswordUseCaseInteractor.switchToIngredientInventoryView();
    }
}
