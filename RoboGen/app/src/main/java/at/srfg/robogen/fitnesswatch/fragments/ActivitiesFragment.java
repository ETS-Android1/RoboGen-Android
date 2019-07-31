package at.srfg.robogen.fitnesswatch.fragments;

import at.srfg.robogen.R;
import at.srfg.robogen.fitnesswatch.fitbit_API.loaders.ResourceLoaderResult;
import at.srfg.robogen.fitnesswatch.fitbit_API.models.activity_lifetime.DailyActivitySummary;
import at.srfg.robogen.fitnesswatch.fitbit_API.models.activity_lifetime.Goals;
import at.srfg.robogen.fitnesswatch.fitbit_API.models.activity_lifetime.Summary;
import at.srfg.robogen.fitnesswatch.fitbit_API.services.ActivityService;

import androidx.loader.content.Loader;
import android.os.Bundle;

import java.util.Date;

/*******************************************************************************
 * ActivitiesFragment class
 * concrete implementation/extension of InfoFragment class
 ******************************************************************************/
public class ActivitiesFragment extends InfoFragment<DailyActivitySummary> {

    /*******************************************************************************
     * overrides for InfoFragment
     ******************************************************************************/
    @Override
    public int getTitleResourceId() {
        return R.string.activity_info;
    }

    @Override
    protected int getLoaderId() {
        return 3;
    }

    @Override
    public androidx.loader.content.Loader<ResourceLoaderResult<DailyActivitySummary>> onCreateLoader(int id, Bundle args) {
        return ActivityService.getDailyActivitySummaryLoader(getActivity(), new Date());
    }

    @Override
    public void onLoadFinished(Loader<ResourceLoaderResult<DailyActivitySummary>> loader, ResourceLoaderResult<DailyActivitySummary> data) {
        super.onLoadFinished(loader, data);
        if (data.isSuccessful()) {
            bindActivityData(data.getResult());
        }
    }

    /*******************************************************************************
     * bindActivityData
     ******************************************************************************/
    public void bindActivityData(DailyActivitySummary dailyActivitySummary) {
        StringBuilder stringBuilder = new StringBuilder();

        Summary summary = dailyActivitySummary.getSummary();
        Goals goals = dailyActivitySummary.getGoals();

        stringBuilder.append("<b>TODAY</b> ");
        stringBuilder.append("<br />");
        printKeys(stringBuilder, summary);

        stringBuilder.append("<br /><br /><hr>");
        stringBuilder.append("<b>GOALS</b> ");
        stringBuilder.append("<br />");
        printKeys(stringBuilder, goals);

        setMainText(stringBuilder.toString());
    }
}
