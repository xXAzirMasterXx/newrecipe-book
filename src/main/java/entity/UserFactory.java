package entity;
import java.util.ArrayList;
import java.util.List;

/**
 * Factory for creating CommonUser objects.
 */
public class UserFactory {

    public User create(String name, String password, List<String> ingredients) {
        return new User(name, password, ingredients);
    }

    public User create(String name, String password) {
        return new User(name, password, new ArrayList<>());
    }
}
