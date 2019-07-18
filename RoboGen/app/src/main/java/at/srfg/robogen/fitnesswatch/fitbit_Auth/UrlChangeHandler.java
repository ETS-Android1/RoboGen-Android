package at.srfg.robogen.fitnesswatch.fitbit_Auth;

/**
 * Created by jboggess on 9/14/16.
 */
public interface UrlChangeHandler {
    void onUrlChanged(String newUrl);
    void onLoadError(int errorCode, CharSequence description);
}
