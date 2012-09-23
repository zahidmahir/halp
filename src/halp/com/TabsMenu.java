package halp.com;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.ListActivity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class TabsMenu extends ListActivity implements OnClickListener {
	
	ArrayList<Conversation> conversations = new ArrayList<Conversation>();
	Conversation selectedConversation;

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
        
    }

    public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		selectedConversation = conversations.get(position);
		Intent openMessageList = new Intent("halp.com.MESSAGELIST");
		startActivity(openMessageList);
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub	
	}
}
