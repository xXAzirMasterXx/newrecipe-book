package use_case.add_recipes;

public class AddRecipeOutputData {

    private final String id;
    private final String name;
    private final int cookingTime;
    private final boolean overwrite;

    public AddRecipeOutputData(String id, String name, int cookingTime, boolean overwrite) {
        this.id = id;
        this.name = name;
        this.cookingTime = cookingTime;
        this.overwrite = overwrite;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCookingTime() {
        return cookingTime;
    }

    public boolean isOverwrite() {
        return overwrite;
    }
}
