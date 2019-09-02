package at.srfg.robogen.fitnesswatch.fitbit_API.fragments;

import android.os.Bundle;

import androidx.loader.content.Loader;

import java.util.List;

import at.srfg.robogen.R;
import at.srfg.robogen.fitnesswatch.fitbit_API.common.loaders.ResourceLoaderResult;
import at.srfg.robogen.fitnesswatch.fitbit_API.models.sleep_logs.Sleep;
import at.srfg.robogen.fitnesswatch.fitbit_API.models.sleep_logs.SleepLogs;
import at.srfg.robogen.fitnesswatch.fitbit_API.services.SleepService;

public class SleepFragment extends InfoFragment<SleepLogs> {

    /*******************************************************************************
     * overrides for InfoFragment
     ******************************************************************************/
    @Override
    public int getTitleResourceId() {
        return R.string.sleep_info;
    }

    @Override
    protected int getLoaderId() {
        return 6;
    }

    @Override
    public androidx.loader.content.Loader<ResourceLoaderResult<SleepLogs>> onCreateLoader(int id, Bundle args) {
        return SleepService.getSleepLogLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<ResourceLoaderResult<SleepLogs>> loader, ResourceLoaderResult<SleepLogs> data) {
        super.onLoadFinished(loader, data);
        if (data.isSuccessful()) {
            bindActivityData(data.getResult());
        }
    }

    /*******************************************************************************
     * bindActivityData
     ******************************************************************************/
    public void bindActivityData(SleepLogs sleepLogs) {

        clearList();

        // if no data, inform user
        List<Sleep> sleeps = sleepLogs.getSleeps();
        if (sleeps.size() == 0) {
            addTextToList(getString(R.string.no_data));
        }

        // run through data and create list entries
        for (Sleep sleep : sleeps) {

            String listEntry =
                    "Datum des Schlafeintrages = " + sleep.getDateOfSleep() + "\n" +
                    "Schlafdauer = " + sleep.getDuration() + "\n" +
                    "Schlaf-Effizienz = " + sleep.getEfficiency() + "\n" +
                    "LogID = " + sleep.getLogId() + "\n" +
                    "Minuten nach Aufwachen = " + sleep.getMinutesAfterWakeup() + "\n" +
                    "Minuten im Wachzustand = " + sleep.getMinutesAwake() + "\n" +
                    "Minuten um einzuschlafen = " + sleep.getMinutesToFallAsleep() + "\n" +
                    "Startzeit = " + sleep.getStartTime() + "\n" +
                    "Zeit im Bett = " + sleep.getTimeInBed() + "\n" +
                    "Typ = " + sleep.getType() + "\n";

            addTextToList(listEntry);
        }
    }
}
