package halp.com;

import java.util.ArrayList;
import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MessageList extends ListActivity implements OnClickListener {
	
	private EditText inputText;
    private static final String TAG = "Halp";
    private static final boolean D = true;
    // String buffer for outgoing messages
    private StringBuffer mOutStringBuffer = ConnectionManager.mOutStringBuffer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messagelist);
        ListView listView = (ListView) findViewById(android.R.id.list);
        listView.setAdapter(new MessageListItemAdapter(this, R.layout.messagelistitem, TabsMenu.selectedConversation.getMessages()));
        inputText = (EditText) findViewById(R.id.etInputText);
        inputText.setOnEditorActionListener(mWriteListener);
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
    

	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

}