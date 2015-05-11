package org.jax.mgi.servermonitoring.websocket;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.jax.mgi.servermonitoring.model.DataPoint;
import org.jax.mgi.servermonitoring.service.DataPointService;

@ServerEndpoint("/serverlastupdate")
public class LastUpdateWebsocket {
	
    @Inject
    private DataPointService dataPointService;
	
	private static final Set<Session> sessions = Collections.synchronizedSet(new HashSet<Session>());

	@OnOpen
	public void onOpen(final Session session) {
		try {
			session.getBasicRemote().sendText("session opened");
			sessions.add(session);
			
			List<DataPoint> list = dataPointService.getLastUpdates();
			
			StringBuffer b = new StringBuffer();
			b.append("[");
			for(DataPoint dp: list) {
				b.append("{\"serverName\": \"" + dp.getServerName().getName() + "\", \"lastUpdated\": \"" + dp.getDataTimeStamp().getTime() + "\"},");
			}
			b.deleteCharAt(b.length() - 1);
			b.append("]");
			
			session.getBasicRemote().sendText(b.toString());
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
			sessions.remove(session);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onServerDataEvent(@Observes final DataPoint data) {
		try {
			for (Session s : sessions) {
				s.getBasicRemote().sendText("{\"serverName\": \"" + data.getServerName().getName() + "\", \"lastUpdated\": \"" + data.getDataTimeStamp().getTime() + "\"}");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
	}
}
