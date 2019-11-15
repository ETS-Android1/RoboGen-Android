package at.srfg.robogen.itemdetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import at.srfg.robogen.R;
import at.srfg.robogen.RoboGen_App;
import at.srfg.robogen.fitnesswatch.FitBitManager;

public class ItemDetailWatch extends ItemDetailBase {

    private RoboGen_App m_cRoboGenApp;

    private final String m_sConnectWatch = "Schritt 1) Verbindung aufbauen mit einem Account für FitBit-Geräte:";
    public FloatingActionButton m_btnConnectWatch;

    private final String m_sUserDataWatch = "Schritt 2) FitBit-Daten ansehen zu Benutzer, Geräten, Aktivitäten und Gewicht:";
    public FloatingActionButton m_btnUserDataWatch;


    /*******************************************************************************
     * Getter
     ******************************************************************************/
    public FitBitManager getWatchManager()
    {
       return m_cRoboGenApp.getRoboGenManager().getWatchManager();
    }

    /*******************************************************************************
     * creating view for watch detail page
     ******************************************************************************/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.main_itemdetail_watch, container, false);

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

        m_cRoboGenApp.getRoboGenManager().FitBit_Init(this.getActivity());

        ((TextView) rootView.findViewById(R.id.item_detail_title)).setText(mItem.m_sEntryHeader);
        ((TextView) rootView.findViewById(R.id.item_detail_text_1)).setText(m_sConnectWatch);

        m_btnConnectWatch = (FloatingActionButton) rootView.findViewById(R.id.bt_search);
        m_btnConnectWatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                makeSnackbarMessage(view, "Starte Verbindung mit Uhr..");

                // starte Verbindung mit Uhr
                m_cRoboGenApp.getRoboGenManager().FitBit_Login(rootView);

                makeSnackbarMessage(view, "Verbindung erfolgreich aufgebaut!");
            }
        });


        ((TextView) rootView.findViewById(R.id.item_detail_text_userdata)).setText(m_sUserDataWatch);

        m_btnUserDataWatch = (FloatingActionButton) rootView.findViewById(R.id.bt_userdata);
        m_btnUserDataWatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // starte Streaming von infos von Uhr
                m_cRoboGenApp.getRoboGenManager().FitBit_StartDataStream();

                makeSnackbarMessage(view,"Benutzerdaten angefordert..");
            }
        });
    }
}
