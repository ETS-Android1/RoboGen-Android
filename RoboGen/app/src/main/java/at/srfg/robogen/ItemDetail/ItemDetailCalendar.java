package at.srfg.robogen.itemdetail;

import android.app.AlertDialog;
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

    public FloatingActionButton m_btnStartCalendar, m_btnRemoveCalendar, m_btnEditCalendar, m_btnRefreshCalendar;
    public Button btnDatePicker, btnTimePicker;
    public EditText txtDate, txtTime;
    private int mYear, mMonth, mDay, mHour, mMinute;

    private String m_urlREAD = "https://power2dm.salzburgresearch.at/robogen/DataBase/DownloadJSON_MyCalendar";
    private String m_urlADD = "https://power2dm.salzburgresearch.at/robogen/DataBase/UploadJSON_MyCalendar";
    private String m_urlEDIT = "https://power2dm.salzburgresearch.at/robogen/DataBase/EditJSON_MyCalendar";
    private String m_urlDELETE = "https://power2dm.salzburgresearch.at/robogen/DataBase/ResetJSON_MyCalendar";
    private RequestQueue m_requestQueue;
    private View m_rootView;

    private enum WriteMode {ADD, EDIT;}

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
        readCalendarJSON(); // first read of items

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

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), AlertDialog.THEME_HOLO_DARK,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String month = "";
                                if ((monthOfYear + 1) < 10) month = "0"+(monthOfYear + 1);
                                else month = ""+(monthOfYear + 1);

                                String day = "";
                                if (dayOfMonth < 10) day = "0"+dayOfMonth;
                                else day = ""+dayOfMonth;

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

                 TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), AlertDialog.THEME_HOLO_DARK,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                String hour = "";
                                if (hourOfDay<10) hour = "0"+hourOfDay;
                                else hour = ""+hourOfDay;

                                String min = "";
                                if (minute<10) min = "0"+minute;
                                else min = ""+minute;

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
                writeEntryToCalendar(rootView, WriteMode.ADD);
            }
        });

        m_btnEditCalendar = (FloatingActionButton) rootView.findViewById(R.id.bt_editUserCalendar);
        m_btnEditCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writeEntryToCalendar(rootView, WriteMode.EDIT);
            }
        });

        m_btnRefreshCalendar = (FloatingActionButton) rootView.findViewById(R.id.bt_refreshUserCalendar);
        m_btnRefreshCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readCalendarJSON();
            }
        });

        m_btnRemoveCalendar = (FloatingActionButton) rootView.findViewById(R.id.bt_removeUserCalendar);
        m_btnRemoveCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteCalendarJSON();
                clearCalendarFields(rootView);
            }
        });
    }

    /*******************************************************************************
     * assign loaded calendar to fields
     ******************************************************************************/
    public void assignCalendarToFields(JSONObject obj) {

        try {
            JSONArray calendarObj = (obj.has("cal"))? obj.getJSONArray("cal"): new JSONArray();
            final List<String> ListElementsArrayList = new ArrayList<String>();

			for (int i=0; i<calendarObj.length(); i++) {
                ListElementsArrayList.add(calendarObj.getJSONObject(i).getString("title"));
            }

            ListView listview = (ListView)m_rootView.findViewById(R.id.calList);
            listview.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, ListElementsArrayList));
            listview.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String data=(String)parent.getItemAtPosition(position);
                    ((TextView) m_rootView.findViewById(R.id.calTitle)).setText(data);
                }
            });
            ViewGroup.LayoutParams params = listview.getLayoutParams();
            params.height = calendarObj.length() * 150;
            listview.setLayoutParams(params);
            listview.requestLayout();
        }
        catch(JSONException ex)
        {
            makeSnackbarMessage(m_rootView, "Die gespeicherten Informationen konnten nicht gelesen werden");
        }
    }

    /*******************************************************************************
     * add an entry to the calendar
     ******************************************************************************/
    public void writeEntryToCalendar(final View rootView, WriteMode mode) {

        try {
            JSONObject newEntry = new JSONObject();
            newEntry.put("title",((TextView) rootView.findViewById(R.id.calTitle)).getText());
            newEntry.put("date",((TextView) rootView.findViewById(R.id.calDate)).getText());
            newEntry.put("time",((TextView) rootView.findViewById(R.id.calTime)).getText());
            newEntry.put("repeat", Integer.toString(((Spinner) rootView.findViewById(R.id.calRepeat)).getSelectedItemPosition()));
            newEntry.put("reminder",((TextView) rootView.findViewById(R.id.calReminder)).getText());

            if(mode == WriteMode.ADD) addCalendarJSON(newEntry);
            else if (mode == WriteMode.EDIT) editCalendarJSON(newEntry);

            clearCalendarFields(rootView);
            makeSnackbarMessage(rootView, "Eintrag erfolgreich hinzugefügt");
        }
        catch(JSONException ex)
        {
            makeSnackbarMessage(rootView, "Der Eintrag konnte nicht hinzugefügt werden");
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
     * read calendar file from server
     ******************************************************************************/
    public void readCalendarJSON() {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, m_urlREAD, (String) null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) { assignCalendarToFields(response); }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) { Log.e("CAL READ ERROR:", error.getMessage(), error); }
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
     * add calendar entry to server
     ******************************************************************************/
    public void addCalendarJSON(JSONObject json) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, m_urlADD, json.toString(),
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) { readCalendarJSON(); } // read and update UI
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) { Log.e("CAL ADD ERROR:", error.getMessage(), error); }
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
     * write calendar file to server
     ******************************************************************************/
    public void editCalendarJSON(JSONObject json) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, m_urlEDIT, json.toString(),
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) { readCalendarJSON(); } // read and update UI
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) { Log.e("CAL EDIT ERROR:", error.getMessage(), error); }
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
     * delete/reset calendar files on server
     ******************************************************************************/
    public void deleteCalendarJSON() {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, m_urlDELETE, (JSONObject) null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) { Log.w("SUCCESS: ", response.toString()); }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) { Log.e("CAL DEL ERROR:", error.getMessage(), error); }
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
}