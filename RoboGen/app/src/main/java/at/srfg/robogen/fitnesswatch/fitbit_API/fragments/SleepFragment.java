package at.srfg.robogen.fitnesswatch.fitbit_API.fragments;

import android.os.Bundle;
import android.util.Log;
import androidx.loader.content.Loader;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.srfg.robogen.R;
import at.srfg.robogen.fitnesswatch.fitbit_API.common.loaders.ResourceLoaderResult;
import at.srfg.robogen.fitnesswatch.fitbit_API.models.sleep_logs.Sleep;
import at.srfg.robogen.fitnesswatch.fitbit_API.models.sleep_logs.SleepLogs;
import at.srfg.robogen.fitnesswatch.fitbit_API.services.SleepService;

public class SleepFragment extends InfoFragment<SleepLogs> {

    private RequestQueue m_requestQueue;
    private String m_urlDownload = "https://power2dm.salzburgresearch.at/robogen/DataBase/DownloadJSON_MySettings";
    private String m_urlUpload = "https://power2dm.salzburgresearch.at/robogen/DataBase/UploadJSON_MySettings";

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

        m_requestQueue = Volley.newRequestQueue(this.getActivity().getBaseContext());

        if (data.isSuccessful()) {
            bindActivityData(data.getResult());
            readFitbitJSON(data.getResult());
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
    public void readFitbitJSON(final SleepLogs sleepLogs) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, m_urlDownload, (String) null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) { writeMinValue(sleepLogs, response.toString()); }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) { Log.e("SETTINGS Download Error: ", error.getMessage(), error); }
                })
        {
            @Override
            public String getBodyContentType(){ return "application/json"; }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                return params;
            }
        };
        m_requestQueue.add(jsonObjectRequest);
    }

    /*******************************************************************************
     * write fitbit file to asset folder
     ******************************************************************************/
    public void writeFitbitJSON(String json) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, m_urlUpload, json,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {} // read and update UI
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) { Log.e("SETTINGS Upload Error: ", error.getMessage(), error); }
                })
        {
            @Override
            public String getBodyContentType(){ return "application/json"; }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                return params;
            }
        };
        m_requestQueue.add(jsonObjectRequest);
    }

    /*******************************************************************************
     * writeMinValue
     ******************************************************************************/
    public void writeMinValue(SleepLogs sleepLogs, String jsonString) {

        List<Sleep> sleeps = sleepLogs.getSleeps();

        int min_value = 100;
        for (Sleep sleep : sleeps) {
            if (sleep.getEfficiency() < min_value)
                min_value = sleep.getEfficiency();
        }

        try {
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
            writeFitbitJSON(jsonObj.toString());
        }
        catch(JSONException ex)
        {
        }

    }

}
