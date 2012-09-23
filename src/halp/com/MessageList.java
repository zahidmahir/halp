package halp.com;

import java.util.ArrayList;
import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ListView;

public class MessageList extends ListActivity{
	
	ArrayList<Message> messages = new ArrayList<Message>();
	Conversation selectedConversation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messagelist);
        for(int i = 0; i < 10; i++){
        	Message m1 = new Message("Keith", "Daniel", "I will rub this the right way", "9:30pm");
        	Message m2 = new Message("Daniel", "Keith", "on your phone", "9:30pm");
        	messages.add(m1);
        	messages.add(m2);
        }
        ListView listView = (ListView) findViewById(android.R.id.list);
        listView.setAdapter(new MessageListItemAdapter(this, R.layout.messagelistitem, messages));
    }
}