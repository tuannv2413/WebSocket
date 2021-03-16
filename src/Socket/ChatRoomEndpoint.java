package Socket;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/chatRoomServer")
public class ChatRoomEndpoint {
	static Set<Session> users = Collections.synchronizedSet(new HashSet<Session>());

	@OnOpen
	public void handleOpen(Session session) {
		users.add(session);
	}
	
	@OnMessage
	public void handlMessage(String message,Session userSession) throws IOException {
		String username= (String) userSession.getUserProperties().get("username");
		if(username == null) {
			userSession.getUserProperties().put("username", message);
			userSession.getBasicRemote().sendText("System: you are connectd as " + message);
		}else {
			for (Session session : users) {
				session.getBasicRemote().sendText(username+": "+message);
			}
		}
	}
	
	@OnClose
	public void handleClose(Session session) {
		users.remove(session);
	}
	
	@OnError
	public void handleError(Throwable t) {
		t.printStackTrace();
	}
}
