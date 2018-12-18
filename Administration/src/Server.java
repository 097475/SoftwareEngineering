

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
@ServerEndpoint(value = "/adm/{type}",
				decoders = { MessageDecoder.class},
				encoders = { MessageEncoder.class}
				)
public class Server {
    /**
     * @OnOpen allows us to intercept the creation of a new session.
     * The session class allows us to send data to the user.
     * In the method onOpen, we'll let the user know that the handshake was 
     * successful.
     */
	IGameManagement IGM = new GameManagementPuppet();
	IUserManagement IUM = new UserManagementPuppet();
	//JSONParser parser = new JSONParser();
    @OnOpen
    public void onOpen(Session session,@PathParam("type") final String type){
    	
    	try {
			//session.getBasicRemote().sendObject(new Message("Connection established"));
    		//new Message("PLAYERS",IUM.getPlayers()).printMessage();
    		if(type.equals("statpanel")) {
    			session.getBasicRemote().sendObject(new Message("ALLPLAYERS",IUM.getAllPlayers()));
    		}
    		else {
    		session.getBasicRemote().sendObject(new Message("PLAYERS",IUM.getPlayers()));
    		session.getBasicRemote().sendObject(new Message("ALLPLAYERS",IUM.getAllPlayers()));
    		session.getBasicRemote().sendObject(new Message("GAMES",IGM.getGames()));
    		}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EncodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
 
    /**
     * When a user sends a message to the server, this method will intercept the message
     * and allow us to react to it. For now the message is read as a String.
     */
    @OnMessage
    public void onMessage(Message message, Session session){
    	switch(message.getType()) {
    	case "BAN": IUM.banPlayer(message.getData().get(0)[0], Long.parseLong(message.getData().get(1)[0])); 
			try {
				session.getBasicRemote().sendObject(new Message("PLAYERS",IUM.getPlayers()));
			} catch (IOException | EncodeException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}break;
    	case "EDIT": Map<String,String> edits = new HashMap<>();
    				 edits.put("username", message.getData().get(1)[0]);
    				 edits.put("email", message.getData().get(2)[0]);
    				 IUM.editPlayer(message.getData().get(0)[0], edits);
			try {
				session.getBasicRemote().sendObject(new Message("PLAYERS",IUM.getPlayers()));
				session.getBasicRemote().sendObject(new Message("ALLPLAYERS",IUM.getAllPlayers()));
			} catch (IOException | EncodeException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}break;
    	case "SSC": IUM.setCapacity(Integer.parseInt(message.getData().get(0)[0]));break;
    	case "SMB": IGM.setMinBet(IGameManagement.gameTypes.valueOf(message.getData().get(0)[0]), Integer.parseInt(message.getData().get(1)[0]));break;
    	case "CLOSE":
    		message.getData().stream().forEach(x -> IGM.closeGame(Integer.parseInt(x[0])));
			try {
				session.getBasicRemote().sendObject(new Message("GAMES",IGM.getGames()));
			} catch (IOException | EncodeException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}break;
    	case "PLAYERINFO": String[] ret = IUM.getAllPlayers().stream().filter(x-> x[0].equals(message.getData().get(0)[0])).findFirst().orElse(null);
			try {
				session.getBasicRemote().sendObject(new Message("PLAYERINFO",Arrays.asList(ret)));
			} catch (IOException | EncodeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} break;
    	case "STATREQ": try {
				session.getBasicRemote().sendObject(new Message(message.getData().get(0)[0],
								this.processRequest(message.getData().get(0)[0],null)));
			} catch (IOException | EncodeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} break;
    	}
    }
 
    
    private List<String[]> processRequest(String type, String addType){ //write sql here
    	List<String[]> ret = null;
		switch(type) {
		case "GAMESTATS": ret = IGM.query("gamestats"); ret.add(new String[] {Float.toString(computeProfitability(Float.parseFloat(ret.get(0)[0]),Float.parseFloat(ret.get(0)[1])))}); break;
		case "PLAYERSTATS": ret = IGM.query("playerstats"); break;
		case "DAILYSTATS": ret = IGM.query("dailystats"); 
		ret.add(new String[] {Float.toString(computeProfitability(Float.parseFloat(ret.get(0)[0]),Float.parseFloat(ret.get(0)[1])))});
		ret.add(new String[] {Float.toString(computeProfitability(Float.parseFloat(ret.get(1)[0]),Float.parseFloat(ret.get(1)[1])))});
		ret.add(new String[] {Float.toString(computeProfitability(Float.parseFloat(ret.get(2)[0]),Float.parseFloat(ret.get(2)[1])))});
		ret.add(new String[] {Float.toString(computeProfitability(Float.parseFloat(ret.get(3)[0]),Float.parseFloat(ret.get(3)[1])))});
								break;
		case "TOTALSTATS": ret = IGM.query("totalstats"); ret.add(new String[] {Float.toString(computeProfitability(Float.parseFloat(ret.get(0)[0]),Float.parseFloat(ret.get(0)[1])))});
		ret.add(new String[] {Float.toString(computeProfitability(Float.parseFloat(ret.get(1)[0]),Float.parseFloat(ret.get(1)[1])))});
		ret.add(new String[] {Float.toString(computeProfitability(Float.parseFloat(ret.get(2)[0]),Float.parseFloat(ret.get(2)[1])))});
		ret.add(new String[] {Float.toString(computeProfitability(Float.parseFloat(ret.get(3)[0]),Float.parseFloat(ret.get(3)[1])))});break;
		case "HISTORYSTATS": ret = IGM.query("history"); 
							List<String[]> tmp = new ArrayList<>();
						     for(String[] elem : ret) {
						    	 tmp.add(new String[] {Float.toString(computeProfitability(Float.parseFloat(elem[0]),Float.parseFloat(elem[1])))});
						     }
						     ret.addAll(tmp);
						     break;
		}
    	return ret;
    }
    /**
     * The user closes the connection.
     * 
     * Note: you can't send messages to the client from this method
     */
    @OnClose
    public void onClose(Session session){

    }
    
    private float computeProfitability(float expenditure, float income) {
    	return (income/expenditure)*100 - 100;
    }
}