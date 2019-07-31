package at.srfg.robogen.fitnesswatch.fitbit_API.fragments;

import android.os.Bundle;

import androidx.loader.content.Loader;

import at.srfg.robogen.R;
import at.srfg.robogen.fitnesswatch.fitbit_API.loaders.ResourceLoaderResult;
import at.srfg.robogen.fitnesswatch.fitbit_API.models.heartrate_intraday.ActivitiesHeart;
import at.srfg.robogen.fitnesswatch.fitbit_API.models.heartrate_intraday.HeartContainer;
import at.srfg.robogen.fitnesswatch.fitbit_API.models.heartrate_intraday.HeartRateZone;
import at.srfg.robogen.fitnesswatch.fitbit_API.services.HeartrateService;

public class HeartrateFragment extends InfoFragment<HeartContainer> {

    /*******************************************************************************
     * overrides for InfoFragment
     ******************************************************************************/
    @Override
    public int getTitleResourceId() {
        return R.string.heartrate_info;
    }

    @Override
    protected int getLoaderId() {
        return 5;
    }

    @Override
    public androidx.loader.content.Loader<ResourceLoaderResult<HeartContainer>> onCreateLoader(int id, Bundle args) {
        return HeartrateService.getHeartRateLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<ResourceLoaderResult<HeartContainer>> loader, ResourceLoaderResult<HeartContainer> data) {
        super.onLoadFinished(loader, data);
        if (data.isSuccessful()) {
            bindHeartInfo(data.getResult());
        }
    }

    /*******************************************************************************
     * bindDevices
     ******************************************************************************/
    public void bindHeartInfo(HeartContainer heartActivities) {

        StringBuilder stringBuilder = new StringBuilder();

        ActivitiesHeart[] activitiesHeart = heartActivities.getActivityHeart();
        for(ActivitiesHeart act: activitiesHeart)
        {
            final HeartRateZone[] heartRateZones = act.getValue().getHeartRateZones();
            for(HeartRateZone zone: heartRateZones)
            {
                stringBuilder.append("<b>CaloriesOut:</b> ");
                stringBuilder.append(zone.getCaloriesOut());
                stringBuilder.append("<br>");

                stringBuilder.append("<b>max:</b> ");
                stringBuilder.append(zone.getMax());
                stringBuilder.append("<br>");

                stringBuilder.append("<b>min:</b> ");
                stringBuilder.append(zone.getMin());
                stringBuilder.append("<br>");

                stringBuilder.append("<b>minutes:</b> ");
                stringBuilder.append(zone.getMinutes());
                stringBuilder.append("<br>");

                stringBuilder.append("<b>name</b> ");
                stringBuilder.append(zone.getName());
                stringBuilder.append("<br><hr>");
            }
        }

        // TODO: response currently does not include Intraday data -> permissions requested by FitBit Forum
        //ActivitiesHeartIntra activitiesHeartIntra = heartActivities.getActivityHeartIntra();


        if (stringBuilder.length() > 0) {
            stringBuilder.replace(stringBuilder.length() - 8, stringBuilder.length(), "");
        }
        else { // Keine Daten
            stringBuilder.append("<b>&nbsp;&nbsp;</b>");
            stringBuilder.append(getString(R.string.no_data));
            stringBuilder.append("<br><br>");
        }

        setMainText(stringBuilder.toString());
    }
}
