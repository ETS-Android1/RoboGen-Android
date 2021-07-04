package at.srfg.robogen.fitnesswatch.fitbit_API.fragments;

import android.os.Bundle;

import androidx.loader.content.Loader;

import at.srfg.robogen.R;
import at.srfg.robogen.fitnesswatch.fitbit_API.common.loaders.ResourceLoaderResult;
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

        clearList();

        // if no data, inform user
        ActivitiesHeart[] activitiesHeart = heartActivities.getActivityHeart();
        if (activitiesHeart.length == 0) {
            addTextToList(getString(R.string.no_data));
        }

        // run through data and create list entries
        for(ActivitiesHeart act: activitiesHeart)
        {
            final HeartRateZone[] heartRateZones = act.getValue().getHeartRateZones();
            for(HeartRateZone zone: heartRateZones)
            {
                String listEntry =
                        "Kalorien (Out) = " + zone.getCaloriesOut() + "\n" +
                        "Maxima = " + zone.getMax()+ "\n" +
                        "Minima = " + zone.getMin() + "\n" +
                        "Minuten = " + zone.getMinutes() + "\n" +
                        "Name/Art/Typ = " + zone.getName() + "\n";

                addTextToList(listEntry);
            }
        }

        // TODO: response currently does not include Intraday data -> permissions requested by FitBit Forum
        //ActivitiesHeartIntra activitiesHeartIntra = heartActivities.getActivityHeartIntra();
    }
}
