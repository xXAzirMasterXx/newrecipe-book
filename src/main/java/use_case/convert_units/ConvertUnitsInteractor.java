package use_case.convert_units;

import entity.Measurement;
import entity.MeasurementFactory;
import entity.MeasurementSystem;

/**
 * Interactor for converting recipe measures between metric and imperial.
 *
 * Behaviour:
 * - For EACH measure string:
 *   1) Try to parse "number + unit" (supports "200 g", "350g", "2 tbs", "1/2 cup", etc.).
 *   2) Normalize the unit (tablespoons -> tbsp, grams -> g, tbs -> tbsp, etc.).
 *   3) If the unit is IMPERIAL, convert it to METRIC.
 *   4) If the unit is METRIC, convert it to IMPERIAL.
 *   5) If the unit/value cannot be parsed, leave the string unchanged.
 *
 * Toggling back to the original system is handled by the View
 * (the View re-displays the original measures; this interactor always
 * converts FROM the original measures, never from already-converted ones).
 */
public class ConvertUnitsInteractor implements ConvertUnitsInputBoundary {

    private final MeasurementFactory measurementFactory;
    private final ConvertUnitsOutputBoundary presenter;

    public ConvertUnitsInteractor(MeasurementFactory measurementFactory,
                                  ConvertUnitsOutputBoundary presenter) {
        this.measurementFactory = measurementFactory;
        this.presenter = presenter;
    }

    @Override
    public void execute(ConvertUnitsInputData inputData) {

        if (inputData == null) {
            presenter.prepareFailView("No data provided for unit conversion.");
            return;
        }

        String[] ingredients = inputData.getIngredients();
        String[] measures = inputData.getMeasures();

        if (ingredients == null || measures == null ||
                ingredients.length == 0 || measures.length == 0) {
            presenter.prepareFailView("Ingredients and measures are required for conversion.");
            return;
        }

        if (ingredients.length != measures.length) {
            presenter.prepareFailView("Ingredients and measures must have the same length.");
            return;
        }

        String[] convertedMeasures = new String[measures.length];

        for (int i = 0; i < measures.length; i++) {
            convertedMeasures[i] = convertSingleMeasure(measures[i]);
        }

        // We consider this a success even if some (or all) lines could not be converted,
        // because unrecognised ones are simply left unchanged.
        ConvertUnitsOutputData outputData = new ConvertUnitsOutputData(
                inputData.getRecipeId(),
                inputData.getRecipeName(),
                ingredients,
                convertedMeasures,
                null  // no single global target system anymore; per-measure flip
        );

        presenter.prepareSuccessView(outputData);
    }

    /**
     * Convert one measurement string by detecting its current system
     * and flipping it:
     *   - IMPERIAL → METRIC
     *   - METRIC   → IMPERIAL
     * If it can't be parsed or the unit is unknown, return the original string.
     */
    private String convertSingleMeasure(String rawMeasure) {
        if (rawMeasure == null) {
            return null;
        }

        String trimmed = rawMeasure.trim();
        if (trimmed.isEmpty()) {
            return rawMeasure;
        }

        // Split into numeric + unit.
        String valueToken;
        String unitToken;

        if (trimmed.contains(" ")) {
            // Case 1: "200 g", "1/2 cup"
            String[] parts = trimmed.split("\\s+", 2);
            if (parts.length != 2) {
                // e.g. "to taste", "1 bunch" → leave unchanged
                return rawMeasure;
            }
            valueToken = parts[0];
            unitToken = parts[1].trim();
        } else {
            // Case 2: "350g", "2tbs", "1/2cup"
            int idx = 0;
            while (idx < trimmed.length()) {
                char c = trimmed.charAt(idx);
                if (Character.isDigit(c) || c == '.' || c == '/') {
                    idx++;
                } else {
                    break;
                }
            }
            if (idx == 0 || idx >= trimmed.length()) {
                // No numeric prefix or no unit part
                return rawMeasure;
            }
            valueToken = trimmed.substring(0, idx);
            unitToken = trimmed.substring(idx).trim();
        }

        double value;
        try {
            value = parseValue(valueToken);
        } catch (IllegalArgumentException ex) {
            // Non-numeric first token → leave unchanged
            return rawMeasure;
        }

        String normalizedUnit = normalizeUnit(unitToken);

        boolean isMetric = isMetricUnit(normalizedUnit);
        boolean isImperial = isImperialUnit(normalizedUnit);

        if (!isMetric && !isImperial) {
            // Unknown unit → leave unchanged
            return rawMeasure;
        }

        double newValue = value;
        String newUnit = normalizedUnit;
        MeasurementSystem newSystem;

        if (isImperial) {
            // IMPERIAL → METRIC
            switch (normalizedUnit) {
                case "cup":
                    double ml = value * 240.0;

                    // convert to litres if >= 1000ml
                    if (ml >= 1000.0) {
                        newValue = ml / 1000.0;
                        newUnit = "l";
                    } else {
                        newValue = ml;
                        newUnit = "ml";
                    }

                    newSystem = MeasurementSystem.METRIC;
                    break;

                case "tbsp":
                    newValue = value * 15.0;    // ml
                    newUnit = "ml";
                    newSystem = MeasurementSystem.METRIC;
                    break;
                case "tsp":
                    newValue = value * 5.0;     // ml
                    newUnit = "ml";
                    newSystem = MeasurementSystem.METRIC;
                    break;
                case "oz":
                    newValue = value * 28.35;   // g
                    newUnit = "g";
                    newSystem = MeasurementSystem.METRIC;
                    break;
                case "lb": {
                    double grams = value * 453.592;
                    if (grams >= 1000.0) {
                        newValue = grams / 1000.0;
                        newUnit = "kg";
                    } else {
                        newValue = grams;
                        newUnit = "g";
                    }
                    newSystem = MeasurementSystem.METRIC;
                    break;
                }
                default:
                    // Shouldn't happen if isImperialUnit() is correct
                    return rawMeasure;
            }
        } else {
            // METRIC → IMPERIAL
            switch (normalizedUnit) {
                case "l":
                    newValue = (value * 1000.0) / 240.0; // convert L → ml → cups
                    newUnit = "cup";
                    newSystem = MeasurementSystem.IMPERIAL;
                    break;
                case "ml":
                    newValue = value / 240.0;   // cups approx
                    newUnit = "cup";
                    newSystem = MeasurementSystem.IMPERIAL;
                    break;
                case "g":
                    newValue = value / 28.35;   // oz approx
                    newUnit = "oz";
                    newSystem = MeasurementSystem.IMPERIAL;
                    break;
                case "kg":
                    newValue = value * 2.20462; // lb
                    newUnit = "lb";
                    newSystem = MeasurementSystem.IMPERIAL;
                    break;
                default:
                    // Unknown metric unit (e.g. "l") → leave unchanged
                    return rawMeasure;
            }
        }

        // ✅ NEW: uniform rounding to 1 decimal place for all units
        newValue = Math.round(newValue * 10.0) / 10.0;

        Measurement m = measurementFactory.create(newValue, newUnit, newSystem);
        return m.toReadableString();
    }

    /**
     * Parse numeric token that may be:
     *  - integer ("1")
     *  - decimal ("1.5")
     *  - simple fraction ("1/2")
     */
    private double parseValue(String token) {
        token = token.trim();

        // Fraction like "1/2"
        if (token.contains("/")) {
            String[] fractionParts = token.split("/");
            if (fractionParts.length != 2) {
                throw new IllegalArgumentException("Invalid fraction: " + token);
            }
            double numerator = Double.parseDouble(fractionParts[0]);
            double denominator = Double.parseDouble(fractionParts[1]);
            if (denominator == 0) {
                throw new IllegalArgumentException("Denominator cannot be zero in fraction: " + token);
            }
            return numerator / denominator;
        }

        return Double.parseDouble(token);
    }

    /**
     * Normalize different spellings of units to a standard form.
     * e.g. "tablespoons" → "tbsp", "grams" → "g", "tbs" → "tbsp", etc.
     */
    private String normalizeUnit(String unit) {
        unit = unit.trim().toLowerCase();

        switch (unit) {
            // imperial
            case "tablespoon":
            case "tablespoons":
            case "tblsp":
            case "tbsp":
            case "tbs":
                return "tbsp";
            case "teaspoon":
            case "teaspoons":
            case "tsp":
                return "tsp";
            case "cup":
            case "cups":
                return "cup";
            case "ounce":
            case "ounces":
            case "oz":
                return "oz";
            case "pound":
            case "pounds":
            case "lb":
            case "lbs":
                return "lb";

            // metric
            case "gram":
            case "grams":
            case "g":
                return "g";
            case "kilogram":
            case "kilograms":
            case "kg":
                return "kg";
            case "millilitre":
            case "milliliter":
            case "millilitres":
            case "milliliters":
            case "ml":
                return "ml";

            // ✅ NEW: litres
            case "l":
            case "litre":
            case "litres":
            case "liter":
            case "liters":
                return "l";

            default:
                return unit;
        }
    }


    private boolean isMetricUnit(String unit) {
        return "g".equals(unit) || "kg".equals(unit) || "ml".equals(unit) || "l".equals(unit);
    }


    private boolean isImperialUnit(String unit) {
        return "cup".equals(unit) ||
                "tbsp".equals(unit) ||
                "tsp".equals(unit) ||
                "oz".equals(unit)  ||
                "lb".equals(unit);
    }
}
