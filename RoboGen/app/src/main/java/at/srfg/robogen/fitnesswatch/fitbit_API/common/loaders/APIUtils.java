package at.srfg.robogen.fitnesswatch.fitbit_API.common.loaders;

import at.srfg.robogen.fitnesswatch.fitbit_API.common.exceptions.MissingScopesException;
import at.srfg.robogen.fitnesswatch.fitbit_API.common.exceptions.TokenExpiredException;
import at.srfg.robogen.fitnesswatch.fitbit_Auth.AccessToken;
import at.srfg.robogen.fitnesswatch.fitbit_Auth.AuthenticationManager;
import at.srfg.robogen.fitnesswatch.fitbit_Auth.Scope;

import android.app.Activity;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/*******************************************************************************
 * Class APIUtils
 ******************************************************************************/
public class APIUtils {

    public static void validateToken(Activity contextActivity, AccessToken accessToken, Scope... scopes) throws MissingScopesException, TokenExpiredException {
        Set<Scope> requiredScopes = new HashSet<>(Arrays.asList(scopes));

        requiredScopes.removeAll(accessToken.getScopes());

        if (!requiredScopes.isEmpty()) {
            throw new MissingScopesException(requiredScopes);
        }

        if (accessToken.hasExpired()) {
            // Check to see if we should logout
            if (AuthenticationManager.getAuthenticationConfiguration().isLogoutOnAuthFailure()) {
                AuthenticationManager.logout(contextActivity);
            } else {
                throw new TokenExpiredException();
            }
        }
    }
}
