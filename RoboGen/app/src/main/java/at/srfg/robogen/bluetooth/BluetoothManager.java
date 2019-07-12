package at.srfg.robogen.bluetooth;

import at.srfg.robogen.R;

import android.app.AlertDialog;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

/*******************************************************************************
 * This class manages bluetooth search for devices and
 * handling serial devices
 ******************************************************************************/
public class BluetoothManager {

    private static final String QBO_MAC_ADDRESS = "B8:27:EB:8E:B5:18";

    // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    private String mConnectedDeviceName = null;

    // Message types sent from the BluetoothReadService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    // Members for communication, pairing and scanning
    private DeviceListActivity mDeviceListActivity = null;
	private BluetoothAdapter mBluetoothAdapter = null;
    private static BluetoothSerialService mSerialService = null;

    // Additional member helpers
    private boolean mLocalEcho = false;
    private Activity mParentActivity = null;
    private Context mParentContext = null;
    private boolean mHasExtraPermissions = false;

    /**
     * logging and debugging
     */
    public static final String LOG_TAG = "BluetoothManager";


    /*******************************************************************************
     * CTOR init with Activity and Context information of caller/user class
     ******************************************************************************/
    public BluetoothManager(Activity act, Context ct)
    {
        mParentActivity = act;
        mParentContext = ct;
        mDeviceListActivity = new DeviceListActivity();

        mSerialService = new BluetoothSerialService(mParentContext, mHandlerBT);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter == null) {
            finishDialogNoBluetooth();
            return;
        }
    }

    /*******************************************************************************
     * on scan result of bluetooth search
     ******************************************************************************/
    public void onScanResult(int requestCode, int resultCode, Intent data) {
        Log.d(LOG_TAG, "onActivityResult " + resultCode);
        switch (requestCode) {

            case REQUEST_CONNECT_DEVICE:

                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {

                    // Get the device MAC address
                    String address = data.getExtras()
                            .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);

                    // Get the BluetoothDevice object
                    BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);

                    // Attempt to connect to the device
                    mSerialService.connect(device);
                }
                break;

            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    Log.d(LOG_TAG, "BT not enabled");

                    finishDialogNoBluetooth();
                }
        }
    }

    /*******************************************************************************
     * establish a connection or search for devices
     ******************************************************************************/
    public void doConnect()
    {
        if (getConnectionState() == BluetoothSerialService.STATE_NONE) {

            // Launch the DeviceListActivity to see devices and do scan
            Intent serverIntent = new Intent(mParentContext, DeviceListActivity.class);
            mParentActivity.startActivityForResult(serverIntent, 1);
        }
        else {
            if (getConnectionState() == BluetoothSerialService.STATE_CONNECTED) {
                mSerialService.stop();
                mSerialService.start();
            }
        }
    }

    /*******************************************************************************
     * for bluetooth we need coarse location access
     ******************************************************************************/
    public void RequestExtraPermissionsForBluetooth(Activity callerActivity)
    {
        if(!mHasExtraPermissions) {
            int PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;
            ActivityCompat.requestPermissions(callerActivity,
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);

            mHasExtraPermissions = true;
        }
    }

    /*******************************************************************************
     * show finishing dialog if no bluetooth is enabled on device
     ******************************************************************************/
    public void finishDialogNoBluetooth() {
         AlertDialog.Builder builder = new AlertDialog.Builder(mParentContext);
         builder.setMessage(R.string.alert_dialog_no_bt)
         .setIcon(android.R.drawable.ic_dialog_info)
         .setTitle(R.string.app_name)
         .setCancelable( false )
         .setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //finish();
                 	   }
                });
         AlertDialog alert = builder.create();
         alert.show();
    }

    /*******************************************************************************
     * get state of connection to device
     ******************************************************************************/
	public int getConnectionState() {
	    return mSerialService.getState();
	}

    /*******************************************************************************
     * send bytes to device
     ******************************************************************************/
    public void send(byte[] out) {
    	mSerialService.write( out );
    }


    /*******************************************************************************
     * The Handler that gets information back from the BluetoothService
     ******************************************************************************/
    private final Handler mHandlerBT = new Handler() {
    	
        @Override
        public void handleMessage(Message msg) {        	
            switch (msg.what) {
            case MESSAGE_STATE_CHANGE:

                Log.i(LOG_TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);

                switch (msg.arg1) {
                case BluetoothSerialService.STATE_CONNECTED:
                	
                	//mInputManager.showSoftInput(mEmulatorView, InputMethodManager.SHOW_IMPLICIT); // TODO: still needed?
                	
                    //mTitle.setText(R.string.title_connected_to);
                    //mTitle.append(mConnectedDeviceName);
                    break;
                    
                case BluetoothSerialService.STATE_CONNECTING:
                    //mTitle.setText(R.string.title_connecting);
                    break;
                    
                case BluetoothSerialService.STATE_LISTEN:
                case BluetoothSerialService.STATE_NONE:
            		//mInputManager.hideSoftInputFromWindow(mEmulatorView.getWindowToken(), 0); // TODO: still needed?
                    //mTitle.setText(R.string.title_not_connected);
                    break;
                }
                break;
            case MESSAGE_WRITE:
            	if (mLocalEcho) {
            		//byte[] writeBuf = (byte[]) msg.obj;  // TODO: still needed?
            		//mEmulatorView.write(writeBuf, msg.arg1);
            	}
                break;
/*                
            case MESSAGE_READ:
                byte[] readBuf = (byte[]) msg.obj;              
                mEmulatorView.write(readBuf, msg.arg1);
                
                break;
*/                
            case MESSAGE_DEVICE_NAME:
                // save the connected device's name
                mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                Toast.makeText(mParentContext, "Connected to "
                               + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                break;
            case MESSAGE_TOAST:
                Toast.makeText(mParentContext, msg.getData().getString(TOAST),
                               Toast.LENGTH_SHORT).show();
                break;
            }
        }
    };    

    

}