package at.srfg.robogen;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import at.srfg.robogen.itemdetail.ItemDetailBase;
import at.srfg.robogen.itemdetail.ItemDetailRobot;
import at.srfg.robogen.itemdetail.ItemDetailWatch;
import at.srfg.robogen.itemdetail.ItemDetailAlexa;

/*******************************************************************************
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 ******************************************************************************/
public class ItemListActivity extends AppCompatActivity {

    /**
     * logging and debugging
     */
    public static final String LOG_TAG = "ItemListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        View recyclerView = findViewById(R.id.item_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
    }

    /*******************************************************************************
     * setup the view for bt recycling
     *******************************************************************************/
    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, ItemContent.ITEMS));
    }

    /*******************************************************************************
     * class SimpleItemRecyclerViewAdapter
     *******************************************************************************/
    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final ItemListActivity mParentActivity;
        private final List<ItemContent.ItemEntry> mValues;

        public Drawable mDrawableConnected = null;
        public Drawable mDrawableNotConnected = null;

        /*******************************************************************************
         * CTOR will init all entry ImageViews, representing state of connection
         *******************************************************************************/
        SimpleItemRecyclerViewAdapter(ItemListActivity parent,
                                      List<ItemContent.ItemEntry> items) {

            mValues = items;
            mParentActivity = parent;
        }

        /*******************************************************************************
         * listening to main page item click
         *******************************************************************************/
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ItemContent.ItemEntry item = (ItemContent.ItemEntry) view.getTag();

                Context context = view.getContext();
                Intent intent = new Intent(context, ItemDetailActivity.class);

                if(item.toString() == "Hugo") {
                    intent.putExtra(ItemDetailBase.ARG_ITEM_ID, item.mEntryID); // id  = 1
                    intent.putExtra(ItemDetailRobot.ARG_ITEM_ID, item.mEntryID); // id  = 1
                }
                else if(item.toString() == "Uhren")
                {
                    intent.putExtra(ItemDetailBase.ARG_ITEM_ID, item.mEntryID); // id  = 2
                    intent.putExtra(ItemDetailWatch.ARG_ITEM_ID, item.mEntryID); // id = 2
                }
                else if(item.toString() == "Alexa")
                {
                    intent.putExtra(ItemDetailBase.ARG_ITEM_ID, item.mEntryID); // id  = 3
                    intent.putExtra(ItemDetailAlexa.ARG_ITEM_ID, item.mEntryID); // id = 3
                }
                else
                {
                    Log.d(LOG_TAG, "Internal Error: invalid option selected");
                }

                context.startActivity(intent);
            }
        };

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_content, parent, false);

            mDrawableConnected = view.getResources().getDrawable(R.drawable.img_connected);
            mDrawableNotConnected = view.getResources().getDrawable(R.drawable.img_not_connected);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {

            if(mValues.get(position).mEntryIsConnected) {
                holder.mConnectionView.setImageDrawable(mDrawableConnected);
            }
            else{
                holder.mConnectionView.setImageDrawable(mDrawableNotConnected);
            }

            holder.mIdView.setText(mValues.get(position).mEntryID);
            holder.mTitleView.setText(mValues.get(position).mEntryTitle);

            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        /*******************************************************************************
         * class representing a viewholder
         *******************************************************************************/
        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView mConnectionView;
            final TextView mIdView;
            final TextView mTitleView;

            ViewHolder(View view) {
                super(view);
                mConnectionView = (ImageView) view.findViewById(R.id.image_connOverview);
                mIdView = (TextView) view.findViewById(R.id.id_text);
                mTitleView = (TextView) view.findViewById(R.id.content);
            }
        }
    }
}
