package at.srfg.robogen.itemdetail;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

import at.srfg.robogen.R;

public class ItemDetailSettings extends ItemDetailBase {

    private final String m_sShowSettings = "Hier können Sie ihre persönlichen Daten angeben und ihren QBO " +
                                           "individuell konfigurieren. Füllen Sie alle Felder aus und " +
                                           "drücken Sie dann auf die Speicher-Schaltfläche um die eingetragenen " +
                                           "Informationen auf dem Tablet zu speichern!";

    public FloatingActionButton m_btnStartSettings;

    public View m_RootView = null;

    private RequestQueue m_requestQueue;
    private String m_urlDownload = "https://power2dm.salzburgresearch.at/robogen/DataBase/DownloadJSON_MySettings";
    private String m_urlUpload = "https://power2dm.salzburgresearch.at/robogen/DataBase/UploadJSON_MySettings";

    /*******************************************************************************
     * creating view for settings detail page
     ******************************************************************************/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        m_RootView = inflater.inflate(R.layout.main_itemdetail_settings, container, false);
        m_requestQueue = Volley.newRequestQueue(this.getActivity().getBaseContext());

        // Show the dummy content as text in a TextView.
        if (mItem != null)
        {
            initGUIComponents();
        }

        return m_RootView;
    }

    /*******************************************************************************
     * init GUI components
     ******************************************************************************/
    private void initGUIComponents(){

        ((TextView) m_RootView.findViewById(R.id.item_detail_title)).setText(mItem.m_sEntryHeader);
        ((TextView) m_RootView.findViewById(R.id.item_detail_text_1)).setText(m_sShowSettings);

        addSeekBarListeners();
        readSettingsJSON();

        m_btnStartSettings = (FloatingActionButton) m_RootView.findViewById(R.id.bt_sendUserData);
        m_btnStartSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                makeSnackbarMessage(view, "Speichere Daten auf Ihrem Gerät...");
                readSettingsJSON();
            }
        });
    }

    /*******************************************************************************
     * init GUI components
     ******************************************************************************/
    private void addSeekBarListeners(){

        // AudioVolume
        ((SeekBar) m_RootView.findViewById(R.id.robotAudioVolume)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                ((TextView)m_RootView.findViewById(R.id.robotAudioVolumeText)).setText(Integer.toString(progress));
            }
        });

        // StressThreshold
        ((SeekBar) m_RootView.findViewById(R.id.robotThresholdStress)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                ((TextView)m_RootView.findViewById(R.id.robotThresholdStressText)).setText(Integer.toString(progress));
            }
        });

        // SleepThreshold
        ((SeekBar) m_RootView.findViewById(R.id.robotThresholdSleep)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                ((TextView)m_RootView.findViewById(R.id.robotThresholdSleepText)).setText(Integer.toString(progress));
            }
        });
    }

    /*******************************************************************************
     * read settings file from asset folder
     ******************************************************************************/
    public void readSettingsJSON() {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, m_urlDownload, (String) null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) { assignSettingsToFields(response.toString()); }
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
     * write settings file to asset folder
     ******************************************************************************/
    public void writeSettingsJSON(String json) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, m_urlUpload, json,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) { /*assignFieldsToSettings(response.toString());*/ } // read and update UI
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
     * assign loaded settings to fields
     ******************************************************************************/
    public void assignSettingsToFields(String jsonString) {

        JSONObject robotSettings = null;
        JSONObject userSettings = null;
        JSONObject userAddress = null;
        JSONObject emergencyAddress = null;
        JSONObject userPersonalData = null;

        try {
            if(jsonString == null || jsonString.length() == 0) return;

            JSONObject obj = new JSONObject(jsonString);
            robotSettings = obj.getJSONObject("robotSettings");
            userSettings = obj.getJSONObject("userSettings");

            userAddress = userSettings.getJSONObject("userAddress");
            emergencyAddress = userSettings.getJSONObject("emergencyAddress");
            userPersonalData = userSettings.getJSONObject("userPersonalData");
        }
        catch(JSONException ex)
        {
            makeSnackbarMessage(m_RootView, "Die gespeicherten Informationen konnten nicht gelesen werden");
        }

        // assign json entries to robot fields
        ((TextView) m_RootView.findViewById(R.id.robotName)).setText(robotSettings.optString("robotName"));
        ((Spinner) m_RootView.findViewById(R.id.robotVoice)).setSelection(robotSettings.optInt("robotVoice"));
        ((SeekBar) m_RootView.findViewById(R.id.robotAudioVolume)).setProgress(robotSettings.optInt("robotAudioVolume"));
        ((Spinner) m_RootView.findViewById(R.id.robotInfoDisplayLocation)).setSelection(robotSettings.optInt("robotInfoDisplayLocation"));
        ((TextView) m_RootView.findViewById(R.id.robotInfoDisplayFontSize)).setText(robotSettings.optString("robotInfoDisplayFontSize"));
        ((Spinner) m_RootView.findViewById(R.id.robotNotificationStyle)).setSelection(robotSettings.optInt("robotNotificationStyle"));
        ((SeekBar) m_RootView.findViewById(R.id.robotThresholdStress)).setProgress(robotSettings.optInt("robotThresholdStress"));
        ((SeekBar) m_RootView.findViewById(R.id.robotThresholdSleep)).setProgress(robotSettings.optInt("robotThresholdSleep"));
        ((CheckBox) m_RootView.findViewById(R.id.doRecognizeFeelingsFromAudio)).setChecked(robotSettings.optBoolean("doRecognizeFeelingsFromAudio"));
        ((CheckBox) m_RootView.findViewById(R.id.doRecognizeFeelingsFromVideo)).setChecked(robotSettings.optBoolean("doRecognizeFeelingsFromVideo"));
        ((CheckBox) m_RootView.findViewById(R.id.isAlive)).setChecked(robotSettings.optBoolean("isAlive"));
        ((Spinner) m_RootView.findViewById(R.id.robotFrequencyOfTips)).setSelection(robotSettings.optInt("robotFrequencyOfTips"));

        // assign json entries to user fields
        ((TextView) m_RootView.findViewById(R.id.userName)).setText(userSettings.optString("userName"));
        ((TextView) m_RootView.findViewById(R.id.userAge)).setText(userSettings.optString("userAge"));
        ((Spinner) m_RootView.findViewById(R.id.userGender)).setSelection(userSettings.optInt("userGender"));
        ((Spinner) m_RootView.findViewById(R.id.userFamilyStatus)).setSelection(userSettings.optInt("userFamilyStatus"));
        ((Spinner) m_RootView.findViewById(R.id.userEducationStatus)).setSelection(userSettings.optInt("userEducationStatus"));
        ((TextView) m_RootView.findViewById(R.id.userEmailAccount)).setText(userSettings.optString("userEmailAccount"));
        ((TextView) m_RootView.findViewById(R.id.userPhoneNumber)).setText(userSettings.optString("userPhoneNumber"));
        ((TextView) m_RootView.findViewById(R.id.streetAddress)).setText(userAddress.optString("streetAddress"));
        ((TextView) m_RootView.findViewById(R.id.city)).setText(userAddress.optString("city"));
        ((TextView) m_RootView.findViewById(R.id.state)).setText(userAddress.optString("state"));
        ((TextView) m_RootView.findViewById(R.id.postalCode)).setText(userAddress.optString("postalCode"));

        // assign json entries to emergency fields
        ((TextView) m_RootView.findViewById(R.id.emergencyEmailAccount)).setText(emergencyAddress.optString("emergencyEmailAccount"));

        // assign json entries to personal user fields
        ((Spinner) m_RootView.findViewById(R.id.userHousingSituation)).setSelection(userPersonalData.optInt("userHousingSituation"));
        ((Spinner) m_RootView.findViewById(R.id.userEmploymentSituation)).setSelection(userPersonalData.optInt("userEmploymentSituation"));
        ((Spinner) m_RootView.findViewById(R.id.userReligionState)).setSelection(userPersonalData.optInt("userReligionState"));
        ((Spinner) m_RootView.findViewById(R.id.userResidenceDuringDay)).setSelection(userPersonalData.optInt("userResidenceDuringDay"));
        ((Spinner) m_RootView.findViewById(R.id.userAverageIncome)).setSelection(userPersonalData.optInt("userAverageIncome"));
        ((Spinner) m_RootView.findViewById(R.id.userMigrationBackground)).setSelection(userPersonalData.optInt("userMigrationBackground"));
        ((Spinner) m_RootView.findViewById(R.id.userRegionalityScale)).setSelection(userPersonalData.optInt("userRegionalityScale"));
        ((Spinner) m_RootView.findViewById(R.id.userKnownDiseases)).setSelection(userPersonalData.optInt("userKnownDiseases"));
        ((Spinner) m_RootView.findViewById(R.id.userWalkingAid)).setSelection(userPersonalData.optInt("userWalkingAid"));
        ((Spinner) m_RootView.findViewById(R.id.userCareSituation)).setSelection(userPersonalData.optInt("userCareSituation"));
        ((Spinner) m_RootView.findViewById(R.id.userMedicine)).setSelection(userPersonalData.optInt("userMedicine"));
        ((Spinner) m_RootView.findViewById(R.id.userDrinking)).setSelection(userPersonalData.optInt("userDrinking"));
        ((TextView) m_RootView.findViewById(R.id.userInterests)).setText(userPersonalData.optString("userInterests"));
        ((TextView) m_RootView.findViewById(R.id.userHobbies)).setText(userPersonalData.optString("userHobbies"));
    }

    /*******************************************************************************
     * assign fields to settings
     ******************************************************************************/
    public void assignFieldsToSettings(String jsonString) {

        JSONObject jsonObj = new JSONObject();
        JSONObject robotSettings = new JSONObject();
        JSONObject userSettings = new JSONObject();
        JSONObject userAddress = new JSONObject();
        JSONObject emergencyAddress = new JSONObject();
        JSONObject userPersonalData = new JSONObject();

        try {
            if(jsonString == null || jsonString.length() == 0) {
                jsonString = "{}";
            }

            jsonObj = new JSONObject(jsonString);

            // assign json entries to robot fields
            robotSettings.put("robotName",((TextView) m_RootView.findViewById(R.id.robotName)).getText());
            robotSettings.put("robotVoice", ((Spinner) m_RootView.findViewById(R.id.robotVoice)).getSelectedItemPosition());
            robotSettings.put("robotAudioVolume", ((SeekBar) m_RootView.findViewById(R.id.robotAudioVolume)).getProgress());
            robotSettings.put("robotInfoDisplayLocation", ((Spinner) m_RootView.findViewById(R.id.robotInfoDisplayLocation)).getSelectedItemPosition());
            robotSettings.put("robotInfoDisplayFontSize", ((TextView) m_RootView.findViewById(R.id.robotInfoDisplayFontSize)).getText());
            robotSettings.put("robotNotificationStyle", ((Spinner) m_RootView.findViewById(R.id.robotNotificationStyle)).getSelectedItemPosition());
            robotSettings.put("robotThresholdStress", ((SeekBar) m_RootView.findViewById(R.id.robotThresholdStress)).getProgress());
            robotSettings.put("robotThresholdSleep", ((SeekBar) m_RootView.findViewById(R.id.robotThresholdSleep)).getProgress());
            robotSettings.put("doRecognizeFeelingsFromAudio", ((CheckBox) m_RootView.findViewById(R.id.doRecognizeFeelingsFromAudio)).isChecked());
            robotSettings.put("doRecognizeFeelingsFromVideo", ((CheckBox) m_RootView.findViewById(R.id.doRecognizeFeelingsFromVideo)).isChecked());
            robotSettings.put("isAlive", ((CheckBox) m_RootView.findViewById(R.id.isAlive)).isChecked());
            robotSettings.put("robotFrequencyOfTips", ((Spinner) m_RootView.findViewById(R.id.robotFrequencyOfTips)).getSelectedItemPosition());

            // assign json entries to user fields
            userSettings.put("userName", ((TextView) m_RootView.findViewById(R.id.userName)).getText());
            userSettings.put("userAge", ((TextView) m_RootView.findViewById(R.id.userAge)).getText());
            userSettings.put("userGender", ((Spinner) m_RootView.findViewById(R.id.userGender)).getSelectedItemPosition());
            userSettings.put("userFamilyStatus", ((Spinner) m_RootView.findViewById(R.id.userFamilyStatus)).getSelectedItemPosition());
            userSettings.put("userEducationStatus", ((Spinner) m_RootView.findViewById(R.id.userEducationStatus)).getSelectedItemPosition());
            userSettings.put("userEmailAccount", ((TextView) m_RootView.findViewById(R.id.userEmailAccount)).getText());
            userSettings.put("userPhoneNumber", ((TextView) m_RootView.findViewById(R.id.userPhoneNumber)).getText());
            userAddress.put("streetAddress", ((TextView) m_RootView.findViewById(R.id.streetAddress)).getText());
            userAddress.put("city", ((TextView) m_RootView.findViewById(R.id.city)).getText());
            userAddress.put("state", ((TextView) m_RootView.findViewById(R.id.state)).getText());
            userAddress.put("postalCode", ((TextView) m_RootView.findViewById(R.id.postalCode)).getText());

            // assign json entries to emergency field
            emergencyAddress.put("emergencyEmailAccount", ((TextView) m_RootView.findViewById(R.id.emergencyEmailAccount)).getText());

            // assign json entries to personal user fields
            userPersonalData.put("userHousingSituation", ((Spinner) m_RootView.findViewById(R.id.userHousingSituation)).getSelectedItemPosition());
            userPersonalData.put("userEmploymentSituation", ((Spinner) m_RootView.findViewById(R.id.userEmploymentSituation)).getSelectedItemPosition());
            userPersonalData.put("userReligionState", ((Spinner) m_RootView.findViewById(R.id.userReligionState)).getSelectedItemPosition());
            userPersonalData.put("userResidenceDuringDay", ((Spinner) m_RootView.findViewById(R.id.userResidenceDuringDay)).getSelectedItemPosition());
            userPersonalData.put("userAverageIncome", ((Spinner) m_RootView.findViewById(R.id.userAverageIncome)).getSelectedItemPosition());
            userPersonalData.put("userMigrationBackground", ((Spinner) m_RootView.findViewById(R.id.userMigrationBackground)).getSelectedItemPosition());
            userPersonalData.put("userRegionalityScale", ((Spinner) m_RootView.findViewById(R.id.userRegionalityScale)).getSelectedItemPosition());
            userPersonalData.put("userKnownDiseases", ((Spinner) m_RootView.findViewById(R.id.userKnownDiseases)).getSelectedItemPosition());
            userPersonalData.put("userWalkingAid", ((Spinner) m_RootView.findViewById(R.id.userWalkingAid)).getSelectedItemPosition());
            userPersonalData.put("userCareSituation", ((Spinner) m_RootView.findViewById(R.id.userCareSituation)).getSelectedItemPosition());
            userPersonalData.put("userMedicine", ((Spinner) m_RootView.findViewById(R.id.userMedicine)).getSelectedItemPosition());
            userPersonalData.put("userDrinking", ((Spinner) m_RootView.findViewById(R.id.userDrinking)).getSelectedItemPosition());
            userPersonalData.put("userInterests", ((TextView) m_RootView.findViewById(R.id.userInterests)).getText());
            userPersonalData.put("userHobbies", ((TextView) m_RootView.findViewById(R.id.userHobbies)).getText());

            // finally puttin everything together
            userSettings.put("userAddress", userAddress);
            userSettings.put("emergencyAddress", emergencyAddress);
            userSettings.put("userPersonalData", userPersonalData);
            jsonObj.put("robotSettings", robotSettings);
            jsonObj.put("userSettings", userSettings);

            // write file
            writeSettingsJSON(jsonObj.toString());
            makeSnackbarMessage(m_RootView, "Daten wurden erfolgreich gespeichert");
        }
        catch(JSONException ex)
        {
            makeSnackbarMessage(m_RootView, "Die gespeicherten Informationen konnten nicht gelesen werden");
        }
    }
}
