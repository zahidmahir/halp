package halp.com;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity {

	ConnectivityManager connectivity;
	NetworkInfo wifiInfo, mobileInfo;
	WifiManager wifiManager;
	TextView fuckLogCat;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fuckLogCat = (TextView) findViewById(R.id.tvLog);
        
        
        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        
     // Setup Connectivity
        connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        wifiInfo = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        mobileInfo = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);


        wifiManager.enableNetwork(0, false);

        wifiManager.startScan();
        
        fuckLogCat.setText("\n\n" + wifiManager.getScanResults());
        
        
        // print info
        Log.d("MainActivity", "\n\n" + wifiInfo.toString());
        Log.d("MyActivity","\n\n" + mobileInfo.toString());
        Log.d("MyActivity", "\n\n" + wifiManager.getScanResults());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
