package at.srfg.robogen.itemdetail;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import at.srfg.robogen.R;
import at.srfg.robogen.fitnesswatch.FitBitManager;

public class ItemDetailWatch extends ItemDetailBase {

    private final String mText_1 = "Test-Schritt) Verbindung aufbauen mit Uhr:";
    public FloatingActionButton mWatchStartButton;

    private FitBitManager mWatchManager = null;

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
        // TODO

        ((TextView) rootView.findViewById(R.id.item_detail_title)).setText(mItem.mEntryHeader);
        ((TextView) rootView.findViewById(R.id.item_detail_text_1)).setText(mText_1);

        mWatchStartButton = (FloatingActionButton) rootView.findViewById(R.id.bt_search);
        mWatchStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Starte Verbindung mit Uhr..", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                // TODO:
                mWatchManager.logIn(rootView);
                mItem.mEntryIsConnected = !mItem.mEntryIsConnected;
            }
        });
    }
}
