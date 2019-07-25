package at.srfg.robogen.fitnesswatch.fitbit_Auth;

/*******************************************************************************
 * Interface LogoutTaskCompletionHandler
 ******************************************************************************/
public interface LogoutTaskCompletionHandler {
    void logoutSuccess();

    void logoutError(String message);
}
