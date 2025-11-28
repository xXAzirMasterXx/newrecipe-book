package interface_adapter.logged_in;

import java.util.List;

/**
 * The State information representing the logged-in user.
 */
public class LoggedInState {
    private String username = "";

    private String password = "";
    private String passwordError;
    private List<String> ingredients;

    public LoggedInState(LoggedInState copy) {
        username = copy.username;
        password = copy.password;
        passwordError = copy.passwordError;
        ingredients = copy.ingredients;
    }

    public LoggedInState() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPasswordError(String passwordError) {
        this.passwordError = passwordError;
    }

    public String getPasswordError() {
        return passwordError;
    }

    public List<String> getIngredients() {return ingredients; }

}
