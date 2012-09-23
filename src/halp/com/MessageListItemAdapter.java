package halp.com;

import java.util.ArrayList;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class MessageListItemAdapter extends ArrayAdapter<Message> {
	
	public MessageListItemAdapter(Context context, int textViewResourceId, ArrayList<Message> e) {
		super(context, textViewResourceId, e);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.messagelistitem, null);
		}

		Message message = this.getItem(position);
		if (message != null) {
			TextView time = (TextView) v.findViewById(R.id.tvMessageTime);
			TextView body = (TextView) v.findViewById(R.id.tvMessageBody);
			LinearLayout colorBox = (LinearLayout) v.findViewById(R.id.llColorBox);
			LinearLayout layout = (LinearLayout) v.findViewById(R.id.llMessageListItem);
			
			if (message.getTime() != null) {
				time.setText(message.getTime());
			}
			
			if (message.getMessage() != null) {
				body.setText(message.getMessage());
			}
			if (message.getSender() != null){
				if(message.getSender().equals("Daniel")){
					colorBox.setBackgroundColor(Color.rgb(176, 176, 176));
					layout.setGravity(Gravity.RIGHT);
				}
				else{
					colorBox.setBackgroundColor(Color.rgb(0, 176, 255));
				}
			}
		}
		return v;
	}
}