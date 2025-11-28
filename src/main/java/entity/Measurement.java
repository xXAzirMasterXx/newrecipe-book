package entity;

/**
 * Domain entity representing a single quantity with a unit,
 * e.g. "1 cup", "200 g", "3 tbsp".
 */
public class Measurement {

    private final double value;              // numeric amount, e.g. 1.0, 200.0
    private final String unit;              // e.g. "cup", "g", "tbsp"
    private final MeasurementSystem system; // METRIC or IMPERIAL

    public Measurement(double value, String unit, MeasurementSystem system) {
        this.value = value;
        this.unit = unit;
        this.system = system;
    }

    public double getValue() {
        return value;
    }

    public String getUnit() {
        return unit;
    }

    public MeasurementSystem getSystem() {
        return system;
    }

    /**
     * Returns a human-readable representation, e.g. "200.0 g".
     */
    public String toReadableString() {
        return value + " " + unit;
    }

    @Override
    public String toString() {
        return "Measurement{" +
                "value=" + value +
                ", unit='" + unit + '\'' +
                ", system=" + system +
                '}';
    }
}
