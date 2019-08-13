package at.srfg.robogen.itemdetail;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import at.srfg.robogen.R;
import at.srfg.robogen.alexa.AlexaManager;
import at.srfg.robogen.fitnesswatch.FitBitManager;

public class ItemDetailAlexa extends ItemDetailBase {

    private AlexaManager mAlexaManager;

    private final String m_sStartAlexa = "Test-Schritt) Verbindung aufbauen mit Alexa:";
    public FloatingActionButton m_btnStartAlexa;

    /*******************************************************************************
     * creating view for anki alexa detail page
     ******************************************************************************/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.main_itemdetail_alexa, container, false);

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
        mAlexaManager = new AlexaManager(activity, activity.getBaseContext());

        ((TextView) rootView.findViewById(R.id.item_detail_title)).setText(mItem.m_sEntryHeader);
        ((TextView) rootView.findViewById(R.id.item_detail_text_1)).setText(m_sStartAlexa);

        m_btnStartAlexa = (FloatingActionButton) rootView.findViewById(R.id.bt_start);
        m_btnStartAlexa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                makeSnackbarMessage(view, "Starte Alexa-Kommunikation..");

                mAlexaManager.InitAlexaInvocation();

                mItem.m_bEntryIsConnected = !mItem.m_bEntryIsConnected;
            }
        });
    }
}
