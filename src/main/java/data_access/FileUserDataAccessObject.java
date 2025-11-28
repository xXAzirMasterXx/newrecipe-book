package data_access;

import entity.User;
import entity.UserFactory;
import use_case.change_password.ChangePasswordUserDataAccessInterface;
import use_case.login.LoginUserDataAccessInterface;
import use_case.logout.LogoutUserDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;
import use_case.add_ingredients.AddIngredientUserDataInterface;
import use_case.ingredient_inventory.IngredientInventoryUserDataAccessInterface;
import use_case.remove_ingredients.RemoveIngredientUserDataInterface;

import java.io.*;
import java.util.*;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.lang.String.valueOf;

/**
 * DAO for user data implemented using a File to persist the data.
 */
public class FileUserDataAccessObject implements SignupUserDataAccessInterface,
                                                 LoginUserDataAccessInterface,
                                                 ChangePasswordUserDataAccessInterface,
                                                 LogoutUserDataAccessInterface,
                                                 AddIngredientUserDataInterface,
                                                 IngredientInventoryUserDataAccessInterface,
                                                 RemoveIngredientUserDataInterface  {

    private static final String HEADER = "username,password,ingredients";

    private final File csvFile;
    private final Map<String, Integer> headers = new LinkedHashMap<>();
    private final Map<String, User> accounts = new HashMap<>();

    private String currentUsername;

    /**
     * Construct this DAO for saving to and reading from a local file.
     * @param csvPath the path of the file to save to
     * @param userFactory factory for creating user objects
     * @throws RuntimeException if there is an IOException when accessing the file
     */
    public FileUserDataAccessObject(String csvPath, UserFactory userFactory) {

        csvFile = new File(csvPath);
        headers.put("username", 0);
        headers.put("password", 1);
        headers.put("ingredients", 2);

        if (csvFile.length() == 0) {
            save();
        }
        else {

            try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
                final String header = reader.readLine();

                if (!header.equals(HEADER)) {
                    throw new RuntimeException(String.format("header should be%n: %s%n but was:%n%s", HEADER, header));
                }

                String row;
                while ((row = reader.readLine()) != null) {
                    final String[] col = row.split(",");
                    final String username = valueOf(col[headers.get("username")]);
                    final String password = valueOf(col[headers.get("password")]);
                    User user;

                    if(col.length == 2){
                        user = userFactory.create(username, password);
                    }
                    else{
                        final String[] ingredient_header = String.valueOf(col[headers.get("ingredients")]).split(" ");
                        List<String> ingredients = new ArrayList<>();
                        Collections.addAll(ingredients, ingredient_header);
                        user = userFactory.create(username, password, ingredients);
                    }

                    accounts.put(username, user);
                }
            }
            catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private void save() {
        final BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter(csvFile));
            writer.write(String.join(",", headers.keySet()));
            writer.newLine();

            for (User user : accounts.values()) {
                String ingredientsJoined = "";
                if (user.getIngredient_inventory() != null && !user.getIngredient_inventory().isEmpty()) {
                    ingredientsJoined = String.join(" ", user.getIngredient_inventory());
                }
                final String line;
                if (ingredientsJoined.isEmpty()) {
                    // Keep compatibility with reader that treats 2-column rows as no ingredients
                    line = String.format("%s,%s", user.getName(), user.getPassword());
                } else {
                    line = String.format("%s,%s,%s", user.getName(), user.getPassword(), ingredientsJoined);
                }
                writer.write(line);
                writer.newLine();
            }

            writer.close();

        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void save(User user) {
        accounts.put(user.getName(), user);
        this.save();
    }

    @Override
    public User get(String username) {
        return accounts.get(username);
    }

    @Override
    public void setCurrentUsername(String name) {
        currentUsername = name;
    }

    @Override
    public String getCurrentUsername() {
        return currentUsername;
    }

    @Override
    public boolean existsByName(String identifier) {
        return accounts.containsKey(identifier);
    }

    @Override
    public void changePassword(User user) {
        // Replace the User object in the map
        accounts.put(user.getName(), user);
        save();
    }

    @Override
    public void addIngredient(User user){
        accounts.put(user.getName(), user);
        save();
    }

    public List<String> getIngredients(String username){
        return accounts.get(username).getIngredient_inventory();
    }

    @Override
    public void removeIngredient(User user) {
        accounts.put(user.getName(), user);
        save();
    }

}
