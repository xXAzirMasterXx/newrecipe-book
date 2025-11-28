package interface_adapter.convert_units;

/**
 * ViewModel for the Convert Units screen.
 * Holds the current ConvertUnitsState.
 */
public class ConvertUnitsViewModel {

    private ConvertUnitsState state = new ConvertUnitsState();

    public ConvertUnitsViewModel() {
    }

    public ConvertUnitsState getState() {
        return state;
    }

    public void setState(ConvertUnitsState state) {
        this.state = state;
    }

    /**
     * Convenience method to reset to a fresh state if needed.
     */
    public void resetState() {
        this.state = new ConvertUnitsState();
    }
}
