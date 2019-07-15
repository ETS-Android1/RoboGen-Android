package at.srfg.robogen.ItemDetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import at.srfg.robogen.R;

public class ItemDetailAlexa extends ItemDetailBase {

    private final String mText_1 = "Test-Schritt) Verbindung aufbauen mit Alexa:";
    public FloatingActionButton mAlexaStartButton;

    /*******************************************************************************
     * creating view for alexa detail page
     ******************************************************************************/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.item_detail_alexa, container, false);

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

        ((TextView) rootView.findViewById(R.id.item_detail_title)).setText(mItem.mEntryHeader);
        ((TextView) rootView.findViewById(R.id.item_detail_text_1)).setText(mText_1);


        mAlexaStartButton = (FloatingActionButton) rootView.findViewById(R.id.bt_search);
        mAlexaStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Starte Alexa..", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                // TODO:
                mItem.mEntryIsConnected = !mItem.mEntryIsConnected;
            }
        });
    }
}
