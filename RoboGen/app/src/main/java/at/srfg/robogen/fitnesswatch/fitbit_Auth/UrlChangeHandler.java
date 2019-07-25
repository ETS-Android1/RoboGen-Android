package at.srfg.robogen.fitnesswatch.fitbit_Auth;

/*******************************************************************************
 * Interface UrlChangeHandler
 ******************************************************************************/
public interface UrlChangeHandler {
    void onUrlChanged(String newUrl);
    void onLoadError(int errorCode, CharSequence description);
}
