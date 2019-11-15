package at.srfg.robogen.contacts;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import at.srfg.robogen.R;

/*******************************************************************************
 * Manager class for emergency contacts
 ******************************************************************************/
public class ContactManager {

    private Context m_ctxParent;
    private ArrayAdapter<String> m_arrAdapter;
    private ListView m_listView;

    /*******************************************************************************
     * CTOR
     ******************************************************************************/
    public ContactManager(Activity act, View listView)
    {
        m_ctxParent = act.getApplicationContext();

        m_arrAdapter = new ArrayAdapter<String>(m_ctxParent, R.layout.contacts_datalist_entry){
            public View getView(int position, View view, ViewGroup viewGroup)
            {
                View v = super.getView(position, view, viewGroup);
                ((TextView)v).setText(Html.fromHtml((String)this.getItem(position)));
                ((TextView)v).setMovementMethod(LinkMovementMethod.getInstance());
                ((TextView)v).setLinkTextColor(Color.parseColor("#5E8ACB")); // R.color.colorPrimary
                return v;
            }
        };

        addContactsToAdapter();

        m_listView = (ListView) listView.findViewById(R.id.contacts_datalist);
        m_listView.setAdapter(m_arrAdapter);
    }

    /*******************************************************************************
     * addContactsToAdapter
     ******************************************************************************/
    private void addContactsToAdapter() {

        ContentResolver cr = m_ctxParent.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if ((cur != null ? cur.getCount() : 0) > 0)
        {
            while (cur != null && cur.moveToNext())
            {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String hasStar = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.STARRED));

                if (hasStar.equals("1") &&
                    cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0)
                {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);

                    while (pCur.moveToNext())
                    {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));

                        m_arrAdapter.add(name + ": " + phoneNo);
                    }
                    pCur.close();
                }
            }
        }
        if(cur!=null){
            cur.close();
        }
    }

    /*******************************************************************************
     * callEmergencyContacts
     ******************************************************************************/
    public void callEmergencyContact(String phoneNumber)
    {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phoneNumber));
        m_ctxParent.startActivity(callIntent);
    }

    /*******************************************************************************
     * messageEmergencyContacts
     ******************************************************************************/
    public void messageEmergencyContact(String phoneNumber, String msg)
    {
        try
        {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, msg, null, null);
            Toast.makeText(m_ctxParent, "Message Sent", Toast.LENGTH_LONG).show();
        }
        catch (Exception ex)
        {
            Toast.makeText(m_ctxParent,ex.getMessage().toString(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }
}
