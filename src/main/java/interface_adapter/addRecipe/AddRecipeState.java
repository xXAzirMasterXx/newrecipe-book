package interface_adapter.add_recipes;

public class AddRecipeState {

    private String error;
    private String success;

    public AddRecipeState() {}

    public AddRecipeState(AddRecipeState copy) {
        this.error = copy.error;
        this.success = copy.success;
    }

    public String getError() { return error; }
    public void setError(String error) { this.error = error; }

    public String getSuccess() { return success; }
    public void setSuccess(String success) { this.success = success; }
}
