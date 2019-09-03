package at.srfg.robogen;

import android.app.Activity;
import android.view.View;

import at.srfg.robogen.alexa.AlexaManager;
import at.srfg.robogen.bluetooth.BluetoothManager;
import at.srfg.robogen.fitnesswatch.FitBitManager;

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


    /*******************************************************************************
     * Alexa
     ******************************************************************************/
    public void InitAlexaManager(Activity act)
    {
        m_cAlexaManager = new AlexaManager(act, act.getBaseContext());
    }

    public void InitAlexaSkillInvocation()
    {
        m_cAlexaManager.InitAlexaSkillInvocation();
    }



    /*******************************************************************************
     * QBO Bluetooth
     ******************************************************************************/
    public void InitBlueToothManager(Activity act, final View rootView)
    {
        m_cBluetoothManager = new BluetoothManager(act, act.getBaseContext(), rootView);
        m_cBluetoothManager.RequestExtraPermissionsForBluetooth(act);
    }

    public void BlueToothConnect()
    {
        m_cBluetoothManager.doConnect();
    }

    public void BlueToothSend(byte data)
    {
        byte[] buffer = new byte[1];
        buffer[0] = data;
        m_cBluetoothManager.send(buffer);
    }



    /*******************************************************************************
     * FitBit
     ******************************************************************************/
    public void InitFitBitManager(Activity act)
    {
        m_cWatchManager = new FitBitManager(act, act.getBaseContext());
    }

    public void FitBitLogin(final View rootView)
    {
        m_cWatchManager.logIn(rootView);
    }

    public void FitBitStartDataStream()
    {
        m_cWatchManager.startUserDataStream();
    }
}