import java.sql.Timestamp;

public class ChatMessage implements Message{
	private String msgString;
	private String username;
	private Timestamp timestamp;
	private int ID;

	public ChatMessage(int ID, String username, String msgString, Timestamp timestamp)
	{
		this.ID = ID;
		this.msgString = msgString;
		this.username = username;
		this.timestamp = timestamp;
	}


	public String getContent() {
		return msgString;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public Timestamp getTimestamp() {
		return this.timestamp;
	}
	public int getID() {
		return this.ID;
	}

	/*public static String formatMessage(Timestamp ts, ChatMessage msg) {
		return ts.toString() + "\t\t" + msg.getUsername() + ":\t\t" + msg.getMessage()+"<br>";
	}*/
	
	public String formatMessage() {
		return this.ID+" "+this.timestamp.toString() + " " + this.username + " : " + this.getContent();
	}
}
