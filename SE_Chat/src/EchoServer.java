

import java.io.IOException;
import javax.inject.Inject;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

 
/** 
 * @ServerEndpoint gives the relative name for the end point
 * This will be accessed via ws://localhost:8080/EchoChamber/echo
 * Where "localhost" is the address of the host,
 * "EchoChamber" is the name of the package
 * and "echo" is the address to access this class from the server
 */
@ServerEndpoint(value = "/rooms/{username}",
				decoders = { MessageDecoder.class},
				encoders = { DataPacketEncoder.class}
				)
public class EchoServer {
    /**
     * @OnOpen allows us to intercept the creation of a new session.
     * The session class allows us to send data to the user.
     * In the method onOpen, we'll let the user know that the handshake was 
     * successful.
     */
	@Inject
	SessionManager SessionManager;
	
	

    @OnOpen
    public void onOpen(Session session,  @PathParam("username") final String username){
    	session.getUserProperties().put("username", username);
		SessionManager.addSession(session);
    }
 
    /**
     * When a user sends a message to the server, this method will intercept the message
     * and allow us to react to it. For now the message is read as a String.
     */
    @OnMessage
    public void onMessage(Message message, Session session){
    	try {
    		   if (message instanceof ChatMessage) {
    			   SessionManager.send((ChatMessage)message);
    			      // We received a MessageA object...
    			   } else if( message instanceof DeleteMessage) {
    				   SessionManager.delete((DeleteMessage)message,(String)session.getUserProperties().get("username"));
    			   }
			
		} catch (IOException | EncodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
 
    /**
     * The user closes the connection.
     * 
     * Note: you can't send messages to the client from this method
     */
    @OnClose
    public void onClose(Session session){
        SessionManager.removeSession(session);
    }
}