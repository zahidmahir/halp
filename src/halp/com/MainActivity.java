package halp.com;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

@TargetApi(10)
public class MainActivity extends Activity {

	//use for wifi
	/*ConnectivityManager connectivity;
	NetworkInfo wifiInfo, mobileInfo;
	WifiManager wifiManager;*/
	TextView fuckLogCat;
	BluetoothAdapter adapter;
	ListenerThread listener;
	
	ArrayList<BluetoothSocket> nodes = new ArrayList<BluetoothSocket>();
	// Debugging
    private static final String TAG = "BluetoothChatService";
    private static final boolean D = true;
	
    private final UUID MY_UUID = UUID.randomUUID();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fuckLogCat = (TextView) findViewById(R.id.tvLog);
        
        // Get local Bluetooth adapter
        adapter = BluetoothAdapter.getDefaultAdapter();
                
        // If the adapter is null, then Bluetooth is not supported
        if (adapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        
        adapter.enable();
        adapter.setName("halp");
        adapter.startDiscovery();
        
        listener = new ListenerThread();
        listener.start();
        
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
    
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);
        startActivity(discoverableIntent);
    }

    // Create a BroadcastReceiver for ACTION_FOUND
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
    	public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // Add the name and address to an array adapter to show in a ListView
                //mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                
                Log.d(TAG, "I see " + device.getName());
                
                if(device.getName().equals("halp")){
                	ConnectThread connector = new ConnectThread(device);
                	connector.start();                    	
                }
            }
        }
    };
    
    protected void onDestory() {
    	super.onDestroy();
    	
    	this.unregisterReceiver(mReceiver);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    /**
     * This thread runs while listening for incoming connections. It behaves
     * like a server-side client. It runs until a connection is accepted
     * (or until cancelled).
     */
    private class ListenerThread extends Thread {
        // The local server socket
        private final BluetoothServerSocket serverSocket;
        public ListenerThread() {
            BluetoothServerSocket tmp = null;

            // Create a new listening server socket
            try {
                tmp = adapter.listenUsingInsecureRfcommWithServiceRecord(
                		"BluetoothChatInsecure", MY_UUID);
            
            } catch (IOException e) {
                Log.e(TAG, "listen() failed", e);
            }
            serverSocket = tmp;
        }

        public void run() {
            if (D) Log.d(TAG, "BEGIN mAcceptThread" + this);
            setName("AcceptThread");

            BluetoothSocket socket = null;

            // Listen to the server socket if we're not connected
            while (true) {
                try {
                    // This is a blocking call and will only return on a
                    // successful connection or an exception
                    socket = serverSocket.accept();
                } catch (IOException e) {
                    Log.e(TAG, "accept() failed", e);
                    break;
                }

                // If a connection was accepted
                if (socket != null) {
                    // Situation normal. Start the connected thread.

                	//TODO add the socket somewhere here
                	nodes.add(socket);
                    Log.d(TAG, "Faggot alert, watch your dick, pussy");
                }
            }
            if (D) Log.i(TAG, "END mAcceptThread");

        }

        public void cancel() {
            if (D) Log.d(TAG, "cancel " + this);
            try {
                serverSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "close() of server failed", e);
            }
        }
    }
    
    private class ConnectThread extends Thread {
        private final BluetoothSocket socket;
        private final BluetoothDevice mmDevice;
     
        public ConnectThread(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket,
            // because mmSocket is final
            BluetoothSocket tmp = null;
            mmDevice = device;
            Log.d(TAG, "Im in the Connect thread constructor");
            // Get a BluetoothSocket to connect with the given BluetoothDevice
            try {
                // MY_UUID is the app's UUID string, also used by the server code
                tmp = device.createInsecureRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) { }
            socket = tmp;
        }
     
        public void run() {
        	
     
            try {
                // Connect the device through the socket. This will block
                // until it succeeds or throws an exception
            	socket.connect();
            } catch (IOException connectException) {
                // Unable to connect; close the socket and get out
                try {
                	socket.close();
                } catch (IOException closeException) { }
                return;
            }
     
            nodes.add(socket);
            Log.d(TAG, "FAGGOT BITCH TITS AWAY");
        }
     
        /** Will cancel an in-progress connection, and close the socket */
        public void cancel() {
            try {
                socket.close();
            } catch (IOException e) { }
        }
    }
    
    
}
