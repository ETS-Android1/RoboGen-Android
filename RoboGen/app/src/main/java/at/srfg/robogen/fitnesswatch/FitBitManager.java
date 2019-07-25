package at.srfg.robogen.fitnesswatch;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;
import java.util.Set;

import at.srfg.robogen.ItemDetailActivity;
import at.srfg.robogen.R;
import at.srfg.robogen.fitnesswatch.fitbit_Auth.AuthenticationHandler;
import at.srfg.robogen.fitnesswatch.fitbit_Auth.AuthenticationManager;
import at.srfg.robogen.fitnesswatch.fitbit_Auth.AuthenticationResult;
import at.srfg.robogen.fitnesswatch.fitbit_Auth.AuthenticationWebviewClient;
import at.srfg.robogen.fitnesswatch.fitbit_Auth.Scope;
import at.srfg.robogen.itemdetail.ItemDetailWatch;

/*******************************************************************************
 * Class FitBitManager
 * serves as AuthenticationHandler and Activity
********************************************************************************
 * Steps for Login into our RoboGen-FitBitAccount:
 *
 * Step 1) Look in FitbitAuthApplication.generateAuthenticationConfiguration()
 *
 * Step 2) this.onResume():
 *         If we are logged in, go to next activity, otherwise, display the login screen
 *
 * Step 3) this.logIn():
 *         Call login to show the login UI (LoginActivity)
 *
 * Step 4) ItemDetailActivity.onActivityResult():
 *         When the Login UI finishes, it will invoke the `onActivityResult` of ItemDetailActivity.
 *         We call `AuthenticationManager.onActivityResult` and set ourselves as a login listener
 *         (via AuthenticationHandler) to check to see if this result was a login result. If the
 *         result code matches login, the AuthenticationManager will process the login request,
 *         and invoke our `onAuthFinished` method.
 *
 *         If the result code was not a login result code, then `onActivityResult` will return
 *         false, and we can handle other onActivityResult result codes.
 *
 * Step 5) this.onAuthFinished():
 *         Now we can parse the auth response! If the auth was successful, we can continue onto
 *         the next activity. Otherwise, we display a generic error message here
 ******************************************************************************/
public class FitBitManager extends AppCompatActivity implements AuthenticationHandler { // TODO: does this even need to be an activity anymore??

    private Activity mParentActivity;
    private Context mParentContext;

    public FitBitManager(Activity act, Context ct)
    {
        mParentActivity = act;
        mParentContext = ct;
    }

    /*******************************************************************************
     * onResume will start process if is logged in succesfully
     ******************************************************************************/
    @Override
    protected void onResume() {
        super.onResume();

        if (AuthenticationManager.isLoggedIn()) {
            onLoggedIn();
        }
    }

    /*******************************************************************************
     * logIn, will logIn the User in FitBit Account
     ******************************************************************************/
    public void logIn(View view) {
        AuthenticationManager.login(mParentActivity);
    }

    /*******************************************************************************
     * onAuthFinished
     ******************************************************************************/
    public void onAuthFinished(AuthenticationResult authenticationResult) {
        if (authenticationResult.isSuccessful()) {
            onLoggedIn();
        } else {
            displayAuthError(authenticationResult);
        }
    }

    public void onLoggedIn() {
        // TODO?
    }

    /*******************************************************************************
     * displayAuthError
     ******************************************************************************/
    private void displayAuthError(AuthenticationResult authenticationResult) {
        String message = "";

        switch (authenticationResult.getStatus()) {
            case dismissed:
                message = getString(R.string.login_dismissed);
                break;
            case error:
                message = authenticationResult.getErrorMessage();
                break;
            case missing_required_scopes:
                Set<Scope> missingScopes = authenticationResult.getMissingScopes();
                String missingScopesText = TextUtils.join(", ", missingScopes);
                message = getString(R.string.missing_scopes_error) + missingScopesText;
                break;
        }

        new AlertDialog.Builder(this)
                .setTitle(R.string.login_title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
                .create()
                .show();
    }

    /*******************************************************************************
     * getHearthRate
     ******************************************************************************/
    public void startUserDataStream()
    {
        // TODO: https://dev.fitbit.com/build/reference/web-api/heart-rate/
        // Resource URL 1: GET https://api.fitbit.com/1/user/[user-id]/activities/heart/date/[date]/[period].json
        // Resource URL 2: GET https://api.fitbit.com/1/user/[user-id]/activities/heart/date/[base-date]/[end-date].json
        // Example request: GET https://api.fitbit.com/1/user/-/activities/heart/date/today/1d.json

        if(AuthenticationManager.isLoggedIn()) {
            Intent intent = UserDataActivity.newIntent(mParentContext);
            mParentActivity.startActivity(intent);
        }
    }
}
