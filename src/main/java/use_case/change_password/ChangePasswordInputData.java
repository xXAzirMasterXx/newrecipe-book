package use_case.change_password;
import java.util.List;

/**
 * The input data for the Change Password Use Case.
 */
public class ChangePasswordInputData {

    private final String password;
    private final String username;
    private final List<String> ingredients;

    public ChangePasswordInputData(String password, String username, List<String> ingredients) {
        this.password = password;
        this.username = username;
        this.ingredients = ingredients;
    }

    String getPassword() {
        return password;
    }

    String getUsername() {return username; }

    List<String> getIngredients() {return ingredients; }

}
