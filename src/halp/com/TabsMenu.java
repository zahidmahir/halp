package halp.com;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.TargetApi;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TabHost.TabSpec;

@TargetApi(10)
public class TabsMenu extends ListActivity implements OnClickListener {
	
	ArrayList<Conversation> conversations = new ArrayList<Conversation>();
	Conversation selectedConversation;
	
	
	// Intent request codes
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;

	// Debugging
    private static final String TAG = "Halp";
    private static final boolean D = true;

    
    // Layout Views
    private TextView mTitle;
    private ListView mConversationView;
    private EditText mOutEditText;
    private Button mSendButton;
    
    
    
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabsmenu);
        TabHost th = (TabHost) findViewById(R.id.tabhost);
        for(int i = 0; i < 10; i++){
        	Conversation c = new Conversation("Keith");
        	conversations.add(c);
        }
        ListView listView = (ListView) findViewById(android.R.id.list);
        listView.setAdapter(new ConversationListItemAdapter(this, R.layout.conversationlistitem, conversations));
        th.setup();
        TabSpec specs1 = th.newTabSpec("tag1");
        specs1.setContent(R.id.tabMessages);
        specs1.setIndicator("Messages");
        th.addTab(specs1);
        TabSpec specs2 = th.newTabSpec("tag2");
        specs2.setContent(R.id.tabNetwork);
        specs2.setIndicator("Network");
        th.addTab(specs2);
        TabSpec specs3 = th.newTabSpec("tag3");
        specs3.setContent(R.id.tabSettings);
        specs3.setIndicator("Settings");
        th.addTab(specs3);
        
        
        // Get local Bluetooth adapter
        ConnectionManager.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
      
        // If the adapter is null, then Bluetooth is not supported
        if (ConnectionManager.mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        
        ConnectionManager.mBluetoothAdapter.setName("halp");
        ensureDiscoverable();
        
        // Initialize array adapters. One for already paired devices and
        // one for newly discovered devices
        ConnectionManager.mPairedDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_name);
        ConnectionManager.mNewDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_name);
        
     // Register for broadcasts when a device is discovered
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(ConnectionManager.mReceiver, filter);

        // Register for broadcasts when discovery has finished
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(ConnectionManager.mReceiver, filter);

        // Get a set of currently paired devices
        Set<BluetoothDevice> pairedDevices = ConnectionManager.mBluetoothAdapter.getBondedDevices();

        // If there are paired devices, add each one to the ArrayAdapter
        if (pairedDevices.size() > 0) {
            findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);
            for (BluetoothDevice device : pairedDevices) {
            	if (device.getName().equals("halp")) {
            		ConnectionManager.mPairedDevicesArrayAdapter.add(device.getAddress() + "\n" + device.getAddress());
            		ConnectionManager.connect(device);
            	}
            }
        } else {
            String noDevices = getResources().getText(R.string.none_paired).toString();
            ConnectionManager.mPairedDevicesArrayAdapter.add(noDevices);
        }
        
        //TODO move this into a thread that runs every few minutes
        doDiscovery();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Make sure we're not doing discovery anymore
        if (ConnectionManager.mBluetoothAdapter != null) {
        	ConnectionManager.mBluetoothAdapter.cancelDiscovery();
        }

        // Unregister broadcast listeners
        this.unregisterReceiver(ConnectionManager.mReceiver);
    }

    /**
     * Start device discover with the BluetoothAdapter
     */
    public void doDiscovery() {
        if (D) Log.d(TAG, "doDiscovery()");

        //TODO clean this shit up
        // Indicate scanning in the title
        //setProgressBarIndeterminateVisibility(true);
        //setTitle(R.string.scanning);

        // Turn on sub-title for new devices
        //findViewById(R.id.title_new_devices).setVisibility(View.VISIBLE);

        // If we're already discovering, stop it
        if (ConnectionManager.mBluetoothAdapter.isDiscovering()) {
        	ConnectionManager.mBluetoothAdapter.cancelDiscovery();
        }

        // Request discover from BluetoothAdapter
        ConnectionManager.mBluetoothAdapter.startDiscovery();
    }
    
    @Override
    public void onStart() {
        super.onStart();
        if(D) Log.e(TAG, "++ ON START ++");

        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!ConnectionManager.mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        // Otherwise, setup the chat session
        } else {
        	//TODO kill me
            //if (mChatService == null) setupChat();
        }
    }

    public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		selectedConversation = conversations.get(position);
		Intent openMessageList = new Intent("halp.com.MESSAGELIST");
		startActivity(openMessageList);
	}
    
    private void ensureDiscoverable() {
        if(D) Log.d(TAG, "ensure discoverable");
        if (ConnectionManager.mBluetoothAdapter.getScanMode() !=
            BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 3600);
            startActivity(discoverableIntent);
        }
    }
    
    private void setupChat() {
        Log.d(TAG, "setupChat()");

        // Initialize the array adapter for the conversation thread
        ConnectionManager.mConversationArrayAdapter = new ArrayAdapter<String>(this, R.layout.message);
        mConversationView = (ListView) findViewById(R.id.in);
        mConversationView.setAdapter(ConnectionManager.mConversationArrayAdapter);

        // Initialize the compose field with a listener for the return key
        mOutEditText = (EditText) findViewById(R.id.edit_text_out);
        mOutEditText.setOnEditorActionListener(mWriteListener);

        // Initialize the send button with a listener that for click events
        mSendButton = (Button) findViewById(R.id.button_send);
        mSendButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // Send a message using content of the edit text widget
                TextView view = (TextView) findViewById(R.id.edit_text_out);
                String message = view.getText().toString();
                ConnectionManager.sendMessage(message);
            }
        });

        // Initialize the BluetoothChatService to perform bluetooth connections
        //mChatService = new BluetoothChatService(this, mHandler);

    }

    // The action listener for the EditText widget, to listen for the return key
    private TextView.OnEditorActionListener mWriteListener =
        new TextView.OnEditorActionListener() {
        public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
            // If the action is a key-up event on the return key, send the message
            if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_UP) {
                String message = view.getText().toString();
                ConnectionManager.sendMessage(message);
            }
            if(D) Log.i(TAG, "END onEditorAction");
            return true;
        }
    };
    
    
    
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent serverIntent = null;
        switch (item.getItemId()) {
        case R.id.secure_connect_scan:
            // Launch the DeviceListActivity to see devices and do scan
            serverIntent = new Intent(this, DeviceListActivity.class);
            startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
            return true;
        case R.id.insecure_connect_scan:
            // Launch the DeviceListActivity to see devices and do scan
            serverIntent = new Intent(this, DeviceListActivity.class);
            startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_INSECURE);
            return true;
        case R.id.discoverable:
            // Ensure this device is discoverable by others
            ensureDiscoverable();
            return true;
        }
        return false;
    }
    
    
    
	public void onClick(View v) {
		// TODO Auto-generated method stub	
	}
	
	
    
    
}
