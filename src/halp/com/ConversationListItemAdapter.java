package halp.com;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ConversationListItemAdapter extends ArrayAdapter<Conversation> {

	public ConversationListItemAdapter(Context context, int textViewResourceId, ArrayList<Conversation> e) {
		super(context, textViewResourceId, e);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.conversationlistitem, null);
		}

		Message message = null;
		if(this.getItem(position).getMessages().size() > 0){
			message = this.getItem(position).getMessages().get(0);
		}
		if (message != null) {
			TextView contact = (TextView) v.findViewById(R.id.tvGroup);
			TextView preview = (TextView) v.findViewById(R.id.tvPreview);
			TextView time = (TextView) v.findViewById(R.id.tvConversationTime);

			if (message.getSender() != null) {
				contact.setText(message.getSender());
			}

			if (message.getMessage() != null) {
				preview.setText(message.getMessage());
			}
			
			if (message.getTime() != null) {
				time.setText(message.getTime());
			}
			
		}
		return v;
	}
}