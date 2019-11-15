package at.srfg.robogen.itemdetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import at.srfg.robogen.R;
import at.srfg.robogen.RoboGen_App;

public class ItemDetailContacts extends ItemDetailBase {

    private final String m_sShowContacts = "Alle Notfall-Kontakte anzeigen:";
    private RoboGen_App m_cRoboGenApp;

    /*******************************************************************************
     * creating view for contacts detail page
     ******************************************************************************/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.main_itemdetail_contacts, container, false);

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
        ((TextView) rootView.findViewById(R.id.item_detail_text_1)).setText(m_sShowContacts);

        m_cRoboGenApp.getRoboGenManager().Contacts_Init(this.getActivity(), rootView);
    }
}
