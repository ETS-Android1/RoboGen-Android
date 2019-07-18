package at.srfg.robogen.fitnesswatch.fitbit_API.services;

import at.srfg.robogen.fitnesswatch.fitbit_API.exceptions.MissingScopesException;
import at.srfg.robogen.fitnesswatch.fitbit_API.exceptions.TokenExpiredException;
import at.srfg.robogen.fitnesswatch.fitbit_API.loaders.ResourceLoaderFactory;
import at.srfg.robogen.fitnesswatch.fitbit_API.loaders.ResourceLoaderResult;
import at.srfg.robogen.fitnesswatch.fitbit_API.models.UserContainer;
import at.srfg.robogen.fitnesswatch.fitbit_Auth.Scope;

import android.app.Activity;
import android.content.Loader;

/**
 * Created by jboggess on 9/14/16.
 */
public class UserService {

    private final static String USER_URL = "https://api.fitbit.com/1/user/-/profile.json";
    private static final ResourceLoaderFactory<UserContainer> USER_PROFILE_LOADER_FACTORY = new ResourceLoaderFactory<>(USER_URL, UserContainer.class);

    public static Loader<ResourceLoaderResult<UserContainer>> getLoggedInUserLoader(Activity activityContext) throws MissingScopesException, TokenExpiredException {
        return USER_PROFILE_LOADER_FACTORY.newResourceLoader(activityContext, new Scope[]{Scope.profile});
    }

}
