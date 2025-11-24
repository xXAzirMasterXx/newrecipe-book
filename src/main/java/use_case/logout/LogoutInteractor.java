package use_case.logout;

/**
 * The Logout Interactor.
 */
public class LogoutInteractor implements LogoutInputBoundary {
    private final LogoutUserDataAccessInterface userDataAccessObject;
    private final LogoutOutputBoundary logoutPresenter;

    public LogoutInteractor(LogoutUserDataAccessInterface userDataAccessInterface,
                            LogoutOutputBoundary logoutOutputBoundary) {
        // save the DAO and Presenter in the instance variables.
        this.userDataAccessObject = userDataAccessInterface;
        this.logoutPresenter = logoutOutputBoundary;
    }

    @Override
    public void execute() {
        // 1. Get the current username from the DAO
        String username = userDataAccessObject.getCurrentUsername();

        // 2. Set the current username to null (log the user out)
        userDataAccessObject.setCurrentUsername(null);

        // 3. Create the output data (it needs to contain the username)
        LogoutOutputData outputData = new LogoutOutputData(username);

        // 4. Tell the presenter to prepare the success view
        logoutPresenter.prepareSuccessView(outputData);
    }
}
