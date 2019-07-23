package at.srfg.robogen.fitnesswatch.fitbit_Auth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashSet;
import java.util.Set;

import at.srfg.robogen.R;


public class LoginActivity extends AppCompatActivity implements AuthenticationHandler {

    public static final String CONFIGURATION_VERSION = "CONFIGURATION_VERSION";
    public static final String AUTHENTICATION_RESULT_KEY = "AUTHENTICATION_RESULT_KEY";
    private static final String CLIENT_CREDENTIALS_KEY = "CLIENT_CREDENTIALS_KEY";
    private static final String EXPIRES_IN_KEY = "EXPIRES_IN_KEY";
    private static final String SCOPES_KEY = "SCOPES_KEY";

    private WebView mLoginView;
    private ProgressBar mLoginProgress;

    public static Intent createIntent(Context context, @NonNull ClientCredentials clientCredentials, @Nullable Long expiresIn, Set<Scope> scopes) {
        return createIntent(context, null, clientCredentials, expiresIn, scopes);
    }

    public static Intent createIntent(Context context, Integer configVersion, @NonNull ClientCredentials clientCredentials, @Nullable Long expiresIn, Set<Scope> scopes) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra(CLIENT_CREDENTIALS_KEY, clientCredentials);
        intent.putExtra(CONFIGURATION_VERSION, configVersion);
        intent.putExtra(EXPIRES_IN_KEY, expiresIn);
        intent.putExtra(SCOPES_KEY, scopes.toArray(new Scope[scopes.size()]));

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mLoginView = (WebView) findViewById(R.id.login_webview);
        mLoginProgress = (ProgressBar) findViewById(R.id.login_progressBar);

        ClientCredentials clientCredentials = getIntent().getParcelableExtra(CLIENT_CREDENTIALS_KEY);
        Long expiresIn = getIntent().getLongExtra(EXPIRES_IN_KEY, 604800);
        Parcelable[] scopes = getIntent().getParcelableArrayExtra(SCOPES_KEY);
        Set<Scope> scopesSet = new HashSet<>();
        for (Parcelable scope : scopes) {
            scopesSet.add((Scope) scope);
        }

        AuthorizationController authorizationController = new AuthorizationController(
                mLoginView,
                clientCredentials,
                this);

        authorizationController.authenticate(expiresIn, scopesSet);
    }

    @Override
    public void onAuthFinished(AuthenticationResult result) {
        mLoginView.setVisibility(View.VISIBLE);

        Intent resultIntent = new Intent();
        resultIntent.putExtra(AUTHENTICATION_RESULT_KEY, result);
        resultIntent.putExtra(CONFIGURATION_VERSION, getIntent().getIntExtra(CONFIGURATION_VERSION, 0));
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}
