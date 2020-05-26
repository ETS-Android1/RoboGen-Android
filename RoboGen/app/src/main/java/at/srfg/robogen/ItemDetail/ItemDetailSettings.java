package at.srfg.robogen.itemdetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

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

        //m_cRoboGenApp.getRoboGenManager().Contacts_Init(this.getActivity(), rootView);

        m_btnStartSettings = (FloatingActionButton) rootView.findViewById(R.id.bt_sendUserData);
        m_btnStartSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                makeSnackbarMessage(view, "Speichere Daten auf Ihrem Gerät...");

                //m_cRoboGenApp.getRoboGenManager().Contacts_SendSMS();
            }
        });
    }
}
