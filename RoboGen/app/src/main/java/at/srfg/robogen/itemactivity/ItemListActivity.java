package at.srfg.robogen.itemactivity;

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

import at.srfg.robogen.itemcontent.ItemContent;
import at.srfg.robogen.R;
import at.srfg.robogen.itemdetail.ItemDetailAlexa;
import at.srfg.robogen.itemdetail.ItemDetailBase;
import at.srfg.robogen.itemdetail.ItemDetailContacts;
import at.srfg.robogen.itemdetail.ItemDetailRobot;
import at.srfg.robogen.itemdetail.ItemDetailWatch;
import at.srfg.robogen.itemdetail.ItemDetailVector;

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
    public static final String m_sLogTag = "ItemListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_itemlist);

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
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, ItemContent.m_listItems));
    }

    /*******************************************************************************
     * class SimpleItemRecyclerViewAdapter
     *******************************************************************************/
    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<ItemContent.ItemEntry> m_listValues;

        public Drawable mDrawableIcon_QBO = null;
        public Drawable mDrawableIcon_FitBit = null;
        public Drawable mDrawableIcon_Alexa = null;
        public Drawable mDrawableIcon_Vector = null;
        public Drawable mDrawableIcon_Contacts = null;

        /*******************************************************************************
         * CTOR will init all entry ImageViews, representing state of connection
         *******************************************************************************/
        SimpleItemRecyclerViewAdapter(ItemListActivity parent,
                                      List<ItemContent.ItemEntry> items) {
            m_listValues = items;
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

                if(item.toString() == "Q.bo") {
                    intent.putExtra(ItemDetailBase.m_sARG_ITEM_ID, item.m_sEntryID); // id  = 1
                    intent.putExtra(ItemDetailRobot.m_sARG_ITEM_ID, item.m_sEntryID); // id  = 1
                }
                else if(item.toString() == "FitBit")
                {
                    intent.putExtra(ItemDetailBase.m_sARG_ITEM_ID, item.m_sEntryID); // id  = 2
                    intent.putExtra(ItemDetailWatch.m_sARG_ITEM_ID, item.m_sEntryID); // id = 2
                }
                else if(item.toString() == "Alexa")
                {
                    intent.putExtra(ItemDetailBase.m_sARG_ITEM_ID, item.m_sEntryID); // id  = 3
                    intent.putExtra(ItemDetailAlexa.m_sARG_ITEM_ID, item.m_sEntryID); // id = 3
                }
                else if(item.toString() == "Vector")
                {
                    intent.putExtra(ItemDetailBase.m_sARG_ITEM_ID, item.m_sEntryID); // id  = 4
                    intent.putExtra(ItemDetailVector.m_sARG_ITEM_ID, item.m_sEntryID); // id = 4
                }
                else if(item.toString() == "Kontakte")
                {
                    intent.putExtra(ItemDetailBase.m_sARG_ITEM_ID, item.m_sEntryID); // id  = 5
                    intent.putExtra(ItemDetailContacts.m_sARG_ITEM_ID, item.m_sEntryID); // id = 5
                }
                else
                {
                    Log.d(m_sLogTag, "Internal Error: invalid option selected");
                }

                context.startActivity(intent);
            }
        };

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.main_itemlist_content, parent, false);

            mDrawableIcon_QBO = view.getResources().getDrawable(R.drawable.icon_qbo);
            mDrawableIcon_FitBit = view.getResources().getDrawable(R.drawable.icon_fitbit);
            mDrawableIcon_Alexa = view.getResources().getDrawable(R.drawable.icon_alexa);
            mDrawableIcon_Vector = view.getResources().getDrawable(R.drawable.icon_vector);
            mDrawableIcon_Contacts = view.getResources().getDrawable(R.drawable.icon_contacts);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {

            if(position == 0) {
                holder.mConnectionView.setImageDrawable(mDrawableIcon_QBO);
            }
            else if(position == 1) {
                holder.mConnectionView.setImageDrawable(mDrawableIcon_FitBit);
            }
            else if(position == 2) {
                holder.mConnectionView.setImageDrawable(mDrawableIcon_Alexa);
            }
            else if(position == 3) {
                holder.mConnectionView.setImageDrawable(mDrawableIcon_Vector);
            }
            else if(position == 4) {
                holder.mConnectionView.setImageDrawable(mDrawableIcon_Contacts);
            }

            holder.mIdView.setText(m_listValues.get(position).m_sEntryID);
            holder.mTitleView.setText(m_listValues.get(position).m_sEntryTitle);

            holder.itemView.setTag(m_listValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return m_listValues.size();
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
