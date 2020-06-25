package at.srfg.robogen.fitnesswatch.fitbit_API.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.loader.content.Loader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
            writeMinValue(data.getResult());
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

    /*******************************************************************************
     * read fitbit file from asset folder
     ******************************************************************************/
    public String readFitbitJSON(Context context) {

        try {
            InputStream is = context.openFileInput("settings.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            return new String(buffer, "UTF-8");
        }
        catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /*******************************************************************************
     * write fitbit file to asset folder
     ******************************************************************************/
    public void writeFitbitJSON(Context context, String json) {

        try {
            OutputStream os = context.openFileOutput("settings.json", Context.MODE_PRIVATE);
            os.write(json.getBytes("UTF-8"));
            os.close();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /*******************************************************************************
     * writeMinValue
     ******************************************************************************/
    public void writeMinValue(SleepLogs sleepLogs) {

        List<Sleep> sleeps = sleepLogs.getSleeps();

        int min_value = 100;
        for (Sleep sleep : sleeps) {
            if (sleep.getEfficiency() < min_value)
                min_value = sleep.getEfficiency();
        }

        try {

            String jsonString = readFitbitJSON(this.getActivity().getBaseContext());
            if(jsonString == null || jsonString.length() == 0) {
                jsonString = "{'fitbitSettings':{}}";
            }

            JSONObject jsonObj = new JSONObject(jsonString);
            JSONObject fitbitSettings = null;
            if (jsonObj.has("fitbitSettings"))
                fitbitSettings = jsonObj.getJSONObject("fitbitSettings");
            else
                fitbitSettings = new JSONObject();
            fitbitSettings.put("sleepMinValue",min_value);
            jsonObj.put("fitbitSettings",fitbitSettings);
            writeFitbitJSON(this.getActivity().getBaseContext(), jsonObj.toString());
        }
        catch(JSONException ex)
        {
        }

    }

}
