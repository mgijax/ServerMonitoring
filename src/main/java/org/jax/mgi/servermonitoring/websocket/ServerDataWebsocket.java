package org.jax.mgi.servermonitoring.websocket;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.enterprise.event.Observes;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.jax.mgi.servermonitoring.model.DataPoint;
import org.jax.mgi.servermonitoring.model.DataPointDTO;

@ServerEndpoint("/websocket")
public class ServerDataWebsocket {
	
	private static final Set<Session> sessions = Collections.synchronizedSet(new HashSet<Session>());

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
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@OnClose
	public void onClose(final Session session) {
		try {
			session.getBasicRemote().sendText("WebSocket Session closed");
			sessions.remove(session);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onServerDataEvent(@Observes final DataPoint data) {
		try {
			for (Session s : sessions) {
				DataPointDTO dto = new DataPointDTO(data);
				s.getBasicRemote().sendObject(dto);
			}
		} catch (IOException | EncodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
	}
}