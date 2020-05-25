package at.srfg.robogen;

import android.app.Activity;
import android.view.View;

import at.srfg.robogen.alexa.AlexaManager;
import at.srfg.robogen.bluetooth.BluetoothManager;
import at.srfg.robogen.fitnesswatch.FitBitManager;
import at.srfg.robogen.contacts.ContactManager;

/*******************************************************************************
 * The AI_Center class is responsible for all combinatoric logic
 * which needs multiple sources of data to work
 * It serves as the main connector in the app bringing together data
 * from all robots, smart watches and Alexa Skills
 ******************************************************************************/
public class RoboGen_Manager {

    private AlexaManager m_cAlexaManager = null;
    private BluetoothManager m_cBluetoothManager = null;
    private FitBitManager m_cWatchManager = null;
    private ContactManager m_cContactManager = null;


    /*******************************************************************************
     * Getter
     ******************************************************************************/
    public AlexaManager getAlexaManager() {
        return m_cAlexaManager;
    }
    public BluetoothManager getBluetoothManager() {
        return m_cBluetoothManager;
    }
    public FitBitManager getWatchManager() {
        return m_cWatchManager;
    }
    public ContactManager getCallManager() {
        return m_cContactManager;
    }


    /*******************************************************************************
     * Alexa
     ******************************************************************************/
    public void Alexa_Init(Activity act)
    {
        m_cAlexaManager = new AlexaManager(act, act.getBaseContext());
    }

    public void Alexa_SkillInvocation()
    {
        m_cAlexaManager.InitAlexaSkillInvocation();
    }


    /*******************************************************************************
     * QBO Bluetooth
     ******************************************************************************/
    public void BlueTooth_Init(Activity act, final View rootView)
    {
        m_cBluetoothManager = new BluetoothManager(act, act.getBaseContext(), rootView);
        m_cBluetoothManager.RequestExtraPermissionsForBluetooth(act);
    }

    public void BlueTooth_Connect()
    {
        m_cBluetoothManager.doConnect();
    }

    public void BlueTooth_Send(byte data)
    {
        byte[] buffer = new byte[1];
        buffer[0] = data;
        m_cBluetoothManager.send(buffer);
    }


    /*******************************************************************************
     * FitBit
     ******************************************************************************/
    public void FitBit_Init(Activity act)
    {
        m_cWatchManager = new FitBitManager(act, act.getBaseContext());
    }

    public void FitBit_Login(final View rootView)
    {
        m_cWatchManager.logIn(rootView);
    }

    public void FitBit_StartDataStream()
    {
        m_cWatchManager.startUserDataStream();
    }


    /*******************************************************************************
     * Contacts
     ******************************************************************************/
    public void Contacts_Init(Activity act, View listView)
    {
        m_cContactManager = new ContactManager(act, listView);
    }

    public void Contacts_SendSMS()
    {
        // For Testing: Test Phone of Armin
        m_cContactManager.messageEmergencyContact("06781287128", "Test-Telefon Armin");
    }


    /*******************************************************************************
     * Analyser Functions (TODO)
     ******************************************************************************/
    //public void AnalyseSleep(int sleepVal) // triggered by Fitbit
    //{
    //    if(sleepVal < 70)
    //    {
    //        //Alexa_SkillInvocation("1");
    //    }
    //}

    //public void AnalyseHeart(int heartVal) // triggered by Fitbit
    //{
    //    if(heartVal < 70)
    //    {
    //        //Alexa_SkillInvocation("2");
    //    }
    //}

    //public void AnalyseEmotion(String emotionVal) // triggered by QBO
    //{
    //    if(emotionVal == "traurig")
    //    {
    //        // TODO: check for FitBit data and do something if bad

    //        //Alexa_SkillInvocation("3");
    //    }
    //}
}