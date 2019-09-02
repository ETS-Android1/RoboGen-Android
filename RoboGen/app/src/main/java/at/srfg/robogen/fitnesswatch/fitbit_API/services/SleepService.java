package at.srfg.robogen.fitnesswatch.fitbit_API.services;

import android.app.Activity;

import at.srfg.robogen.fitnesswatch.fitbit_API.common.exceptions.MissingScopesException;
import at.srfg.robogen.fitnesswatch.fitbit_API.common.exceptions.TokenExpiredException;
import at.srfg.robogen.fitnesswatch.fitbit_API.common.loaders.ResourceLoaderFactory;
import at.srfg.robogen.fitnesswatch.fitbit_API.common.loaders.ResourceLoaderResult;
import at.srfg.robogen.fitnesswatch.fitbit_API.models.sleep_logs.SleepLogs;
import at.srfg.robogen.fitnesswatch.fitbit_Auth.Scope;

public class SleepService {
    private final static String USER_URL = "https://api.fitbit.com/1.2/user/-/sleep/list.json?beforeDate=2019-09-03&sort=desc&offset=0&limit=1";
    private static final ResourceLoaderFactory<SleepLogs> SLEEP_LOADER_FACTORY = new ResourceLoaderFactory<>(USER_URL, SleepLogs.class);

    public static androidx.loader.content.Loader<ResourceLoaderResult<SleepLogs>> getSleepLogLoader(Activity activityContext) throws MissingScopesException, TokenExpiredException {
        return SLEEP_LOADER_FACTORY.newResourceLoader(activityContext, new Scope[]{Scope.profile});
    }
}
