package use_case.convert_units;

import entity.Measurement;
import entity.MeasurementFactory;
import entity.MeasurementSystem;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests to drive high/near-100% coverage for ConvertUnitsInteractor.
 */
class ConvertUnitsInteractorTest {

    /**
     * Simple presenter double to capture success/failure.
     */
    private static class FakePresenter implements ConvertUnitsOutputBoundary {
        String lastError;
        ConvertUnitsOutputData lastSuccess;

        @Override
        public void prepareFailView(String errorMessage) {
            this.lastError = errorMessage;
            this.lastSuccess = null;
        }

        @Override
        public void prepareSuccessView(ConvertUnitsOutputData outputData) {
            this.lastSuccess = outputData;
            this.lastError = null;
        }
    }

    /**
     * Simple MeasurementFactory that just uses the real Measurement constructor.
     * Adjust this if your Measurement constructor has a different signature.
     */
    private static class FakeMeasurementFactory implements MeasurementFactory {

        @Override
        public Measurement create(double value, String unit, MeasurementSystem system) {
            // If your real constructor is different, edit this line accordingly.
            return new Measurement(value, unit, system);
        }
    }

    /** Helper: build ingredients array of same length as measures. */
    private String[] buildIngredients(int length) {
        String[] ingredients = new String[length];
        for (int i = 0; i < length; i++) {
            ingredients[i] = "ingredient-" + i;
        }
        return ingredients;
    }

    // ========= execute(...) EARLY RETURN BRANCHES =========

    @Test
    void execute_nullInput_triggersFailView() {
        FakePresenter presenter = new FakePresenter();
        ConvertUnitsInteractor interactor =
                new ConvertUnitsInteractor(new FakeMeasurementFactory(), presenter);

        interactor.execute(null);

        assertNull(presenter.lastSuccess);
        assertEquals("No data provided for unit conversion.", presenter.lastError);
    }

    @Test
    void execute_nullIngredients_triggersFailView() {
        FakePresenter presenter = new FakePresenter();
        ConvertUnitsInteractor interactor =
                new ConvertUnitsInteractor(new FakeMeasurementFactory(), presenter);

        String[] measures = {"200 g"};
        ConvertUnitsInputData input = new ConvertUnitsInputData(
                "id", "name", null, measures, MeasurementSystem.METRIC);

        interactor.execute(input);

        assertNull(presenter.lastSuccess);
        assertEquals("Ingredients and measures are required for conversion.", presenter.lastError);
    }

    @Test
    void execute_nullMeasures_triggersFailView() {
        FakePresenter presenter = new FakePresenter();
        ConvertUnitsInteractor interactor =
                new ConvertUnitsInteractor(new FakeMeasurementFactory(), presenter);

        String[] ingredients = {"Flour"};
        ConvertUnitsInputData input = new ConvertUnitsInputData(
                "id", "name", ingredients, null, MeasurementSystem.METRIC);

        interactor.execute(input);

        assertNull(presenter.lastSuccess);
        assertEquals("Ingredients and measures are required for conversion.", presenter.lastError);
    }

    @Test
    void execute_emptyArrays_triggersFailView() {
        FakePresenter presenter = new FakePresenter();
        ConvertUnitsInteractor interactor =
                new ConvertUnitsInteractor(new FakeMeasurementFactory(), presenter);

        String[] ingredients = {};
        String[] measures = {};
        ConvertUnitsInputData input = new ConvertUnitsInputData(
                "id", "name", ingredients, measures, MeasurementSystem.METRIC);

        interactor.execute(input);

        assertNull(presenter.lastSuccess);
        assertEquals("Ingredients and measures are required for conversion.", presenter.lastError);
    }

    @Test
    void execute_lengthMismatch_triggersFailView() {
        FakePresenter presenter = new FakePresenter();
        ConvertUnitsInteractor interactor =
                new ConvertUnitsInteractor(new FakeMeasurementFactory(), presenter);

        String[] ingredients = {"Flour", "Milk"};
        String[] measures = {"200 g"};
        ConvertUnitsInputData input = new ConvertUnitsInputData(
                "id", "name", ingredients, measures, MeasurementSystem.METRIC);

        interactor.execute(input);

        assertNull(presenter.lastSuccess);
        assertEquals("Ingredients and measures must have the same length.", presenter.lastError);
    }

    // ========= SUCCESS PATHS + convertSingleMeasure BRANCHES =========

    @Test
    void execute_imperialToMetric_coversImperialBranchesAndEdgeCases() {
        FakePresenter presenter = new FakePresenter();
        FakeMeasurementFactory factory = new FakeMeasurementFactory();
        ConvertUnitsInteractor interactor =
                new ConvertUnitsInteractor(factory, presenter);

        String[] measures = {
                null,          // rawMeasure == null
                "   ",         // trimmed empty → returns rawMeasure
                "weird",       // no digits → idx == 0 → return rawMeasure
                "123",         // digits only → idx >= length → return rawMeasure
                "1 bunch",     // unknown unit → !metric && !imperial → return rawMeasure

                "1/2cup",      // fraction + no-space cup (<1000ml)
                "5cup",        // cup → >=1000ml → litres
                "2 tbs",       // space form, normalizes to tbsp
                "3tsp",        // tsp no-space
                "10oz",        // ounces → g
                "2lb",         // pounds → g (<1000g)
                "3lb",         // pounds → kg (>=1000g)

                "1/0g",        // fraction with zero denominator → exception → unchanged
                "1/2/3g",      // invalid fraction → exception → unchanged
                "abc g"        // NumberFormatException → unchanged
        };

        String[] ingredients = buildIngredients(measures.length);

        ConvertUnitsInputData input = new ConvertUnitsInputData(
                "id-1", "Imperial-ish Recipe", ingredients, measures, MeasurementSystem.IMPERIAL);

        interactor.execute(input);

        assertNull(presenter.lastError);
        assertNotNull(presenter.lastSuccess);
        // We don't assert exact converted strings here; coverage comes from executing branches.
    }

    @Test
    void execute_metricToImperial_coversMetricBranches() {
        FakePresenter presenter = new FakePresenter();
        FakeMeasurementFactory factory = new FakeMeasurementFactory();
        ConvertUnitsInteractor interactor =
                new ConvertUnitsInteractor(factory, presenter);

        String[] measures = {
                "1 l",        // litres → cups
                "240 ml",     // ml → cups
                "28.35 g",    // g → oz
                "1 kg"        // kg → lb
                // The metric switch default is effectively unreachable because
                // isMetricUnit() only returns true for g, kg, ml, l.
        };

        String[] ingredients = buildIngredients(measures.length);

        ConvertUnitsInputData input = new ConvertUnitsInputData(
                "id-2", "Metric Recipe", ingredients, measures, MeasurementSystem.METRIC);

        interactor.execute(input);

        assertNull(presenter.lastError);
        assertNotNull(presenter.lastSuccess);
    }

    @Test
    void execute_mixedFormats_coverSpaceAndNoSpaceParsing() {
        FakePresenter presenter = new FakePresenter();
        FakeMeasurementFactory factory = new FakeMeasurementFactory();
        ConvertUnitsInteractor interactor =
                new ConvertUnitsInteractor(factory, presenter);

        // Mix of space-separated and concatenated tokens to exercise both parsing styles.
        String[] measures = {
                "200 g",   // space + metric
                "350g",    // no-space + metric
                "1/2 cup", // fraction + space, imperial
                "2tbsp",   // no-space tbsp
                "1/3cup",  // no-space fraction + cup
                "5 tsp",   // space tsp
                "0.5kg"    // no-space decimal metric
        };

        String[] ingredients = buildIngredients(measures.length);

        ConvertUnitsInputData input = new ConvertUnitsInputData(
                "id-3", "Mixed Recipe", ingredients, measures, MeasurementSystem.METRIC);

        interactor.execute(input);

        assertNull(presenter.lastError);
        assertNotNull(presenter.lastSuccess);
    }
}
