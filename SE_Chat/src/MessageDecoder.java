import java.io.StringReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.json.Json;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class MessageDecoder implements Decoder.Text<Message> {
	JsonParser parser;

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(EndpointConfig arg0) {
		// TODO Auto-generated method stub
	}

	private Message createMessage(Map<String,String> args) {
		String type = args.remove("type");
		Message msg = null;

		switch(type) {
			case "MESSAGE": msg = new ChatMessage(SessionManager.getCurrentID(),args.get("username"), args.get("message"),  new Timestamp(new Date().getTime())); break;
			case "DELETE" : 		System.out.println(args.get("IDList")); /*msg = new DeleteMessage(args.get("IDs").split(","));*/ break;
		}
		return msg;
	}
	@Override
	public Message decode(String arg0) throws DecodeException {
		Event event;
		String attr = null;
		List<Integer> args = new ArrayList<>();
		Map<String,String> data = new HashMap<>();
		parser = Json.createParser(new StringReader(arg0));
		while(parser.hasNext())
		{
			event = parser.next();
			switch(event) {
				case VALUE_STRING: 
					if(attr!= null && attr.equals("IDList")) {
						args.add(Integer.parseInt(parser.getString()));
					}
					else
						data.put(attr, parser.getString()); 
					break;
				case KEY_NAME:
					attr = parser.getString();
					break;							
				default:
					break;
			}
		}
		
		if(args.isEmpty())
			return createMessage(data);
		else
			return createMessage(args);
	}

	private Message createMessage(List<Integer> args) {
		Message msg = null;
		msg = new DeleteMessage(args);
		return msg;
	}

	@Override
	public boolean willDecode(String arg0) {
		// TODO Auto-generated method stub
		return true;
	}

}
