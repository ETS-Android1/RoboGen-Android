package at.srfg.robogen.itemdetail;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import at.srfg.robogen.R;
import at.srfg.robogen.fitnesswatch.FitBitManager;

public class ItemDetailWatch extends ItemDetailBase {

    private final String mText_1 = "Schritt 1) Verbindung aufbauen mit einem Account für FitBit-Geräte:";
    public FloatingActionButton mWatchStartButton;

    private final String mText_Heartrate = "Schritt 2) Herzrate anfordern von registriertem Account:";
    public FloatingActionButton mAlexaButton_Heartrate;


    private FitBitManager mWatchManager = null;

    public FitBitManager getWatchManager()
    {
        return mWatchManager;
    }

    /*******************************************************************************
     * creating view for watch detail page
     ******************************************************************************/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.item_detail_watch, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            initGUIComponents(rootView);
        }

        return rootView;
    }

    /*******************************************************************************
     * init GUI components
     ******************************************************************************/
    private void initGUIComponents(final View rootView){

        Activity activity = this.getActivity();
        mWatchManager = new FitBitManager(activity, activity.getBaseContext());

        ((TextView) rootView.findViewById(R.id.item_detail_title)).setText(mItem.mEntryHeader);
        ((TextView) rootView.findViewById(R.id.item_detail_text_1)).setText(mText_1);

        mWatchStartButton = (FloatingActionButton) rootView.findViewById(R.id.bt_search);
        mWatchStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                makeSnackbarMessage(view, "Starte Verbindung mit Uhr..");

                // starte Verbindung mit Uhr
                mWatchManager.logIn(rootView);
                mItem.mEntryIsConnected = !mItem.mEntryIsConnected;

                makeSnackbarMessage(view, "Verbindung erfolgreich aufgebaut!");
            }
        });


        ((TextView) rootView.findViewById(R.id.item_detail_text_heartrate)).setText(mText_Heartrate);
        ((TextView) rootView.findViewById(R.id.item_detail_result_heartrate)).setText("Noch keine Herzrate bezogen");

        mAlexaButton_Heartrate = (FloatingActionButton) rootView.findViewById(R.id.bt_heartrate);
        mAlexaButton_Heartrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                makeSnackbarMessage(view,"Herzrate anfordern..");

                // starte Verbindung mit Uhr
                ((TextView) rootView.findViewById(R.id.item_detail_result_heartrate))
                        .setText("Die Herzrate ist: " + mWatchManager.getHeartRate());
            }
        });
    }
}
