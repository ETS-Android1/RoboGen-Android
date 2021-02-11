package at.srfg.robogen.ItemDetail;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;
import java.util.Calendar;

import at.srfg.robogen.R;
import at.srfg.robogen.RoboGen_App;

public class ItemDetailNutrition extends at.srfg.robogen.itemdetail.ItemDetailBase {

    private final String m_sShowNutrition = "Hier können Sie ihre Ernährung nachtragen und verwalten!";
    private RoboGen_App m_cRoboGenApp;

    public FloatingActionButton m_btnStartCalendar, m_btnRemoveCalendar, m_btnEditCalendar, m_btnRefreshCalendar;
    public Button btnDatePicker, btnTimePicker;
    public EditText txtDate, txtTime;
    private int mYear, mMonth, mDay, mHour, mMinute;

    //private String m_urlREAD = "https://power2dm.salzburgresearch.at/robogen/DataBase/DownloadJSON_MyCalendar";
    //private String m_urlADD = "https://power2dm.salzburgresearch.at/robogen/DataBase/UploadJSON_MyCalendar";
    //private String m_urlEDIT = "https://power2dm.salzburgresearch.at/robogen/DataBase/EditJSON_MyCalendar";
    //private String m_urlDELETE = "https://power2dm.salzburgresearch.at/robogen/DataBase/ResetJSON_MyCalendar";
    private RequestQueue m_requestQueue;
    private View m_rootView;

    private enum WriteMode {ADD, EDIT;}

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
        readNutritionDiaryJSON(); // first read of items

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
                writeEntryToNutritionDiary(rootView, ItemDetailNutrition.WriteMode.ADD);
            }
        });

        m_btnEditCalendar = (FloatingActionButton) rootView.findViewById(R.id.bt_editUserCalendar);
        m_btnEditCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writeEntryToNutritionDiary(rootView, ItemDetailNutrition.WriteMode.EDIT);
            }
        });

        m_btnRefreshCalendar = (FloatingActionButton) rootView.findViewById(R.id.bt_refreshUserCalendar);
        m_btnRefreshCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readNutritionDiaryJSON();
            }
        });

        m_btnRemoveCalendar = (FloatingActionButton) rootView.findViewById(R.id.bt_removeUserCalendar);
        m_btnRemoveCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteNutritionDiaryJSON();
                clearNutritionDiary(rootView);
            }
        });
    }

    /*******************************************************************************
     * assign loaded calendar to fields
     ******************************************************************************/
    public void assignNutritionDiaryToFields(JSONObject obj) {

        // TODO: see calender function
    }

    /*******************************************************************************
     * add an entry to the calendar
     ******************************************************************************/
    public void writeEntryToNutritionDiary(final View rootView, ItemDetailNutrition.WriteMode mode) {

        // TODO: see calender function
    }

    /*******************************************************************************
     * clear input
     ******************************************************************************/
    public void clearNutritionDiary(final View rootView) {

        // TODO: see calender function
    }


    /*******************************************************************************
     * read calendar file from server
     ******************************************************************************/
    public void readNutritionDiaryJSON() {

        // TODO: see calender function
    }

    /*******************************************************************************
     * add calendar entry to server
     ******************************************************************************/
    public void addNutritionDiaryJSON(JSONObject json) {

        // TODO: see calender function
    }

    /*******************************************************************************
     * write calendar file to server
     ******************************************************************************/
    public void editNutritionDiaryJSON(JSONObject json) {

        // TODO: see calender function
    }

    /*******************************************************************************
     * delete/reset calendar files on server
     ******************************************************************************/
    public void deleteNutritionDiaryJSON() {

        // TODO: see calender function
    }
}
