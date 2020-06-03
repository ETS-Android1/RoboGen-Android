package at.srfg.robogen.itemdetail;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

import at.srfg.robogen.itemactivity.ItemDetailActivity;
import at.srfg.robogen.itemactivity.ItemListActivity;
import at.srfg.robogen.R;
import at.srfg.robogen.RoboGen_App;
import at.srfg.robogen.bluetooth.BluetoothManager;

/*******************************************************************************
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 ******************************************************************************/
public class ItemDetailRobot extends ItemDetailBase {

    private RoboGen_App m_cRoboGenApp;

    private final String m_sConnectRobot = "Schritt 1) Verbindung aufbauen mit Q.Bo One via Bluetooth. " +
                                   "Das Gerät muss zuerst gekoppelt werden und dann verbunden. " +
                                    "Beides kann mit dem folgenden Schalter erledigt werden:";
    private final String m_sSendRobot = "Schritt 2) Nach einem erfolgreichen Verbindungsaufbau können Testdaten versendet werden. " +
                                   "Mit den folgenden Schaltern können Daten an den Roboter gesendet werden:";
    private final String m_sQuitRobot = "Schritt 3) Die Kommunikation kann mit dem folgenden Schalter beendet werden: ";

    public FloatingActionButton m_btnConnectRobot;
    public FloatingActionButton m_btnSendRobot_Code1;
    public FloatingActionButton m_btnSendRobot_Code2;
    public FloatingActionButton m_btnSendRobot_Code3;
    public FloatingActionButton m_btnSendRobot_Code4;
    public FloatingActionButton m_btnSendRobot_Quit;

    /*******************************************************************************
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     ******************************************************************************/
    public ItemDetailRobot() {}

    /*******************************************************************************
     * creating view for robot detail page
     ******************************************************************************/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.main_itemdetail_robot, container, false);

        if (mItem != null)
        {
            m_cRoboGenApp = ((RoboGen_App)getActivity().getApplication());
            initGUIComponents(rootView);
        }

        return rootView;
    }

    /*******************************************************************************
     * init GUI components
     ******************************************************************************/
    private void initGUIComponents(final View rootView)
    {
        // init text
        ((TextView) rootView.findViewById(R.id.item_detail_title)).setText(mItem.m_sEntryHeader);
        ((TextView) rootView.findViewById(R.id.item_detail_text_1)).setText(m_sConnectRobot);
        ((TextView) rootView.findViewById(R.id.item_detail_text_2)).setText(m_sSendRobot);
        ((TextView) rootView.findViewById(R.id.item_detail_text_3)).setText(m_sQuitRobot);

        // init bluetooth manager
        m_cRoboGenApp.getRoboGenManager().BlueTooth_Init(this.getActivity(), rootView);


        // init bluetooth buttons
        m_btnConnectRobot = (FloatingActionButton) rootView.findViewById(R.id.bt_search);
        m_btnConnectRobot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                makeSnackbarMessage(view, "Prüfung auf vorhandene Bluetooth-Verbindung..");
                m_cRoboGenApp.getRoboGenManager().BlueTooth_Connect();
            }
        });
        m_btnSendRobot_Code1 = (FloatingActionButton) rootView.findViewById(R.id.bt_send_code1);
        m_btnSendRobot_Code1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                SendSingleByteToDevice(view, (byte) 1);
            }
        });

        m_btnSendRobot_Code2 = (FloatingActionButton) rootView.findViewById(R.id.bt_send_code2);
        m_btnSendRobot_Code2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                SendSingleByteToDevice(view, (byte) 2);
            }
        });

        m_btnSendRobot_Code3 = (FloatingActionButton) rootView.findViewById(R.id.bt_send_code3);
        m_btnSendRobot_Code3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                SendSingleByteToDevice(view, (byte) 3);
            }
        });

        m_btnSendRobot_Code4 = (FloatingActionButton) rootView.findViewById(R.id.bt_send_code4);
        m_btnSendRobot_Code4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                //SendSingleByteToDevice(view, (byte) 4);
                SendSettingsFileToDevice(view);
            }
        });

        m_btnSendRobot_Quit = (FloatingActionButton) rootView.findViewById(R.id.bt_quit);
        m_btnSendRobot_Quit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                SendSingleByteToDevice(view, (byte) 'q');
            }
        });
    }

    /*******************************************************************************
     * sending data to the connected bluetooth device
     ******************************************************************************/
    private void SendSingleByteToDevice(View view, byte data)
    {
        makeSnackbarMessage(view, "Sende Daten-Kommando an Gerät: " + data);
        m_cRoboGenApp.getRoboGenManager().BlueTooth_SendSingleByte(data);
    }

    /*******************************************************************************
     * sending data to the connected bluetooth device
     ******************************************************************************/
    private void SendSettingsFileToDevice(View view)
    {
        try {
            InputStream is = view.getContext().openFileInput("settings.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            makeSnackbarMessage(view, "Sende Benutzer-Einstellungen an Gerät!");
            m_cRoboGenApp.getRoboGenManager().BlueTooth_SendMultipleBytes(buffer);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /*******************************************************************************
     * getter
     ******************************************************************************/
    public BluetoothManager getBluetoothManager() {
       return m_cRoboGenApp.getRoboGenManager().getBluetoothManager();
    }
}
