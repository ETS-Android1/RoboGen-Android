/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package at.srfg.robogen.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelUuid;
import android.util.Log;

import at.srfg.robogen.R;

/*******************************************************************************
 * This class does all the work for setting up and managing Bluetooth
 * connections with other devices. It has a thread that listens for
 * incoming connections, a thread for connecting with a device, and a
 * thread for performing data transmissions when connected.
 ******************************************************************************/
public class BluetoothSerialService {

    // Debugging
    private static final String TAG = "BluetoothReadService";
    private static final boolean D = true;

    // old static UUID -> use this if dynamic UUID from ParcelUuid array is not working
	//private static final UUID DefaultPortServiceClass_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // Member fields
    private final BluetoothAdapter m_cAdapter;
    private final Handler m_cHandler;
    private ConnectThread m_cConnectThread;
    private ConnectedThread m_cConnectedThread;
    private int m_iState;
    private Context m_ctxContext;

    // Constants that indicate the current connection state
    public static final int STATE_NONE = 0;       // we're doing nothing
    public static final int STATE_LISTEN = 1;     // now listening for incoming connections
    public static final int STATE_CONNECTING = 2; // now initiating an outgoing connection
    public static final int STATE_CONNECTED = 3;  // now connected to a remote device

    /*******************************************************************************
     * Constructor. Prepares a new BluetoothChat session.
     * @param context  The UI Activity Context
     * @param handler  A Handler to send messages back to the UI Activity
     ******************************************************************************/
    public BluetoothSerialService(Context context, Handler handler) {
        m_cAdapter = BluetoothAdapter.getDefaultAdapter();
        m_iState = STATE_NONE;
        m_cHandler = handler;
        m_ctxContext = context;
    }

    /*******************************************************************************
     * Set the current state of the chat connection
     * @param state  An integer defining the current connection state
     ******************************************************************************/
    private synchronized void setState(int state) {
        if (D) Log.d(TAG, "setState() " + m_iState + " -> " + state);
        m_iState = state;

        // Give the new state to the Handler so the UI Activity can update
        m_cHandler.obtainMessage(BluetoothManager.MESSAGE_STATE_CHANGE, state, -1).sendToTarget();
    }

    /*******************************************************************************
     * Return the current connection state.
     *******************************************************************************/
    public synchronized int getState() {
        return m_iState;
    }

    /*******************************************************************************
     * Start the chat service. Specifically start AcceptThread to begin a
     * session in listening (server) mode. Called by the Activity onResume()
     *******************************************************************************/
    public synchronized void start() {
        if (D) Log.d(TAG, "start");

        // Cancel any thread attempting to make a connection
        if (m_cConnectThread != null) {
            m_cConnectThread.cancel();
            m_cConnectThread = null;
        }

        // Cancel any thread currently running a connection
        if (m_cConnectedThread != null) {
            m_cConnectedThread.cancel();
            m_cConnectedThread = null;
        }

        setState(STATE_NONE);
    }

    /*******************************************************************************
     * Start the ConnectThread to initiate a connection to a remote device.
     * @param device  The BluetoothDevice to connect
     ******************************************************************************/
    public synchronized void connect(BluetoothDevice device) {
        if (D) Log.d(TAG, "connect to: " + device);

        // Cancel any thread attempting to make a connection
        if (m_iState == STATE_CONNECTING) {
            if (m_cConnectThread != null) {m_cConnectThread.cancel(); m_cConnectThread = null;}
        }

        // Cancel any thread currently running a connection
        if (m_cConnectedThread != null) {m_cConnectedThread.cancel(); m_cConnectedThread = null;}

        // Start the thread to connect with the given device
        m_cConnectThread = new ConnectThread(device);
        m_cConnectThread.start();
        setState(STATE_CONNECTING);
    }

    /*******************************************************************************
     * Start the ConnectedThread to begin managing a Bluetooth connection
     * @param socket  The BluetoothSocket on which the connection was made
     * @param device  The BluetoothDevice that has been connected
     ******************************************************************************/
    public synchronized void connected(BluetoothSocket socket, BluetoothDevice device) {
        if (D) Log.d(TAG, "connected");

        // Cancel the thread that completed the connection
        if (m_cConnectThread != null) {
            m_cConnectThread.cancel();
            m_cConnectThread = null;
        }

        // Cancel any thread currently running a connection
        if (m_cConnectedThread != null) {
            m_cConnectedThread.cancel();
            m_cConnectedThread = null;
        }

        // Start the thread to manage the connection and perform transmissions
        m_cConnectedThread = new ConnectedThread(socket);
        m_cConnectedThread.start();

        // Send the name of the connected device back to the UI Activity
        Message msg = m_cHandler.obtainMessage(BluetoothManager.MESSAGE_DEVICE_NAME);
        Bundle bundle = new Bundle();
        bundle.putString(BluetoothManager.DEVICE_NAME, device.getName());
        msg.setData(bundle);
        m_cHandler.sendMessage(msg);

        setState(STATE_CONNECTED);
    }

    /*******************************************************************************
     * Stop all threads
     ******************************************************************************/
    public synchronized void stop() {
        if (D) Log.d(TAG, "stop");

        if (m_cConnectThread != null) {
            m_cConnectThread.cancel();
            m_cConnectThread = null;
        }

        if (m_cConnectedThread != null) {
            m_cConnectedThread.cancel();
            m_cConnectedThread = null;
        }

        setState(STATE_NONE);
    }

    /*******************************************************************************
     * Write to the ConnectedThread in an unsynchronized manner
     * @param out The bytes to write
     * @see ConnectedThread#write(byte[])
     ******************************************************************************/
    public void write(byte[] out) {
        // Create temporary object
        ConnectedThread r;
        // Synchronize a copy of the ConnectedThread
        synchronized (this) {
            if (m_iState != STATE_CONNECTED) return;
            r = m_cConnectedThread;
        }
        // Perform the write unsynchronized
        r.write(out);
    }
    
    /*******************************************************************************
     * Indicate that the connection attempt failed and notify the UI Activity.
     ******************************************************************************/
    private void connectionFailed() {
        setState(STATE_NONE);

        // Send a failure message back to the Activity
        Message msg = m_cHandler.obtainMessage(BluetoothManager.MESSAGE_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString(BluetoothManager.TOAST,
                         m_ctxContext.getResources().getText(R.string.device_connection_failed).toString());
        msg.setData(bundle);
        m_cHandler.sendMessage(msg);
    }

    /*******************************************************************************
     * Indicate that the connection was lost and notify the UI Activity.
     ******************************************************************************/
    private void connectionLost() {
        setState(STATE_NONE);

        // Send a failure message back to the Activity
        Message msg = m_cHandler.obtainMessage(BluetoothManager.MESSAGE_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString(BluetoothManager.TOAST,
                         m_ctxContext.getResources().getText(R.string.device_connection_lost).toString());
        msg.setData(bundle);
        m_cHandler.sendMessage(msg);
    }

    /*******************************************************************************
     * This thread runs while attempting to make an outgoing connection
     * with a device. It runs straight through; the connection either
     * succeeds or fails.
     ******************************************************************************/
    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            mmDevice = device;
            BluetoothSocket tmp = null;

            // Get a BluetoothSocket for a connection with the given BluetoothDevice
            // (use static member UUID in case of any socket related problems)
            try {
                final ParcelUuid[] uuidArray = device.getUuids();
                tmp = device.createRfcommSocketToServiceRecord(uuidArray[0].getUuid());

            } catch (IOException e) {
                Log.e(TAG, "create() failed", e);
            }
            mmSocket = tmp;
        }

        public void run() {
            Log.i(TAG, "BEGIN mConnectThread");
            setName("ConnectThread");

            // Always cancel discovery because it will slow down a connection
            m_cAdapter.cancelDiscovery();

            // Make a connection to the BluetoothSocket
            try {
                // blocking call that will only return on a successful connection or an exception
                mmSocket.connect();
            } catch (IOException e) {
                connectionFailed();

                // Close the socket
                try {
                    mmSocket.close();
                } catch (IOException e2) {
                    Log.e(TAG, "unable to close() socket during connection failure", e2);
                }

                // Start the service over to restart listening mode
                // BluetoothSerialService.this.start();
                return;
            }

            // Reset the ConnectThread because we're done
            synchronized (BluetoothSerialService.this) {
                m_cConnectThread = null;
            }

            // Start the connected thread
            connected(mmSocket, mmDevice);
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "close() of connect socket failed", e);
            }
        }
    }

    /*******************************************************************************
     * This thread runs during a connection with a remote device.
     * It handles all incoming and outgoing transmissions.
     ******************************************************************************/
    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        

        public ConnectedThread(BluetoothSocket socket) {
            Log.d(TAG, "create ConnectedThread");
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the BluetoothSocket input and output streams
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "temp sockets not created", e);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            Log.i(TAG, "BEGIN mConnectedThread");
            byte[] buffer = new byte[1024];
            int bytes;

            // Keep listening to the InputStream while connected
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);

                    if (D) Log.d(TAG, buffer.toString());

                    // Send the obtained bytes to the UI Activity
                    m_cHandler.obtainMessage(BluetoothManager.MESSAGE_READ, bytes, -1, buffer).sendToTarget(); // TODO: needed?

                } catch (IOException e) {
                    Log.e(TAG, "disconnected", e);
                    connectionLost();
                    break;
                }
            }
        }

        /*******************************************************************************
         * Write to the connected OutStream.
         * @param buffer  The bytes to write
         ******************************************************************************/
        public void write(byte[] buffer) {
            try {
                mmOutStream.write(buffer);

                // Share the sent message back to the UI Activity
                m_cHandler.obtainMessage(BluetoothManager.MESSAGE_WRITE, buffer.length, -1, buffer).sendToTarget();
            } catch (IOException e) {
                Log.e(TAG, "Exception during write", e);
            }
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "close() of connect socket failed", e);
            }
        }
    }
}
