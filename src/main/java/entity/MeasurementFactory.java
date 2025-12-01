package entity;

/**
 * Factory interface for creating Measurement entities.
 * This mirrors the style of your RecipeFactory.
 */
public interface MeasurementFactory {

    /**
     * Create a Measurement with the given components.
     *
     * @param value   numeric amount (e.g. 1.0, 200.0)
     * @param unit    unit string (e.g. "cup", "g", "tbsp")
     * @param system  measurement system (METRIC or IMPERIAL)
     * @return a new Measurement entity
     */
    Measurement create(double value, String unit, MeasurementSystem system);
}
