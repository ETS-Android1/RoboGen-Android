package at.srfg.robogen;

import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*******************************************************************************
 * Helper class for providing sample content for user interfaces created by
 ******************************************************************************/
public class ItemContent {

    /**
     * Members for Item creation
     */
    public static final List<ItemEntry> ITEMS = new ArrayList<ItemEntry>();
    public static final Map<String, ItemEntry> ITEM_MAP = new HashMap<String, ItemEntry>();

    static {
        // Add some sample items.
        addItem(new ItemEntry("1.", "Hugo", "Management-Seite für Q.Bo One - Roboter"));
        addItem(new ItemEntry("2.", "Uhren", "Management-Seite für Fitbit Charge 2 - Smart Watches"));
        addItem(new ItemEntry("3.", "Alexa", "Management-Seite für Alexa Entscheidungsbäume"));
    }

    private static void addItem(ItemEntry item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.mEntryID, item);
    }

    /*******************************************************************************
     * An item representing a piece of content.
     ******************************************************************************/
    public static class ItemEntry {
        //public ImageView mConnectionState;
        public final String mEntryID;
        public final String mEntryTitle;
        public final String mEntryHeader;

        public ItemEntry(String id, String title, String header) {
            this.mEntryID = id;
            this.mEntryTitle = title;
            this.mEntryHeader = header;
        }

        @Override
        public String toString() {
            return mEntryTitle;
        }
    }
}