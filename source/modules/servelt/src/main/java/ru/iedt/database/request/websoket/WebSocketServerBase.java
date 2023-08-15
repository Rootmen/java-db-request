package ru.iedt.database.request.websoket;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/chat/{secret}/{session_id}")
public class WebSocketServerBase {
    @OnOpen
    public void onOpen(Session session,
                       @PathParam("secret") String secret,
                       @PathParam("session_id") String sessionId){
        System.out.println("Open Connection ...");
    }

    @OnClose
    public void onClose(){
        System.out.println("Close Connection ...");
    }

    @OnMessage
    public String onMessage(String message){
        System.out.println("Message from the client: " + message);
        String echoMsg = "Echo from the server : " + message;
        return echoMsg;
    }

    @OnError
    public void onError(Throwable e){
        e.printStackTrace();
    }
}