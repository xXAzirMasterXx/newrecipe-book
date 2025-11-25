package interface_adapter.logout;

import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.logged_in.LoggedInState;
import interface_adapter.login.LoginViewModel;
import interface_adapter.login.LoginState;
import use_case.logout.LogoutOutputBoundary;
import use_case.logout.LogoutOutputData;

/**
 * The Presenter for the Logout Use Case.
 */
public class LogoutPresenter implements LogoutOutputBoundary {

    private final LoggedInViewModel loggedInViewModel;
    private final ViewManagerModel viewManagerModel;
    private final LoginViewModel loginViewModel;

    public LogoutPresenter(ViewManagerModel viewManagerModel,
                           LoggedInViewModel loggedInViewModel,
                           LoginViewModel loginViewModel) {

        this.viewManagerModel = viewManagerModel;
        this.loggedInViewModel = loggedInViewModel;
        this.loginViewModel = loginViewModel;
    }

    @Override
    public void prepareSuccessView(LogoutOutputData response) {

        // ======================================
        // 1. Clear LoggedInState username
        // ======================================
        LoggedInState loggedInState = loggedInViewModel.getState();
        loggedInState.setUsername("");   // remove username on logged-in screen
        loggedInViewModel.setState(loggedInState);
        this.loggedInViewModel.firePropertyChange();



        // ======================================
        // 2. Update LoginState (show the username that logged out)
        // ======================================
        LoginState loginState = loginViewModel.getState();
        loginState.setUsername(response.getUsername()); // suggested username on login screen
        loginState.setPassword("");                     // passwords should always clear on logout
        loginViewModel.setState(loginState);
        this.loginViewModel.firePropertyChange();

        // ======================================
        // 3. Switch View
        // ======================================
        viewManagerModel.setState(loginViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }
}
