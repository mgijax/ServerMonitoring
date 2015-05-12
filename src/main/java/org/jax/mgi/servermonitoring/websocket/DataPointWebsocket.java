package org.jax.mgi.servermonitoring.websocket;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.enterprise.event.Observes;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.jax.mgi.servermonitoring.model.DataPoint;
import org.jax.mgi.servermonitoring.model.DataPointDTO;

@ServerEndpoint("/websocket")
public class DataPointWebsocket {
	
	private static final Set<Session> sessions = Collections.synchronizedSet(new HashSet<Session>());
	private static final HashMap<String, String> sessionMap = new HashMap<String, String>();

	@OnOpen
	public void onOpen(final Session session) {
		try {
			session.getBasicRemote().sendText("session opened");
			sessions.add(session);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@OnMessage
	public void onMessage(final String message, final Session client) {
		try {
			client.getBasicRemote().sendText("client message: " + message);
			sessionMap.put(client.getId(), message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@OnClose
	public void onClose(final Session session) {
		try {
			sessions.remove(session);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onServerDataEvent(@Observes final DataPoint data) {
		try {
			for (Session s : sessions) {
				if(sessionMap.containsKey(s.getId())) {
					if(data.getServerName().getName().equals(sessionMap.get(s.getId())) || sessionMap.get(s.getId()).equals("all")) {
						DataPointDTO dto = new DataPointDTO(data);
						s.getBasicRemote().sendText(dto.toJSON());
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
	}
}