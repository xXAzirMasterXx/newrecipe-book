package use_case.add_recipes;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class UnitConversionUtils {

    private UnitConversionUtils() {}

    private static final double TSP_TO_ML = 5.0;
    private static final double TBSP_TO_ML = 15.0;
    private static final double OZ_TO_G = 28.3495;
    private static final double LB_TO_G = 453.592;

    private static final Pattern NUMBER_PATTERN =
            Pattern.compile("(\\d+\\s+\\d+/\\d+|\\d+/\\d+|\\d+(?:\\.\\d+)?)");

    public static String[] toMetric(String[] measures) {
        if (measures == null) return null;
        String[] result = new String[measures.length];
        for (int i = 0; i < measures.length; i++) {
            result[i] = convertSingleToMetric(measures[i]);
        }
        return result;
    }

    public static String[] toImperial(String[] measures) {
        if (measures == null) return null;
        String[] result = new String[measures.length];
        for (int i = 0; i < measures.length; i++) {
            result[i] = convertSingleToImperial(measures[i]);
        }
        return result;
    }

    private static String convertSingleToMetric(String raw) {
        if (raw == null) return null;
        String s = raw.trim();
        if (s.isEmpty()) return s;

        double quantity = extractQuantity(s);
        String lower = s.toLowerCase(Locale.ROOT);

        if (containsAny(lower, "tbsp", "tbs", "tablespoon")) {
            double ml = quantity * TBSP_TO_ML;
            return format(ml) + " mL";
        }

        if (containsAny(lower, "tsp", "teaspoon")) {
            double ml = quantity * TSP_TO_ML;
            return format(ml) + " mL";
        }

        if (containsAny(lower, "lb", "pound")) {
            double g = quantity * LB_TO_G;
            return format(g) + " g";
        }

        if (containsAny(lower, "oz", "ounce")) {
            double g = quantity * OZ_TO_G;
            return format(g) + " g";
        }

        if (containsAny(lower, "g ", " g", "gram", "grams")) {
            return normalizeWithNumber(quantity, "g");
        }

        if (containsAny(lower, "ml", "milliliter", "millilitre")) {
            return normalizeWithNumber(quantity, "mL");
        }

        return raw;
    }

    private static String convertSingleToImperial(String raw) {
        if (raw == null) return null;
        String s = raw.trim();
        if (s.isEmpty()) return s;

        double quantity = extractQuantity(s);
        String lower = s.toLowerCase(Locale.ROOT);

        if (containsAny(lower, "tbsp", "tbs", "tablespoon")) {
            return normalizeWithNumber(quantity, "tbsp");
        }
        if (containsAny(lower, "tsp", "teaspoon")) {
            return normalizeWithNumber(quantity, "tsp");
        }
        if (containsAny(lower, "lb", "pound")) {
            return normalizeWithNumber(quantity, "lb");
        }
        if (containsAny(lower, "oz", "ounce")) {
            return normalizeWithNumber(quantity, "oz");
        }

        // grams -> oz (your requirement)
        if (containsAny(lower, "g ", " g", "gram", "grams")) {
            double oz = quantity / OZ_TO_G;
            return format(oz) + " oz";
        }

        // mL -> tsp / tbsp (simple heuristic)
        if (containsAny(lower, "ml", "milliliter", "millilitre")) {
            if (quantity >= TBSP_TO_ML) {
                double tbsp = quantity / TBSP_TO_ML;
                return format(tbsp) + " tbsp";
            } else {
                double tsp = quantity / TSP_TO_ML;
                return format(tsp) + " tsp";
            }
        }

        return raw;
    }

    private static double extractQuantity(String s) {
        Matcher m = NUMBER_PATTERN.matcher(s);
        if (!m.find()) {
            return 1.0; // e.g. "to taste"
        }
        String numStr = m.group(1).trim();

        if (numStr.contains(" ")) { // "1 1/2"
            String[] parts = numStr.split("\\s+");
            if (parts.length == 2 && parts[1].contains("/")) {
                double whole = parseDoubleSafe(parts[0]);
                double frac = parseFraction(parts[1]);
                return whole + frac;
            }
        }

        if (numStr.contains("/")) { // "1/2"
            return parseFraction(numStr);
        }

        return parseDoubleSafe(numStr);
    }

    private static double parseFraction(String frac) {
        String[] parts = frac.split("/");
        if (parts.length != 2) return 0.0;
        double num = parseDoubleSafe(parts[0]);
        double den = parseDoubleSafe(parts[1]);
        if (den == 0) return 0.0;
        return num / den;
    }

    private static double parseDoubleSafe(String s) {
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private static boolean containsAny(String s, String... needles) {
        for (String n : needles) {
            if (s.contains(n)) return true;
        }
        return false;
    }

    private static String format(double value) {
        if (Math.abs(value - Math.round(value)) < 1e-6) {
            return String.valueOf(Math.round(value));
        }
        return String.format(Locale.ROOT, "%.2f", value);
    }

    private static String normalizeWithNumber(double quantity, String unit) {
        return format(quantity) + " " + unit;
    }
}
