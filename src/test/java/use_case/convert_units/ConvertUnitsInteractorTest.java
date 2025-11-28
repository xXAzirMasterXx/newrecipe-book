package use_case.convert_units;

import entity.Measurement;
import entity.MeasurementFactory;
import entity.MeasurementSystem;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ConvertUnitsInteractor.
 */
class ConvertUnitsInteractorTest {

    /**
     * Simple MeasurementFactory for tests.
     */
    private final MeasurementFactory factory =
            (double value, String unit, MeasurementSystem system) ->
                    new Measurement(value, unit, system);

    /**
     * Helper to build an interactor with a presenter that asserts success
     * and returns the output data to the test via a single-element array.
     */
    private ConvertUnitsInteractor interactorWithSuccessCapture(ConvertUnitsOutputData[] captureSlot) {
        ConvertUnitsOutputBoundary presenter = new ConvertUnitsOutputBoundary() {
            @Override
            public void prepareSuccessView(ConvertUnitsOutputData data) {
                captureSlot[0] = data;
            }

            @Override
            public void prepareFailView(String error) {
                fail("Unexpected failure: " + error);
            }
        };

        return new ConvertUnitsInteractor(factory, presenter);
    }

    /**
     * Helper to build an interactor that should fail with a specific message.
     */
    private ConvertUnitsInteractor interactorExpectingFailure(String expectedMessage) {
        ConvertUnitsOutputBoundary presenter = new ConvertUnitsOutputBoundary() {
            @Override
            public void prepareSuccessView(ConvertUnitsOutputData data) {
                fail("Expected failure, but got success.");
            }

            @Override
            public void prepareFailView(String error) {
                assertEquals(expectedMessage, error);
            }
        };

        return new ConvertUnitsInteractor(factory, presenter);
    }

    @Test
    void convertsImperialCupToMetricMl() {
        String[] ingredients = {"Water"};
        String[] measures = {"1 cup"};

        ConvertUnitsInputData input = new ConvertUnitsInputData(
                "r1",
                "Test Recipe",
                ingredients,
                measures,
                MeasurementSystem.METRIC
        );

        ConvertUnitsOutputData[] capture = new ConvertUnitsOutputData[1];
        ConvertUnitsInteractor interactor = interactorWithSuccessCapture(capture);

        interactor.execute(input);

        assertNotNull(capture[0]);
        String[] converted = capture[0].getConvertedMeasures();
        assertEquals(1, converted.length);
        assertEquals("240.0 ml", converted[0]); // 1 cup → 240.0 ml
    }

    @Test
    void convertsImperialTablespoonToMetricMl_includingTbsAlias() {
        String[] ingredients = {"Olive Oil", "Sugar"};
        String[] measures = {"2 tbsp", "3 tbs"}; // "tbs" should normalize to "tbsp"

        ConvertUnitsInputData input = new ConvertUnitsInputData(
                "r2",
                "Test Recipe 2",
                ingredients,
                measures,
                MeasurementSystem.METRIC
        );

        ConvertUnitsOutputData[] capture = new ConvertUnitsOutputData[1];
        ConvertUnitsInteractor interactor = interactorWithSuccessCapture(capture);

        interactor.execute(input);

        String[] converted = capture[0].getConvertedMeasures();
        assertEquals(2, converted.length);
        // 2 tbsp → 30.0 ml
        assertEquals("30.0 ml", converted[0]);
        // 3 tbs → 45.0 ml
        assertEquals("45.0 ml", converted[1]);
    }

    @Test
    void convertsImperialTeaspoonToMetricMl() {
        String[] ingredients = {"Salt"};
        String[] measures = {"1 tsp"};

        ConvertUnitsInputData input = new ConvertUnitsInputData(
                "r3",
                "Test Recipe 3",
                ingredients,
                measures,
                MeasurementSystem.METRIC
        );

        ConvertUnitsOutputData[] capture = new ConvertUnitsOutputData[1];
        ConvertUnitsInteractor interactor = interactorWithSuccessCapture(capture);

        interactor.execute(input);

        String[] converted = capture[0].getConvertedMeasures();
        assertEquals(1, converted.length);
        assertEquals("5.0 ml", converted[0]); // 1 tsp → 5.0 ml
    }

    @Test
    void convertsImperialOunceToMetricGrams() {
        String[] ingredients = {"Butter"};
        String[] measures = {"1 oz"};

        ConvertUnitsInputData input = new ConvertUnitsInputData(
                "r4",
                "Test Recipe 4",
                ingredients,
                measures,
                MeasurementSystem.METRIC
        );

        ConvertUnitsOutputData[] capture = new ConvertUnitsOutputData[1];
        ConvertUnitsInteractor interactor = interactorWithSuccessCapture(capture);

        interactor.execute(input);

        String[] converted = capture[0].getConvertedMeasures();
        assertEquals(1, converted.length);
        // 1 oz → 28.35 g → 28.4 g after rounding to 1 dp
        assertEquals("28.4 g", converted[0]);
    }

    @Test
    void convertsImperialPoundsToMetricGOrKg() {
        String[] ingredients = {"Beef", "Flour"};
        String[] measures = {"1 lb", "2.5 lb"};

        ConvertUnitsInputData input = new ConvertUnitsInputData(
                "r5",
                "Test Recipe 5",
                ingredients,
                measures,
                MeasurementSystem.METRIC
        );

        ConvertUnitsOutputData[] capture = new ConvertUnitsOutputData[1];
        ConvertUnitsInteractor interactor = interactorWithSuccessCapture(capture);

        interactor.execute(input);

        String[] converted = capture[0].getConvertedMeasures();
        assertEquals(2, converted.length);

        // 1 lb → 453.592 g → 453.6 g
        assertEquals("453.6 g", converted[0]);

        // 2.5 lb → 1133.98 g → >= 1000g → 1.1 kg
        assertEquals("1.1 kg", converted[1]);
    }

    @Test
    void convertsMetricMlToImperialCup() {
        String[] ingredients = {"Water"};
        String[] measures = {"200 ml"};

        ConvertUnitsInputData input = new ConvertUnitsInputData(
                "r6",
                "Test Recipe 6",
                ingredients,
                measures,
                MeasurementSystem.IMPERIAL
        );

        ConvertUnitsOutputData[] capture = new ConvertUnitsOutputData[1];
        ConvertUnitsInteractor interactor = interactorWithSuccessCapture(capture);

        interactor.execute(input);

        String[] converted = capture[0].getConvertedMeasures();
        assertEquals(1, converted.length);

        // 200 ml → 0.8333... cup → 0.8 cup (1 d.p.)
        assertEquals("0.8 cup", converted[0]);
    }

    @Test
    void convertsMetricGramsToImperialOunces() {
        String[] ingredients = {"Butter"};
        String[] measures = {"500 g"};

        ConvertUnitsInputData input = new ConvertUnitsInputData(
                "r7",
                "Test Recipe 7",
                ingredients,
                measures,
                MeasurementSystem.IMPERIAL
        );

        ConvertUnitsOutputData[] capture = new ConvertUnitsOutputData[1];
        ConvertUnitsInteractor interactor = interactorWithSuccessCapture(capture);

        interactor.execute(input);

        String[] converted = capture[0].getConvertedMeasures();
        assertEquals(1, converted.length);

        // 500 g → 500 / 28.35 ≈ 17.64 → 17.6 oz
        assertEquals("17.6 oz", converted[0]);
    }

    @Test
    void convertsMetricKgToImperialPounds() {
        String[] ingredients = {"Rice"};
        String[] measures = {"1 kg"};

        ConvertUnitsInputData input = new ConvertUnitsInputData(
                "r8",
                "Test Recipe 8",
                ingredients,
                measures,
                MeasurementSystem.IMPERIAL
        );

        ConvertUnitsOutputData[] capture = new ConvertUnitsOutputData[1];
        ConvertUnitsInteractor interactor = interactorWithSuccessCapture(capture);

        interactor.execute(input);

        String[] converted = capture[0].getConvertedMeasures();
        assertEquals(1, converted.length);

        // 1 kg → 2.20462 lb → 2.2 lb (1 d.p.)
        assertEquals("2.2 lb", converted[0]);
    }

    @Test
    void parsesFractionAndConverts() {
        String[] ingredients = {"Milk"};
        String[] measures = {"1/2 cup"};

        ConvertUnitsInputData input = new ConvertUnitsInputData(
                "r9",
                "Test Recipe 9",
                ingredients,
                measures,
                MeasurementSystem.METRIC
        );

        ConvertUnitsOutputData[] capture = new ConvertUnitsOutputData[1];
        ConvertUnitsInteractor interactor = interactorWithSuccessCapture(capture);

        interactor.execute(input);

        String[] converted = capture[0].getConvertedMeasures();
        assertEquals(1, converted.length);

        // 1/2 cup → 0.5 cup → 120 ml → 120.0 ml
        assertEquals("120.0 ml", converted[0]);
    }

    @Test
    void leavesUnrecognisedOrNonNumericMeasuresUnchanged() {
        String[] ingredients = {"Salt", "Pepper", "Herbs"};
        String[] measures = {"Pinch", "to taste", "1 bunch"};

        ConvertUnitsInputData input = new ConvertUnitsInputData(
                "r10",
                "Test Recipe 10",
                ingredients,
                measures,
                MeasurementSystem.METRIC   // any target; they should stay unchanged
        );

        ConvertUnitsOutputData[] capture = new ConvertUnitsOutputData[1];
        ConvertUnitsInteractor interactor = interactorWithSuccessCapture(capture);

        interactor.execute(input);

        String[] converted = capture[0].getConvertedMeasures();
        assertArrayEquals(measures, converted);
    }

    @Test
    void failsIfNoIngredientsOrMeasures() {
        String[] ingredients = {};
        String[] measures = {};

        ConvertUnitsInputData input = new ConvertUnitsInputData(
                "r11",
                "Empty Recipe",
                ingredients,
                measures,
                MeasurementSystem.METRIC
        );

        ConvertUnitsInteractor interactor =
                interactorExpectingFailure("Ingredients and measures are required for conversion.");

        interactor.execute(input);
    }

    @Test
    void failsIfLengthsMismatch() {
        String[] ingredients = {"Salt", "Pepper"};
        String[] measures = {"1 tsp"}; // mismatch

        ConvertUnitsInputData input = new ConvertUnitsInputData(
                "r12",
                "Bad Recipe",
                ingredients,
                measures,
                MeasurementSystem.METRIC
        );

        ConvertUnitsInteractor interactor =
                interactorExpectingFailure("Ingredients and measures must have the same length.");

        interactor.execute(input);
    }
}
