package at.srfg.robogen.ItemDetail;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.srfg.robogen.R;
import at.srfg.robogen.RoboGen_App;

public class ItemDetailNutrition extends at.srfg.robogen.itemdetail.ItemDetailBase {

    private final String m_sShowNutrition = "Hier können Sie ihre Ernährung nachtragen und verwalten!";
    private RoboGen_App m_cRoboGenApp;

    public FloatingActionButton m_btnStartCalendar, m_btnRemoveCalendar, m_btnRefreshCalendar;
    public Button btnDatePicker, btnTimePicker;
    public EditText txtDate, txtTime;
    private int mYear, mMonth, mDay, mHour, mMinute;

    private String m_urlREAD = "https://power2dm.salzburgresearch.at/robogen/DataBase/DownloadJSON_MyNutrition";
    private String m_urlADD = "https://power2dm.salzburgresearch.at/robogen/DataBase/UploadJSON_MyNutrition";
    private String m_urlDELETE = "https://power2dm.salzburgresearch.at/robogen/DataBase/ResetJSON_MyNutrition";
    private RequestQueue m_requestQueue;
    private View m_rootView;

    /*******************************************************************************
     * creating view for calendar detail page
     ******************************************************************************/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.main_itemdetail_nutrition, container, false);
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
        ((TextView) rootView.findViewById(R.id.item_detail_text_1)).setText(m_sShowNutrition);

        m_rootView = rootView;
        readNutritionJSON(); // first read of items

        btnDatePicker=(Button)rootView.findViewById(R.id.btn_date);
        btnTimePicker=(Button)rootView.findViewById(R.id.btn_time);
        txtDate=(EditText)rootView.findViewById(R.id.nutDate);
        txtTime=(EditText)rootView.findViewById(R.id.nutTime);

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
                writeEntryToNutrition(rootView);
            }
        });

        m_btnRefreshCalendar = (FloatingActionButton) rootView.findViewById(R.id.bt_refreshUserCalendar);
        m_btnRefreshCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readNutritionJSON();
            }
        });

        m_btnRemoveCalendar = (FloatingActionButton) rootView.findViewById(R.id.bt_removeUserCalendar);
        m_btnRemoveCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteNutritionJSON();
                clearNutritionFields(rootView);
            }
        });
    }

    /*******************************************************************************
     * assign loaded calendar to fields
     ******************************************************************************/
    public void assignNutritionToFields(String str) {

        // remove error message -> bad quick fix... TODO: needs a better solution
        String allLines = "";
        if(str.contains("[")) {
            allLines = str.substring(str.indexOf("["));
            allLines.trim();
        }
        else {
            allLines = str.replace("org.json.JSONException: End of input at character 0 of ", "");
        }

        // fill adapter array with entries split by new line
        final List<String> ListElementsArrayList = new ArrayList<String>();
        String[] lines = allLines.split("\\r?\\n");
        for (String line : lines) {
            ListElementsArrayList.add(line);
        }
        ListView listview = (ListView)m_rootView.findViewById(R.id.nutList);
        listview.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, ListElementsArrayList));
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String data=(String)parent.getItemAtPosition(position);
                String[] parts = data.split(" \\| ");
                ((TextView) m_rootView.findViewById(R.id.nutTitle)).setText(parts[0]);
                ((TextView) m_rootView.findViewById(R.id.nutAmount)).setText(parts[1]);
                ((TextView) m_rootView.findViewById(R.id.nutDate)).setText(parts[2]);
                ((TextView) m_rootView.findViewById(R.id.nutTime)).setText(parts[4]);
            }
        });
        ViewGroup.LayoutParams params = listview.getLayoutParams();
        params.height = ListElementsArrayList.size() * 150;
        listview.setLayoutParams(params);
        listview.requestLayout();
    }

    /*******************************************************************************
     * add an entry to the calendar
     ******************************************************************************/
    public void writeEntryToNutrition(final View rootView) {

        try {
            JSONObject newEntry = new JSONObject();
            newEntry.put("food",((TextView) rootView.findViewById(R.id.nutTitle)).getText());
            newEntry.put("amount",((TextView) rootView.findViewById(R.id.nutAmount)).getText());
            newEntry.put("date",((TextView) rootView.findViewById(R.id.nutDate)).getText());
            newEntry.put("time",((TextView) rootView.findViewById(R.id.nutTime)).getText());

            addNutritionJSON(newEntry);
            clearNutritionFields(rootView);
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
    public void clearNutritionFields(final View rootView) {

        ((TextView) rootView.findViewById(R.id.nutTitle)).setText("");
        ((TextView) rootView.findViewById(R.id.nutAmount)).setText("");
        ((TextView) rootView.findViewById(R.id.nutDate)).setText("");
        ((TextView) rootView.findViewById(R.id.nutTime)).setText("");
    }


    /*******************************************************************************
     * read calendar file from server
     ******************************************************************************/
    public void readNutritionJSON() {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, m_urlREAD, (String) null,

                new Response.Listener<JSONObject>(){ // INFO: this will never be called... TODO: needs a better solution
                    @Override
                    public void onResponse(JSONObject response) { assignNutritionToFields(""); }
                },
                new Response.ErrorListener(){ // INFO: ERROR isnt really an error here.. TODO: needs a better solution
                    @Override
                    public void onErrorResponse(VolleyError error) { assignNutritionToFields(error.getMessage());  }
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
    public void addNutritionJSON(JSONObject json) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, m_urlADD, json.toString(),
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) { readNutritionJSON(); } // read and update UI
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
     * delete/reset calendar files on server
     ******************************************************************************/
    public void deleteNutritionJSON() {

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
