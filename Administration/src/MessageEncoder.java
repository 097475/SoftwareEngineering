import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class MessageEncoder implements Encoder.Text<Message> {

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(EndpointConfig arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public String encode(Message arg0) throws EncodeException {
		// TODO Auto-generated method stub
		JsonArrayBuilder ja = Json.createArrayBuilder();
		//JsonObjectBuilder value = Json.createObjectBuilder();
		ja.add(arg0.getType());
		for(String[] elem : arg0.getData())
		{					
			ja.add(String.join(" ", elem));
		}
	    return ja.build().toString();
	}

}
