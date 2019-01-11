/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.EncodeException;
import javax.websocket.Session;

/**
 *
 * @author Utente_
 */
@ApplicationScoped
public class SessionManager {
	private final Map<Integer,ChatMessage> chat = createLRUMap(25);
	private final Set<Session> users = new HashSet<>();
	private static int ID = 0;
	
	@SuppressWarnings("serial")
	public static <K, V> Map<K, V> createLRUMap(final int maxEntries) {
	    return new LinkedHashMap<K, V>(maxEntries*10/7, 0.7f, true) {
	        @Override
	        protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
	            return size() > maxEntries;
	        }
	    };
	}
	
	public SessionManager() {
	}

	public static int getCurrentID() {
		return ID++;
	}
	private List<String> getAllMessages() {
		return chat.values().stream().map(ChatMessage::formatMessage).collect(Collectors.toList());
	}
	public void addSession(Session session) {
		users.add(session);
		try {
			session.getBasicRemote().sendObject(new DataPacket(DataPacketTypes.ALLMESSAGE,this.getAllMessages()));
		} catch (IOException | EncodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void removeSession(Session session) {
		users.remove(session);
	}

    public void send(ChatMessage message) throws IOException, EncodeException
    {
    	List<String> tmp = new ArrayList<>();
    	tmp.add(message.formatMessage());
    	chat.put(message.getID(),message);
    	DataPacket dp = new DataPacket(DataPacketTypes.MESSAGE,tmp);
        for(Session s : users)
        {
            if(s.isOpen())
            {
                    s.getBasicRemote().sendObject(dp);
            }
        }
    }

	public void delete(DeleteMessage message, String username) throws IOException, EncodeException {
		// TODO Auto-generated method stub
		for(int id : message.getIDs()) {
			if(username.equals("Admin") || chat.get(id).getUsername().equals(username))
				chat.remove(id);
		}
		DataPacket dp = new DataPacket(DataPacketTypes.ALLMESSAGE,this.getAllMessages());
		 for(Session s : users)
	        {
	            if(s.isOpen())
	            {
	                    s.getBasicRemote().sendObject(dp);
	            }
	        }
	}
}
