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

import at.srfg.robogen.ItemDetail.ItemDetailNutrition;
import at.srfg.robogen.RoboGen_Constants;
import at.srfg.robogen.itemcontent.ItemContent;
import at.srfg.robogen.R;
import at.srfg.robogen.itemdetail.ItemDetailAlexa;
import at.srfg.robogen.itemdetail.ItemDetailBase;
import at.srfg.robogen.itemdetail.ItemDetailContacts;
import at.srfg.robogen.itemdetail.ItemDetailCalendar;
import at.srfg.robogen.itemdetail.ItemDetailRobot;
import at.srfg.robogen.itemdetail.ItemDetailWatch;
import at.srfg.robogen.itemdetail.ItemDetailVector;
import at.srfg.robogen.itemdetail.ItemDetailSettings;
import at.srfg.robogen.itemdetail.ItemDetailWV;

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
        public Drawable mDrawableIcon_Calendar = null;
        public Drawable mDrawableIcon_Nutrition = null;
        public Drawable mDrawableIcon_Settings = null;
        public Drawable mDrawableIcon_WV = null;

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
                intent.putExtra(ItemDetailBase.m_sARG_ITEM_ID, item.m_sEntryID); // id  = 1

                if(item.toString() == RoboGen_Constants.ItemListEntry_QBO) {
                    intent.putExtra(ItemDetailRobot.m_sARG_ITEM_ID, item.m_sEntryID); // id  = 1
                }
                else if(item.toString() == RoboGen_Constants.ItemListEntry_FitBit)
                {
                    intent.putExtra(ItemDetailWatch.m_sARG_ITEM_ID, item.m_sEntryID); // id = 2
                }
                else if(item.toString() == RoboGen_Constants.ItemListEntry_Alexa)
                {
                    intent.putExtra(ItemDetailAlexa.m_sARG_ITEM_ID, item.m_sEntryID); // id = 3
                }
                else if(item.toString() == RoboGen_Constants.ItemListEntry_Vector)
                {
                    intent.putExtra(ItemDetailVector.m_sARG_ITEM_ID, item.m_sEntryID); // id = 4
                }
                else if(item.toString() == RoboGen_Constants.ItemListEntry_Contacts)
                {
                    intent.putExtra(ItemDetailContacts.m_sARG_ITEM_ID, item.m_sEntryID); // id = 5
                }
                else if(item.toString() == RoboGen_Constants.ItemListEntry_Calendar)
                {
                    intent.putExtra(ItemDetailCalendar.m_sARG_ITEM_ID, item.m_sEntryID); // id = 6
                }
                else if(item.toString() == RoboGen_Constants.ItemListEntry_Nutrition)
                {
                    intent.putExtra(ItemDetailNutrition.m_sARG_ITEM_ID, item.m_sEntryID); // id = 7
                }
                else if(item.toString() == RoboGen_Constants.ItemListEntry_Settings)
                {
                    intent.putExtra(ItemDetailSettings.m_sARG_ITEM_ID, item.m_sEntryID); // id = 8
                }
                else if(item.toString() == RoboGen_Constants.ItemListEntry_WV)
                {
                    intent.putExtra(ItemDetailWV.m_sARG_ITEM_ID, item.m_sEntryID); // id = 9
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
            mDrawableIcon_Calendar = view.getResources().getDrawable(R.drawable.icon_calendar);
            mDrawableIcon_Nutrition = view.getResources().getDrawable(R.drawable.icon_nutrition);
            mDrawableIcon_Settings = view.getResources().getDrawable(R.drawable.icon_settings);
            mDrawableIcon_WV = view.getResources().getDrawable(R.drawable.arrow_right);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {

            /*
            if(position == RoboGen_Constants.ItemListPos_QBO) {
                holder.mConnectionView.setImageDrawable(mDrawableIcon_QBO);
            }
            else if(position == RoboGen_Constants.ItemListPos_FitBit) {
                holder.mConnectionView.setImageDrawable(mDrawableIcon_FitBit);
            }
            else if(position == RoboGen_Constants.ItemListPos_Alexa) {
                holder.mConnectionView.setImageDrawable(mDrawableIcon_Alexa);
            }
            else if(position == RoboGen_Constants.ItemListPos_Vector) {
                holder.mConnectionView.setImageDrawable(mDrawableIcon_Vector);
            }
            else if(position == RoboGen_Constants.ItemListPos_Contacts) {
                holder.mConnectionView.setImageDrawable(mDrawableIcon_Contacts);
            }
            else if(position == RoboGen_Constants.ItemListPos_Calendar) {
                holder.mConnectionView.setImageDrawable(mDrawableIcon_Calendar);
            }
            else if(position == RoboGen_Constants.ItemListPos_Nutrition) {
                holder.mConnectionView.setImageDrawable(mDrawableIcon_Nutrition);
            }
            else if(position == RoboGen_Constants.ItemListPos_Settings) {
                holder.mConnectionView.setImageDrawable(mDrawableIcon_Settings);
            }
            else if(position == RoboGen_Constants.ItemListPos_EB) {
                holder.mConnectionView.setImageDrawable(mDrawableIcon_EB);
            }
            */

            //holder.mIdView.setText(m_listValues.get(position).m_sEntryID);
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
