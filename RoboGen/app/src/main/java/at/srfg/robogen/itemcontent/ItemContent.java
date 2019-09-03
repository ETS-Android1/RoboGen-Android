package at.srfg.robogen.itemcontent;


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
    public static final List<ItemEntry> m_listItems = new ArrayList<ItemEntry>();
    public static final Map<String, ItemEntry> m_mapItems = new HashMap<String, ItemEntry>();

    static {
        // Add some sample items.
        addItem(new ItemEntry("1.", "Q.bo", "Management-Seite für Q.bo One - Roboter", false));
        addItem(new ItemEntry("2.", "FitBit", "Management-Seite für Fitbit Charge 2 - Smart Watches", false));
        addItem(new ItemEntry("3.", "Alexa", "Management-Seite für Alexa Entscheidungsbäume", false));
        addItem(new ItemEntry("4.", "Vector", "Management-Seite für Anki Vector Roboter", false));
    }

    private static void addItem(ItemEntry item) {
        m_listItems.add(item);
        m_mapItems.put(item.m_sEntryID, item);
    }

    /*******************************************************************************
     * An item representing a piece of content.
     ******************************************************************************/
    public static class ItemEntry {
        public final String m_sEntryID;
        public final String m_sEntryTitle;
        public final String m_sEntryHeader;
        public boolean m_bEntryIsConnected;

        public ItemEntry(String id, String title, String header, boolean connected) {
            this.m_sEntryID = id;
            this.m_sEntryTitle = title;
            this.m_sEntryHeader = header;
            this.m_bEntryIsConnected = connected;
        }

        @Override
        public String toString() {
            return m_sEntryTitle;
        }
    }
}