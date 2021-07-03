package at.srfg.robogen.itemcontent;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.srfg.robogen.RoboGen_Constants;

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
        //addItem(new ItemEntry(RoboGen_Constants.ItemListID_QBO, RoboGen_Constants.ItemListEntry_QBO, "Management-Seite für Q.bo One - Roboter"));
        //addItem(new ItemEntry(RoboGen_Constants.ItemListID_FitBit, RoboGen_Constants.ItemListEntry_FitBit, "Management-Seite für Fitbit Charge 2 - Smart Watches"));
        //addItem(new ItemEntry(RoboGen_Constants.ItemListID_Alexa, RoboGen_Constants.ItemListEntry_Alexa, "Management-Seite für Alexa Entscheidungsbäume"));
        //addItem(new ItemEntry(RoboGen_Constants.ItemListID_Vector, RoboGen_Constants.ItemListEntry_Vector, "Management-Seite für Anki Vector Roboter"));
        //addItem(new ItemEntry(RoboGen_Constants.ItemListID_Contacts, RoboGen_Constants.ItemListEntry_Contacts, "Management-Seite für Notfallkontakte"));
        //addItem(new ItemEntry(RoboGen_Constants.ItemListID_Calendar, RoboGen_Constants.ItemListEntry_Calendar, "Management-Seite für Kalender und Erinnerungen"));
        //addItem(new ItemEntry(RoboGen_Constants.ItemListID_Nutrition, RoboGen_Constants.ItemListEntry_Nutrition, "Management-Seite für Ernährungstagebuch"));
        addItem(new ItemEntry(RoboGen_Constants.ItemListID_WV, RoboGen_Constants.ItemListEntry_WV,  "Management-Seite für WebView"));
        //addItem(new ItemEntry(RoboGen_Constants.ItemListID_Settings, RoboGen_Constants.ItemListEntry_Settings, "Einstellungen und persönliche Daten"));
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

        public ItemEntry(String id, String title, String header) {
            this.m_sEntryID = id;
            this.m_sEntryTitle = title;
            this.m_sEntryHeader = header;
        }

        @Override
        public String toString() {
            return m_sEntryTitle;
        }
    }
}