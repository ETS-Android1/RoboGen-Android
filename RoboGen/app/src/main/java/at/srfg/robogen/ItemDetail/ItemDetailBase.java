package at.srfg.robogen.itemdetail;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.snackbar.Snackbar;

import at.srfg.robogen.itemcontent.ItemContent;
import at.srfg.robogen.R;

public class ItemDetailBase extends Fragment {

    protected ItemContent.ItemEntry mItem;

    /*******************************************************************************
     * The fragment argument representing the item ID that this fragment
     * represents.
     ******************************************************************************/
    public static final String m_sARG_ITEM_ID = "item_id";

    /*******************************************************************************
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     ******************************************************************************/
    public ItemDetailBase() {
    }

    /*******************************************************************************
     * override creation method to init item content
     ******************************************************************************/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(m_sARG_ITEM_ID)) {

            // Load the dummy content specified by the fragment arguments.
            mItem = ItemContent.m_mapItems.get(getArguments().getString(m_sARG_ITEM_ID));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.m_sEntryTitle);
                appBarLayout.setCollapsedTitleTextAppearance(R.style.Toolbar_TitleItemDetail);
                appBarLayout.setExpandedTitleTextAppearance(R.style.Toolbar_TitleItemDetail);
            }
        }
    }

    /*******************************************************************************
     * helper function to make snackbars, used in derived classes
     ******************************************************************************/
    protected void makeSnackbarMessage(View view, String msg)
    {
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }
}
