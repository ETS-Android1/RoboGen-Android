package at.srfg.robogen.itemdetail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import at.srfg.robogen.ItemDetailActivity;
import at.srfg.robogen.ItemListActivity;
import at.srfg.robogen.R;
import at.srfg.robogen.bluetooth.BluetoothManager;

/*******************************************************************************
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 ******************************************************************************/
public class ItemDetailRobot extends ItemDetailBase {

    private BluetoothManager mBluetoothManager = null;

    private final String mText_1 = "Schritt 1) Verbindung aufbauen mit dem Roboter Hugo via Bluetooth.\n" +
                                   "Das Gerät muss zuerst gekoppelt werden und dann verbunden.\n" +
                                    "Beides kann mit dem folgenden Schalter erledigt werden:";
    private final String mText_2 = "Schritt 2) Nachdem eine Verbindung erfolgreich aufgebaut wurde können Testdaten versendet werden.\n" +
                                   "Mit dem folgenden Schalter können Test-Daten an den Roboter gesendet werden:";

    public FloatingActionButton mSearchButton;
    public FloatingActionButton mSendButton;


    /*******************************************************************************
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     ******************************************************************************/
    public ItemDetailRobot() {
    }

    /*******************************************************************************
     * creating view for robot detail page
     ******************************************************************************/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.item_detail_robot, container, false);

        if (mItem != null) {
            initGUIComponents(rootView);
        }

        return rootView;
    }

    /*******************************************************************************
     * init GUI components
     ******************************************************************************/
    private void initGUIComponents(final View rootView)
    {
        // init text
        ((TextView) rootView.findViewById(R.id.item_detail_title)).setText(mItem.mEntryHeader);
        ((TextView) rootView.findViewById(R.id.item_detail_text_1)).setText(mText_1);
        ((TextView) rootView.findViewById(R.id.item_detail_text_2)).setText(mText_2);

        // init bluetooth manager
        Activity activity = this.getActivity();
        mBluetoothManager = new BluetoothManager(activity, activity.getBaseContext());
        mBluetoothManager.RequestExtraPermissionsForBluetooth(activity);


        // init bluetooth buttons
        mSearchButton = (FloatingActionButton) rootView.findViewById(R.id.bt_search);
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                makeSnackbarMessage(view, "Prüfung auf vorhandene Bluetooth-Verbindung..");
                mBluetoothManager.doConnect();
            }
        });
        mSendButton = (FloatingActionButton) rootView.findViewById(R.id.bt_send);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                makeSnackbarMessage(view, "Sende Daten an Gerät");

                // TODO: test data
                byte[] buffer = new byte[1];
                buffer[0] = 1;
                mBluetoothManager.send(buffer);

                // TODO:
                mItem.mEntryIsConnected = !mItem.mEntryIsConnected;
            }
        });
    }

    /*******************************************************************************
     * on result bluetooth search select
     *******************************************************************************/
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mBluetoothManager.onScanResult(requestCode, resultCode, data);
    }
}
