package at.srfg.robogen.fitnesswatch;

import at.srfg.robogen.itemactivity.ItemDetailActivity;
import at.srfg.robogen.fitnesswatch.fitbit_Auth.AuthenticationConfiguration;
import at.srfg.robogen.fitnesswatch.fitbit_Auth.AuthenticationConfigurationBuilder;
import at.srfg.robogen.fitnesswatch.fitbit_Auth.AuthenticationManager;
import at.srfg.robogen.fitnesswatch.fitbit_Auth.ClientCredentials;
import at.srfg.robogen.fitnesswatch.fitbit_Auth.Scope;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

/*******************************************************************************
 * Class FitbitAuthApplication
 * static application initialized with android manifest file
 ******************************************************************************/
public class FitbitAuthApplication extends Application {

    /**
     * These client credentials come from creating an app on https://dev.fitbit.com.
     * <p>
     * To use with your own app, register as a developer at https://dev.fitbit.com, create an application,
     * set the "OAuth 2.0 Application Type" to "Client", enter a word for the redirect url as a url
     * (like `https://finished` or `https://done` or `https://completed`, etc.), and save.
     * <p>
     */
    //!! THIS SHOULD BE IN AN ANDROID KEYSTORE!! See https://developer.android.com/training/articles/keystore.html
    private static final String CLIENT_SECRET = "fb1d4feca889b225724051a673f5876f";

    /**
     * This key was generated using the SecureKeyGenerator [java] class. Run as a Java application (not Android)
     * This key is used to encrypt the authentication token in Android user preferences. If someone decompiles
     * your application they'll have access to this key, and access to your user's authentication token
     */
    //!! THIS SHOULD BE IN AN ANDROID KEYSTORE!! See https://developer.android.com/training/articles/keystore.html
    private static final String SECURE_KEY = "CVPdQNAT6fBI4rrPLEn9x0+UV84DoqLFiNHpKOPLRW0=";

    /**
     * This method sets up the authentication config needed for
     * successfully connecting to the Fitbit API. Here we include our client credentials,
     * requested scopes, and  where to return after login
     */
    public static AuthenticationConfiguration generateAuthenticationConfiguration(Context context, Class<? extends Activity> mainActivityClass) {

        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;

            // Load clientId and redirectUrl from application manifest
            String clientId = bundle.getString("at.srfg.robogen.fitnesswatch.CLIENT_ID");
            String redirectUrl = bundle.getString("at.srfg.robogen.fitnesswatch.REDIRECT_URL");

            ClientCredentials CLIENT_CREDENTIALS = new ClientCredentials(clientId, CLIENT_SECRET, redirectUrl);
            return new AuthenticationConfigurationBuilder()

                    .setClientCredentials(CLIENT_CREDENTIALS)
                    .setEncryptionKey(SECURE_KEY)
                    .setTokenExpiresIn(2592000L) // 30 days
                    .setBeforeLoginActivity(new Intent(context, mainActivityClass))
                    .addRequiredScopes(Scope.profile, Scope.settings)
                    .addOptionalScopes(Scope.activity, Scope.weight, Scope.heartrate, Scope.sleep)
                    .setLogoutOnAuthFailure(true)
                    .build();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 1. When the application starts, load our keys and configure the AuthenticationManager
     */
    @Override
    public void onCreate() {
        super.onCreate();
        AuthenticationManager.configure(this, generateAuthenticationConfiguration(this, ItemDetailActivity.class));
    }
}
