package halp.com;

import java.util.ArrayList;

public class Conversation {
	
	private String group;
	private ArrayList<Message> messages;

	public Conversation(){
		messages = new ArrayList<Message>();
	}
	
	public Conversation(String g){
		group = g;
		messages = new ArrayList<Message>();
	}
	
	public Conversation(String g, ArrayList<Message> m){
		group = g;
		messages = new ArrayList<Message>();
		messages = m;
	}
	
	public String getGroup(){ return group;}
	
	public ArrayList<Message> getMessages(){ return messages;}
	
	public void setGroup(String g){
		group = g;
	}
	
	public void setMessages(ArrayList<Message> m){
		messages = m;
	}
	
	public void addMessage(Message m){
		messages.add(m);
	}
}
