package at.srfg.robogen.itemdetail;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.Spinner;
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

        addSeekBarListeners(rootView);
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
     * init GUI components
     ******************************************************************************/
    private void addSeekBarListeners(final View rootView){

        // AudioVolume
        ((SeekBar) rootView.findViewById(R.id.robotAudioVolume)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                ((TextView)rootView.findViewById(R.id.robotAudioVolumeText)).setText(Integer.toString(progress));
            }
        });

        // StressThreshold
        ((SeekBar) rootView.findViewById(R.id.robotThresholdStress)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                ((TextView)rootView.findViewById(R.id.robotThresholdStressText)).setText(Integer.toString(progress));
            }
        });

        // SleepThreshold
        ((SeekBar) rootView.findViewById(R.id.robotThresholdSleep)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                ((TextView)rootView.findViewById(R.id.robotThresholdSleepText)).setText(Integer.toString(progress));
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
        JSONObject userPersonalData = null;

        try {
            String jsonString = readSettingsJSON(this.getActivity().getBaseContext());
            if(jsonString == null || jsonString.length() == 0) return;

            JSONObject obj = new JSONObject(jsonString);
            robotSettings = obj.getJSONObject("robotSettings");
            userSettings = obj.getJSONObject("userSettings");

            userAddress = userSettings.getJSONObject("userAddress");
            userPersonalData = userSettings.getJSONObject("userPersonalData");
        }
        catch(JSONException ex)
        {
            makeSnackbarMessage(rootView, "Die gespeicherten Informationen konnten nicht gelesen werden");
        }

        // assign json entries to robot fields
        ((TextView) rootView.findViewById(R.id.robotName)).setText(robotSettings.optString("robotName"));
        ((Spinner) rootView.findViewById(R.id.robotVoice)).setSelection(robotSettings.optInt("robotVoice"));
        ((SeekBar) rootView.findViewById(R.id.robotAudioVolume)).setProgress(robotSettings.optInt("robotAudioVolume"));
        ((Spinner) rootView.findViewById(R.id.robotInfoDisplayLocation)).setSelection(robotSettings.optInt("robotInfoDisplayLocation"));
        ((TextView) rootView.findViewById(R.id.robotInfoDisplayFontSize)).setText(robotSettings.optString("robotInfoDisplayFontSize"));
        ((Spinner) rootView.findViewById(R.id.robotNotificationStyle)).setSelection(robotSettings.optInt("robotNotificationStyle"));
        ((SeekBar) rootView.findViewById(R.id.robotThresholdStress)).setProgress(robotSettings.optInt("robotThresholdStress"));
        ((SeekBar) rootView.findViewById(R.id.robotThresholdSleep)).setProgress(robotSettings.optInt("robotThresholdSleep"));
        ((CheckBox) rootView.findViewById(R.id.doRecognizeFeelingsFromAudio)).setChecked(robotSettings.optBoolean("doRecognizeFeelingsFromAudio"));
        ((CheckBox) rootView.findViewById(R.id.doRecognizeFeelingsFromVideo)).setChecked(robotSettings.optBoolean("doRecognizeFeelingsFromVideo"));
        ((CheckBox) rootView.findViewById(R.id.isAlive)).setChecked(robotSettings.optBoolean("isAlive"));

        // assign json entries to user fields
        ((TextView) rootView.findViewById(R.id.userName)).setText(userSettings.optString("userName"));
        ((TextView) rootView.findViewById(R.id.userAge)).setText(userSettings.optString("userAge"));
        ((Spinner) rootView.findViewById(R.id.userGender)).setSelection(userSettings.optInt("userGender"));
        ((Spinner) rootView.findViewById(R.id.userFamilyStatus)).setSelection(userSettings.optInt("userFamilyStatus"));
        ((Spinner) rootView.findViewById(R.id.userEducationStatus)).setSelection(userSettings.optInt("userEducationStatus"));
        ((TextView) rootView.findViewById(R.id.userEmailAccount)).setText(userSettings.optString("userEmailAccount"));
        ((TextView) rootView.findViewById(R.id.userPhoneNumber)).setText(userSettings.optString("userPhoneNumber"));
        ((TextView) rootView.findViewById(R.id.streetAddress)).setText(userAddress.optString("streetAddress"));
        ((TextView) rootView.findViewById(R.id.city)).setText(userAddress.optString("city"));
        ((TextView) rootView.findViewById(R.id.state)).setText(userAddress.optString("state"));
        ((TextView) rootView.findViewById(R.id.postalCode)).setText(userAddress.optString("postalCode"));

        // assign json entries to personal user fields
        ((Spinner) rootView.findViewById(R.id.userHousingSituation)).setSelection(userPersonalData.optInt("userHousingSituation"));
        ((Spinner) rootView.findViewById(R.id.userEmploymentSituation)).setSelection(userPersonalData.optInt("userEmploymentSituation"));
        ((Spinner) rootView.findViewById(R.id.userReligionState)).setSelection(userPersonalData.optInt("userReligionState"));
        ((Spinner) rootView.findViewById(R.id.userResidenceDuringDay)).setSelection(userPersonalData.optInt("userResidenceDuringDay"));
        ((Spinner) rootView.findViewById(R.id.userAverageIncome)).setSelection(userPersonalData.optInt("userAverageIncome"));
        ((Spinner) rootView.findViewById(R.id.userMigrationBackground)).setSelection(userPersonalData.optInt("userMigrationBackground"));
        ((Spinner) rootView.findViewById(R.id.userRegionalityScale)).setSelection(userPersonalData.optInt("userRegionalityScale"));
        ((Spinner) rootView.findViewById(R.id.userKnownDiseases)).setSelection(userPersonalData.optInt("userKnownDiseases"));
        ((Spinner) rootView.findViewById(R.id.userWalkingAid)).setSelection(userPersonalData.optInt("userWalkingAid"));
        ((Spinner) rootView.findViewById(R.id.userCareSituation)).setSelection(userPersonalData.optInt("userCareSituation"));
        ((Spinner) rootView.findViewById(R.id.userMedicine)).setSelection(userPersonalData.optInt("userMedicine"));
        ((Spinner) rootView.findViewById(R.id.userDrinking)).setSelection(userPersonalData.optInt("userDrinking"));
    }

    /*******************************************************************************
     * assign fields to settings
     ******************************************************************************/
    public void assignFieldsToSettings(final View rootView) {

        JSONObject jsonObj = new JSONObject();
        JSONObject robotSettings = new JSONObject();
        JSONObject userSettings = new JSONObject();
        JSONObject userAddress = new JSONObject();
        JSONObject userPersonalData = new JSONObject();

        try {
            // assign json entries to robot fields
            robotSettings.put("robotName",((TextView) rootView.findViewById(R.id.robotName)).getText());
            robotSettings.put("robotVoice", ((Spinner) rootView.findViewById(R.id.robotVoice)).getSelectedItemPosition());
            robotSettings.put("robotAudioVolume", ((SeekBar) rootView.findViewById(R.id.robotAudioVolume)).getProgress());
            robotSettings.put("robotInfoDisplayLocation", ((Spinner) rootView.findViewById(R.id.robotInfoDisplayLocation)).getSelectedItemPosition());
            robotSettings.put("robotInfoDisplayFontSize", ((TextView) rootView.findViewById(R.id.robotInfoDisplayFontSize)).getText());
            robotSettings.put("robotNotificationStyle", ((Spinner) rootView.findViewById(R.id.robotNotificationStyle)).getSelectedItemPosition());
            robotSettings.put("robotThresholdStress", ((SeekBar) rootView.findViewById(R.id.robotThresholdStress)).getProgress());
            robotSettings.put("robotThresholdSleep", ((SeekBar) rootView.findViewById(R.id.robotThresholdSleep)).getProgress());
            robotSettings.put("doRecognizeFeelingsFromAudio", ((CheckBox) rootView.findViewById(R.id.doRecognizeFeelingsFromAudio)).isChecked());
            robotSettings.put("doRecognizeFeelingsFromVideo", ((CheckBox) rootView.findViewById(R.id.doRecognizeFeelingsFromVideo)).isChecked());
            robotSettings.put("isAlive", ((CheckBox) rootView.findViewById(R.id.isAlive)).isChecked());

            // assign json entries to user fields
            userSettings.put("userName", ((TextView) rootView.findViewById(R.id.userName)).getText());
            userSettings.put("userAge", ((TextView) rootView.findViewById(R.id.userAge)).getText());
            userSettings.put("userGender", ((Spinner) rootView.findViewById(R.id.userGender)).getSelectedItemPosition());
            userSettings.put("userFamilyStatus", ((Spinner) rootView.findViewById(R.id.userFamilyStatus)).getSelectedItemPosition());
            userSettings.put("userEducationStatus", ((Spinner) rootView.findViewById(R.id.userEducationStatus)).getSelectedItemPosition());
            userSettings.put("userEmailAccount", ((TextView) rootView.findViewById(R.id.userEmailAccount)).getText());
            userSettings.put("userPhoneNumber", ((TextView) rootView.findViewById(R.id.userPhoneNumber)).getText());
            userAddress.put("streetAddress", ((TextView) rootView.findViewById(R.id.streetAddress)).getText());
            userAddress.put("city", ((TextView) rootView.findViewById(R.id.city)).getText());
            userAddress.put("state", ((TextView) rootView.findViewById(R.id.state)).getText());
            userAddress.put("postalCode", ((TextView) rootView.findViewById(R.id.postalCode)).getText());

            // assign json entries to personal user fields
            userPersonalData.put("userHousingSituation", ((Spinner) rootView.findViewById(R.id.userHousingSituation)).getSelectedItemPosition());
            userPersonalData.put("userEmploymentSituation", ((Spinner) rootView.findViewById(R.id.userEmploymentSituation)).getSelectedItemPosition());
            userPersonalData.put("userReligionState", ((Spinner) rootView.findViewById(R.id.userReligionState)).getSelectedItemPosition());
            userPersonalData.put("userResidenceDuringDay", ((Spinner) rootView.findViewById(R.id.userResidenceDuringDay)).getSelectedItemPosition());
            userPersonalData.put("userAverageIncome", ((Spinner) rootView.findViewById(R.id.userAverageIncome)).getSelectedItemPosition());
            userPersonalData.put("userMigrationBackground", ((Spinner) rootView.findViewById(R.id.userMigrationBackground)).getSelectedItemPosition());
            userPersonalData.put("userRegionalityScale", ((Spinner) rootView.findViewById(R.id.userRegionalityScale)).getSelectedItemPosition());
            userPersonalData.put("userKnownDiseases", ((Spinner) rootView.findViewById(R.id.userKnownDiseases)).getSelectedItemPosition());
            userPersonalData.put("userWalkingAid", ((Spinner) rootView.findViewById(R.id.userWalkingAid)).getSelectedItemPosition());
            userPersonalData.put("userCareSituation", ((Spinner) rootView.findViewById(R.id.userCareSituation)).getSelectedItemPosition());
            userPersonalData.put("userMedicine", ((Spinner) rootView.findViewById(R.id.userMedicine)).getSelectedItemPosition());
            userPersonalData.put("userDrinking", ((Spinner) rootView.findViewById(R.id.userDrinking)).getSelectedItemPosition());

            // finally puttin everything together
            userSettings.put("userAddress", userAddress);
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
