package halp.com;

public class Message {
	
	private String sender;
	private String receiver;
	private String message;
	private String time;
	
	public Message(){
		
	}
	
	public Message(String s, String r, String m, String t){
		sender = s;
		receiver = r;
		message = m;
		time = t;
	}

	public String getSender(){ return sender;}

	public String getReceiver(){ return receiver;}

	public String getMessage(){ return message;}

	public String getTime(){ return time;}

	public void setSender(String s){ sender = s;}

	public void setReceiver(String r){ receiver = r;}

	public void setMessage(String m){ message = m;}

	public void setTime(String t){ time = t;}

}
