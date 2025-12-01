package use_case.convert_units;

public interface ConvertUnitsOutputBoundary {

    void prepareSuccessView(ConvertUnitsOutputData outputData);

    void prepareFailView(String errorMessage);
}
