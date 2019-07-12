package at.srfg.robogen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import at.srfg.robogen.ItemDetail.ItemDetailBase;
import at.srfg.robogen.ItemDetail.ItemDetailRobot;
import at.srfg.robogen.ItemDetail.ItemDetailWatch;
import at.srfg.robogen.ItemDetail.ItemDetailAlexa;

/*******************************************************************************
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 ******************************************************************************/
public class ItemListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        //if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
        //}

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

        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ItemContent.ItemEntry item = (ItemContent.ItemEntry) view.getTag();

                Context context = view.getContext();
                Intent intent = new Intent(context, ItemDetailActivity.class);

                if(item.toString() == "Hugo") {
                    intent.putExtra(ItemDetailBase.ARG_ITEM_ID, item.id); // id  = 1
                    intent.putExtra(ItemDetailRobot.ARG_ITEM_ID, item.id); // id  = 1
                }
                else if(item.toString() == "Uhren")
                {
                    intent.putExtra(ItemDetailBase.ARG_ITEM_ID, item.id); // id  = 2
                    intent.putExtra(ItemDetailWatch.ARG_ITEM_ID, item.id); // id = 2
                }
                else if(item.toString() == "Alexa")
                {
                    intent.putExtra(ItemDetailBase.ARG_ITEM_ID, item.id); // id  = 3
                    intent.putExtra(ItemDetailAlexa.ARG_ITEM_ID, item.id); // id = 3
                }
                else
                {
                    // bad
                }

                context.startActivity(intent);
            }
        };

        SimpleItemRecyclerViewAdapter(ItemListActivity parent,
                                      List<ItemContent.ItemEntry> items) {
            mValues = items;
            mParentActivity = parent;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mIdView.setText(mValues.get(position).id);
            holder.mContentView.setText(mValues.get(position).content);

            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;
            final TextView mContentView;

            ViewHolder(View view) {
                super(view);
                mIdView = (TextView) view.findViewById(R.id.id_text);
                mContentView = (TextView) view.findViewById(R.id.content);
            }
        }
    }
}
