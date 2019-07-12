package at.srfg.robogen;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBar;
import android.view.MenuItem;

import at.srfg.robogen.ItemDetail.ItemDetailBase;
import at.srfg.robogen.ItemDetail.ItemDetailRobot;
import at.srfg.robogen.ItemDetail.ItemDetailWatch;
import at.srfg.robogen.ItemDetail.ItemDetailAlexa;

/*******************************************************************************
 * An activity representing a single Item detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link ItemListActivity}.
 ******************************************************************************/
public class ItemDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Create the detail fragment and add it to the activity
        // using a fragment transaction.
        if (savedInstanceState == null) {

            Bundle arguments = new Bundle();
            String itemID = getIntent().getStringExtra(ItemDetailBase.ARG_ITEM_ID);

            if(itemID.equals("1."))
            {
                arguments.putString(ItemDetailRobot.ARG_ITEM_ID, getIntent().getStringExtra(ItemDetailRobot.ARG_ITEM_ID));
                ItemDetailRobot fragment = new ItemDetailRobot();
                fragment.setArguments(arguments);
                getSupportFragmentManager().beginTransaction().add(R.id.item_detail_container, fragment).commit();
            }
            else if(itemID.equals("2."))
            {
                arguments.putString(ItemDetailWatch.ARG_ITEM_ID, getIntent().getStringExtra(ItemDetailWatch.ARG_ITEM_ID));
                ItemDetailWatch fragment = new ItemDetailWatch();
                fragment.setArguments(arguments);
                getSupportFragmentManager().beginTransaction().add(R.id.item_detail_container, fragment).commit();
            }
            else if(itemID.equals("3."))
            {
                arguments.putString(ItemDetailAlexa.ARG_ITEM_ID, getIntent().getStringExtra(ItemDetailAlexa.ARG_ITEM_ID));
                ItemDetailAlexa fragment = new ItemDetailAlexa();
                fragment.setArguments(arguments);
                getSupportFragmentManager().beginTransaction().add(R.id.item_detail_container, fragment).commit();
            }
            else
            {
                // all went wrong
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
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpTo(new Intent(this, ItemListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
