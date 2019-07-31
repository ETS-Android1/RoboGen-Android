package at.srfg.robogen.fitnesswatch.fitbit_API.common.loaders;

import at.srfg.robogen.fitnesswatch.fitbit_Auth.AuthenticationManager;
import at.srfg.robogen.fitnesswatch.fitbit_Auth.Scope;
import at.srfg.robogen.fitnesswatch.fitbit_Http.BasicHttpRequest;
import at.srfg.robogen.fitnesswatch.fitbit_Http.BasicHttpResponse;

import android.app.Activity;
import androidx.loader.content.AsyncTaskLoader;
import android.os.Handler;

import com.google.gson.Gson;

import java.util.Locale;

/*******************************************************************************
 * ResourceLoader Templated Class
 ******************************************************************************/
public class ResourceLoader<T> extends AsyncTaskLoader<ResourceLoaderResult<T>> {

    private final static String EOL = System.getProperty("line.separator");

    private final String m_sURL;
    private final Class<T> m_tClassType;
    private final Activity m_cContextActivity;
    private final Handler m_hHandler;
    private final Scope[] m_arrRequiredScopes;

    public ResourceLoader(Activity context, String url, Scope[] requiredScopes, Handler handler, Class<T> classType) {
        super(context);
        this.m_cContextActivity = context;
        this.m_sURL = url;
        this.m_tClassType = classType;
        this.m_hHandler = handler;
        this.m_arrRequiredScopes = requiredScopes;
    }

    @Override
    public ResourceLoaderResult<T> loadInBackground() {
        try {
            APIUtils.validateToken(m_cContextActivity, AuthenticationManager.getCurrentAccessToken(), m_arrRequiredScopes);

            BasicHttpRequest request = AuthenticationManager
                    .createSignedRequest()
                    .setContentType("Application/json")
                    .setUrl(m_sURL)
                    .build();

            final BasicHttpResponse response = request.execute();
            int responseCode = response.getStatusCode();
            final String json = response.getBodyAsString();
            if (response.isSuccessful()) {
                final T resource = new Gson().fromJson(json, m_tClassType);
                return ResourceLoaderResult.onSuccess(resource);
            } else {
                if (responseCode == 401) {
                    if (AuthenticationManager.getAuthenticationConfiguration().isLogoutOnAuthFailure()) {
                        // Token may have been revoked or expired
                        m_hHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                AuthenticationManager.logout(m_cContextActivity);
                            }
                        });
                    }
                    return ResourceLoaderResult.onLoggedOut();
                } else {
                    String errorMessage = String.format(
                            Locale.GERMAN,
                            "Error making request:%s\tHTTP Code:%d%s\tResponse Body:%s%s%s\n",
                            EOL, responseCode, EOL, EOL, json, EOL);
                    return ResourceLoaderResult.onError(errorMessage);
                }
            }
        } catch (Exception e) {
            return ResourceLoaderResult.onException(e);
        }
    }
}
