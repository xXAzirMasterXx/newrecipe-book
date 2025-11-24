package use_case.add_recipes;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class UnitConversionUtils {

    private UnitConversionUtils() {}

    private static final double TSP_TO_ML = 5.0;
    private static final double TBSP_TO_ML = 15.0;
    private static final double OZ_TO_G = 28;
    private static final double LB_TO_G = 454;

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

    // =========================
    // Single-value converters
    // =========================

    private static String convertSingleToMetric(String raw) {
        if (raw == null) return null;
        String s = raw.trim();
        if (s.isEmpty()) return s;

        double quantity = extractQuantity(s);
        String lower = s.toLowerCase(Locale.ROOT);

        // tbsp -> mL
        if (containsAny(lower, "tbsp", "tbs", "tablespoon")) {
            double ml = quantity * TBSP_TO_ML;
            return format(ml) + " mL";
        }

        // tsp -> mL
        if (containsAny(lower, "tsp", "teaspoon")) {
            double ml = quantity * TSP_TO_ML;
            return format(ml) + " mL";
        }

        // lb -> g
        if (containsAny(lower, "lb", "pound")) {
            double g = quantity * LB_TO_G;
            return format(g) + " g";
        }

        // oz -> g
        if (containsAny(lower, "oz", "ounce")) {
            double g = quantity * OZ_TO_G;
            return format(g) + " g";
        }

        // already grams -> normalize even if it's "400g chicken"
        if (isGramUnit(lower)) {
            return normalizeWithNumber(quantity, "g");
        }

        // already mL -> normalize
        if (containsAny(lower, "ml", "milliliter", "millilitre")) {
            return normalizeWithNumber(quantity, "mL");
        }

        // unknown unit: return original
        return raw;
    }

    private static String convertSingleToImperial(String raw) {
        if (raw == null) return null;
        String s = raw.trim();
        if (s.isEmpty()) return s;

        double quantity = extractQuantity(s);
        String lower = s.toLowerCase(Locale.ROOT);

        // already in common imperial units: normalize formatting
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

        // grams -> lb or oz
        // Handles "400 g", "400g", "400 g chicken", "400g chicken", "grams"
        if (isGramUnit(lower)) {
            if (quantity >= LB_TO_G) {          // >= 1 pound
                double lb = quantity / LB_TO_G;
                return format(lb) + " lb";
            } else {                            // < 1 pound
                double oz = quantity / OZ_TO_G;
                return format(oz) + " oz";
            }
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

        // unknown unit: return original
        return raw;
    }

    // =========================
    // Parsing helpers
    // =========================

    private static double extractQuantity(String s) {
        Matcher m = NUMBER_PATTERN.matcher(s);
        if (!m.find()) {
            // default if we can't find a number (e.g., "to taste")
            return 1.0;
        }
        String numStr = m.group(1).trim();

        // "1 1/2"
        if (numStr.contains(" ")) {
            String[] parts = numStr.split("\\s+");
            if (parts.length == 2 && parts[1].contains("/")) {
                double whole = parseDoubleSafe(parts[0]);
                double frac = parseFraction(parts[1]);
                return whole + frac;
            }
        }

        // "1/2"
        if (numStr.contains("/")) {
            return parseFraction(numStr);
        }

        // "1" or "1.5"
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

    // =========================
    // Unit detection helpers
    // =========================

    private static boolean containsAny(String s, String... needles) {
        for (String n : needles) {
            if (s.contains(n)) return true;
        }
        return false;
    }

    /**
     * Detect if the string uses grams as a unit, including forms like:
     * "400 g", "400g", "400 g chicken", "400g chicken", "grams", "gram".
     */
    private static boolean isGramUnit(String lower) {
        // "400 g", "400 g chicken"
        boolean numberSpaceG = lower.matches(".*\\d+\\s*g\\b.*");
        // "400g", "400g chicken"
        boolean numberG = lower.matches(".*\\d+g\\b.*");
        // explicit word usage
        boolean wordGram = lower.contains("gram");

        return numberSpaceG || numberG || wordGram;
    }

    // =========================
    // Formatting helpers
    // =========================

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
