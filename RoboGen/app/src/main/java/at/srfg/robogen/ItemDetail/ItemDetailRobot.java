package at.srfg.robogen.ItemDetail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

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

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.item_detail)).setText(mItem.details);


            Activity activity = this.getActivity();
            mBluetoothManager = new BluetoothManager(activity, activity.getBaseContext());
            mBluetoothManager.RequestExtraPermissionsForBluetooth(activity);

            mItem.searchButton = (FloatingActionButton) rootView.findViewById(R.id.bt_search);
            mItem.searchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Prüfung auf vorhandene Bluetooth-Verbindung..", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                    mBluetoothManager.doConnect();
                }
            });

            mItem.sendButton = (FloatingActionButton) rootView.findViewById(R.id.bt_send);
            mItem.sendButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Snackbar.make(view, "Sende Daten an Gerät", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                    // TODO: test data
                    byte[] buffer = new byte[1];
                    buffer[0] = 1;
                    mBluetoothManager.send(buffer);
                }
            });
        }

        return rootView;
    }

    /*******************************************************************************
     * on result bluetooth search select
     *******************************************************************************/
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mBluetoothManager.onScanResult(requestCode, resultCode, data);
    }
}
