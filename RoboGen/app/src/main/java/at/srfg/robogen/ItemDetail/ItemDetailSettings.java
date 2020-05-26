package at.srfg.robogen.itemdetail;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import at.srfg.robogen.R;
import at.srfg.robogen.RoboGen_App;

public class ItemDetailSettings extends ItemDetailBase {

    private final String m_sShowSettings = "Hier können Sie ihre persönlichen Daten angeben und ihren QBO " +
                                           "individuell konfigurieren. Füllen Sie alle Felder aus und " +
                                           "drücken Sie dann auf die Speicher-Schaltfläche um die eingetragenen " +
                                           "Informationen auf dem Tablet zu speichern!";
    private RoboGen_App m_cRoboGenApp;

    public FloatingActionButton m_btnStartSettings;

    /*******************************************************************************
     * creating view for settings detail page
     ******************************************************************************/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.main_itemdetail_settings, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null)
        {
            m_cRoboGenApp = ((RoboGen_App)getActivity().getApplication());
            initGUIComponents(rootView);
        }

        return rootView;
    }

    /*******************************************************************************
     * init GUI components
     ******************************************************************************/
    private void initGUIComponents(final View rootView){

        ((TextView) rootView.findViewById(R.id.item_detail_title)).setText(mItem.m_sEntryHeader);
        ((TextView) rootView.findViewById(R.id.item_detail_text_1)).setText(m_sShowSettings);

        assignSettingsToFields(rootView);

        m_btnStartSettings = (FloatingActionButton) rootView.findViewById(R.id.bt_sendUserData);
        m_btnStartSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                makeSnackbarMessage(view, "Speichere Daten auf Ihrem Gerät...");
                assignFieldsToSettings(rootView);
            }
        });
    }

    /*******************************************************************************
     * read settings file from asset folder
     ******************************************************************************/
    public String readSettingsJSON(Context context) {

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
     * write settings file to asset folder
     ******************************************************************************/
    public void writeSettingsJSON(Context context, String json) {

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
     * assign loaded settings to fields
     ******************************************************************************/
    public void assignSettingsToFields(final View rootView) {

        JSONObject robotSettings = null;
        JSONObject userSettings = null;
        JSONObject userAddress = null;
        JSONArray userPhoneNumbers = null;
        JSONObject userPersonalData = null;
        JSONArray userKnownDiseases = null;

        try {
            String jsonString = readSettingsJSON(this.getActivity().getBaseContext());
            if(jsonString == null || jsonString.length() == 0) return;

            JSONObject obj = new JSONObject(jsonString);
            robotSettings = obj.getJSONObject("robotSettings");
            userSettings = obj.getJSONObject("userSettings");

            userAddress = userSettings.getJSONObject("userAddress");
            userPhoneNumbers = userSettings.getJSONArray("userPhoneNumbers");
            userPersonalData = userSettings.getJSONObject("userPersonalData");
            userKnownDiseases = userPersonalData.getJSONArray("userKnownDiseases");
        }
        catch(JSONException ex)
        {
            makeSnackbarMessage(rootView, "Die gespeicherten Informationen konnten nicht gelesen werden");
        }

        // assign json entries to robot fields
        ((TextView) rootView.findViewById(R.id.robotName)).setText(robotSettings.optString("robotName"));
        ((TextView) rootView.findViewById(R.id.robotVoice)).setText(robotSettings.optString("robotVoice"));
        ((TextView) rootView.findViewById(R.id.robotAudioVolume)).setText(robotSettings.optString("robotAudioVolume"));
        ((TextView) rootView.findViewById(R.id.robotInfoDisplayLocation)).setText(robotSettings.optString("robotInfoDisplayLocation"));
        ((TextView) rootView.findViewById(R.id.robotInfoDisplayFontSize)).setText(robotSettings.optString("robotInfoDisplayFontSize"));
        ((TextView) rootView.findViewById(R.id.robotThresholdStress)).setText(robotSettings.optString("robotThresholdStress"));
        ((TextView) rootView.findViewById(R.id.robotThresholdSleep)).setText(robotSettings.optString("robotThresholdSleep"));
        ((CheckBox) rootView.findViewById(R.id.doRecognizeFeelingsFromAudio)).setChecked(robotSettings.optBoolean("doRecognizeFeelingsFromAudio"));
        ((CheckBox) rootView.findViewById(R.id.doRecognizeFeelingsFromVideo)).setChecked(robotSettings.optBoolean("doRecognizeFeelingsFromVideo"));
        ((CheckBox) rootView.findViewById(R.id.isAlive)).setChecked(robotSettings.optBoolean("isAlive"));

        // assign json entries to user fields
        ((TextView) rootView.findViewById(R.id.userName)).setText(userSettings.optString("userName"));
        ((TextView) rootView.findViewById(R.id.userAge)).setText(userSettings.optString("userAge"));
        ((TextView) rootView.findViewById(R.id.userGender)).setText(userSettings.optString("userGender"));
        ((TextView) rootView.findViewById(R.id.userFamilyStatus)).setText(userSettings.optString("userFamilyStatus"));
        ((TextView) rootView.findViewById(R.id.userEducationStatus)).setText(userSettings.optString("userEducationStatus"));
        ((TextView) rootView.findViewById(R.id.userEmailAccount)).setText(userSettings.optString("userEmailAccount"));
        ((TextView) rootView.findViewById(R.id.streetAddress)).setText(userAddress.optString("streetAddress"));
        ((TextView) rootView.findViewById(R.id.city)).setText(userAddress.optString("city"));
        ((TextView) rootView.findViewById(R.id.state)).setText(userAddress.optString("state"));
        ((TextView) rootView.findViewById(R.id.postalCode)).setText(userAddress.optString("postalCode"));

        // TODO: phone numbers

        // assign json entries to personal user fields
        ((TextView) rootView.findViewById(R.id.userHousingSituation)).setText(userPersonalData.optString("userHousingSituation"));
        ((TextView) rootView.findViewById(R.id.userEmploymentSituation)).setText(userPersonalData.optString("userEmploymentSituation"));
        ((TextView) rootView.findViewById(R.id.userReligionState)).setText(userPersonalData.optString("userReligionState"));
        ((TextView) rootView.findViewById(R.id.userResidenceDuringDay)).setText(userPersonalData.optString("userResidenceDuringDay"));
        ((TextView) rootView.findViewById(R.id.userAverageIncome)).setText(userPersonalData.optString("userAverageIncome"));
        ((TextView) rootView.findViewById(R.id.userMigrationBackground)).setText(userPersonalData.optString("userMigrationBackground"));
        ((TextView) rootView.findViewById(R.id.userRegionalityScale)).setText(userPersonalData.optString("userRegionalityScale"));

        // TODO: user diseases
    }

    /*******************************************************************************
     * assign fields to settings
     ******************************************************************************/
    public void assignFieldsToSettings(final View rootView) {

        JSONObject jsonObj = new JSONObject();
        JSONObject robotSettings = new JSONObject();
        JSONObject userSettings = new JSONObject();
        JSONObject userAddress = new JSONObject();
        JSONArray userPhoneNumbers = new JSONArray();
        JSONObject userPersonalData = new JSONObject();
        JSONArray userKnownDiseases = new JSONArray();

        try {
            // assign json entries to robot fields
            robotSettings.put("robotName",((TextView) rootView.findViewById(R.id.robotName)).getText());
            robotSettings.put("robotVoice", ((TextView) rootView.findViewById(R.id.robotVoice)).getText());
            robotSettings.put("robotAudioVolume", ((TextView) rootView.findViewById(R.id.robotAudioVolume)).getText());
            robotSettings.put("robotInfoDisplayLocation", ((TextView) rootView.findViewById(R.id.robotInfoDisplayLocation)).getText());
            robotSettings.put("robotInfoDisplayFontSize", ((TextView) rootView.findViewById(R.id.robotInfoDisplayFontSize)).getText());
            robotSettings.put("robotThresholdStress", ((TextView) rootView.findViewById(R.id.robotThresholdStress)).getText());
            robotSettings.put("robotThresholdSleep", ((TextView) rootView.findViewById(R.id.robotThresholdSleep)).getText());
            robotSettings.put("doRecognizeFeelingsFromAudio", ((CheckBox) rootView.findViewById(R.id.doRecognizeFeelingsFromAudio)).isChecked());
            robotSettings.put("doRecognizeFeelingsFromVideo", ((CheckBox) rootView.findViewById(R.id.doRecognizeFeelingsFromVideo)).isChecked());
            robotSettings.put("isAlive", ((CheckBox) rootView.findViewById(R.id.isAlive)).isChecked());

            // assign json entries to user fields
            userSettings.put("userName", ((TextView) rootView.findViewById(R.id.userName)).getText());
            userSettings.put("userAge", ((TextView) rootView.findViewById(R.id.userAge)).getText());
            userSettings.put("userGender", ((TextView) rootView.findViewById(R.id.userGender)).getText());
            userSettings.put("userFamilyStatus", ((TextView) rootView.findViewById(R.id.userFamilyStatus)).getText());
            userSettings.put("userEducationStatus", ((TextView) rootView.findViewById(R.id.userEducationStatus)).getText());
            userSettings.put("userEmailAccount", ((TextView) rootView.findViewById(R.id.userEmailAccount)).getText());
            userAddress.put("streetAddress", ((TextView) rootView.findViewById(R.id.streetAddress)).getText());
            userAddress.put("city", ((TextView) rootView.findViewById(R.id.city)).getText());
            userAddress.put("state", ((TextView) rootView.findViewById(R.id.state)).getText());
            userAddress.put("postalCode", ((TextView) rootView.findViewById(R.id.postalCode)).getText());

            // TODO: phone numbers

            // assign json entries to personal user fields
            userPersonalData.put("userHousingSituation", ((TextView) rootView.findViewById(R.id.userHousingSituation)).getText());
            userPersonalData.put("userEmploymentSituation", ((TextView) rootView.findViewById(R.id.userEmploymentSituation)).getText());
            userPersonalData.put("userReligionState", ((TextView) rootView.findViewById(R.id.userReligionState)).getText());
            userPersonalData.put("userResidenceDuringDay", ((TextView) rootView.findViewById(R.id.userResidenceDuringDay)).getText());
            userPersonalData.put("userAverageIncome", ((TextView) rootView.findViewById(R.id.userAverageIncome)).getText());
            userPersonalData.put("userMigrationBackground", ((TextView) rootView.findViewById(R.id.userMigrationBackground)).getText());
            userPersonalData.put("userRegionalityScale", ((TextView) rootView.findViewById(R.id.userRegionalityScale)).getText());

            // TODO: user diseases


            // finally puttin everything together
            userPersonalData.put("userKnownDiseases", userKnownDiseases);
            userSettings.put("userAddress", userAddress);
            userSettings.put("userPhoneNumbers", userPhoneNumbers);
            userSettings.put("userPersonalData", userPersonalData);
            jsonObj.put("robotSettings", robotSettings);
            jsonObj.put("userSettings", userSettings);

            // write file
            writeSettingsJSON(this.getActivity().getBaseContext(), jsonObj.toString());
            makeSnackbarMessage(rootView, "Daten wurden erfolgreich gespeichert");
        }
        catch(JSONException ex)
        {
            makeSnackbarMessage(rootView, "Die gespeicherten Informationen konnten nicht gelesen werden");
        }
    }
}
