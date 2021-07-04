package at.srfg.robogen.fitnesswatch.fitbit_API.services;

import android.app.Activity;

import at.srfg.robogen.fitnesswatch.fitbit_API.common.exceptions.MissingScopesException;
import at.srfg.robogen.fitnesswatch.fitbit_API.common.exceptions.TokenExpiredException;
import at.srfg.robogen.fitnesswatch.fitbit_API.common.loaders.ResourceLoaderFactory;
import at.srfg.robogen.fitnesswatch.fitbit_API.common.loaders.ResourceLoaderResult;
import at.srfg.robogen.fitnesswatch.fitbit_API.models.heartrate_intraday.HeartContainer;
import at.srfg.robogen.fitnesswatch.fitbit_Auth.Scope;

public class HeartrateService {

    private final static String HEART_URL = "https://api.fitbit.com/1/user/-/activities/heart/date/today/1d.json";
    //private final static String HEART_URL = "https://api.fitbit.com/1/user/-/activities/heart/date/today/today/1min.json";
    private static final ResourceLoaderFactory<HeartContainer> HEARTRATE_LOADER_FACTORY = new ResourceLoaderFactory<>(HEART_URL, HeartContainer.class);

    public static androidx.loader.content.Loader<ResourceLoaderResult<HeartContainer>> getHeartRateLoader(Activity activityContext) throws MissingScopesException, TokenExpiredException {
        return HEARTRATE_LOADER_FACTORY.newResourceLoader(activityContext, new Scope[]{Scope.heartrate});
    }
}
