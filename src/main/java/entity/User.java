package entity;
import java.util.List;

/**
 * A simple entity representing a user. Users have a username and password..
 */
public class User {

    private final String name;
    private final String password;
    private List<String> ingredients;

    /**
     * Creates a new user with the given non-empty name and non-empty password.
     * @param name the username
     * @param password the password
     * @throws IllegalArgumentException if the password or name are empty
     */
    public User(String name, String password, List<String> ingredients) {
        if ("".equals(name)) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if ("".equals(password)) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        this.name = name;
        this.password = password;
        this.ingredients = ingredients;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public List<String> getIngredient_inventory() {return ingredients; }

}
