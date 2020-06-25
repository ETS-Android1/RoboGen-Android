package at.srfg.robogen.itemdetail;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.util.Log;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.util.Calendar;
import java.util.ArrayList;
import java.util.List;

import at.srfg.robogen.R;
import at.srfg.robogen.RoboGen_App;

public class ItemDetailCalendar extends ItemDetailBase {

    private final String m_sShowCalendar = "Hier können Sie ihre Termine verwalten und Erinnerungen " +
                                           "konfigurieren. Drücken Sie das Kalender-Symbol " +
                                           "um neue Einträge hinzuzufügen oder einen vorhandenen Eintrag " +
                                           "um diesen zu editieren! Mit dem x-Symbol können Sie den aktuellen " +
                                           "Eintrag entfernen!";
    private RoboGen_App m_cRoboGenApp;

    public FloatingActionButton m_btnStartCalendar, m_btnRemoveCalendar;
    Button btnDatePicker, btnTimePicker;
    EditText txtDate, txtTime;
    private int mYear, mMonth, mDay, mHour, mMinute;

    /*******************************************************************************
     * creating view for calendar detail page
     ******************************************************************************/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.main_itemdetail_calendar, container, false);

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
        ((TextView) rootView.findViewById(R.id.item_detail_text_1)).setText(m_sShowCalendar);

        assignCalendarToFields(rootView);
        btnDatePicker=(Button)rootView.findViewById(R.id.btn_date);
        btnTimePicker=(Button)rootView.findViewById(R.id.btn_time);
        txtDate=(EditText)rootView.findViewById(R.id.calDate);
        txtTime=(EditText)rootView.findViewById(R.id.calTime);

        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                String month = "";
                                if ((monthOfYear + 1) < 10)
                                    month = "0"+(monthOfYear + 1);
                                else
                                    month = ""+(monthOfYear + 1);
                                String day = "";
                                if (dayOfMonth < 10)
                                    day = "0"+dayOfMonth;
                                else
                                    day = ""+dayOfMonth;
                                txtDate.setText(year + "-" + month + "-" + day);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        btnTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                 TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                String hour = "";
                                if (hourOfDay<10)
                                    hour = "0"+hourOfDay;
                                else
                                    hour = ""+hourOfDay;
                                String min = "";
                                if (minute<10)
                                    min = "0"+minute;
                                else
                                    min = ""+minute;
                                txtTime.setText(hour + ":" + min);
                            }
                        }, mHour, mMinute, true);
                timePickerDialog.show();
            }
        });

        m_btnStartCalendar = (FloatingActionButton) rootView.findViewById(R.id.bt_addUserCalendar);
        m_btnStartCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                makeSnackbarMessage(view, "Speichere Daten auf Ihrem Gerät...");
                addEntryToCalendar(rootView);
            }
        });

        m_btnRemoveCalendar = (FloatingActionButton) rootView.findViewById(R.id.bt_removeUserCalendar);
        m_btnRemoveCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearCalendarFields(rootView);
            }
        });
    }

    /*******************************************************************************
     * read calendar file from asset folder
     ******************************************************************************/
    public String readCalendarJSON(Context context) {

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
     * write calendar file to asset folder
     ******************************************************************************/
    public void writeCalendarJSON(Context context, String json) {

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
     * assign loaded calendar to fields
     ******************************************************************************/
    public void assignCalendarToFields(final View rootView) {

        JSONArray calendarObj = null;

        try {
            String jsonString = readCalendarJSON(this.getActivity().getBaseContext());
            if(jsonString == null || jsonString.length() == 0) {
                jsonString = "{'calSettings':[]}";
            }

            JSONObject obj = new JSONObject(jsonString);
            if (obj.has("calSettings"))
			    calendarObj = obj.getJSONArray("calSettings");
			else
			    calendarObj = new JSONArray();
            Log.w("all",obj.toString());
            //Log.w("cal",calendarObj.toString());
            ListView listview = (ListView)rootView.findViewById(R.id.calList);
            final List<String> ListElementsArrayList = new ArrayList<String>();
			for (int i=0; i<calendarObj.length(); i++) {
			    JSONObject activity = calendarObj.getJSONObject(i);
                ListElementsArrayList.add(activity.getString("title"));
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, ListElementsArrayList);
            listview.setAdapter(adapter);
            listview.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    editCalendarEntry(rootView,position);
                }
            });

            ViewGroup.LayoutParams params = listview.getLayoutParams();
            params.height = calendarObj.length() * 50;
            listview.setLayoutParams(params);
            listview.requestLayout();

        }
        catch(JSONException ex)
        {
            makeSnackbarMessage(rootView, "Die gespeicherten Informationen konnten nicht gelesen werden");
        }

    }

    /*******************************************************************************
     * edit a calendar entry
     ******************************************************************************/
    public void editCalendarEntry(final View rootView, int idx) {
        JSONArray calendarObj = null;

        try {
            String jsonString = readCalendarJSON(this.getActivity().getBaseContext());
            if(jsonString == null || jsonString.length() == 0) return;

            JSONObject obj = new JSONObject(jsonString);
            calendarObj = obj.getJSONArray("calSettings");
            JSONObject tmpObj = calendarObj.getJSONObject(idx);

            ((TextView) rootView.findViewById(R.id.calTitle)).setText(tmpObj.getString("title"));
            ((TextView) rootView.findViewById(R.id.calDate)).setText(tmpObj.getString("date"));
            ((TextView) rootView.findViewById(R.id.calTime)).setText(tmpObj.getString("time"));
            ((Spinner) rootView.findViewById(R.id.calRepeat)).setSelection(tmpObj.getInt("repeat"));
            ((TextView) rootView.findViewById(R.id.calReminder)).setText(tmpObj.getString("reminder"));

            calendarObj.remove(idx);

            obj.put("cal",calendarObj);
            writeCalendarJSON(this.getActivity().getBaseContext(), obj.toString());
            assignCalendarToFields(rootView);

        }
        catch(JSONException ex)
        {
            makeSnackbarMessage(rootView, "Die gespeicherten Informationen konnten nicht gelesen werden");
        }
    }

    /*******************************************************************************
     * clear input
     ******************************************************************************/
    public void clearCalendarFields(final View rootView) {

        ((TextView) rootView.findViewById(R.id.calTitle)).setText("");
        ((TextView) rootView.findViewById(R.id.calDate)).setText("");
        ((TextView) rootView.findViewById(R.id.calTime)).setText("");
        ((Spinner) rootView.findViewById(R.id.calRepeat)).setSelection(0);
        ((TextView) rootView.findViewById(R.id.calReminder)).setText("");

    }

    /*******************************************************************************
     * add an entry to the calendar
     ******************************************************************************/
    public void addEntryToCalendar(final View rootView) {

        try {
            String jsonString = readCalendarJSON(this.getActivity().getBaseContext());
            if(jsonString == null || jsonString.length() == 0) {
                jsonString = "{'calSettings':[]}";
            }

            JSONObject obj = new JSONObject(jsonString);
            JSONArray calendarObj = null;
            if (obj.has("calSettings"))
                calendarObj = obj.getJSONArray("calSettings");
            else
                calendarObj = new JSONArray();
            JSONObject newEntry = new JSONObject();
            newEntry.put("title",((TextView) rootView.findViewById(R.id.calTitle)).getText());
            newEntry.put("date",((TextView) rootView.findViewById(R.id.calDate)).getText());
            newEntry.put("time",((TextView) rootView.findViewById(R.id.calTime)).getText());
            newEntry.put("repeat", ((Spinner) rootView.findViewById(R.id.calRepeat)).getSelectedItemPosition());
            newEntry.put("reminder",((TextView) rootView.findViewById(R.id.calReminder)).getText());
            calendarObj.put(newEntry);
            obj.put("calSettings",calendarObj);
            writeCalendarJSON(this.getActivity().getBaseContext(), obj.toString());
            assignCalendarToFields(rootView);
            clearCalendarFields(rootView);
            makeSnackbarMessage(rootView, "Eintrag erfolgreich hinzugefügt");

        }
        catch(JSONException ex)
        {
            makeSnackbarMessage(rootView, "Der Eintrag konnte nicht hinzugefügt werden");
        }

    }
}
