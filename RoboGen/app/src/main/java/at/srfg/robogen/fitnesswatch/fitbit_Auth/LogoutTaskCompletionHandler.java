package at.srfg.robogen.fitnesswatch.fitbit_Auth;

/**
 * Created by jboggess on 9/19/16.
 */
public interface LogoutTaskCompletionHandler {
    void logoutSuccess();

    void logoutError(String message);
}
