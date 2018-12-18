import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

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

	@Override
	public Message decode(String arg0) throws DecodeException {
		Event event;
		List<String> data = new ArrayList<>();
		parser = Json.createParser(new StringReader(arg0));
		while(parser.hasNext())
		{
			event = parser.next();
			switch(event) {
				case VALUE_STRING: 
					data.add(parser.getString()); break;
				default:
					break;
			}
		}
		
		return new Message(data.get(0),data.subList(1, data.size()));
	}

	@Override
	public boolean willDecode(String arg0) {
		// TODO Auto-generated method stub
		return true;
	}

}
