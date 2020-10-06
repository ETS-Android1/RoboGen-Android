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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.util.Calendar;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public Button btnDatePicker, btnTimePicker;
    public EditText txtDate, txtTime;
    private int mYear, mMonth, mDay, mHour, mMinute;

    private String m_urlBase ="https://power2dm.salzburgresearch.at/robogen";
    private RequestQueue m_requestQueue;
    private View m_rootView;

    /*******************************************************************************
     * creating view for calendar detail page
     ******************************************************************************/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.main_itemdetail_calendar, container, false);

        m_requestQueue = Volley.newRequestQueue(this.getActivity().getBaseContext());

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

        m_rootView = rootView;
        readCalendarJSON();

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
     * read calendar file from server
     ******************************************************************************/
    public void readCalendarJSON() {

        makeRequest(m_urlBase + "/DataBase/DownloadJSON_MyCalendar", Request.Method.POST, "");
    }

    /*******************************************************************************
     * write calendar file to server
     ******************************************************************************/
    public void writeCalendarJSON(String json) {

        makeRequest(m_urlBase + "/DataBase/UploadJSON_MyCalendar", Request.Method.POST, json);
    }

    /*******************************************************************************
     * delete/reset calendar files on server
     ******************************************************************************/
    public void deleteCalendarJSON() {

        makeRequest(m_urlBase + "/DataBase/ResetJSON_MyCalendar", Request.Method.POST, "");
    }


    /*******************************************************************************
     * assign loaded calendar to fields
     ******************************************************************************/
    public void assignCalendarToFields(String jsonString) {

        JSONArray calendarObj = null;

        try {
            if(jsonString == null || jsonString.length() == 0) {
                jsonString = "{'cal':[]}";
            }

            JSONObject obj = new JSONObject(jsonString);
            if (obj.has("cal"))
			    calendarObj = obj.getJSONArray("cal");
			else
			    calendarObj = new JSONArray();

            //Log.w("all",obj.toString());
            //Log.w("cal",calendarObj.toString());

            ListView listview = (ListView)m_rootView.findViewById(R.id.calList);
            final List<String> ListElementsArrayList = new ArrayList<String>();
			for (int i=0; i<calendarObj.length(); i++) {
			    JSONArray array = calendarObj.getJSONArray(0);
			    JSONObject entry = array.getJSONObject(i);
                ListElementsArrayList.add(entry.getString("title")); // TODO: JSON has two array indicators??
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, ListElementsArrayList);
            listview.setAdapter(adapter);
            listview.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //editCalendarEntry(rootView,position);
                }
            });

            ViewGroup.LayoutParams params = listview.getLayoutParams();
            params.height = calendarObj.length() * 50;
            listview.setLayoutParams(params);
            listview.requestLayout();
        }
        catch(JSONException ex)
        {
            makeSnackbarMessage(m_rootView, "Die gespeicherten Informationen konnten nicht gelesen werden");
        }
    }

    /*******************************************************************************
     * edit a calendar entry
     ******************************************************************************/
    //public void editCalendarEntry(final View rootView, int idx) {
    //    JSONArray calendarObj = null;
    //
    //    try {
    //        String jsonString = readCalendarJSON();
    //        if(jsonString == null || jsonString.length() == 0) return;
    //
    //        JSONObject obj = new JSONObject(jsonString);
    //        calendarObj = obj.getJSONArray("calSettings");
    //        JSONObject tmpObj = calendarObj.getJSONObject(idx);
    //
    //        ((TextView) rootView.findViewById(R.id.calTitle)).setText(tmpObj.getString("title"));
    //        ((TextView) rootView.findViewById(R.id.calDate)).setText(tmpObj.getString("date"));
    //        ((TextView) rootView.findViewById(R.id.calTime)).setText(tmpObj.getString("time"));
    //        ((Spinner) rootView.findViewById(R.id.calRepeat)).setSelection(tmpObj.getInt("repeat"));
    //        ((TextView) rootView.findViewById(R.id.calReminder)).setText(tmpObj.getString("reminder"));
    //
    //        calendarObj.remove(idx);
    //
    //        obj.put("cal",calendarObj);
    //        writeCalendarJSON(obj.toString());
    //        assignCalendarToFields(rootView);
    //
    //    }
    //    catch(JSONException ex)
    //    {
    //        makeSnackbarMessage(rootView, "Die gespeicherten Informationen konnten nicht gelesen werden");
    //    }
    //}

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

            JSONObject newEntry = new JSONObject();
            newEntry.put("title",((TextView) rootView.findViewById(R.id.calTitle)).getText());
            newEntry.put("date",((TextView) rootView.findViewById(R.id.calDate)).getText());
            newEntry.put("time",((TextView) rootView.findViewById(R.id.calTime)).getText());
            newEntry.put("repeat", ((Spinner) rootView.findViewById(R.id.calRepeat)).getSelectedItemPosition());
            newEntry.put("reminder",((TextView) rootView.findViewById(R.id.calReminder)).getText());

            writeCalendarJSON(newEntry.toString());
            readCalendarJSON();

            clearCalendarFields(rootView);
            makeSnackbarMessage(rootView, "Eintrag erfolgreich hinzugefügt");
        }
        catch(JSONException ex)
        {
            makeSnackbarMessage(rootView, "Der Eintrag konnte nicht hinzugefügt werden");
        }
    }

    /*******************************************************************************
     * private internal helper method: makeRequest
     * handles request answer in main thread
     ******************************************************************************/
    private void makeRequest(String requestURL, int requestType, final String jsonParam)
    {
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(requestType, requestURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        assignCalendarToFields(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.w("ERROR: ", error.toString());
                    }

                    protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("param1", jsonParam);
                        return params;
                    };
                }
        );

        m_requestQueue.add(stringRequest);
    }
}
