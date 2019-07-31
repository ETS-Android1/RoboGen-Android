package at.srfg.robogen.itemdetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import at.srfg.robogen.R;

public class ItemDetailVector extends ItemDetailBase {

    private final String m_sStartVector = "Test-Schritt) Verbindung aufbauen mit Anki Vector Roboter:";
    public FloatingActionButton m_btnStartVector;

    /*******************************************************************************
     * creating view for anki vector detail page
     ******************************************************************************/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.main_itemdetail_vector, container, false);

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

        ((TextView) rootView.findViewById(R.id.item_detail_title)).setText(mItem.m_sEntryHeader);
        ((TextView) rootView.findViewById(R.id.item_detail_text_1)).setText(m_sStartVector);

        m_btnStartVector = (FloatingActionButton) rootView.findViewById(R.id.bt_search);
        m_btnStartVector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                makeSnackbarMessage(view, "Starte Vector-Kommunikation..");

                // TODO:
                mItem.m_bEntryIsConnected = !mItem.m_bEntryIsConnected;
            }
        });
    }
}
