package ru.iedt.database.request.webosket;

import ru.iedt.database.request.database.store.QueryStoreList;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value = "/chat/{app_id}/{secret}/{session_id}")
public class WebSocketServerBase {
    private final Map<String, List<Session>> sessionsList = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session,
                       @PathParam("app_id") String appId,
                       @PathParam("secret") String secret,
                       @PathParam("session_id") String sessionId) {
        System.out.println("Open Connection ...");
        sessionsList.computeIfAbsent(appId, key -> new ArrayList<>()).add(session);
    }

    @OnClose
    public void onClose(Session session,
                        CloseReason reason,
                        @PathParam("app_id") String appId) {
        System.out.println("Closing a WebSocket due to "+reason.getReasonPhrase());
        final List<Session> sessions = this.sessionsList.get(appId);
        final Iterator<Session> sessionIterator = sessions.iterator();
        while (sessionIterator.hasNext()) {
            final Session chatSession = sessionIterator.next();
            if (session.getId().equals(chatSession.getId())) {
                sessionIterator.remove();
                break;
            }
        }
    }

    @OnMessage
    public String onMessage(Session session, String message) {
        System.out.println("Message from the client: " + message);
        String echoMsg = "Echo from the server : " + message;
        return echoMsg;
    }

    @OnError
    public void onError(Session session, Throwable e) {
        e.printStackTrace();
    }
}