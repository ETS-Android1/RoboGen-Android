package at.srfg.robogen.itemactivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import at.srfg.robogen.R;
import at.srfg.robogen.fitnesswatch.fitbit_Auth.AuthenticationManager;
import at.srfg.robogen.itemdetail.ItemDetailAlexa;
import at.srfg.robogen.itemdetail.ItemDetailBase;
import at.srfg.robogen.itemdetail.ItemDetailRobot;
import at.srfg.robogen.itemdetail.ItemDetailWatch;
import at.srfg.robogen.itemdetail.ItemDetailVector;

/*******************************************************************************
 * An activity representing a single Item detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link ItemListActivity}.
 ******************************************************************************/
public class ItemDetailActivity extends AppCompatActivity {

    /**
     * logging and debugging
     */
    public static final String m_sLogTag = "ItemDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_itemdetail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar (reuse if back-arrow ever needed again)
        //ActionBar actionBar = getSupportActionBar();
        //if (actionBar != null) {
            //actionBar.setDisplayHomeAsUpEnabled(true);
        //}

        // Create the detail fragment and add it to the activity using a fragment transaction.
        if (savedInstanceState == null) {

            Bundle arguments = new Bundle();
            String itemID = getIntent().getStringExtra(ItemDetailBase.m_sARG_ITEM_ID);

            if(itemID.equals("1.")) // Q.bo robot
            {
                arguments.putString(ItemDetailRobot.m_sARG_ITEM_ID, getIntent().getStringExtra(ItemDetailRobot.m_sARG_ITEM_ID));
                ItemDetailRobot fragment = new ItemDetailRobot();
                fragment.setArguments(arguments);
                getSupportFragmentManager().beginTransaction().add(R.id.item_detail_container, fragment).commit();
            }
            else if(itemID.equals("2.")) // FitBit watch
            {
                arguments.putString(ItemDetailWatch.m_sARG_ITEM_ID, getIntent().getStringExtra(ItemDetailWatch.m_sARG_ITEM_ID));
                ItemDetailWatch fragment = new ItemDetailWatch();
                fragment.setArguments(arguments);
                getSupportFragmentManager().beginTransaction().add(R.id.item_detail_container, fragment).commit();
            }
            else if(itemID.equals("3.")) // Alexa
            {
                arguments.putString(ItemDetailAlexa.m_sARG_ITEM_ID, getIntent().getStringExtra(ItemDetailAlexa.m_sARG_ITEM_ID));
                ItemDetailAlexa fragment = new ItemDetailAlexa();
                fragment.setArguments(arguments);
                getSupportFragmentManager().beginTransaction().add(R.id.item_detail_container, fragment).commit();
            }
            else if(itemID.equals("4.")) // Vector
            {
                arguments.putString(ItemDetailVector.m_sARG_ITEM_ID, getIntent().getStringExtra(ItemDetailVector.m_sARG_ITEM_ID));
                ItemDetailVector fragment = new ItemDetailVector();
                fragment.setArguments(arguments);
                getSupportFragmentManager().beginTransaction().add(R.id.item_detail_container, fragment).commit();
            }
            else
            {
                Log.d(m_sLogTag, "Internal Error: invalid option selected");
            }
        }
    }

    /*******************************************************************************
     * onOptionsItemSelected
     *******************************************************************************/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {

            // represents the Home or Up button. In this activity only the Up button is shown
            navigateUpTo(new Intent(this, ItemListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*******************************************************************************
     * used for result handling of activities inside of fragments
     *******************************************************************************/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.item_detail_container);
        if (currentFragment instanceof ItemDetailWatch) {

            /**
             *  When the FitBit Login UI finishes, it will invoke the `onActivityResult` of this activity.
             *  We call `AuthenticationManager.onActivityResult` and set ourselves as a login listener
             *  (via AuthenticationHandler) to check to see if this result was a login result. If the
             *  result code matches login, the AuthenticationManager will process the login request,
             *  and invoke our `onAuthFinished` method.
             *
             *  If the result code was not a login result code, then `onActivityResult` will return
             *  false, and we can handle other onActivityResult result codes.
             */

            ItemDetailWatch watchFragment = (ItemDetailWatch)currentFragment;
            if (!AuthenticationManager.onActivityResult(requestCode, resultCode, data, watchFragment.getWatchManager())) {
                // Handle other activity results, if needed
            }
        }
        else if(currentFragment instanceof ItemDetailRobot) {

            /**
             *  When the bluetooth Search returns with a user selected device to connect to
             *  we can bind the device with the app by calling BluetoothManager.onScanResult()
             */

            ItemDetailRobot robotFragment = (ItemDetailRobot)currentFragment;
            robotFragment.getBluetoothManager().onScanResult(requestCode, resultCode, data);
        }
    }
}
