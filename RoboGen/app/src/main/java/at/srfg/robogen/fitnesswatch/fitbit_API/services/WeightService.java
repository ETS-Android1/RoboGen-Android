package at.srfg.robogen.fitnesswatch.fitbit_API.services;

import at.srfg.robogen.fitnesswatch.fitbit_API.common.exceptions.MissingScopesException;
import at.srfg.robogen.fitnesswatch.fitbit_API.common.exceptions.TokenExpiredException;
import at.srfg.robogen.fitnesswatch.fitbit_API.common.loaders.ResourceLoaderFactory;
import at.srfg.robogen.fitnesswatch.fitbit_API.common.loaders.ResourceLoaderResult;
import at.srfg.robogen.fitnesswatch.fitbit_API.models.weight_logs.WeightLogs;
import at.srfg.robogen.fitnesswatch.fitbit_Auth.Scope;

import android.app.Activity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/*******************************************************************************
 * WeightService Class
 ******************************************************************************/
public class WeightService {

    //private final static String WEIGHT_URL = "https://api.fitbit.com/1/user/-/body/log/weight/date/%s/%s.json";
    private final static String WEIGHT_URL = "https://api.fitbit.com/1/user/-/body/log/weight/date/today.json";
    private static final ResourceLoaderFactory<WeightLogs> WEIGHT_LOG_LOADER_FACTORY = new ResourceLoaderFactory<>(WEIGHT_URL, WeightLogs.class);
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    public static androidx.loader.content.Loader<ResourceLoaderResult<WeightLogs>> getWeightLogLoader(Activity activityContext, Date startDate, int calendarDateType, int number) throws MissingScopesException, TokenExpiredException {
        //String periodSuffix = "d";
        //switch (calendarDateType) {
        //    case Calendar.WEEK_OF_YEAR:
        //        periodSuffix = "w";
        //        break;
        //    case Calendar.MONTH:
        //        periodSuffix = "m";
        //        break;
        //}
//
        //return WEIGHT_LOG_LOADER_FACTORY.newResourceLoader(
        //        activityContext,
        //        new Scope[]{Scope.weight},
        //        dateFormat.format(startDate),
        //        String.format(Locale.US, "%d%s", number, periodSuffix));

        return WEIGHT_LOG_LOADER_FACTORY.newResourceLoader(activityContext, new Scope[]{Scope.weight});
    }
}
