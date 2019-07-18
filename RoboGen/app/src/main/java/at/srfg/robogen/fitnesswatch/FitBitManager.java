package at.srfg.robogen.fitnesswatch;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Set;

import at.srfg.robogen.R;
import at.srfg.robogen.fitnesswatch.fitbit_Auth.AuthenticationHandler;
import at.srfg.robogen.fitnesswatch.fitbit_Auth.AuthenticationManager;
import at.srfg.robogen.fitnesswatch.fitbit_Auth.AuthenticationResult;
import at.srfg.robogen.fitnesswatch.fitbit_Auth.Scope;


public class FitBitManager extends AppCompatActivity implements AuthenticationHandler {

    private Activity mParentActivity;
    private Context mParentContext;

    public FitBitManager(Activity act, Context ct)
    {
        mParentActivity = act;
        mParentContext = ct;
    }


    @Override
    protected void onResume() {
        super.onResume();

        /**
         *  (Look in FitbitAuthApplication for Step 1)
         */


        /**
         *  2. If we are logged in, go to next activity
         *      Otherwise, display the login screen
         */
        if (AuthenticationManager.isLoggedIn()) {
            onLoggedIn();
        }
    }


    public void logIn(View view) {
        /**
         *  3. Call login to show the login UI
         */
        AuthenticationManager.login(mParentActivity);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /**
         *  4. When the Login UI finishes, it will invoke the `onActivityResult` of this activity.
         *  We call `AuthenticationManager.onActivityResult` and set ourselves as a login listener
         *  (via AuthenticationHandler) to check to see if this result was a login result. If the
         *  result code matches login, the AuthenticationManager will process the login request,
         *  and invoke our `onAuthFinished` method.
         *
         *  If the result code was not a login result code, then `onActivityResult` will return
         *  false, and we can handle other onActivityResult result codes.
         *
         */

        if (!AuthenticationManager.onActivityResult(requestCode, resultCode, data, this)) {
            // Handle other activity results, if needed
        }

    }

    public void onAuthFinished(AuthenticationResult authenticationResult) {

        /**
         * 5. Now we can parse the auth response! If the auth was successful, we can continue onto
         *      the next activity. Otherwise, we display a generic error message here
         */
        if (authenticationResult.isSuccessful()) {
            onLoggedIn();
        } else {
            displayAuthError(authenticationResult);
        }
    }

    public void onLoggedIn() {
        //Intent intent = UserDataActivity.newIntent(this);
        //startActivity(intent);
    }


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
}
