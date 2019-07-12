package at.srfg.robogen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import at.srfg.robogen.bluetooth.BluetoothManager;

/*******************************************************************************
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 ******************************************************************************/
public class ItemDetailFragment extends Fragment {

    BluetoothManager mBluetoothManager = null;

    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private ItemContent.ItemEntry mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = ItemContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.content);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.item_detail, container, false);

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
